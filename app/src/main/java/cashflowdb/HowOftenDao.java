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

import cashflowdb.HowOften;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HOW_OFTEN".
*/
public class HowOftenDao extends AbstractDao<HowOften, Long> {

    public static final String TABLENAME = "HOW_OFTEN";

    /**
     * Properties of entity HowOften.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Often = new Property(1, String.class, "often", false, "OFTEN");
        public final static Property Sourceid = new Property(2, long.class, "sourceid", false, "SOURCEID");
    };

    private DaoSession daoSession;

    private Query<HowOften> source_HowOftenListQuery;

    public HowOftenDao(DaoConfig config) {
        super(config);
    }
    
    public HowOftenDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HOW_OFTEN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"OFTEN\" TEXT," + // 1: often
                "\"SOURCEID\" INTEGER NOT NULL );"); // 2: sourceid
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HOW_OFTEN\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HowOften entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String often = entity.getOften();
        if (often != null) {
            stmt.bindString(2, often);
        }
        stmt.bindLong(3, entity.getSourceid());
    }

    @Override
    protected void attachEntity(HowOften entity) {
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
    public HowOften readEntity(Cursor cursor, int offset) {
        HowOften entity = new HowOften( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // often
            cursor.getLong(offset + 2) // sourceid
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HowOften entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOften(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSourceid(cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HowOften entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HowOften entity) {
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
    
    /** Internal query to resolve the "howOftenList" to-many relationship of Source. */
    public List<HowOften> _querySource_HowOftenList(long sourceid) {
        synchronized (this) {
            if (source_HowOftenListQuery == null) {
                QueryBuilder<HowOften> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Sourceid.eq(null));
                source_HowOftenListQuery = queryBuilder.build();
            }
        }
        Query<HowOften> query = source_HowOftenListQuery.forCurrentThread();
        query.setParameter(0, sourceid);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getSourceDao().getAllColumns());
            builder.append(" FROM HOW_OFTEN T");
            builder.append(" LEFT JOIN SOURCE T0 ON T.\"SOURCEID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected HowOften loadCurrentDeep(Cursor cursor, boolean lock) {
        HowOften entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Source source = loadCurrentOther(daoSession.getSourceDao(), cursor, offset);
         if(source != null) {
            entity.setSource(source);
        }

        return entity;    
    }

    public HowOften loadDeep(Long key) {
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
    public List<HowOften> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<HowOften> list = new ArrayList<HowOften>(count);
        
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
    
    protected List<HowOften> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<HowOften> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
