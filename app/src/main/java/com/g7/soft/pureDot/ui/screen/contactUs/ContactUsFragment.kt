package com.g7.soft.pureDot.ui.screen.contactUs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentContactUsBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.zeugmasolutions.localehelper.currentLocale


class ContactUsFragment : Fragment() {
    private lateinit var binding: FragmentContactUsBinding
    internal lateinit var viewModel: ContactUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_contact_us, container, false)

        viewModel = ViewModelProvider(this).get(ContactUsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitBtn.setOnClickListener {
            viewModel.submit(requireActivity().currentLocale.toLanguageTag())
                .observeApiResponse(this@ContactUsFragment, {
                    findNavController().popBackStack()
                }, validationObserve = {
                    binding.nameTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_NAME)
                            getString(R.string.error_empty_name) else null

                    binding.emailTil.error = when (it) {
                        ProjectConstant.Companion.ValidationError.EMPTY_EMAIL -> getString(R.string.error_empty_email)
                        ProjectConstant.Companion.ValidationError.INVALID_EMAIL -> getString(R.string.error_invalid_email)
                        else -> null
                    }

                    binding.messageTil.error =
                        if (it == ProjectConstant.Companion.ValidationError.EMPTY_MESSAGE)
                            getString(R.string.error_empty_message) else null
                })
        }

        binding.twitterIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Puredot2030")))
        }
        binding.facebookIb.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/Puredot-104256265027094")
                )
            )
        }
        binding.instagramIb.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/puredot2030/")
                )
            )
        }
        binding.tiktokIb.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.tiktok.com/@puredot2030?lang=en&is_copy_url=1&is_from_webapp=v3")
                )
            )
        }
        binding.youtubeIb.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UCrLL_kAdfEVaZH81t-L-UCg")
                )
            )
        }
        binding.snapchatIb.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.snapchat.com/add/puredot2030")
                )
            )
        }
    }

}