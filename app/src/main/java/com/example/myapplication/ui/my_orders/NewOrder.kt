package com.example.myapplication.ui.my_orders

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.connection.Wsc
import com.example.myapplication.listeners.setSafeOnClickListener
import com.example.myapplication.models.Data
import com.example.myapplication.models.Data.myOrders
import com.example.myapplication.models.Data.selected_services
import com.example.myapplication.models.NewOrderSend
import com.example.myapplication.models.ReplyAdd
import com.example.myapplication.ui.MainActivity
import com.google.gson.GsonBuilder
import org.json.JSONObject

class NewOrder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)

        val yes = findViewById<ImageView>(R.id.yes)
        yes.setSafeOnClickListener { save() }
        val no = findViewById<ImageView>(R.id.no)
        no.setSafeOnClickListener { cancel() }
        val recyclerService = findViewById<RecyclerView>(R.id.recyclerSelService)
        val allPrice = findViewById<TextView>(R.id.all_price)

        recyclerService.layoutManager = LinearLayoutManager(this)
        recyclerService.adapter = RecyclerSelService(selected_services)
        var total = 0
        for (i in selected_services.indices) {
            total += selected_services[i].price
        }
        allPrice.text = "$total р."

        val num = mutableListOf<String>()
        for (i in Data.myCar.indices){
            num.add(Data.myCar[i].number)
        }
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(0)

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, num
            )
            spinner.adapter = adapter
        }
    }

    fun save(){

        var idCar = 0
        val car = findViewById<Spinner>(R.id.spinner)
        for (i in Data.myCar.indices){
            if (car.selectedItem == Data.myCar[i].number){
                idCar = Data.myCar[i].id
            }
        }

        val idServ = mutableListOf<Int>()
        for (i in selected_services.indices){
            idServ.add(selected_services[i].id)
        }
        val pref: SharedPreferences = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        val id = pref.getInt("id", 0)
        val newOrder = JSONObject()
        val order = NewOrderSend()
        order.payment_type = 0
        order.car_id = idCar
        order.person_id = id
        order.services = idServ

        val builder = GsonBuilder()
        val gson = builder.create()

        newOrder.put("id", "addOrder")
        newOrder.put("order", gson.toJson(order))
        Log.d("json", newOrder.toString())
        Wsc.webSocketClient.send(newOrder.toString())
        while (Data.answer == ""){
            Thread.sleep(100)
        }
        Log.d("message", Data.answer)
        val ans: ReplyAdd = gson.fromJson(Data.answer, ReplyAdd::class.java)
        Data.answer = ""
        if (ans.success){
            selected_services.clear()
            myOrders.clear()
            cancel()

        }
        else{
            Toast.makeText(this, "Ошибка подключения к серверу", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancel(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}