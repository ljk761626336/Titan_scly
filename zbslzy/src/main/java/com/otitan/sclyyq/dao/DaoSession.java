package com.otitan.sclyyq.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig ymDaoConfig;
    private final DaoConfig jgdcDaoConfig;
    private final DaoConfig customerDaoConfig;
    private final DaoConfig orderDaoConfig;

    private final YmDao ymDao;
    private final JgdcDao jgdcDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        ymDaoConfig = daoConfigMap.get(YmDao.class).clone();
        ymDaoConfig.initIdentityScope(type);

        jgdcDaoConfig = daoConfigMap.get(JgdcDao.class).clone();
        jgdcDaoConfig.initIdentityScope(type);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        ymDao = new YmDao(ymDaoConfig, this);
        jgdcDao = new JgdcDao(jgdcDaoConfig, this);
        customerDao = new CustomerDao(customerDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);

        registerDao(Ym.class, ymDao);
        registerDao(Jgdc.class, jgdcDao);
        registerDao(Customer.class, customerDao);
        registerDao(Order.class, orderDao);
    }
    
    public void clear() {
        ymDaoConfig.getIdentityScope().clear();
        jgdcDaoConfig.getIdentityScope().clear();
        customerDaoConfig.getIdentityScope().clear();
        orderDaoConfig.getIdentityScope().clear();
    }

    public YmDao getYmDao() {
        return ymDao;
    }

    public JgdcDao getJgdcDao() {
        return jgdcDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

}
