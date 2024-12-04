package com.example.myapplication.ui.cars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.TextView.*
import com.example.myapplication.*
import com.example.myapplication.connection.Wsc
import com.example.myapplication.listeners.setSafeOnClickListener
import com.example.myapplication.models.Cars
import com.example.myapplication.models.Data
import com.example.myapplication.models.NewCarSend
import com.example.myapplication.models.ReplyAdd
import com.example.myapplication.ui.MainActivity

import com.google.gson.GsonBuilder
import org.json.JSONObject

class NewCar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_car)
        val yes = findViewById<ImageView>(R.id.yes)
        yes.setSafeOnClickListener { save() }
        val no = findViewById<ImageView>(R.id.no)
        no.setSafeOnClickListener { back() }

        val marksSpinner = mutableListOf<String>()
        for (i in Data.dataModel.marks.indices){
            marksSpinner.add(Data.dataModel.marks[i].name)
        }
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(0)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, marksSpinner
            )
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateModels()
            }

        }
    }
    fun updateModels(){
        val modelsSpinner = mutableListOf<String>()
        val mar = findViewById<Spinner>(R.id.spinner)
        for (i in Data.dataModel.models.indices){
            if (Data.dataModel.models[i].mark_name == mar.selectedItem)
                modelsSpinner.add(Data.dataModel.models[i].name)
        }
        val spinnerMod = findViewById<Spinner>(R.id.spinnerModel)
        spinnerMod.setSelection(0)
        if (spinnerMod != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item, modelsSpinner
            )
            spinnerMod.adapter = adapter
        }
    }

    fun save(){
        val num = findViewById<EditText>(R.id.number)
        val vin = findViewById<EditText>(R.id.VIN_number)
        val year = findViewById<EditText>(R.id.year)
        val marks = findViewById<Spinner>(R.id.spinner)
        val models = findViewById<Spinner>(R.id.spinnerModel)
        val errNum = findViewById<TextView>(R.id.err_num)
        val errVin = findViewById<TextView>(R.id.err_vin)
        val errYear = findViewById<TextView>(R.id.err_year)
        val numberRegex = "[а-яА-Я]{1}"+"[0-9]{3}"+"[а-яА-Я]{2}"+"\\ "+"[0-9]{1,3}"
        val vINNumberRegex = "[a-zA-Z0-9]{16,17}"
        val yearRegex = "[0-9]{4}"
        var nice =true
        if (!num.text.matches(numberRegex.toRegex())){
            errNum.visibility = VISIBLE
            nice = false
        }
        else errNum.visibility = INVISIBLE
        if (!vin.text.matches(vINNumberRegex.toRegex())){
            errVin.visibility = VISIBLE
            nice = false
        }
        else errVin.visibility = INVISIBLE
        if (!year.text.matches(yearRegex.toRegex())){
            errYear.visibility = VISIBLE
            nice = false
        }
        else errYear.visibility = INVISIBLE
        if (nice){
            saveDate()
        }
    }

    private fun saveDate(){
        var idCarModel = 0
        val mod = findViewById<Spinner>(R.id.spinnerModel)
        for (i in Data.dataModel.models.indices){
            if (mod.selectedItem == Data.dataModel.models[i].name){
                idCarModel = Data.dataModel.models[i].id
            }
        }
        val num = findViewById<EditText>(R.id.number)
        val vin = findViewById<EditText>(R.id.VIN_number)
        val year = findViewById<EditText>(R.id.year)
        val mark = findViewById<Spinner>(R.id.spinner)

        val newCarData = NewCarSend()
        newCarData.number = num.text.toString()
        newCarData.vin_number = vin.text.toString()
        newCarData.year = year.text.toString().toInt()
        newCarData.model_id = idCarModel

        val newCar = JSONObject()
        val builder = GsonBuilder()
        val gson = builder.create()
        newCar.put("id", "addCar")
        newCar.put("car", gson.toJson(newCarData))
        Log.d("json", newCar.toString())
        Wsc.webSocketClient.send(newCar.toString())
        while (Data.answer == ""){
            Thread.sleep(100)
        }
        Log.d("message", Data.answer)

        val ans: ReplyAdd = gson.fromJson(Data.answer, ReplyAdd::class.java)
        Data.answer = ""
        if (ans.success){
            val newCarsData = Cars()
            newCarsData.id = ans.carid
            newCarsData.number = num.text.toString()
            newCarsData.vin_number = vin.text.toString()
            newCarsData.year = year.text.toString().toInt()
            newCarsData.model_id = idCarModel
            newCarsData.carmark_name = mark.selectedItem.toString()
            newCarsData.name_model =mod.selectedItem.toString()
            Data.myCar.add(newCarsData)
            back()
        }
        else{
            Toast.makeText(this, "Ошибка подключения к серверу", Toast.LENGTH_SHORT).show()
        }
    }
    private fun back(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}