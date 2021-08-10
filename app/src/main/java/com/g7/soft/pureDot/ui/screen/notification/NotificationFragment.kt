package com.g7.soft.pureDot.ui.screen.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.NotificationsAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentNotificationBinding
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding
    internal lateinit var viewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_notification,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        val tokenId = "" // todo
        viewModel.getNotifications(requireActivity().currentLocale.toLanguageTag(), tokenId)

        // setup observers
        val notificationAdapter = NotificationsAdapter(this)
        binding.notificationsRv.adapter = notificationAdapter
        viewModel.notificationsResponse.observe(viewLifecycleOwner, {
            viewModel.notificationsLcee.value!!.response.value = it
            notificationAdapter.submitList(it.data)
        })

        // add decoration divider
        binding.notificationsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        viewModel.doNotify.observe(viewLifecycleOwner, {
            val tokenId = "" //todo
            viewModel.doNotify(requireActivity().currentLocale.toLanguageTag(), tokenId = tokenId)
                .observe(viewLifecycleOwner, {

                    // show loading
                    if (it.status == ProjectConstant.Companion.Status.LOADING)
                        ProjectDialogUtils.showLoading(requireContext())
                    else ProjectDialogUtils.hideLoading()

                    // reset the switch if have error
                    if (it.status == ProjectConstant.Companion.Status.API_ERROR
                        || it.status == ProjectConstant.Companion.Status.NETWORK_ERROR
                    )
                        binding.doNotifyS.isChecked = !binding.doNotifyS.isChecked
                })
        })
    }
}