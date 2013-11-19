package org.example.rap.databinding;

import java.util.Arrays;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.example.rap.databinding.model.Person;

public class CellModifier implements ICellModifier {

	private View personsView;

	public CellModifier(View view) {
		super();
		this.personsView = view;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		int columnIndex = personsView.getColumnNames().indexOf(property);

		Object result = null;
		Person node = (Person) element;

		switch (columnIndex) {
		case 0:// row id
			result = node.getFirstName();
			break;
		case 1:// name
			result = node.getLastName();
			break;
		default:
			result = "unknown";
			break;
		}

		return result;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		int columnIndex = personsView.getColumnNames().indexOf(property);

		TableItem item = (TableItem) element;
		Person node = (Person) item.getData();
		String valueString;

		switch (columnIndex) {
		case 0:
			valueString = ((String) value).trim();
			node.setFirstName(valueString);
			System.out.println(valueString);
			break;
		case 1:
			valueString = ((String) value).trim();
			node.setLastName(valueString);
			break;
		default:
			break;
		}

	}
}