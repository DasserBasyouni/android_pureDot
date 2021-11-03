package com.g7.soft.pureDot.ui.screen.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.NotificationsAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentNotificationBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

// todo lock it in case of guest
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
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())

            viewModel.getNotifications(requireActivity().currentLocale.toLanguageTag(), tokenId)
            viewModel.getUserData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.userDataResponse.observeApiResponse(this, {
            viewModel.doNotify.value = it?.doNotify
        })
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
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                viewModel.doNotify(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                )
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
            }
        })
    }
}