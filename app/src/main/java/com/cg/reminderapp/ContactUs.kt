package com.cg.reminderapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ContactUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
    }

    fun buttonClick(view: View) {
        val intent:Intent
        when(view.id)
        {
            R.id.callIB->{
                intent= Intent(Intent.ACTION_DIAL, Uri.parse("tel:7989067848"))
                startActivity(intent)
            }
            R.id.smsIB->{
                intent=Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:7989067848"))
                startActivity(intent)
            }
            R.id.mailIB->{
                intent=Intent(Intent.ACTION_SENDTO,Uri.parse("mailto: vamsirevuri11@gmail.com"))
                startActivity(intent)
            }
        }

    }
}