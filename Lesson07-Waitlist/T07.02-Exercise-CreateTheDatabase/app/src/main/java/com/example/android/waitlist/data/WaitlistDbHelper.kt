package com.example.android.waitlist.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WaitlistDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        val DATABASE_NAME = "waitlist.db"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        val SQL_CREATE_WAITLIST_TABLE =
                "CREATE TABLE ${WaitlistContract.WaitlistEntry.TABLE_NAME} ( " +
                        "${WaitlistContract.WaitlistEntry.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "${WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME} TEXT NOT NULL," +
                        "${WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE} INT NOT NULL," +
                        "${WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP} TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");"

        sqLiteDatabase?.execSQL(SQL_CREATE_WAITLIST_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, prevVersion: Int, newVersion: Int) {
        sqLiteDatabase?.execSQL("DROP TABLE ${WaitlistContract.WaitlistEntry.TABLE_NAME}")
        onCreate(sqLiteDatabase)
    }
}