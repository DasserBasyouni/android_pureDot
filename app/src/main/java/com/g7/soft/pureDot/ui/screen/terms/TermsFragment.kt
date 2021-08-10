package com.g7.soft.pureDot.ui.screen.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentTermsBinding

class TermsFragment : Fragment() {
    private lateinit var binding: FragmentTermsBinding
    internal lateinit var viewModel: TermsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_terms, container, false)

        viewModel = ViewModelProvider(this).get(TermsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}