package com.cg.reminderapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

val PREF_NAME="BeforeTime"
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun radioClick(view: View) {
        when(view.id)
        {
            R.id.before10R->{
                changeBeforeTime(10)
                Toast.makeText(this,"Your ALL Reminders will be 10min early",Toast.LENGTH_SHORT).show()

            }
            R.id.before30R->{
                changeBeforeTime(30)
                Toast.makeText(this,"Your ALL Reminders will be 30min early",Toast.LENGTH_SHORT).show()
            }
            R.id.before60R->{
                changeBeforeTime(60)
                Toast.makeText(this,"Your ALL Reminders will be 1 hour early",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun changeBeforeTime(min:Int) {
        val pref=getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor=pref.edit()
        editor.apply {

            putLong("before",(min.toLong())*60*1000)
            commit()
        }


    }
}