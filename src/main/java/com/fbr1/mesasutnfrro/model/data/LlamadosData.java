package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Llamado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;

public class LlamadosData {

    private final static Logger logger = LoggerFactory.getLogger(LlamadosData.class);

    public List<Llamado> getAll(){

        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Llamado");

        List<Llamado> llamadosList = query.list();
        tx.commit();
        session.close();

        return llamadosList;
    }

    public List<Llamado> getLlamadosOfYear(int year){
        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Llamado l where l.año =:año" )
                             .setParameter("año", year);

        List<Llamado> llamadosList = query.list();
        tx.commit();
        session.close();

        return llamadosList;
    }

    public Llamado getLlamado(int year, int numero){
        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Llamado l " +
                "where l.año =:año and l.numero=:numero" )
                .setParameter("año", year)
                .setParameter("numero", numero);

        Llamado llamado = (Llamado) query.getSingleResult();
        tx.commit();
        session.close();

        return llamado;
    }

    public Llamado getlastLlamado(){
        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Llamado l " +
                                          "where l.date = " +
                                          "(select max(ll.date) from Llamado ll)");

        Llamado llamado = (Llamado) query.getSingleResult();
        tx.commit();
        session.close();

        return llamado;
    }

}