package com.g7.soft.pureDot.ui.screen.trackOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.TimeLineAdapter
import com.g7.soft.pureDot.databinding.FragmentTrackOrderBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale

class TrackOrderFragment : Fragment() {

    private lateinit var binding: FragmentTrackOrderBinding
    private lateinit var viewModelFactory: TrackOrderViewModelFactory
    internal lateinit var viewModel: TrackOrderViewModel
    private val args: TrackOrderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_track_order, container, false)

        viewModelFactory = TrackOrderViewModelFactory(
            order = args.order,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrackOrderViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        val tokenId = "" // todo
        viewModel.fetchScreenData(requireActivity().currentLocale.toLanguageTag(), tokenId)

        // setup observers
        val timelineAdapter = TimeLineAdapter()
        binding.timelineRv.adapter = timelineAdapter
        viewModel.orderTrackingResponse.observe(viewLifecycleOwner, {
            viewModel.orderTrackingLcee.value!!.response.value = it
            timelineAdapter.updateOrderStatus(it.data)
        })

        // setup click listener
        binding.complaintBtn.setOnClickListener {
            findNavController().navigate(R.id.submitComplainFragment)
        }
        binding.trackOrRateBtn.setOnClickListener {
            if (viewModel.order?.isDelivered == true){
              // todo rate
            } else
                cancelOrder()
        }
    }

    fun cancelOrder() {
        ProjectDialogUtils.showAskingDialog(
            context = requireContext(),
            iconResId = R.drawable.ic_cancel,
            titleResId = R.string.question_sure_cancel,
            messageResId = R.string.amount_will_be_refunded_to_your_wallet,
            positiveRunnable = {
                val tokenId = "" // todo
                viewModel.cancelOrder(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId,
                ).observeApiResponse(this, {
                    findNavController().navigate(R.id.action_trackOrderFragment_to_homeFragment)
                })
            }
        )
    }

}