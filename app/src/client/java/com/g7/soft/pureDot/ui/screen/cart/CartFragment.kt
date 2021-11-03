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
import com.g7.soft.pureDot.repo.CartRepository
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.utils.ProjectDialogUtils
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

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            viewModel = ViewModelProvider(this@CartFragment).get(CartViewModel::class.java)

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@CartFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())

            viewModel.checkCartItems(
                requireActivity().currentLocale.toLanguageTag(),
                tokenId = tokenId,
                context = requireContext()
            )
        }

        // setup observers
        viewModel.orderResponse.observe(viewLifecycleOwner, {
            viewModel.orderLcee.value!!.response.value = it

            CartHeaderAdapter(this).let { adapter ->
                binding.cartItemsRv.adapter = adapter
                adapter.submitList(it.data?.orders)
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

            // validate products availability
            val firstUnavailableProduct =
                viewModel.orderResponse.value?.data?.firstOrder?.products?.firstOrNull { it.isAvailable == false }
            if (firstUnavailableProduct != null) {
                ProjectDialogUtils.showSimpleMessage(
                    requireContext(),
                    message = getString(
                        R.string.error_unavailable_product_,
                        firstUnavailableProduct.name
                    ),
                    drawableResId = R.drawable.ic_secure_shield
                )
                return@setOnClickListener
            }

            val bundle = bundleOf(
                "masterOrder" to viewModel.orderResponse.value?.data,
                "productApiShopOrder" to viewModel.apiShopOrders.value?.toTypedArray()
            )
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment, bundle)
        }

        binding.clearCartBtn.setOnClickListener {
            CartRepository("").clearCart(lifecycleScope, requireContext())
            viewModel.orderResponse.value = viewModel.orderResponse.value.also { it?.data?.orders = null }
            viewModel.orderLcee.value = viewModel.orderLcee.value.also { it?.response?.value = null }
        }
    }

}