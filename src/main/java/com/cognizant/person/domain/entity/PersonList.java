package com.cognizant.person.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonList", propOrder = {
    "persons"
})
@XmlRootElement
public class PersonList {

	private List<Person> persons;
	
	
	 public java.util.List<Person> getPersons() {
	        if (persons == null) {
	        	persons = new ArrayList<Person>();
	        }
	        return this.persons;
	    }
}
