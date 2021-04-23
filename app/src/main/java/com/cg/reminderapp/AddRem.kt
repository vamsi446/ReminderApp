package com.cg.reminderapp

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.cg.androidstorage.DBWrapper
import kotlinx.android.synthetic.main.activity_add_rem.*
import java.text.SimpleDateFormat
import java.util.*



class AddRem : AppCompatActivity() ,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_rem1)
    }


    fun dateClick(view: View) {
        val calendar= Calendar.getInstance()
        DatePickerDialog(this,this,calendar.get(
            Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
    fun timeClick(view: View) {

        TimePickerDialog(this, this ,12,0,false).show()


    }
    override fun onTimeSet(view: TimePicker?, p1: Int, p2: Int) {
        Toast.makeText(this,"TIME : $p1 hrs $p2 minutes", Toast.LENGTH_SHORT).show()
        timeTV.text="$p1:$p2"

    }

    override fun onDateSet(view: DatePicker?, y: Int, m: Int, d: Int) {
        var M=m+1
        Toast.makeText(this,"DATE : $d-$M-$y", Toast.LENGTH_SHORT).show()
        dateTV.text="$d-$M-$y"
    }

    fun addClick(view: View) {
        val title=titleE.text.toString()
        val description=descE.text.toString()
        val date=dateTV.text.toString()
        val time=timeTV.text.toString()
        var dateAndTime=date+" $time"
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var dateObj=sdf.parse(dateAndTime) as Date
        var millisTime=dateObj.time
        val pref=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val beforeTime=pref.getLong("before",0)
        millisTime-=beforeTime


        if(title.isNotEmpty()&&
            date!="Selected Date"
            &&time!="Selected Time" && description.isNotEmpty()) {
            addToCalendar(title, millisTime, description)
            // val rem=Reminder(title,date,time,description)
            // remList.add(rem)
            val wrapper = DBWrapper(this)
            wrapper.saveReminder(title, date, time, description)
            Toast.makeText(this, "Title : $title \nDate : $date \nTime : $time \nDescription : $description",
                    Toast.LENGTH_LONG).show()
            //launchCalendar(title,millis)

            var alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            //val bIntent=Intent(MY_BROADCAST_SCHEDULE_ACTION)
            val bIntent = Intent(this, MyAlarmReceiver::class.java)
            //val cName=ComponentName("com.cg.reminderapp","MyAlarmReceiver")
            //bIntent.setComponent(cName)
            bIntent.putExtra("title", title)
            bIntent.putExtra("date", date)
            bIntent.putExtra("time", time)
            bIntent.putExtra("description", description)

            val cursor = wrapper.retrieveReminders()
            var id=idOfReminder(title,this)///defined as global function
            bIntent.putExtra("reqCode",id)
            var pi=PendingIntent.getBroadcast(this,id,bIntent,0)

            alarmManager.setExact(AlarmManager.RTC,millisTime,pi)
            Log.d("MainActivity",title)
            Log.d("MainActivity","$id")

            sendNotification()


        }
        else
        {
            Toast.makeText(this,"Enter all the Details!",Toast.LENGTH_LONG).show()
        }


    }



    private fun addToCalendar(title: String, millisTime: Long, description: String) {
        val cr=contentResolver
        val uri=android.provider.CalendarContract.Events.CONTENT_URI
        val cValues=ContentValues()
        cValues.put(CalendarContract.Events.CALENDAR_ID,1)
        cValues.put(CalendarContract.Events.TITLE,title)
        cValues.put(CalendarContract.Events.DTSTART,millisTime)
        cValues.put(CalendarContract.Events.DTEND,(millisTime+(60*60*1000)))
        cValues.put(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().timeZone.id)
        cr.insert(uri,cValues)


    }
    private fun launchCalendar(title: String,millisTime: Long)
    {
        val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)

                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,millisTime )
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME,millisTime+(60*60*1000) )


            }
            startActivity(intent)
    }
    private fun sendNotification() {
        val nManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var builder: Notification.Builder
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            val channel= NotificationChannel("test","Reminder"
                , NotificationManager.IMPORTANCE_HIGH)

            nManager.createNotificationChannel(channel)
            builder= Notification.Builder(this,"test")
        }
        else{
            builder= Notification.Builder(this)
        }
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setContentTitle("New Reminder Added")
        builder.setContentText("Click to view details")

        val intent = Intent(this,ViewRem::class.java)
        var pendInt= PendingIntent.getActivity(this,0,intent,0)
        val myNotification =builder.build()
        builder.setContentIntent(pendInt)
        nManager.notify(1,myNotification)

    }

    fun cancelClick(view: View) {
        var builder= AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to go back?")
        builder.setPositiveButton("No")  {
                dlg, i->
            var i= Intent(this,AddRem::class.java)
            startActivity(i)


        }
        builder.setNegativeButton("Yes") {dlg,i ->
            dlg.cancel()
            finish()
        }
        builder.create().show()

    }




}
fun idOfReminder(reqTitle:String,context: Context):Int
{
    val wrapper=DBWrapper(context)
    val cursor=wrapper.retrieveReminders()
    cursor?.moveToFirst()
    if (cursor?.columnCount!! > 0) {
        if(cursor?.moveToFirst()) {

            do {

                val title = cursor?.getString(cursor?.getColumnIndex(DBHelper.CLM_TITLE))
                if(title==reqTitle)
                    return cursor?.getInt(cursor?.getColumnIndex(DBHelper.CLM_id))


            } while (cursor?.moveToNext()==true)
        }
    }
    return -1
}
