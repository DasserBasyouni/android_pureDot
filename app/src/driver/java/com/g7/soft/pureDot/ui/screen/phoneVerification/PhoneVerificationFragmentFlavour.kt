package com.g7.soft.pureDot.ui.screen.phoneVerification

import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.util.ProjectDialogUtils

class PhoneVerificationFragmentFlavour {
    fun signUpSuccessfulAction(fragment: PhoneVerificationFragment) {
        if (fragment.viewModel.verificationResponse.value?.data != null) {
            ProjectDialogUtils.showSimpleMessage(
                fragment.requireContext(), R.string.msg_driver_account_in_review,
                drawableResId = R.drawable.ic_secure_shield,
                positiveBtnTextResId = R.string.ok
            ) {
                fragment.requireActivity().finish()
            }
        } else {
            // todo show invalid verification or something went wrong, both are valid cases, make sure of that!
            ProjectDialogUtils.showSimpleMessage(
                fragment.requireContext(),
                R.string.something_went_wrong,
                R.drawable.ic_secure_shield
            )
        }
    }


}