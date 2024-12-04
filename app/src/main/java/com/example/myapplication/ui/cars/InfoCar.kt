package com.example.myapplication.ui.cars

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.myapplication.R
import com.example.myapplication.connection.Wsc
import com.example.myapplication.models.Data
import com.example.myapplication.models.MyCars
import com.example.myapplication.models.ReplyAdd
import com.example.myapplication.ui.MainActivity
import com.google.gson.GsonBuilder
import org.json.JSONObject


class InfoCar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_car)
        val markModel = findViewById<AppCompatTextView>(R.id.mark_model_car)
        val vinNum = findViewById<TextView>(R.id.vin_num_info)
        val num = findViewById<TextView>(R.id.num_info)
        val year = findViewById<TextView>(R.id.year_info)

        markModel.text = Data.selected_car.carmark_name + " " + Data.selected_car.name_model
        vinNum.text = Data.selected_car.vin_number
        num.text = Data.selected_car.number
        year.text = Data.selected_car.year.toString()

        val back = findViewById<AppCompatImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val delete = findViewById<AppCompatImageView>(R.id.delete)
        delete.setOnClickListener{
            val jsonObject = JSONObject()
            val builder = GsonBuilder()
            val gson = builder.create()
            jsonObject.put("id", "deleteCar")
            jsonObject.put("car", gson.toJson(Data.selected_car))
            Log.d("Car", jsonObject.toString())
            Wsc.webSocketClient.send(
                jsonObject.toString()
            )
            while (Data.answer == "") {
                Thread.sleep(50)
            }
            val rep = gson.fromJson(Data.answer, ReplyAdd::class.java)
            Data.answer = ""
            if (rep.success){
                for (i in Data.myCar.indices){
                    if(Data.myCar[i].id == Data.selected_car.id){
                        Data.myCar.removeAt(i)
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }
}