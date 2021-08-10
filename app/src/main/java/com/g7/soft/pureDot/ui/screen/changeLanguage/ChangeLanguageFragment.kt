package com.g7.soft.pureDot.ui.screen.changeLanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentChangeLanguageBinding
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.currentLocale
import java.util.*


class ChangeLanguageFragment : Fragment() {
    private lateinit var binding: FragmentChangeLanguageBinding

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
            if (checkedId == R.id.englishRb)
                changeLocaleTo(Locale("en"))
            else
                changeLocaleTo(Locale("ar"))
        }
    }


    private fun changeLocaleTo(locale: Locale) = (activity as LocaleAwareCompatActivity).let {
        if (it.currentLocale != locale) {
            it.updateLocale(locale)
            findNavController().popBackStack()
        }
    }
}