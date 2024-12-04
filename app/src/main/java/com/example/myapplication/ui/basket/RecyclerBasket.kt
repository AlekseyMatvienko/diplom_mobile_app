package com.example.myapplication.ui.basket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Services


class RecyclerBasket(private val service: MutableList<Services>):
    RecyclerView.Adapter<RecyclerBasket.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val serviceName: TextView = itemView.findViewById(R.id.service_name)
        val category: TextView = itemView.findViewById(R.id.category)
        val price: TextView = itemView.findViewById(R.id.price)
        val delete: ImageView = itemView.findViewById(R.id.delete)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_basket, parent, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)  {
        holder.serviceName.text = "Название: " + service[position].name
        holder.category.text = "Категория: " + service[position].name_types
        holder.price.text = service[position].price.toString() + " р."
        holder.delete.setOnClickListener {
            service.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, service.size)
        }
    }


    override fun getItemCount(): Int {
        return service.size
    }


}