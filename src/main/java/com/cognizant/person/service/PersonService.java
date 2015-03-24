package com.cognizant.person.service;

import com.cognizant.person.domain.entity.Person;
import com.cognizant.person.domain.entity.PersonList;

/**
 * Basic Contract
 * Adding comments is an anti-pattern.
 */
public interface PersonService {

	Person savePersonDetails(Person person);

	Person getPerson(String id);

	PersonList getAllPersons();

}
