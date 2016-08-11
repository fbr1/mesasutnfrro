package com.fbr1.mesasutnfrro.model.data;


import com.fbr1.mesasutnfrro.model.entity.VisitedURL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisitedURLsData {

    private final static Logger logger = LoggerFactory.getLogger(VisitedURLsData.class);


    public Set<String> getAll(){

        Session session = null;
        Transaction tx = null;
        List<VisitedURL> visitedURLs = null;
        Set<String> urlList = new HashSet<>();

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            Query query = session.createQuery("from VisitedURL");

            visitedURLs = query.list();
            for (VisitedURL vu : visitedURLs){
                urlList.add(vu.getUrl());
            }
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

        return urlList;
    }

    public void addAll(Set<String> URLs){

        Session session = null;
        Transaction tx = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            int i = 0;
            for ( String url : URLs) {

                VisitedURL visitedURL = new VisitedURL(url);
                session.save(visitedURL);

                if ( i % 20 == 0 ) {
                    // Clear cache
                    session.flush();
                    session.clear();
                }
                i++;
            }
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
