package com.g7.soft.pureDot.ui.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.CartHeaderAdapter
import com.g7.soft.pureDot.databinding.FragmentCartBinding
import com.g7.soft.pureDot.repo.ClientRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            val tokenId =
                ClientRepository("").getLocalUserData(requireContext()).tokenId
            viewModel.getProductsInCart(
                requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId,
                context = requireContext()
            )
        }

        // setup observers
        viewModel.productsInCartResponse.observe(viewLifecycleOwner, {
            viewModel.productsInCartLcee.value!!.response.value = it

            CartHeaderAdapter(this).let { adapter ->
                binding.cartItemsRv.adapter = adapter
                adapter.submitList(it.data)
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
            val bundle = bundleOf(
                "storesProductsCartDetails" to viewModel.productsInCartResponse.value?.data?.toTypedArray()
            )
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment, bundle)
        }
    }

}