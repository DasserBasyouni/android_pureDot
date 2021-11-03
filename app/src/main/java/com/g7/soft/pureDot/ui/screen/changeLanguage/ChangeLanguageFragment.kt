package com.g7.soft.pureDot.ui.screen.changeLanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.databinding.FragmentChangeLanguageBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class ChangeLanguageFragment : Fragment() {
    private lateinit var binding: FragmentChangeLanguageBinding
    private lateinit var viewModel: ChangeLanguageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_change_language,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(ChangeLanguageViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init data
        binding.radioGroup.check(
            if (requireActivity().currentLocale.toLanguageTag()
                    .contains("en", ignoreCase = true)
            )
                R.id.englishRb else R.id.arabicRb
        )

        // listen
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            lifecycleScope.launch {
                val isEnglish = checkedId == R.id.englishRb
                val isGuestAccount = UserRepository("").getIsGuestAccount(requireContext())

                if (isGuestAccount)
                    changeLocaleTo(isEnglish)
                else {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Timber.w("Fetching FCM registration token failed")
                            Timber.w(task.exception)
                            return@OnCompleteListener
                        }

                        val fcmToken = task.result

                        viewModel.changeLanguage(
                            requireActivity().currentLocale.toLanguageTag(),
                            fcmToken,
                            ApiConstant.Language.fromIsEnglish(isEnglish).value
                        ).observeApiResponse(this@ChangeLanguageFragment, {
                            changeLocaleTo(isEnglish)
                        })
                    })
                }
            }
        }
    }


    private fun changeLocaleTo(isEnglish: Boolean) = (activity as LocaleAwareCompatActivity).let {
        val locale = if (isEnglish) Locales.English else Locales.Arabic
        if (it.currentLocale != locale) {
            it.updateLocale(locale)
            findNavController().popBackStack()
        }
    }
}