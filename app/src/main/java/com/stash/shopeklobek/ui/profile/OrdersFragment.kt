package com.stash.shopeklobek.ui.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stash.shopeklobek.databinding.FragmentOrdersBinding
import com.stash.shopeklobek.model.entities.Order
import com.stash.shopeklobek.ui.BaseFragment

class OrdersFragment : BaseFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate)
{

    private lateinit var adapterOrder: AdapterOrder
    private lateinit var order: ArrayList<Order>
    private val profileViewModel by lazy{
        ProfileViewModel.create(this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bandle = Bundle().apply {
            putString("id","")
        }

       // findNavController().navigate(2,bandle)

        profileViewModel.getOrders()


        order=ArrayList<Order>()
        order.add(Order("55.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("88.5$","cancel","2021-04-10 10:28:21.052"))
        order.add(Order("550.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("55.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("88.5$","cancel","2021-04-10 10:28:21.052"))
        order.add(Order("550.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("55.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("88.5$","cancel","2021-04-10 10:28:21.052"))
        order.add(Order("550.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("55.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("88.5$","cancel","2021-04-10 10:28:21.052"))
        order.add(Order("550.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("55.5$","done","2021-04-10 10:28:21.052"))
        order.add(Order("88.5$","cancel","2021-04-10 10:28:21.052"))
        order.add(Order("550.5$","done","2021-04-10 10:28:21.052"))

        adapterOrder = AdapterOrder(ArrayList())

        binding.reOrderList.layoutManager =
            LinearLayoutManager(context)
        binding?.reOrderList?.adapter = adapterOrder

        profileViewModel.orders.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                adapterOrder.setOrders(it)
                binding.ivEmptyOrders.visibility=View.GONE
            }else{
                binding.ivEmptyOrders.visibility=View.VISIBLE
            }

        })


    }
}