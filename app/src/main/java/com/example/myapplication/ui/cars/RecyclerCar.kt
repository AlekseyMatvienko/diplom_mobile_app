package com.example.myapplication.ui.cars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.listeners.OnClickRecycleItemListener
import com.example.myapplication.models.Cars

class RecyclerCar(private val cars: List<Cars>, private val onClickListener: OnClickRecycleItemListener):
    RecyclerView.Adapter<RecyclerCar.MyViewHolder>() {
    var onItemClick: ((Cars) -> Unit)? = null
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mark: TextView = itemView.findViewById(R.id.mark_date)
        val model: TextView = itemView.findViewById(R.id.model_date)
        val number: TextView = itemView.findViewById(R.id.number_date)
        val image: ImageView = itemView.findViewById(R.id.mark_image)
        val fon: ImageButton = itemView.findViewById(R.id.fon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_car, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)  {
        holder.mark.text = cars[position].carmark_name
        holder.model.text = cars[position].name_model
        holder.number.text = cars[position].number
        holder.fon.setOnClickListener{
            onClickListener.onClick(cars[position])
        }

        when (cars[position].carmark_name){
            "Audi"-> holder.image.setImageResource(R.drawable.audi)
            "BMW"-> holder.image.setImageResource(R.drawable.bmw)
            "Chevrolet"-> holder.image.setImageResource(R.drawable.chevrolet)
            "Ford"-> holder.image.setImageResource(R.drawable.ford)
            "Honda"-> holder.image.setImageResource(R.drawable.honda)
            "Hyundai"-> holder.image.setImageResource(R.drawable.hyundai)
            "Jeep"-> holder.image.setImageResource(R.drawable.jeep)
            "Kia"-> holder.image.setImageResource(R.drawable.kia)
            "Lexus"-> holder.image.setImageResource(R.drawable.lexus)
            "Mazda"-> holder.image.setImageResource(R.drawable.mazda)
            "Mersedes-Benz"-> holder.image.setImageResource(R.drawable.mercedes_benz)
            "Mitsubishi"-> holder.image.setImageResource(R.drawable.mitsubishi)
            "Nissan"-> holder.image.setImageResource(R.drawable.nissan)
            "Opel"-> holder.image.setImageResource(R.drawable.opel)
            "Renault"-> holder.image.setImageResource(R.drawable.renault)
            //"Subaru"-> holder.image.setImageResource(R.drawable.subaru)
            "Suzuki"-> holder.image.setImageResource(R.drawable.suzuki)
            "Toyota"-> holder.image.setImageResource(R.drawable.toyota)
            "Volkswagen"-> holder.image.setImageResource(R.drawable.volkswagen)
            "Volvo"-> holder.image.setImageResource(R.drawable.volvo)

        }
    }


    override fun getItemCount(): Int {
        return cars.size
    }

}