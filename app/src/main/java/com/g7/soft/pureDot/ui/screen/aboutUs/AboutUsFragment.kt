package com.g7.soft.pureDot.ui.screen.aboutUs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentAboutUsBinding
import com.g7.soft.pureDot.ui.screen.MainActivity


class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding
    internal lateinit var viewModel: AboutUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_about_us, container, false)

        viewModel = ViewModelProvider(this).get(AboutUsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nestedScrollView.scrollTo(0, 0)
        (requireActivity() as MainActivity).binding.nestedScrollView.scrollTo(0, 0)

        binding.twitterIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Puredot2030")))
        }
        binding.facebookIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Puredot-104256265027094")))
        }
        binding.instagramIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/puredot2030/")))
        }
        binding.tiktokIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@puredot2030?lang=en&is_copy_url=1&is_from_webapp=v3")))
        }
        binding.youtubeIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCrLL_kAdfEVaZH81t-L-UCg")))
        }
        binding.snapchatIb.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapchat.com/add/puredot2030")))
        }
    }

}