package com.cognizant.person.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.person.domain.entity.Person;
import com.cognizant.person.domain.entity.PersonList;
import com.cognizant.person.domain.repository.GenericHibernateRepository;
import com.cognizant.person.domain.repository.GenericRepository;
import com.cognizant.person.service.PersonService;
import com.sun.jersey.api.NotFoundException;

/**
 * {@link PersonService} implementation class
 *
 */
@Transactional
@Service("personService")
public class PersonServiceImpl implements PersonService {
	
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PersonServiceImpl.class);
	
	GenericRepository<Person, Long> personRepository;
	
	@Autowired
	public PersonServiceImpl(SessionFactory sessionFactory) {
		personRepository = new GenericHibernateRepository<Person, Long>(sessionFactory) {
		};
	}
	
	@Transactional(propagation =Propagation.REQUIRES_NEW)
	@Override
	public Person savePersonDetails(Person person) {
		LOGGER.info(">>> repo store - " + person.getFirstName());
		personRepository.makePersistent(person);
		return person;
	}
	
	@Override
	public Person getPerson(final String id) {
		
		Person person = personRepository.findByAttribute("id", Long.parseLong(id));
		
		if (person == null) {
			throw new NotFoundException("Person id [ " + id +" ] not found");
		}
		
		return person;
	}
	
	@Override
	public PersonList getAllPersons() {
		
		List<Person> persons = personRepository.findAll();
		PersonList  list = new PersonList();
		
		for (Person person : persons) {
			list.getPersons().add(person);
		}
		
		return list;
	}
}
