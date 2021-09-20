package com.g7.soft.pureDot.ui.screen.profile

import android.os.Bundle
import androidx.core.os.bundleOf
import com.g7.soft.pureDot.adapter.ProfileAdapter
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse

class ProfileFragmentFlavour {
    fun fetchData(fragment: ProfileFragment) = Unit

    fun observe(fragment: ProfileFragment) = Unit

    fun getProfileAdapter(
        fragment: ProfileFragment,
        networkRequestResponse: NetworkRequestResponse<UserDataModel?>
    ) = ProfileAdapter(fragment, networkRequestResponse.data)

    fun getProfileEditBundle(viewModel: ProfileViewModel): Bundle = bundleOf(
        "userData" to viewModel.userDataResponse.value?.data
    )
}