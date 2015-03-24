package com.cognizant.person.domain.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Generic Persistence Interface 
 * This interface defines basic CRUD operations and necessary methods required like find, findAll
 * which is applicable for any entity its avoid replication of code in DAO classes , 
 * This interface can be extended if any entity required additional methods.
 *
 * @param <T> {@link Serializable} entity class
 * @param <ID> {@link Serializable} primary key of the entity
 */
public interface GenericRepository<T, ID extends Serializable> {

	/**
	 * find an object by its attribute and value
	 * @param attribute {@link String} - entity variable name
	 * @param value {@link String} 
	 * @return T entity object 
	 */
	T findByAttribute(final String attribute,final  Object value);

	/**
	 * find list of object which has the same value for give attibure
	 * @param attribute {@link String} - entity variable name
	 * @param value
	 * @return {@link List}<T> - list of entities 
	 */
	List<T> findAllByAttribute(String attribute, Object value);

	/**
	 * get all the entitties in the table
	 * SELECT all operation
	 * @return {@link List}<T> - list of entities 
	 */
	List<T> findAll();

	/**
	 * delete all the entities 
	 * @return count - delete entities count
	 */
	long deleteAll();

	/**
	 * save or update the entity 
	 * create/ update operation
	 * @param entity
	 * @return entity 
	 */
	T makePersistent(T entity);

	/**
	 * save or update list of entities
	 * @param Collection - entities List<T>
	 */
	void makePersistent(Collection<T> entities);

	/**
	 * delete an object
	 * @param entity
	 */
	void makeTransient(T entity);

	/**
	 * delete list of objects 
	 * @param entities {@link List}<SignupDetail>)
	 */
	void makeTransient(Collection<T> entities);

	/**
	 * Count list of account numbers
	 * @return
	 */
	long countAll();

}
