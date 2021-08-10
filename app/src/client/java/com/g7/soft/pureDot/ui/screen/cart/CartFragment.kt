package com.g7.soft.pureDot.ui.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.CartHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCartBinding
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.zeugmasolutions.localehelper.currentLocale

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    internal lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_cart, container, false)

        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        viewModel.getCartItems(requireActivity().currentLocale.toLanguageTag(), "") // todo token

        // setup observers
        viewModel.cartItemsResponse.observe(viewLifecycleOwner, {
            viewModel.cartItemsLcee.value!!.response.value = it

            CartHeaderAdapter(this, it.data?.products ?: mutableListOf()).let { adapter ->
                binding.cartItemsRv.adapter = adapter
                adapter.submitList(it.data?.products?.map { product ->
                    product.shop?.name
                }?.toSet()?.toList())
            }
        })

        // add decoration divider
        binding.cartItemsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.checkoutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }
    }

}