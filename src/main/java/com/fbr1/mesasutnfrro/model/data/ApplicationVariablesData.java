package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.ApplicationVariables;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApplicationVariablesData {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationVariablesData.class);

    public ApplicationVariables get(){

        Session session = null;
        Transaction tx = null;
        ApplicationVariables appVars = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            Query query = session.createQuery("from ApplicationVariables av " +
                    "where av.id = " +
                    "(select max(avv.id) from ApplicationVariables avv)");

            appVars = (ApplicationVariables) query.getSingleResult();

            tx.commit();
        }catch (Exception ex){
            if (tx != null){
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
        }finally {
            if (session != null){
                session.close();
            }
        }

        return appVars;
    }

    public void update(ApplicationVariables appVars){

        Session session = null;
        Transaction tx = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            session.update(appVars);

            tx.commit();
        }catch (Exception ex){
            if (tx != null){
                tx.rollback();
            }
            logger.error(ex.getMessage(), ex);
        }finally {
            if (session != null){
                session.close();
            }
        }

    }
}
