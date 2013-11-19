package org.example.rap.databinding.model;

public class Person extends ModelObject {
	
	private String firstName;
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String string) {
	    firePropertyChange("firstName", this.firstName, this.firstName= string);
	}

	public void setLastName(String string) {
		firePropertyChange("lastName", this.lastName, this.lastName= string);
	}
}