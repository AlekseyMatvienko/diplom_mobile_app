package com.example.myapplication.ui.my_orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.R
import com.example.myapplication.connection.Wsc
import com.example.myapplication.models.Data
import com.example.myapplication.models.ReplyAdd
import com.google.gson.GsonBuilder
import org.json.JSONObject

class InfoOrder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_order)

        val numOrder = findViewById<AppCompatTextView>(R.id.num_order)
        val startDate = findViewById<TextView>(R.id.start_date_info)
        val finishDate = findViewById<TextView>(R.id.finish_date_info)
        val status = findViewById<TextView>(R.id.status_info)
        val car = findViewById<TextView>(R.id.car_info)
        val services = findViewById<RecyclerView>(R.id.recyclerServices)

        numOrder.text = "Заказ № " + Data.selected_order.id.toString()
        startDate.text = Data.selected_order.start_date
        finishDate.text = Data.selected_order.finish_date
        status.text = Data.selected_order.status
        car.text = Data.selected_order.car.carmark_name + " " + Data.selected_order.car.name_model +  " (" + Data.selected_order.car.number + ")"
        services.layoutManager = LinearLayoutManager(this)
        services.adapter = RecyclerInfoOrder(Data.selected_order.servicesList)
        val delete = findViewById<AppCompatImageView>(R.id.delete)
        if (Data.selected_order.status_id != 1 && Data.selected_order.status_id != 2){
            delete.visibility = AppCompatImageView.INVISIBLE
        }
        if (finishDate.text == "")
        {
            finishDate.text = "В процессе"
        }

        val back = findViewById<AppCompatImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        delete.setOnClickListener{
            val jsonObject = JSONObject()
            val builder = GsonBuilder()
            val gson = builder.create()
            jsonObject.put("id", "deleteOrder")
            jsonObject.put("order", gson.toJson(Data.selected_order))
            Log.d("Order", jsonObject.toString())
            Wsc.webSocketClient.send(
                jsonObject.toString()
            )
            while (Data.answer == "") {
                Thread.sleep(50)
            }
            val rep = gson.fromJson(Data.answer, ReplyAdd::class.java)
            Data.answer = ""
            if (rep.success){
                for (i in Data.myOrders.indices){
                    if(Data.myOrders[i].id == Data.selected_order.id){
                        Data.myOrders.removeAt(i)
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }


    }
}