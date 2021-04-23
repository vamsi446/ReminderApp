package com.cg.reminderapp

import android.os.Parcelable
import java.io.Serializable

var remList= mutableListOf<Reminder>()
data class Reminder(var title:String, var date:String, var time:String, var description:String)
{

}