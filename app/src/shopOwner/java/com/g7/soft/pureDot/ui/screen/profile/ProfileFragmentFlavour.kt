package com.g7.soft.pureDot.ui.screen.profile

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.BranchDetailsAdapter
import com.g7.soft.pureDot.adapter.ProfileAdapter
import com.g7.soft.pureDot.model.UserDataModel
import com.g7.soft.pureDot.network.response.NetworkRequestResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class ProfileFragmentFlavour {
    fun fetchData(fragment: ProfileFragment) {
        fragment.lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(fragment.requireContext())

            fragment.viewModel.getUserData(
                fragment.requireActivity().currentLocale.toLanguageTag(),
                tokenId
            )
        }
    }

    fun observe(fragment: ProfileFragment) = Unit

    fun getProfileEditBundle(viewModel: ProfileViewModel): Bundle = bundleOf(
        "userData" to viewModel.userDataResponse.value?.data
    )

    fun setupAdapters(
        fragment: ProfileFragment,
        networkRequestResponse: NetworkRequestResponse<UserDataModel?>
    ) {
        fragment.binding.settingsRv.adapter = ProfileAdapter(fragment, networkRequestResponse.data)
        fragment.binding.branchDetailsRv.adapter =
            BranchDetailsAdapter(fragment, networkRequestResponse.data)
        fragment.binding.settingsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(
                    fragment.requireContext(),
                    R.drawable.reviews_divider_layer
                )!!
            )
        )
    }
}