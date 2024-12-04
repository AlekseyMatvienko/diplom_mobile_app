package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.Data.selected_services
import com.example.myapplication.models.ServiceTypes
import com.example.myapplication.models.Services

class CustomRecyclerAdapter(private val service: List<Services>, private val serviceType: List<ServiceTypes>):
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View, contexts: Context) : RecyclerView.ViewHolder(itemView){
        val serviceName: TextView = itemView.findViewById(R.id.service_name)
        val category: TextView = itemView.findViewById(R.id.category)
        val price: TextView = itemView.findViewById(R.id.price)
        val add: androidx.appcompat.widget.AppCompatButton = itemView.findViewById(R.id.add)
        val purple = ContextCompat.getColor(contexts,R.color.base_purple)
    }


override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val itemView =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_services, parent, false)
    return MyViewHolder(itemView, contexts = parent.context)
}



override fun onBindViewHolder(holder: MyViewHolder, position: Int)  {
    holder.serviceName.text = "Название: " + service[position].name
    for (i in serviceType.indices){
        if (serviceType[i].id == service[position].service_type_id){
            service[position].name_types = serviceType[i].name
        }
    }
    holder.category.text = "Категория: " + service[position].name_types
    holder.price.text = service[position].price.toString() + " р."
    for (i in selected_services.indices) {
        if (service[position].id == selected_services[i].id)
        {
            holder.add.text ="Убрать из корзины"
            holder.add.setTextColor(Color.GRAY)
            service[position].yes_on_basket = true
        }
    }
    holder.add.setOnClickListener {
        if (!service[position].yes_on_basket){
            selected_services.add(service[position])
            holder.add.text ="Убрать из корзины"
            holder.add.setTextColor(Color.GRAY)
            service[position].yes_on_basket = true
        }
        else{
            for (i in selected_services.indices) {
                if (service[position].id == selected_services[i].id) {
                    selected_services.removeAt(i)
                    break
                }
            }
            holder.add.text ="+ Добавить в корзину"
            holder.add.setTextColor(holder.purple)
            service[position].yes_on_basket = false
        }
    }
}


override fun getItemCount(): Int {
    return service.size
}

}