package com.univer.onlinestore.data.product.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "productDatabase.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_ENTRIES = """
            CREATE TABLE ${ProductContract.ProductEntry.TABLE_NAME} (
                ${ProductContract.ProductEntry.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${ProductContract.ProductEntry.COLUMN_NAME} TEXT NOT NULL,
                ${ProductContract.ProductEntry.COLUMN_DESCRIPTION} TEXT,
                ${ProductContract.ProductEntry.COLUMN_PRICE} REAL NOT NULL,
                ${ProductContract.ProductEntry.COLUMN_IMAGES} TEXT,
                ${ProductContract.ProductEntry.COLUMN_CURRENCY} TEXT,
                ${ProductContract.ProductEntry.COLUMN_CATEGORY} TEXT
            )
        """

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ProductContract.ProductEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}