package com.cg.reminderapp

import android.app.AlarmManager
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
val MY_BROADCAST_SCHEDULE_ACTION="com.cg.androidreceiver.action.scheduled"
var alarmReceiver=MyAlarmReceiver()
lateinit var pref: SharedPreferences
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter= IntentFilter(MY_BROADCAST_SCHEDULE_ACTION)

       // registerReceiver(alarmReceiver,filter)
        setContentView(R.layout.activity_main1)
    }
   val MENU_CONTACT_US=1
    val MENU_SETTINGS=2
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val contactUsItem= menu?.add(1,MENU_CONTACT_US,2,"CONTACT US")
        //contactUsItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        val settings =menu?.add(1,MENU_SETTINGS,1,"SETTINGS")
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            MENU_CONTACT_US->{
                var i=Intent(this,ContactUs::class.java)
                startActivity(i)
            }
            MENU_SETTINGS->{
                var i=Intent(this,SettingsActivity::class.java)
                startActivity(i)
            }

        }

        return super.onOptionsItemSelected(item)
    }
    fun onAddClick(view: View) {
        val i= Intent(this,AddRem::class.java)
        startActivity(i)

    }
    fun onViewClick(view: View) {
        val i= Intent(this,ViewRem::class.java)
        startActivity(i)
    }

    override fun onDestroy() {
        //unregisterReceiver(alarmReceiver)
        super.onDestroy()
    }
}