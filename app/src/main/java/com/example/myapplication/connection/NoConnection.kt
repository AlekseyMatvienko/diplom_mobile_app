package com.example.myapplication.connection

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myapplication.Check
import com.example.myapplication.R
import com.example.myapplication.reg_auth.Authtorization

class NoConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)
        /*val pref = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        val mainText = findViewById<TextView>(R.id.textView)
        val text = findViewById<TextView>(R.id.textView2)
        val stateServer = pref?.getBoolean("serverOn", true)
        if (stateServer == false){
            mainText.text = "Ошибка сервера"
            text.text = "Ошибка подключения к серверу. Повторите попытку позже"
        }*/
        val update = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.button)
        update.setOnClickListener { update() }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun update(){
        if (Check().hasConnection(this)){
            val intent = Intent(this, Authtorization::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Нет подключения к интернету", Toast.LENGTH_SHORT).show()
        }
    }

}