package cashflowdb;

import cashflowdb.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "REPEAT_TRANSACTION".
 */
public class RepeatTransaction {

    private Long id;
    private Double pound;
    private java.util.Date transactiondate;
    private String recomments;
    private Boolean confirm;
    private Long catID;
    private long actualtrasactionID;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RepeatTransactionDao myDao;

    private InOutTransaction inOutTransaction;
    private Long inOutTransaction__resolvedKey;


    public RepeatTransaction() {
    }

    public RepeatTransaction(Long id) {
        this.id = id;
    }

    public RepeatTransaction(Long id, Double pound, java.util.Date transactiondate, String recomments, Boolean confirm, Long catID, long actualtrasactionID) {
        this.id = id;
        this.pound = pound;
        this.transactiondate = transactiondate;
        this.recomments = recomments;
        this.confirm = confirm;
        this.catID = catID;
        this.actualtrasactionID = actualtrasactionID;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRepeatTransactionDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPound() {
        return pound;
    }

    public void setPound(Double pound) {
        this.pound = pound;
    }

    public java.util.Date getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(java.util.Date transactiondate) {
        this.transactiondate = transactiondate;
    }

    public String getRecomments() {
        return recomments;
    }

    public void setRecomments(String recomments) {
        this.recomments = recomments;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public Long getCatID() {
        return catID;
    }

    public void setCatID(Long catID) {
        this.catID = catID;
    }

    public long getActualtrasactionID() {
        return actualtrasactionID;
    }

    public void setActualtrasactionID(long actualtrasactionID) {
        this.actualtrasactionID = actualtrasactionID;
    }

    /** To-one relationship, resolved on first access. */
    public InOutTransaction getInOutTransaction() {
        long __key = this.actualtrasactionID;
        if (inOutTransaction__resolvedKey == null || !inOutTransaction__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InOutTransactionDao targetDao = daoSession.getInOutTransactionDao();
            InOutTransaction inOutTransactionNew = targetDao.load(__key);
            synchronized (this) {
                inOutTransaction = inOutTransactionNew;
            	inOutTransaction__resolvedKey = __key;
            }
        }
        return inOutTransaction;
    }

    public void setInOutTransaction(InOutTransaction inOutTransaction) {
        if (inOutTransaction == null) {
            throw new DaoException("To-one property 'actualtrasactionID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.inOutTransaction = inOutTransaction;
            actualtrasactionID = inOutTransaction.getId();
            inOutTransaction__resolvedKey = actualtrasactionID;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
