package com.example.myapplication.ui.my_orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Services

class RecyclerInfoOrder(private val service: MutableList<Services>)
    :RecyclerView.Adapter<RecyclerInfoOrder.MyViewHolder>() {
        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val serviceName: TextView = itemView.findViewById(R.id.name)
            val price: TextView = itemView.findViewById(R.id.price)

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_info_service, parent, false)
            return MyViewHolder(itemView)
        }


        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: MyViewHolder, position: Int)  {
            holder.serviceName.text = service[position].name
            holder.price.text = service[position].price.toString() + " р."
        }


        override fun getItemCount(): Int {
            return service.size
        }



}