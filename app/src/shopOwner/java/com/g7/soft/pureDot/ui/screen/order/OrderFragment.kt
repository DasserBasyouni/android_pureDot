package com.g7.soft.pureDot.ui.screen.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ProductsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentOrderBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {

    companion object {
        var refreshData: ((String?) -> Unit)? = null
        var isRunning = false
    }


    internal lateinit var binding: FragmentOrderBinding
    private lateinit var viewModelFactory: OrderViewModelFactory
    internal lateinit var viewModel: OrderViewModel
    private val args: OrderFragmentArgs by navArgs()


    override fun onStart() {
        super.onStart()
        isRunning = true
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order, container, false)

        viewModelFactory = OrderViewModelFactory(
            masterOrder = args.masterOrder,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderViewModel::class.java)

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            binding.viewModel = viewModel
            binding.currency = currencySymbol
            binding.lifecycleOwner = this@OrderFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshData = {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())
                viewModel.getMasterOrder(requireActivity().currentLocale.toLanguageTag(), tokenId)
            }
        }

        viewModel.orderResponse.observe(viewLifecycleOwner, {
            // init data
            ProductsAdapter(this).let { adapter ->
                binding.cartReviewItemsRv.adapter = adapter
                adapter.submitList(it.data?.firstOrder?.products)
            }

            // setup listeners
            binding.actionBtn.setOnClickListener {
                lifecycleScope.launch {
                    val tokenId = UserRepository("").getTokenId(requireContext())

                    viewModel.changeOrderStatus(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId
                    ).observeApiResponse(this@OrderFragment, {
                        viewModel.orderResponse.value?.data?.firstOrder?.status =
                            ApiConstant.OrderStatus.getShopOwnerNextStatus(viewModel.orderResponse.value?.data?.firstOrder?.status)?.value
                        binding.invalidateAll()
                    })
                }
            }
        })
    }


    fun cancelOrder() {
        ProjectDialogUtils.showAskingDialog(
            context = requireContext(),
            iconResId = R.drawable.ic_cancel,
            titleResId = R.string.question_sure_cancel,
            messageResId = R.string.amount_will_be_refunded_to_your_wallet,
            positiveRunnable = {
                lifecycleScope.launch {
                    val tokenId = UserRepository("").getTokenId(requireContext())

                    viewModel.cancelOrder(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                    ).observeApiResponse(this@OrderFragment, {
                        findNavController().popBackStack()
                    })
                }
            }
        )
    }
}