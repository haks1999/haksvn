package com.haks.haksvn.common.menu.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.haks.haksvn.common.menu.model.Menu;

@Repository
public class MenuDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	/*
	public MenuDao(){}
	
	public MenuDao( Session session ){
		this.session = session;
	}
	*/
	
	public List<Menu> retrieveMenuList() {
		/*
		 * Query selectClause1 = session.createQuery("select emp.name from Employee emp where emp.id = 2");
List<String> empList1 = selectClause1.list();

Criterion salary = Restrictions.gt("salary", 2000);
Criterion name = Restrictions.ilike("firstNname","zara%");
cr.setProjection(Projections.avg("salary"));
		 */
		//session.cre
		//List<Menu> result = session.createQuery( "from Menu" ).list();
		//Session session = sessionFactory.openSession();
		Session session = sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked") List<Menu> result = session.createCriteria(Menu.class)
				//.add(Restrictions.eq("menuLevel", 1))
				.add(Restrictions.eqProperty("menuSeq", "parentMenuSeq"))
				.addOrder(Order.asc("menuOrder"))
				.list();
		//session.update(result);
		//session.i
		return result;
	}
}
