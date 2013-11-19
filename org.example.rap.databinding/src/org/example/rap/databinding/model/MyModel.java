package org.example.rap.databinding.model;

import java.util.ArrayList;
import java.util.List;

public class MyModel extends ModelObject {

	private List<Person> persons = new ArrayList<Person>();

	public MyModel() {
		Person p = new Person();
		p.setFirstName("Joe");
		p.setLastName("Darcey");
		persons.add(p);
		p = new Person();
		p.setFirstName("Jim");
		p.setLastName("Knopf");
		persons.add(p);
		p = new Person();
		p.setFirstName("Jim");
		p.setLastName("Bean");
		persons.add(p);

	}

	public List<Person> getPersons() {
		return persons;
	}
}