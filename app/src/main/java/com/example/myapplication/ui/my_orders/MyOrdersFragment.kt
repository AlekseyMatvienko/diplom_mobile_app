package com.example.myapplication.ui.my_orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentMyOrdersBinding
import com.example.myapplication.models.Data.myOrders
import com.example.myapplication.ui.MainActivity

class MyOrdersFragment : Fragment() {

    private lateinit var myOrdersViewModel: MyOrdersViewModel
    private var _binding: FragmentMyOrdersBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myOrdersViewModel =
            ViewModelProvider(this).get(MyOrdersViewModel::class.java)

        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root


        myOrdersViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).changeTitle()
        Log.d("size", myOrders.size.toString())


        if(myOrders.size > 0){
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = OrdersFragment(myOrders)
        }
        /*Data.replies.toObservable().subscribeBy(
                onNext = {
                    println(it)
                },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}