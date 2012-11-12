package com.lbx.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	//数据库路径
	public static final String DB_NAME_PATH = "";
	
	//数据库名称
	private static final String DB_NAME = "fan107.db";
	
	public static final String USER_TABLE_NAME = "users";
	public static final String RECEIVE_ADDRESS_TBL_NAME = "receive_address";
	
	//创建user数据表
	private static final String CREATE_TABLE_USER = "create table if not exists " + USER_TABLE_NAME 
		+ " (id int primary key," 
		+ "usergroup int, " 
		+ "username varchar(20),"
		+ "userpass varchar(32)," 
		+ "nickname varchar(20)," 
		+ "gender int,"  
		+ "birthday varchar(20),"       
		+ "totalpoint decimal(8, 1),"       
		+ "currentpoint decimal(8, 1,"       
		+ "usepoint decimal(8, 1),"       
		+ "spreadcount int,"       
		+ "mobile varchar(20),"       
		+ "email varchar(100),"       
		+ "address varchar(200),"       
		+ "utype int,"       
		+ "shopid int,"       
		+ "addtime datetime"
		+ ")";

	private SQLiteDatabase mDatabase;

	public DBHelper(Context context) {
		super(context, DB_NAME_PATH + DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mDatabase = db;
		mDatabase.execSQL(CREATE_TABLE_USER);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		mDatabase = db;
	}
	
	/**
	 * 往表里插入数据
	 * @param tableName
	 * @param contentValues
	 */
	public void insert(String tableName, ContentValues contentValues) {
		mDatabase = getWritableDatabase();
		mDatabase.insert(tableName, null, contentValues);
	}
	
	/**
	 * 通过sql语句往表里插入数据
	 * @param sql
	 */
	public void insert(String sql) {
		mDatabase = getWritableDatabase();
		mDatabase.execSQL(sql);
	}
	
	/**
	 * 查询表中的数据
	 * @param tableName
	 * @param columns
	 * @param orderBy
	 * @return
	 */
	public Cursor query(String tableName, String[] columns, String orderBy) {
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase
				.query(tableName, columns, null, null, null, null, orderBy);
		return c;
	}
	
	/**
	 * 通过sql语句查询表中的数据
	 * @param sql
	 * @return
	 */
	public Cursor query(String sql) {
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase.rawQuery(sql, null);
		return c;
	}


	/**
	 * 查询数据库中的值是否存在
	 * @param tableName
	 * @param word
	 * @return
	 */
	public boolean existInDB(String tableName, String key, String keyValue) {
		mDatabase = getReadableDatabase();
		Cursor c = mDatabase.query(tableName, null, key + "='" + keyValue + "'",
				null, null, null, null);

		if (!c.moveToFirst()) {
			return false;
		}

		return true;
	}

	/**
	 * 获取可写的数据库
	 * @return
	 */
	public SQLiteDatabase getWritableDB() {
		return getWritableDatabase();
	}
	
	/**
	 * 获取可读的数据库
	 */
	public SQLiteDatabase getReadableDB() {
		return getReadableDatabase();
	}

	/**
	 * 删除一张表
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName) {
		mDatabase = getReadableDatabase();
		mDatabase.execSQL("drop table if exists " + tableName);
	}
	
	/**
	 * 关闭数据库连接
	 */
	public void close() {
		if (mDatabase != null) {
			mDatabase.close();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	/**
	 * 更新表中的数据
	 * @param tableName
	 * @param setWhat
	 * @param whereValue
	 * SQL语句示例: update word_table set voice='123456' where word='baby';
	 */
	public void updateTable(String tableName, String setWhat, String whereValue) {
		mDatabase = getWritableDatabase();
		String SQL = "update ";
		SQL += tableName + " set " + setWhat + " where " + whereValue; 
		mDatabase.execSQL(SQL);
	}
	
	/**
	 * 通过sql语句更新表中的数据
	 * @param sql
	 */
	public void updateTable(String sql) {
		mDatabase = getWritableDatabase();
		mDatabase.execSQL(sql);
	}
	
	/**
	 * 删除表中的行
	 * @param tableName
	 * @param where
	 * SQL语句示例: DELETE FROM 表名称 WHERE 列名称 = 值
	 * DELETE FROM Person WHERE LastName = 'Wilson' 
	 */
	public void deleteTableContent(String tableName, String whereValue ) {
		mDatabase = getWritableDatabase();
		mDatabase.execSQL("delete from " + tableName + " where " + whereValue);
	}
	
	/**
	 * 通过sql语句删除表中的行
	 * @param sql
	 */
	public void deleteTableContent(String sql) {
		mDatabase = getWritableDatabase();
		mDatabase.execSQL(sql);
	}
}
