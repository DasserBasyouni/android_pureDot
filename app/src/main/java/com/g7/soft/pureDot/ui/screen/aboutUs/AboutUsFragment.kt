package com.g7.soft.pureDot.ui.screen.aboutUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentAboutUsBinding

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

        binding.maroofIv.setOnClickListener {

        }
        binding.twitterIv.setOnClickListener {

        }
        binding.facebookIv.setOnClickListener {

        }
        binding.instagramIv.setOnClickListener {

        }
        binding.linkedinIv.setOnClickListener {

        }
    }

}