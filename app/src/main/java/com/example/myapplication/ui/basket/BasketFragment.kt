package com.example.myapplication.ui.basket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentBasketBinding
import com.example.myapplication.models.Data.selected_services
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.ui.my_orders.NewOrder

class BasketFragment : Fragment() {

    private lateinit var basketViewModel: BasketViewModel
    private var _binding: FragmentBasketBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        basketViewModel =
            ViewModelProvider(this).get(BasketViewModel::class.java)

        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        val root: View = binding.root

        basketViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).changeTitle()
        if (selected_services.size > 0) {
            binding.recyclerViewBasket.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewBasket.adapter = RecyclerBasket(selected_services)
        }
        else{
            binding.addService.visibility = TextView.VISIBLE
            binding.emptyBasket.visibility = ImageView.VISIBLE
            binding.goService.visibility = androidx.appcompat.widget.AppCompatButton.VISIBLE
            binding.cancel.visibility = androidx.appcompat.widget.AppCompatButton.INVISIBLE
            binding.makeOrder.visibility =androidx.appcompat.widget.AppCompatButton.INVISIBLE
            binding.allPrice.visibility = TextView.INVISIBLE
            binding.all.visibility = TextView.INVISIBLE
            binding.imageView2.visibility = ImageView.INVISIBLE
        }
        var total = 0
        for (i in selected_services.indices) {
            total += selected_services[i].price
        }
        binding.allPrice.text = "$total р."
        binding.cancel.setOnClickListener { cancel() }
        binding.makeOrder.setOnClickListener { newOrder() }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun editPrice() {
        var total = 0
        for (i in selected_services.indices) {
            total += selected_services[i].price
        }
        binding.allPrice.text = "$total р."
    }
    private fun cancel(){
        selected_services.clear()
        binding.recyclerViewBasket.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewBasket.adapter = RecyclerBasket(selected_services)
        editPrice()
    }

    private fun newOrder(){
        if(selected_services.size > 0){
            val intent = Intent(requireContext(), NewOrder::class.java)
            startActivity(intent)
        }
        else {
            Toast.makeText(requireContext(), "Не выбрано ни одной услуги.", Toast.LENGTH_LONG).show()
        }
    }
}