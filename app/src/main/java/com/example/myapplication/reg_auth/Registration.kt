package com.example.myapplication.reg_auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.*
import com.example.myapplication.connection.NoConnection
import com.example.myapplication.connection.Wsc
import com.example.myapplication.models.Account
import com.example.myapplication.models.Data
import com.example.myapplication.ui.MainActivity
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.lang.Double.parseDouble

class Registration : AppCompatActivity(), TextWatcher {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Check().hasConnection(this)){
            val intent = Intent(this, NoConnection::class.java)
            startActivity(intent)
        }

        setContentView(R.layout.activity_registration)
        actionBar?.hide()
        val create =  findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.reg2)
        val back = findViewById<ImageView>(R.id.back)


        back.setOnClickListener{ back() }
        create.setOnClickListener { createAccount() }

        val mail = findViewById<EditText>(R.id.Email2)
        val phone = findViewById<EditText>(R.id.Phone2)
        val fio = findViewById<EditText>(R.id.FIO2)
        val pass = findViewById<EditText>(R.id.Password)

        //Проверка полей во время ввода с помощью интерфейса TextWatcher
        //Функция afterTextChanged
        mail.addTextChangedListener(this)
        phone.addTextChangedListener(this)
        fio.addTextChangedListener(this)
        pass.addTextChangedListener(this)
    }

        override fun afterTextChanged(s: Editable?) {
            val mail = findViewById<EditText>(R.id.Email2)
            val phone = findViewById<EditText>(R.id.Phone2)
            val fio = findViewById<EditText>(R.id.FIO2)
            val pass = findViewById<EditText>(R.id.Password)

            val mcross = findViewById<ImageView>(R.id.crmail)
            val mmark = findViewById<ImageView>(R.id.cmmail)
            val pcross = findViewById<ImageView>(R.id.crphone)
            val pmark = findViewById<ImageView>(R.id.cmphone)
            val fcross = findViewById<ImageView>(R.id.crfio)
            val fmark = findViewById<ImageView>(R.id.cmfio)
            val pwcross = findViewById<ImageView>(R.id.crpass)
            val pwmark = findViewById<ImageView>(R.id.cmpass)

            if (mcross.visibility == ImageView.VISIBLE){
                mail.setTextColor(resources.getColor(R.color.base_red))
            }
            if (mmark.visibility == ImageView.VISIBLE){
                mail.setTextColor(resources.getColor(R.color.base_green))
            }
            if (pcross.visibility == ImageView.VISIBLE){
                phone.setTextColor(resources.getColor(R.color.base_red))
            }
            if (pmark.visibility == ImageView.VISIBLE){
                phone.setTextColor(resources.getColor(R.color.base_green))
            }
            if (fcross.visibility == ImageView.VISIBLE){
                fio.setTextColor(resources.getColor(R.color.base_red))
            }
            if (fmark.visibility == ImageView.VISIBLE){
                fio.setTextColor(resources.getColor(R.color.base_green))
            }
            if (pwcross.visibility == ImageView.VISIBLE){
                pass.setTextColor(resources.getColor(R.color.base_red))
            }
            if (pwmark.visibility == ImageView.VISIBLE){
                pass.setTextColor(resources.getColor(R.color.base_green))
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val mail = findViewById<EditText>(R.id.Email2)
            val phone = findViewById<EditText>(R.id.Phone2)
            val fio = findViewById<EditText>(R.id.FIO2)
            val pass = findViewById<EditText>(R.id.Password)

            val mcross = findViewById<ImageView>(R.id.crmail)
            val mmark = findViewById<ImageView>(R.id.cmmail)
            val pcross = findViewById<ImageView>(R.id.crphone)
            val pmark = findViewById<ImageView>(R.id.cmphone)
            val fcross = findViewById<ImageView>(R.id.crfio)
            val fmark = findViewById<ImageView>(R.id.cmfio)
            val pwcross = findViewById<ImageView>(R.id.crpass)
            val pwmark = findViewById<ImageView>(R.id.cmpass)

            if (s.toString() == mail.text.toString()){
                val emailAddressRegex =
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,50}" +
                            "\\@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,15}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,10}" +
                            ")+"
                if ( s.toString().length> 10 && s.toString().matches(emailAddressRegex.toRegex())){
                    mmark.visibility = ImageView.VISIBLE
                    mcross.visibility = ImageView.INVISIBLE
                }
                else
                {
                    mcross.visibility = ImageView.VISIBLE
                    mmark.visibility = ImageView.INVISIBLE
                }
            }
            else{
                if (s.toString() == phone.text.toString()){
                    var num = true
                    try {
                        val num = parseDouble(s.toString())
                    } catch (e: NumberFormatException) {
                        num = false
                    }
                    if (s.toString().length == 11 && num){
                        pmark.visibility = ImageView.VISIBLE
                        pcross.visibility = ImageView.INVISIBLE
                    }
                    else
                    {
                        pcross.visibility = ImageView.VISIBLE
                        pmark.visibility = ImageView.INVISIBLE
                    }
                }
                else{
                    val regFio = "[а-яА-Я ]{6,40}"
                    if (s.toString() == fio.text.toString()){
                        if (s.toString().matches(regFio.toRegex())){
                            fmark.visibility = ImageView.VISIBLE
                            fcross.visibility = ImageView.INVISIBLE
                        }
                        else
                        {
                            fcross.visibility = ImageView.VISIBLE
                            fmark.visibility = ImageView.INVISIBLE
                        }
                    }
                    else{
                        val passwordRegex =
                            "[a-zA-Z0-9+._%\\-(~!@#$^&*\\-='|{}\\]:<>,?/)]{1,50}"
                        if (s.toString().length > 6 && s.toString().matches(passwordRegex.toRegex())){
                            pwmark.visibility = ImageView.VISIBLE
                            pwcross.visibility = ImageView.INVISIBLE
                        }
                        else
                        {
                            pwcross.visibility = ImageView.VISIBLE
                            pwmark.visibility = ImageView.INVISIBLE
                        }
                    }
                }
            }

        }
    private fun back() {
        val intent = Intent(this, Authtorization::class.java)
        startActivity(intent)
        finish()
    }

    private fun createAccount(){
        val mmark = findViewById<ImageView>(R.id.cmmail)
        val pmark = findViewById<ImageView>(R.id.cmphone)
        val fmark = findViewById<ImageView>(R.id.cmfio)
        val pwmark = findViewById<ImageView>(R.id.cmpass)

        val agree = findViewById<CheckBox>(R.id.checkagree)
        if(agree.isChecked){ if(mmark.visibility == ImageView.VISIBLE
            && pmark.visibility == ImageView.VISIBLE
            && fmark.visibility == ImageView.VISIBLE
            && pwmark.visibility == ImageView.VISIBLE
            ) {
                val reply = findViewById<TextView>(R.id.reply)
                val error = findViewById<TextView>(R.id.error)
                reply.text = " "

                sendRequest()
                while(Data.answer =="") {
                    Thread.sleep(100)
                }
                Log.d("mess", Data.answer)
                val builder = GsonBuilder()
                val gson = builder.create()
                val ans: Account = gson.fromJson(Data.answer, Account::class.java)
                Data.answer = ""
                if (ans.success) {
                    val mail = findViewById<EditText>(R.id.Email2)
                    val phone = findViewById<EditText>(R.id.Phone2)
                    val fio = findViewById<EditText>(R.id.FIO2)
                    val pass = findViewById<EditText>(R.id.Password)
                    val sharedPref: SharedPreferences =
                        this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putInt("id", ans.userid)
                    editor.putString("email", mail.text.toString())
                    editor.putString("fio", fio.text.toString())
                    editor.putString("phone", phone.text.toString())
                    editor.putString("password", pass.text.toString())
                    editor.apply()

                    val checkAcc = JSONObject()

                    checkAcc.put("id", "login")
                    checkAcc.put("email", mail.text.toString())
                    checkAcc.put("password", pass.text.toString())

                    Wsc.webSocketClient.send(checkAcc.toString())

                    while(Data.answer =="") {
                        Thread.sleep(100)
                    }
                    Log.d("mess", Data.answer)

                    val answer: Account = gson.fromJson(Data.answer, Account::class.java)
                    Data.answer = ""
                    if (answer.success) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                }
                else{
                    if (ans.error == "ERR_PHONE_TAKEN"){
                        error.text = "Пользователь с введённым телефоном уже существует."
                        error.visibility = TextView.VISIBLE
                    }
                }
        }
        else Toast.makeText(this, "Введены неверные данные", Toast.LENGTH_SHORT).show() }
        else Toast.makeText(this, "Подтвердите согласие на обработку персональных данных", Toast.LENGTH_LONG).show()
    }
    private fun sendRequest() {
        val jsondate = JSONObject()
        val mail = findViewById<EditText>(R.id.Email2)
        val phone = findViewById<EditText>(R.id.Phone2)
        val fio = findViewById<EditText>(R.id.FIO2)
        val pass = findViewById<EditText>(R.id.Password)
        jsondate.put("id", "register")
        jsondate.put("email", mail.text.toString())
        jsondate.put("phone", phone.text.toString())
        jsondate.put("fio", fio.text.toString())
        jsondate.put("password",pass.text.toString())
        Log.d("Send date", jsondate.toString())
        Wsc.webSocketClient.send(
            jsondate.toString()
        )
    }

    /*private fun createWebSocketClient(coinbaseUri: URI?) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d("Coinbase", "onOpen")
            }
            override fun onMessage(message: String?) {
                Log.d(ContentValues.TAG, "onMessage: $message")
                registred(message)

            }
            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(ContentValues.TAG, "onClose")
            }
            override fun onError(ex: Exception?) {
                Log.e(ContentValues.TAG, "onError: ${ex?.message}")
            }
        }
    }

    fun regaccess(){
        val intent = Intent(this, Authtorization::class.java)
        startActivity(intent);
    }
    private fun initWebSocket() {
        val autoServiceUri: URI? = URI("ws://54.151.62.0:5000/client")

        createWebSocketClient(autoServiceUri)
        webSocketClient.connect()
    }*/

    /*override fun onResume() {
        super.onResume()
        Sockets().initWebSocket()
    }

    override fun onPause() {
        super.onPause()
        Log.d("Connect", "onClose")
        Wsc.webSocketClient.close()
    }*/


    fun registred (reply: String?){
        reply?.let {

            val builder = GsonBuilder()
            val gson = builder.create()
            //Словарь

            val mail = findViewById<EditText>(R.id.Email2)
            val phone = findViewById<EditText>(R.id.Phone2)
            val fio = findViewById<EditText>(R.id.FIO2)
            val pass = findViewById<EditText>(R.id.Password)
            val people: Account = gson.fromJson(reply, Account::class.java)
            //Данные словаря
            Log.d("err", people.error)
            if (people.success) {
                val usid = findViewById<TextView>(R.id.reply)
                usid.text = people.userid.toString()
                val sharedPref: SharedPreferences =
                    this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPref.edit()
                editor.putInt("id", usid.text.toString().toInt())
                editor.putString("email", mail.text.toString())
                editor.putString("fio", fio.text.toString())
                editor.putString("phone", phone.text.toString())
                editor.putString("password", pass.text.toString())
                editor.apply()
            }
            else{
                if (people.error == "ERR_PHONE_TAKEN"){
                    val usid = findViewById<TextView>(R.id.reply)
                    usid.text = "err"
                }
            }
        }
    }
}

