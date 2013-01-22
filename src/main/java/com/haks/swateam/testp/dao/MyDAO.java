package com.haks.swateam.testp.dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.haks.swateam.testp.model.Person;
 
@Repository
public class MyDAO {
     
	@Autowired
    private SessionFactory sessionFactory;
         
    @Transactional(readOnly=false)
    public void addPerson(Person p) {
        Session session = sessionFactory.openSession();
        session.save(p);
        session.close();       
    }
}
