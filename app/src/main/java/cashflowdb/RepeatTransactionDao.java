package cashflowdb;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import cashflowdb.RepeatTransaction;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REPEAT_TRANSACTION".
*/
public class RepeatTransactionDao extends AbstractDao<RepeatTransaction, Long> {

    public static final String TABLENAME = "REPEAT_TRANSACTION";

    /**
     * Properties of entity RepeatTransaction.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Pound = new Property(1, Double.class, "pound", false, "POUND");
        public final static Property Transactiondate = new Property(2, java.util.Date.class, "transactiondate", false, "TRANSACTIONDATE");
        public final static Property Recomments = new Property(3, String.class, "recomments", false, "RECOMMENTS");
        public final static Property Confirm = new Property(4, Boolean.class, "confirm", false, "CONFIRM");
        public final static Property CatID = new Property(5, Long.class, "catID", false, "CAT_ID");
        public final static Property ActualtrasactionID = new Property(6, long.class, "actualtrasactionID", false, "ACTUALTRASACTION_ID");
    };

    private DaoSession daoSession;

    private Query<RepeatTransaction> inOutTransaction_RepeatTransactionListQuery;

    public RepeatTransactionDao(DaoConfig config) {
        super(config);
    }
    
    public RepeatTransactionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REPEAT_TRANSACTION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"POUND\" REAL," + // 1: pound
                "\"TRANSACTIONDATE\" INTEGER," + // 2: transactiondate
                "\"RECOMMENTS\" TEXT," + // 3: recomments
                "\"CONFIRM\" INTEGER," + // 4: confirm
                "\"CAT_ID\" INTEGER," + // 5: catID
                "\"ACTUALTRASACTION_ID\" INTEGER NOT NULL );"); // 6: actualtrasactionID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REPEAT_TRANSACTION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, RepeatTransaction entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Double pound = entity.getPound();
        if (pound != null) {
            stmt.bindDouble(2, pound);
        }
 
        java.util.Date transactiondate = entity.getTransactiondate();
        if (transactiondate != null) {
            stmt.bindLong(3, transactiondate.getTime());
        }
 
        String recomments = entity.getRecomments();
        if (recomments != null) {
            stmt.bindString(4, recomments);
        }
 
        Boolean confirm = entity.getConfirm();
        if (confirm != null) {
            stmt.bindLong(5, confirm ? 1L: 0L);
        }
 
        Long catID = entity.getCatID();
        if (catID != null) {
            stmt.bindLong(6, catID);
        }
        stmt.bindLong(7, entity.getActualtrasactionID());
    }

    @Override
    protected void attachEntity(RepeatTransaction entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public RepeatTransaction readEntity(Cursor cursor, int offset) {
        RepeatTransaction entity = new RepeatTransaction( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1), // pound
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // transactiondate
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // recomments
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0, // confirm
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // catID
            cursor.getLong(offset + 6) // actualtrasactionID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, RepeatTransaction entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPound(cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1));
        entity.setTransactiondate(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setRecomments(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setConfirm(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
        entity.setCatID(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setActualtrasactionID(cursor.getLong(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(RepeatTransaction entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(RepeatTransaction entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "repeatTransactionList" to-many relationship of InOutTransaction. */
    public List<RepeatTransaction> _queryInOutTransaction_RepeatTransactionList(long actualtrasactionID) {
        synchronized (this) {
            if (inOutTransaction_RepeatTransactionListQuery == null) {
                QueryBuilder<RepeatTransaction> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.ActualtrasactionID.eq(null));
                inOutTransaction_RepeatTransactionListQuery = queryBuilder.build();
            }
        }
        Query<RepeatTransaction> query = inOutTransaction_RepeatTransactionListQuery.forCurrentThread();
        query.setParameter(0, actualtrasactionID);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getInOutTransactionDao().getAllColumns());
            builder.append(" FROM REPEAT_TRANSACTION T");
            builder.append(" LEFT JOIN IN_OUT_TRANSACTION T0 ON T.\"ACTUALTRASACTION_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected RepeatTransaction loadCurrentDeep(Cursor cursor, boolean lock) {
        RepeatTransaction entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        InOutTransaction inOutTransaction = loadCurrentOther(daoSession.getInOutTransactionDao(), cursor, offset);
         if(inOutTransaction != null) {
            entity.setInOutTransaction(inOutTransaction);
        }

        return entity;    
    }

    public RepeatTransaction loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<RepeatTransaction> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<RepeatTransaction> list = new ArrayList<RepeatTransaction>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<RepeatTransaction> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<RepeatTransaction> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
