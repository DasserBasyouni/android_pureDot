package com.g7.soft.pureDot.ui.screen.trackOrder

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
import com.g7.soft.pureDot.adapter.TimeLineAdapter
import com.g7.soft.pureDot.databinding.FragmentTrackOrderBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.myOrders.MyOrdersFragment
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class TrackOrderFragment : Fragment() {

    companion object {
        var refreshData: ((String?) -> Unit)? = null
        var isRunning = false
    }


    private lateinit var binding: FragmentTrackOrderBinding
    private lateinit var viewModelFactory: TrackOrderViewModelFactory
    internal lateinit var viewModel: TrackOrderViewModel
    private val args: TrackOrderFragmentArgs by navArgs()


    override fun onStart() {
        super.onStart()
        MyOrdersFragment.isRunning = true
    }

    override fun onStop() {
        super.onStop()
        MyOrdersFragment.isRunning = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_track_order, container, false)

        viewModelFactory = TrackOrderViewModelFactory(
            orderId = args.orderId,
            orderNumber = args.orderNumber,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrackOrderViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshData = { orderId ->
            if (viewModel.orderId == orderId)
                lifecycleScope.launch {
                    val tokenId = UserRepository("").getTokenId(requireContext())
                    viewModel.fetchScreenData(
                        requireActivity().currentLocale.toLanguageTag(), tokenId
                    )
                }
        }

        // fetch data
        lifecycleScope.launch {
            val tokenId =
                UserRepository("").getTokenId(requireContext())
            viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.orderTrackingResponse.observe(viewLifecycleOwner, {
            viewModel.orderTrackingLcee.value!!.response.value = it

            val timelineAdapter = TimeLineAdapter(it.data?.last()?.status)
            binding.timelineRv.adapter = timelineAdapter
            timelineAdapter.updateOrderStatus(it.data)
            binding.invalidateAll()
        })

        // setup click listener
        binding.complaintBtn.setOnClickListener {
            findNavController().navigate(R.id.submitComplainFragment)
        }
        binding.cancelBtn.setOnClickListener { cancelOrder() }
    }

    fun cancelOrder() {
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
                    ).observeApiResponse(this@TrackOrderFragment, {
                        findNavController().navigate(R.id.action_trackOrderFragment_to_homeFragment)
                    })
                }
            }
        )
    }

}