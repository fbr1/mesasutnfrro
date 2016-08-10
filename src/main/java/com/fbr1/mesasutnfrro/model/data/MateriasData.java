package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Materia;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class MateriasData {

    public List<Materia> getAll(){

        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Materia");

        List<Materia> materiasList = query.list();
        tx.commit();

        return materiasList;
    }
}
