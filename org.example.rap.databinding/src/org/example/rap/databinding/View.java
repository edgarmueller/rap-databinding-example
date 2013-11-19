package org.example.rap.databinding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.example.rap.databinding.model.MyModel;
import org.example.rap.databinding.model.Person;

/**
 * This view shows a &quot;mail message&quot;. This class is contributed through
 * the plugin.xml.
 */
public class View extends ViewPart {

	public static final String ID = "org.example.rap.databinding.view";

	public List<String> columns = new ArrayList<String>();

	public View() {
		columns.add("firstName");
		columns.add("lastName");
	}
	
	private void createFixedColumn(TableViewer tableViewer) {
		//final Image icon = Activator.getImage(ICON_VALIDATION_ERROR);
		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setMoveable(false);
		column.getColumn().setData("width", 100); //$NON-NLS-1$
		column.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText("(!)");
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipText(java.lang.Object)
			 */
			@Override
			public String getToolTipText(Object element) {
				return "Hello tooltip!";
			}
		});
		
		column.getColumn().setText("Status");
		column.getColumn().setWidth(80);
	}

	@Override
	public void createPartControl(Composite parent) {

		// create a table viewer with visible header
		TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.getTable().setData(RWT.FIXED_COLUMNS, new Integer(1));
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(viewer.getTable()), new TextCellEditor(viewer.getTable()) });
		viewer.setCellModifier(new CellModifier(this));
		viewer.setColumnProperties(getColumnNames().toArray(new String[2]));
		final TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(viewer,
				new FocusCellOwnerDrawHighlighter(viewer));


		final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(viewer) {
			@Override
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		TableViewerEditor.create(viewer, focusCellManager, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		ColumnViewerToolTipSupport.enableFor(viewer);
		
		// create an example model which will be visualized
		MyModel model = new MyModel();

		// create a content provider
		ObservableListContentProvider cp = new ObservableListContentProvider();

		createFixedColumn(viewer);
		
		// create a column for each attribute & setup the databinding
		for (String attribute : getColumnNames()) {
			// create a new column
			TableViewerColumn tvc = new TableViewerColumn(viewer, SWT.LEFT);
			// determine the attribute that should be observed

			IObservableMap map = BeanProperties.value(Person.class, attribute).observeDetail(
					cp.getKnownElements());
			tvc.setLabelProvider(new ObservableMapCellLabelProvider(map) {
				@Override
				public void update(ViewerCell cell) {
					super.update(cell);

					if (cell.getText().equals("foo")) {
						cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					} else {
						cell.setBackground(null);
					}
				}
	
			});
			// set the column title & set the size
			tvc.getColumn().setText(attribute);
			tvc.getColumn().setWidth(80);
		}
		
		viewer.setContentProvider(cp);
		viewer.setInput(BeanProperties.list("persons").observe(model));

	}

	List<String> getColumnNames() {
		return columns;
	}

	@Override
	public void setFocus() {
	}
}
