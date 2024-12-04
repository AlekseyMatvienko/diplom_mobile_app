package com.example.myapplication.reg_auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.myapplication.*
import com.example.myapplication.connection.NoConnection
import com.example.myapplication.connection.Sockets
import com.example.myapplication.connection.Wsc
import com.example.myapplication.models.Account
import com.example.myapplication.models.Data
import com.example.myapplication.ui.MainActivity
import com.google.gson.GsonBuilder
import org.json.JSONObject
import kotlin.concurrent.thread


class Authtorization : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Check().hasConnection(this)){
            val intent = Intent(this, NoConnection::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_authtorization)

        val log = findViewById<AppCompatButton>(R.id.log)
        val reg = findViewById<AppCompatButton>(R.id.reg)

        log.setOnClickListener { login() }
        reg.setOnClickListener { registration() }


    }
    private var i = false
    private fun registration(){
        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
        finish()
    }


    private fun login() {
        i = false
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val log = findViewById<AppCompatButton>(R.id.log)
        progress.visibility = ProgressBar.VISIBLE
        log.visibility = AppCompatButton.INVISIBLE
        val login = findViewById<EditText>(R.id.login)
        val password = findViewById<EditText>(R.id.password)

        val checkAcc = JSONObject()

        checkAcc.put("id", "login")
        checkAcc.put("email", login.text.toString())
        checkAcc.put("password", password.text.toString())

        Wsc.webSocketClient.send(checkAcc.toString())

        while(Data.answer =="") {
            Thread.sleep(100)
            Log.d("Ещё 100", i.toString())
        }
        Log.d("mess", Data.answer)
        val builder = GsonBuilder()
        val gson = builder.create()
        val ans: Account = gson.fromJson(Data.answer, Account::class.java)
        Data.answer = ""
        if (ans.success){
            val sharedPref: SharedPreferences = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt("id", ans.userid)
            editor.putString("email", login.text.toString())
            editor.putString("password", password.text.toString())
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            progress.visibility = ProgressBar.INVISIBLE
            log.visibility = AppCompatButton.VISIBLE
            val err = findViewById<TextView>(R.id.error)
            err.visibility = TextView.VISIBLE
            password.text = null
        }
    }

    override fun onResume() {
        super.onResume()
        if (!Wsc.con) {
            Sockets().initWebSocket()
            Wsc.con = true
            thread {
                val sharedPref: SharedPreferences = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
                val id = sharedPref.getInt("id", 0)
                if (id != 0){
                    val fon = findViewById<ImageButton>(R.id.fon)
                    fon.visibility = View.VISIBLE
                    val mail = sharedPref.getString("email", "")
                    val pass = sharedPref.getString("password", "")
                    val checkAcc = JSONObject()

                    checkAcc.put("id", "login")
                    checkAcc.put("email", mail)
                    checkAcc.put("password", pass)
                    var connec = false
                    while ( !connec){
                        try {
                            Wsc.webSocketClient.send(checkAcc.toString())
                            connec = true
                        }
                        catch (e:Exception){
                            Log.e("err", e.toString())
                            Thread.sleep(200)
                        }
                    }


                    while(Data.answer =="") {
                        Thread.sleep(100)
                        Log.d("Ещё 100", i.toString())
                    }
                    Log.d("mess", Data.answer)
                    val builder = GsonBuilder()
                    val gson = builder.create()
                    val ans: Account = gson.fromJson(Data.answer, Account::class.java)
                    Data.answer = ""
                    if (ans.success){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
