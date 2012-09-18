package com.lbx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBHelper extends SQLiteOpenHelper {
	
	// 数据库存放的路径
	public static final String DB_NAME_PATH = "";
	
	// 数据库名称
	private static final String DB_NAME = "fan107.db";
	public static final String RECEIVE_ADDRESS_TBL_NAME = "receive_address";
	public static final String ERROR_TBL_NAME = "ERROR_WORD";
	public static final String HISTORY_TBL_NAME = "history_word_table";

	private static final String SQL_CREATE_TABLE_WORD_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ RECEIVE_ADDRESS_TBL_NAME
			+ "("
			+ "_id integer PRIMARY KEY,"
			+ "user_name TEXT,"
			+ "area TEXT,"
			+ "address TEXT,"
			+ "moblie TEXT,"
			+ ");";

	private static final String SQL_CREATE_TABLE_ERROR_WORD_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ ERROR_TBL_NAME
			+ "("
			+ "_id integer PRIMARY KEY,"
			+ "word TEXT"
			+ ");";
	
	private static final String SQL_CREATE_HISTORY_WORD_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ HISTORY_TBL_NAME
			+ "("
			+ "_time time PRIMARY KEY,"
			+ "word TEXT"
			+ ");";
			

	private SQLiteDatabase mDatabase;

	public DBHelper(Context context) {
		super(context, DB_NAME_PATH + DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mDatabase = db;
		mDatabase.execSQL(SQL_CREATE_TABLE_WORD_TABLE);
		mDatabase.execSQL(SQL_CREATE_TABLE_ERROR_WORD_TABLE);
		mDatabase.execSQL(SQL_CREATE_HISTORY_WORD_TABLE);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		mDatabase = db;
		mDatabase.execSQL(SQL_CREATE_TABLE_WORD_TABLE);
		mDatabase.execSQL(SQL_CREATE_TABLE_ERROR_WORD_TABLE);
		mDatabase.execSQL(SQL_CREATE_HISTORY_WORD_TABLE);
	}

	public void insert(ContentValues contentValues) {
		mDatabase = getWritableDatabase();
		mDatabase.insert(TBL_NAME, null, contentValues);
	}

	public void insertErrorWord(ContentValues contentValues) {
		mDatabase = getWritableDatabase();
		mDatabase.insert(ERROR_TBL_NAME, null, contentValues);
	}
	
	/**
	 * 往表中插入数据
	 * @param tableName
	 * @param contentValues
	 */
	public void insert(String tableName, ContentValues contentValues) {
		mDatabase = getWritableDatabase();
		mDatabase.insert(tableName, null, contentValues);
	}

	public Cursor query() {
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase
				.query(TBL_NAME, null, null, null, null, null, null);
		return c;
	}

	public Cursor queryWord() {
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase.query(TBL_NAME, new String[] { "word",
				"mean AS _id" }, null, null, null, null, "word asc");
		return c;
	}

	// + "where word = " + word
	public Cursor queryWordRow(String word) {
		word = word.replace("'", "''");
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase.rawQuery("select * from " + TBL_NAME
				+ " where word = " + "'" + word + "'", null);
		return c;
	}
	
	public Cursor queryStr(String str) {
		str = str.replace("'", "''");
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase.rawQuery("select word AS _id from " + TBL_NAME + " where word like '" + str + "%' order by word limit 50" , null);
		
		return c;
	}
	
	public Cursor queryHistoryTable() {
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase.rawQuery("select word AS _id from " + HISTORY_TBL_NAME + " order by _time DESC", null);
		return c;
	}

	public boolean existInDB(String tableName, String word) {
		mDatabase = getReadableDatabase();
		String newWord = word.replace("'", "''"); // 转义字符 '
		Cursor c = mDatabase.query(tableName, null, "word='" + newWord + "'",
				null, null, null, null);

		if (!c.moveToFirst()) {
			return false;
		}

		return true;
	}

	public SQLiteDatabase getDB() {
		return getWritableDatabase();
	}

	/**
	 * 删除指定表
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName) {
		mDatabase = getReadableDatabase();
		mDatabase.execSQL("drop table if exists " + tableName);
	}

	public void close() {
		if (mDatabase != null) {
			mDatabase.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public void updateTable(String tableName, String setWhat, String where) {
		mDatabase = getWritableDatabase();
		String SQL = "update ";
		SQL += tableName + " set " + setWhat + " where " + where; 
		mDatabase.execSQL(SQL);
	}
	
	public void deleteTableContent(String tableName) {
		mDatabase = getWritableDatabase();
		mDatabase.execSQL("delete from " + tableName);
	}
	
	public void deleteNullWordContent() {
		mDatabase = getWritableDatabase();
		mDatabase.execSQL("delete from word_table where pron is null and voice is null and mean is null and examples is null");
		mDatabase.execSQL("delete from word_table where word like '%�%'");
	}
}
