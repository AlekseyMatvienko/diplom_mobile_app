package com.example.myapplication.ui.my_orders

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Data
import com.example.myapplication.models.Orders


class OrdersFragment(private val order: List<Orders>):
    RecyclerView.Adapter<OrdersFragment.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val status: TextView = itemView.findViewById(R.id.status)
        val markModel: TextView = itemView.findViewById(R.id.mark_model)
        val number: TextView = itemView.findViewById(R.id.number)
        val detailed: TextView = itemView.findViewById(R.id.detailed)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_orders, parent, false)
        return MyViewHolder(itemView)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)  {
        holder.status.text = "Статус: " + order[position].status
        holder.markModel.text = "Машина: " + order[position].car.carmark_name + " " + order[position].car.name_model
        var allPrice = 0
        for (i in order[position].services.indices){
            allPrice += order[position].servicesList[i].price
        }
        holder.number.text = "Стоимость: $allPrice р."
        holder.detailed.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(view.context, InfoOrder::class.java)
            Data.selected_order = order[position]
            view.context.startActivity(intent)
        })
    }


    override fun getItemCount(): Int {
        return order.size
    }

}