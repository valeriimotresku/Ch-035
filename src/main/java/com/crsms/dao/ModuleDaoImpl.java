package com.crsms.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crsms.domain.Module;

/**
 * 
 * @author St. Roman
 *
 */

@Repository("moduleDao")
public class ModuleDaoImpl implements ModuleDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = LogManager.getLogger(ModuleDaoImpl.class);

	@Override
	public void add(Module module) {
		try {
			sessionFactory.getCurrentSession().persist(module);
		} catch (Exception e) {
			logger.error("Error in save module: " + e);
		}
	}
	
	@Override
	public void update(Module module) {
		try {
			sessionFactory.getCurrentSession().update(module);
		} catch (Exception e) {
			logger.error("Error in update module: " + e);
		}
	}

	@Override
	public void delete(Module module) {
		try {
			sessionFactory.getCurrentSession().delete(module);
		} catch (Exception e) {
			logger.error("Error in delete module: " + e);
		}
	}

	/**
	 * Returns Module or null
	 */
	@Override
	public Module getById(Long id) {
		Module module = null;
		try {
			module = (Module) sessionFactory.getCurrentSession().get(Module.class, id);
		} catch (Exception e) {
			logger.error("Error in get module by id: " + e);
		}
		return module;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Module> getAll() {
		List<Module> list = new ArrayList<Module>();
		try {
			list = sessionFactory.getCurrentSession().getNamedQuery("getAll").list();
		} catch (Exception e) {
			logger.error("Error in get all modules: " + e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Module> getAllByCourseId(Long courseId) {
		List<Module> list = new ArrayList<Module>();
		try {
			list = sessionFactory.getCurrentSession().getNamedQuery("getAllByCourseId").setParameter("id", courseId).list();
		} catch (Exception e) {
			logger.error("Error in get all modules by course id: " + e);
		}
		return list;
	}

	@Override
	public void deleteById(Long id) {
	    try {
	    	sessionFactory.getCurrentSession().getNamedQuery("deleteById").setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			logger.error("Error in delete module by id: " + e);
		}
	}

}