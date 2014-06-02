package com.ishibiki.isalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "mydata.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "mydata";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TEL = "tel";
	public static final String[] COL_ARR = {ID,NAME,TEL};

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "create table " + TABLE_NAME + "(" + ID
				+ " INTEGER PRIMARY KEY,"+ NAME + " TEXT," + TEL + " TEXT);";
		db.execSQL(query);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists"+ TABLE_NAME);
		onCreate(db);
	}
}
