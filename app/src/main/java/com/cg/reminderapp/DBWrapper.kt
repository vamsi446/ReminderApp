package com.cg.androidstorage


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.cg.reminderapp.DBHelper

class DBWrapper(val context: Context){
    val helper= DBHelper(context)//db and table available
    val db:SQLiteDatabase=helper.writableDatabase
    fun saveReminder(title:String, date:String, time:String, description:String):Long
    {//insert
        val cValues= ContentValues()
        cValues.put(DBHelper.CLM_TITLE,title)
        cValues.put(DBHelper.CLM_DATE,date)
        cValues.put(DBHelper.CLM_TIME,time)
        cValues.put(DBHelper.CLM_DESCRIPTION,description)

        return db.insert(DBHelper.TABLE_NAME,null,cValues)


    }

    fun retrieveReminders(): Cursor
    { //query
        val clms= arrayOf(DBHelper.CLM_id,DBHelper.CLM_TITLE,DBHelper.CLM_DATE, DBHelper.CLM_TIME, DBHelper.CLM_DESCRIPTION)
        return db.query(DBHelper.TABLE_NAME,clms,null,null,null,null,null)

    }
    fun deleteReminder(title: String)
    {
        val args= arrayOf(title)
        db.delete(DBHelper.TABLE_NAME,"${DBHelper.CLM_TITLE}= ?",args)
    }
    fun deleteAll()
    {
        db.delete(DBHelper.TABLE_NAME,null,null)
    }


}