package com.cg.reminderapp
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

class DBHelper(context:Context):
    SQLiteOpenHelper(context,"reminder.db",null,1)
{
    companion object{
        val TABLE_NAME="reminderTable"
        val CLM_TITLE="title"
        val CLM_DATE="date"
        val CLM_TIME="time"
        val CLM_DESCRIPTION="description"
        val CLM_id="_id"

        val TABLE_QUERY="create table  $TABLE_NAME($CLM_id INTEGER PRIMARY KEY AUTOINCREMENT,$CLM_TITLE text, $CLM_DATE text, $CLM_TIME text, $CLM_DESCRIPTION text)"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        //executed first time;table creation
        try{ db?.execSQL(TABLE_QUERY) }
        catch(e:Exception)
        {
            Log.d("DBHelper","ERROR creating table: ${e.message}")
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //create a new table
        //existing table-changes to schema
        //drop an old table and create a new one

    }

}