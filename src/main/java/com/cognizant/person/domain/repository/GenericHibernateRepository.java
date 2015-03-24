package com.cognizant.person.domain.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;


/**
 * Generic DAO pattern for data access 
 * @author Saravanan Renganathan
 *
 * Transaction and {@link SessionFactory} should be injected from businesslayer 
 * @param EntityClass
 * @param Id
 * 
 * <code>
 * 
 *  GenericHibernateRepository<SignupDetail, Long> repository = 
 *  						new GenericHibernateRepository<SignupDetail, Long>(sessionFactory) {
		};
 *</code>
 */


public abstract class GenericHibernateRepository<T extends Serializable, ID extends Serializable>  
										implements GenericRepository< T, ID> {


    private Class<T> persistentClass;

    private SessionFactory sessionFactory;
    
    @SuppressWarnings("unchecked")
    public GenericHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }
    
    public void refresh(Object value) {
        getSession().refresh(value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T findByAttribute(String attribute, Object value) {
        return (T) createCriteria().add(Restrictions.eq(attribute, value)).uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAllByAttribute(String attribute, Object value) {
        return (List<T>)createCriteria().add(Restrictions.eq(attribute, value)).list();
    }
    
    @SuppressWarnings("unchecked")
    protected List<T> findAllByAttribute(String attribute, Object value, LockMode mode) {
        return (List<T>)createCriteria().add(Restrictions.eq(attribute, value)).setLockMode(mode).list();
    }

    /**
     * get all entities 
     * @return
     */
    @Override
    public List<T> findAll() {
        return list();
    }

    /**
     * record count 
     * it is a unique record count
     * @return
     */
    @Override
    public long countAll() {
        Criteria crit = getSession().createCriteria(getPersistentClass()).setProjection(Projections.rowCount());
        return (Long) crit.uniqueResult();
        
    }

    /**
     * Warning: bulk DELETE queries do not cause cascade-deletes. See the
     * comments to the Hibernate 3 migration guide:
     * http://hibernate.org/250.html
     * 
     * @return The number of objects that were deleted.
     */
    @Override
    public long deleteAll() {
        return getSession().createQuery("delete from " + getPersistentClass().getName()).executeUpdate();
    }

    /**
     * delete all 
     */
    public void deleteAllCascade() {
        List<T> all = findAll();
        makeTransient(all);
    }

    
    /**
     * to persist the entiry save or update
     * @param entity
     * @return object
     */
    @Override
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }
    
    /**
     * presist entity save
     * @param entity
     * @return
     */
    public T save(T entity) {
        getSession().save(entity);
        return entity;
    }

    /**
     * persist list of entity 
     * @param entities
     */
    @Override
    public void makePersistent(Collection<T> entities) {
        Session session = getSession();
        for (T entity : entities) {
            session.saveOrUpdate(entity);
        }
    }
    
    /**
     * merge all entities 
     * @param entities
     */
    public void mergeAll(Collection<T> entities) {
        Session session = getSession();
        for (T entity : entities) {
            session.merge(entity);
        }
    }
    
    /**
     * save all
     * @param entities
     */
    public void saveAll(Collection<T> entities) {
        Session session = getSession();
        for (T entity : entities) {
            session.save(entity);
        }
    }

    /**
     * remove the entity 
     * @param entity
     */
    @Override
    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    /**
     * delete list for entity 
     * @param entities
     */
    @Override
    public void makeTransient(Collection<T> entities) {
        Session session = getSession();
        for (T entity : entities) {
            session.delete(entity);
        }
    }

    /**
     * merge the ditached entity 
     * @param detachedEntity
     * @return
     */
    @SuppressWarnings("unchecked")
    public T merge(T detachedEntity) {
        return (T) getSession().merge(detachedEntity);
    }

    public void evict(T entity) {
        getSession().evict(entity);
    }
    
    public void evictId(ID id) {
    	sessionFactory.getCache().evictEntity(getPersistentClass(), id);
    }

    /**
     * flush
     */
    public void flush() {
        getSession().flush();
    }

    /**
     * clear the session
     */
    public void clear() {
        getSession().clear();
    }

     
    @SuppressWarnings("unchecked")
    protected List<T> list(Criterion... criteria) {
        Criteria crit = createCriteria();
        for (Criterion c : criteria) {
            crit.add(c);
        }
        return crit.list();
    }

    
    public Criteria createCriteria() {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    @SuppressWarnings("unchecked")
    protected List<T> list(Criteria criteria) {
        return criteria.list();
    }

   

}