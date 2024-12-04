package com.example.myapplication.ui.cars

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.*
import com.example.myapplication.databinding.FragmentCarsBinding
import com.example.myapplication.listeners.OnClickRecycleItemListener
import com.example.myapplication.models.Cars
import com.example.myapplication.models.Data.myCar
import com.example.myapplication.models.Data.selected_car
import com.example.myapplication.ui.MainActivity

class CarsFragment : Fragment() {

    private lateinit var carsViewModel: CarsViewModel
    private var _binding: FragmentCarsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        carsViewModel =
            ViewModelProvider(this).get(CarsViewModel::class.java)

        _binding = FragmentCarsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        carsViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fab.setOnClickListener { addCar() }
        (activity as MainActivity).changeTitle()

        if (myCar.size == 0){
            binding.noCar.visibility = ImageView.VISIBLE
            binding.plsAddCar.visibility = TextView.VISIBLE
        }
        else {
            binding.recyclerViewCar.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewCar.adapter = RecyclerCar(myCar, object: OnClickRecycleItemListener {
                override fun onClick(car: Cars) {
                    selected_car = car
                    val intent = Intent(context, InfoCar::class.java)
                    startActivity(intent)
                }
            })
        }
    }
    /*fun findCarModel(myCars: MutableList<Cars>){
        Models().findModelMark()
        for (ca in myCars.indices) {
            for (mo in dataModel.models.indices) {
                if (myCars[ca].model_id == dataModel.models[mo].id){
                    myCars[ca].name_model = dataModel.models[mo].name
                    myCars[ca].carMarkName = dataModel.models[mo].mark_name
                }
            }
        }
    }*/
    fun addCar(){
        val intent = Intent(context, NewCar::class.java)
        startActivity(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}