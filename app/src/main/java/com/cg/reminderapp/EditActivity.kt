package com.cg.reminderapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.cg.androidstorage.DBWrapper
import kotlinx.android.synthetic.main.activity_add_rem.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit)
        val intent=intent
        val title=intent.getStringExtra("title")!!
        val description=intent.getStringExtra("desc")!!
        val date=intent.getStringExtra("date")!!
        val time=intent.getStringExtra("time")!!
        val index=intent.getIntExtra("position",0)
        val wrapper=DBWrapper(this)
        wrapper.deleteReminder(title)
        edTitleE.setText(title)
        edDateTV.setText(date)
        edTimeTV.setText(time)
        edDescE.setText(description)

    }

    fun editClick(view: View) {

        var editedTitle=edTitleE.text.toString()
        var editedDate=edDateTV.text.toString()
        var editedTime=edTimeTV.text.toString()
        var editedDesc=edDescE.text.toString()

        if(editedTitle.isNotEmpty()&& editedDate.isNotEmpty()&&editedTime.isNotEmpty() &&editedDesc.isNotEmpty())
        {

            val editedRem=Reminder(editedTitle,editedDate,editedTime,editedDesc)
            val intent=intent
            val index=intent.getIntExtra("position",0)
            val wrapper=DBWrapper(this)
            wrapper.saveReminder(editedTitle,editedDate,editedTime,editedDesc)
            remList.set(index,editedRem)
            adapter.notifyDataSetChanged()

            

            Toast.makeText(this,
                "Title : $editedTitle \nDate : $editedDate \nTime : $editedTime", Toast.LENGTH_LONG).show()
            finish()

        }
        else
        {
            Toast.makeText(this,"Enter all the Details!", Toast.LENGTH_LONG).show()
        }

    }
    override fun onTimeSet(view: TimePicker?, p1: Int, p2: Int) {
        Toast.makeText(this,"TIME : $p1 hrs $p2 minutes", Toast.LENGTH_SHORT).show()
        edTimeTV.text="$p1:$p2"

    }

    override fun onDateSet(view: DatePicker?, y: Int, m: Int, d: Int) {
        Toast.makeText(this,"DATE : $d-$m-$y", Toast.LENGTH_SHORT).show()
        edDateTV.text="$d-$m-$y"
    }
    fun cancelEdit(view: View) {
        finish()
    }

    fun editTimeClick(view: View) {
        TimePickerDialog(this, this ,12,0,false).show()

    }
    fun editDateClick(view: View) {
        val calendar= Calendar.getInstance()
        DatePickerDialog(this,this,calendar.get(
            Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}