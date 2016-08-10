package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class ApplicationVariablesData {

    public ApplicationVariables get(){
        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from ApplicationVariables av " +
                                          "where av.id = " +
                                          "(select max(avv.id) from ApplicationVariables avv)");

        ApplicationVariables appVars = (ApplicationVariables) query.getSingleResult();

        tx.commit();
        session.close();

        return appVars;
    }

    public void update(ApplicationVariables appVars){
        SessionFactory sessionFactory = HibernateManager.getSessionFactory();

        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();

        session.update(appVars);

        tx.commit();
        session.close();
    }
}
