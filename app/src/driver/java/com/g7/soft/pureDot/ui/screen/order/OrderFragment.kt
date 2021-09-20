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
import com.g7.soft.pureDot.databinding.FragmentOrderBinding
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {

    internal lateinit var binding: FragmentOrderBinding
    private lateinit var viewModelFactory: OrderViewModelFactory
    internal lateinit var viewModel: OrderViewModel
    private val args: OrderFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order, container, false)

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            viewModelFactory = OrderViewModelFactory(
                masterOrder = args.masterOrder,
            )
            viewModel = ViewModelProvider(
                this@OrderFragment,
                viewModelFactory
            ).get(OrderViewModel::class.java)

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@OrderFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup listeners
        binding.cancelBtn.setOnClickListener {
            /*lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.cancelOrder(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                )
            }*/
        }
        binding.complaintBtn.setOnClickListener {
            findNavController().navigate(R.id.submitComplainFragment)
        }
    }

    /*fun cancelOrder() {
        ProjectDialogUtils.showAskingDialog(
            context = requireContext(),
            iconResId = R.drawable.ic_cancel,
            titleResId = R.string.question_sure_cancel,
            messageResId = R.string.amount_will_be_refunded_to_your_wallet,
            positiveRunnable = {
                lifecycleScope.launch {
                    val tokenId =
                        UserRepository("").getTokenId(requireContext())

                    viewModel.cancelOrder(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId,
                    ).observeApiResponse(this@OrderFragment, {
                        findNavController().popBackStack()
                    })
                }
            }
        )
    }*/
}