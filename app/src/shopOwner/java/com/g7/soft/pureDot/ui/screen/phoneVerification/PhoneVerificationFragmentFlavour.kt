package com.g7.soft.pureDot.ui.screen.phoneVerification

import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.utils.ProjectDialogUtils

class PhoneVerificationFragmentFlavour {
    fun signUpSuccessfulAction(fragment: PhoneVerificationFragment) {
        if (fragment.viewModel.verificationResponse.value?.data != null) {
            ProjectDialogUtils.showSimpleMessage(
                fragment.requireContext(), R.string.msg_account_created,
                drawableResId = R.drawable.ic_secure_shield,
                title = fragment.getString(
                    R.string.conc_hello_,
                    fragment.viewModel.verificationResponse.value?.data?.name
                ),
                positiveBtnTextResId = R.string.start_shopping
            ) {
                fragment.findNavController()
                    .navigate(R.id.action_phoneVerificationFragment_to_homeFragment)
            }
        } else {
            // todo show invalid verification or something went wrong, both are valid cases
        }
    }


}