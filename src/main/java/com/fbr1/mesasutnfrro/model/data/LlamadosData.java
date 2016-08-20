package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LlamadosData {

    private final static Logger logger = LoggerFactory.getLogger(LlamadosData.class);

    public List<Llamado> getAll(){
        Session session = null;
        Transaction tx = null;
        List<Llamado> llamadosList = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            Query query = session.createQuery("from Llamado");

            llamadosList = query.list();
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

        return llamadosList;
    }

    public List<Llamado> getLlamadosOfYear(int year){
        Session session = null;
        Transaction tx = null;
        List<Llamado> llamadosList = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            Query query = session.createQuery("from Llamado l where l.año =:año" )
                    .setParameter("año", year);

            llamadosList = query.list();
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

        return llamadosList;
    }

    public Llamado getLlamado(int year, int numero){
        Session session = null;
        Transaction tx = null;
        Llamado llamado = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            Query query = session.createQuery("from Llamado l " +
                    "where l.año =:año and l.numero=:numero" )
                    .setParameter("año", year)
                    .setParameter("numero", numero);

            llamado = (Llamado) query.getSingleResult();
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

        return llamado;
    }

    public Llamado getlastLlamado(){
        Session session = null;
        Transaction tx = null;
        Llamado llamado = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            Query query = session.createQuery("from Llamado l " +
                    "where l.date = " +
                    "(select max(ll.date) from Llamado ll)");

            llamado = (Llamado) query.getSingleResult();
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

        return llamado;

    }

    public void add(Llamado llamado){
        Session session = null;
        Transaction tx = null;

        try{
            SessionFactory sessionFactory = HibernateManager.getSessionFactory();
            session = sessionFactory.getCurrentSession();

            tx = session.beginTransaction();

            session.save(llamado);

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