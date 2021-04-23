package com.cg.reminderapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.cg.androidstorage.DBWrapper
import kotlinx.android.synthetic.main.activity_add_rem.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_rem.*
import kotlinx.android.synthetic.main.reminder_item_1.*
import java.text.SimpleDateFormat
import java.util.*
lateinit var selectedReminder:Reminder
lateinit var adapter:ArrayAdapter<Reminder>
lateinit var newAdapter: SimpleCursorAdapter
lateinit var wrapper:DBWrapper
class ViewRem : AppCompatActivity() ,AdapterView.OnItemClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_rem)
        wrapper= DBWrapper(this)
        remList.clear()

        fillList()
        adapter = ReminderAdapter(this, R.layout.reminder_item_1, remList)
        lv.adapter = adapter

        //val cursor=wrapper.retrieveReminders()
        //cursor.moveToFirst()
       // var from= arrayOf(DBHelper.CLM_id,DBHelper.CLM_TITLE,DBHelper.CLM_DATE,DBHelper.CLM_TIME,DBHelper.CLM_DESCRIPTION)

       // var to= intArrayOf(R.id.titleTV,R.id.remDateTV,R.id.remTimeTV,R.id.descriptionTV)

      // newAdapter= SimpleCursorAdapter(this,R.layout.reminder_item_1,cursor,from,to)
        //lv.adapter= newAdapter
      lv.setOnItemClickListener(this)
    }

    fun fillList()
    {

        val cursor=wrapper.retrieveReminders()

        if (cursor.columnCount > 0) {
            if(cursor.moveToFirst()) {

                do {
                    val title = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TITLE))
                    val date = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_DATE))
                    val time=cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TIME))
                    val description = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_DESCRIPTION))
                    remList.add(Reminder(title, date, time, description))

                } while (cursor.moveToNext())
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedReminder= remList[position]

        var builder=AlertDialog.Builder(this)
        builder.setMessage("Do you want to make changes?")
        builder.setPositiveButton("EDIT")  {
                dlg, i->
            
            var i= Intent(this,EditActivity::class.java)
            i.putExtra("position",position)
            i.putExtra("title", selectedReminder.title)
            i.putExtra("date", selectedReminder.date)
            i.putExtra("time", selectedReminder.time)
            i.putExtra("desc", selectedReminder.description)

            startActivity(i)



        }
        builder.setNegativeButton("DELETE") {dlg,i ->
            remList.removeAt(position)
            val cr=contentResolver
            val uri=android.provider.CalendarContract.Events.CONTENT_URI
            val args= arrayOf(selectedReminder.title)


            cr.delete(uri,"${android.provider.CalendarContract.Events.TITLE}=?",args)



            adapter.notifyDataSetChanged()
            Toast.makeText(this,"Selected Reminder with Deleted ",Toast.LENGTH_SHORT).show()
            var alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val bIntent=Intent(this,MyAlarmReceiver::class.java)
            //val cName= ComponentName("com.cg.reminderapp","MyAlarmReceiver")
            //bIntent.setComponent(cName)
            bIntent.putExtra("title", selectedReminder.title)
            bIntent.putExtra("date", selectedReminder.date)
            bIntent.putExtra("time", selectedReminder.time)
            bIntent.putExtra("description", selectedReminder.description)
            var dateAndTime= selectedReminder.date+" ${selectedReminder.time}"
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
            var dateObj=sdf.parse(dateAndTime) as Date
            var millisTime=dateObj.time
            val pref=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val beforeTime=pref.getLong("before",0)
            millisTime-=beforeTime
            var reqcode=idOfReminder(selectedReminder.title,this)
            var wrapper=DBWrapper(this)
            wrapper.deleteReminder(selectedReminder.title)

            var pi= PendingIntent.getBroadcast(this,reqcode,bIntent,0)
            alarmManager.setExact(AlarmManager.RTC,millisTime,pi)
            alarmManager.cancel(pi)


        }
        builder.setNeutralButton("Cancel"){dlg,i->dlg.cancel()}
        builder.create().show()

    }



    class ReminderAdapter(context: Context, val layoutRes:Int, val data:List<Reminder>):ArrayAdapter<Reminder>(context,layoutRes,data)
    {
        override fun getItem(position: Int): Reminder? {
            return data[position]
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val reminder=getItem(position)
            lateinit var view:View

            view=convertView?: LayoutInflater.from(context).inflate(layoutRes,null)
            val titleT=view.findViewById<TextView>(R.id.titleTV)
            val dateT=view.findViewById<TextView>(R.id.remDateTV)
            val timeT=view.findViewById<TextView>(R.id.remTimeTV)
            val descT=view.findViewById<TextView>(R.id.descriptionTV)

            titleT.setText(reminder?.title)
            dateT.setText(reminder?.date)
            timeT.setText(reminder?.time)
            descT.setText(reminder?.description)
            //return super.getView(position, convertView, parent)
            return view
            
        }

    }


}