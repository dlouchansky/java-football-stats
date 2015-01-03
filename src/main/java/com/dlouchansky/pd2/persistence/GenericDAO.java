package com.dlouchansky.pd2.persistence;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T> {
    Logger logger = LoggerFactory.getLogger(GenericDAO.class);

    private Class<T> persistentClass;

    public GenericDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void add(T object) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
            logger.info("Object added: " + object.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void update(T object) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void saveOrUpdate(T object) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public T getById(Long id) {
        Session session = null;
        T object = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            object = (T) session.load(getPersistentClass(), id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return object;
    }

    public List<T> getAll() {
        Session session = null;
        List<T> objects = new ArrayList<T>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            objects = session.createCriteria(getPersistentClass()).list();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return objects;
    }

    public void delete(T obj) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(obj);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void refresh(T obj) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.refresh(obj);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void deleteAll() {
        getAll().forEach(this::delete);
    }

}
