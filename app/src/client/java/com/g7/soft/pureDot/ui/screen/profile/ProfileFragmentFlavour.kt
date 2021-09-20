package com.g7.soft.pureDot.ui.screen.profile

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.adapter.ProfileAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class ProfileFragmentFlavour {
    fun fetchData(fragment: ProfileFragment) {
        fragment.viewModel.getSignUpFields(fragment.requireActivity().currentLocale.toLanguageTag())
    }

    fun observe(fragment: ProfileFragment) {
        fragment.viewModel.signUpFieldsResponse.observe(fragment.viewLifecycleOwner, {
            fragment.viewModel.signUpFieldsLcee.value!!.response.value = it

            if (it.status == ProjectConstant.Companion.Status.SUCCESS) {
                // fetch data
                fragment.lifecycleScope.launch {
                    val tokenId =
                        UserRepository("").getTokenId(fragment.requireContext())

                    fragment.viewModel.getUserData(fragment.requireActivity().currentLocale.toLanguageTag(), tokenId)
                }
            }
        })
    }

    fun getProfileAdapter(
        fragment: ProfileFragment,
        networkRequestResponse: NetworkRequestResponse<UserDataModel?>
    ) = ProfileAdapter(fragment, networkRequestResponse.data, fragment.viewModel.signUpFieldsResponse.value?.data)

    fun getProfileEditBundle(viewModel: ProfileViewModel): Bundle = bundleOf(
        "userData" to viewModel.userDataResponse.value?.data,
        "signUpFields" to viewModel.signUpFieldsResponse.value?.data
    )
}