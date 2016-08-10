package com.fbr1.mesasutnfrro.model.data;

import com.fbr1.mesasutnfrro.model.entity.Mesa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MesasData {
    private final static Logger logger = LoggerFactory.getLogger(MesasData.class);

    public List<Mesa> getAll(){

        SessionFactory sessionFactory = HibernateManager.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Mesa");
        List<Mesa> mesasList = query.list();
        sessionFactory.close();

        return mesasList;
    }
}
