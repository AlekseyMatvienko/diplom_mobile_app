package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.*
import com.example.myapplication.connection.NoConnection
import com.example.myapplication.connection.Wsc
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.models.*
import com.example.myapplication.models.Data.answer
import com.example.myapplication.models.Data.myCar
import com.example.myapplication.models.Data.myOrders
import com.example.myapplication.reg_auth.Authtorization
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Check().hasConnection(this)){
            val intent = Intent(this, NoConnection::class.java)
            startActivity(intent)
        }
        if (!Data.dataModel.success) {
            sendRequest()
            while (answer == "") {
                Thread.sleep(100)
            }
            dataProcessing(answer)
            answer = ""
        }
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_main, R.id.navigation_cars, R.id.navigation_my_orders, R.id.navigation_basket
            )
        )*/

        navView.setupWithNavController(navController)
        val user = findViewById<AppCompatImageView>(R.id.user)
        user.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId){
                    R.id.profile ->{
                        Toast.makeText(this, "Профиль", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.exit ->{
                        val sharedPref: SharedPreferences = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPref.edit()
                        editor.remove("id")
                        editor.apply()
                        val intent = Intent(this, Authtorization::class.java)
                        startActivity(intent)
                        finish()
                        true
                    }
                    else-> false
                }

            }
            popupMenu.inflate(R.menu.left_menu)
            try{
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            }
            catch (e: Exception){
                Log.e("Main", "Ошибка отображения иконок", e)
            }finally {
                popupMenu.show()
            }

        }
        findMyCar()
        findMyOrder()
        thread {
            while (true){
                updateBasket()
                Thread.sleep(200)
            }
        }
    }

    private fun updateBasket(){
        val basket = findViewById<TextView>(R.id.size_basket)

        basket.text = Data.selected_services.size.toString()
        basket.visibility = TextView.VISIBLE

    }

    private fun findMyCar(){
        if (myCar.size == 0) {
            val jsonSend = JSONObject()
            jsonSend.put("id", "getMyCars")
            Log.d("send", jsonSend.toString())
            Wsc.webSocketClient.send(
                jsonSend.toString()
            )
            while (answer == "") {
                Thread.sleep(50)
            }
            val gson = GsonBuilder().create()
            val car = gson.fromJson(answer, MyCars::class.java)
            answer = ""
            if (car.cars.size > 0) {
                MyCars().findCarModel(car.cars)

                myCar = car.cars
            }
        }
    }

    private fun findMyOrder(){
        if(myOrders.size < 1) {
            val jsonSend = JSONObject()
            jsonSend.put("id", "getMyOrders")
            Wsc.webSocketClient.send(
                jsonSend.toString()
            )
            while (answer == "") {
                Thread.sleep(50)
            }
            val gson = GsonBuilder().create()
            val order = gson.fromJson(answer, MyOrders::class.java)
            answer = ""

            if (order.orders.size > 0) {
                myOrders = order.orders
                Log.d("size", myOrders.size.toString())
                Orders().findDate()
            }
        }
    }

    private fun checkAuth(){
        val sharedPref: SharedPreferences = this.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        val id = sharedPref.getInt("id", 0)
        val email = sharedPref.getString("email", "")
        val password = sharedPref.getString("password", "")
        if (id == 0){
            val intent = Intent(this, Authtorization::class.java)
            startActivity(intent)
        }
        else{
            val checkAcc = JSONObject()

            checkAcc.put("id", "login")
            checkAcc.put("email", email)
            checkAcc.put("password", password)

            Wsc.webSocketClient.send(checkAcc.toString())

            while(answer =="") {
                Thread.sleep(100)
            }
            Log.d("mess", answer)
            val builder = GsonBuilder()
            val gson = builder.create()
            val ans: Account = gson.fromJson(answer, Account::class.java)
            answer = ""
            if (!ans.success){
                val intent = Intent(this, Authtorization::class.java)
                startActivity(intent)
            }
        }
    }

    fun changeTitle(){
        val navView: BottomNavigationView = binding.navView
        val sel = navView.menu.findItem(navView.selectedItemId)
        val title = findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.title)
        title.text = sel.toString()
    }

    private fun sendRequest() {
        val jsonDate = JSONObject()
        jsonDate.put("id", "requestData")
        Log.d("send",jsonDate.toString())
        Wsc.webSocketClient.send(
            jsonDate.toString()
        )
    }

    private fun dataProcessing(message: String) {
        val gson = GsonBuilder().create()
        Data.dataModel =
            gson.fromJson(message, DataModel::class.java)
        Models().findModelMark()
    }
}

//loadFragment(MainFragment())
        //navView = findViewById(R.id.nav_view) as BottomNavigationView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*val title = findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.title)
        navView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.navigation_main -> {
                    title.text = "Главная"
                   loadFragment(MainFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.navigation_cars -> {
                    title.text = "Машины"
                    loadFragment(CarsFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.navigation_my_orders -> {
                    title.text = "Мои заказы"
                    loadFragment(MyOrdersFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.navigation_basket -> {
                    title.text = "Корзина"
                    loadFragment(BasketFragment())
                    return@setOnNavigationItemReselectedListener
                }
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_activity_main,fragment)
        transaction.addToBackStack(null)
        transaction.commit()*/

