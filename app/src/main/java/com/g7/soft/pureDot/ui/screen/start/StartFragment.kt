package com.g7.soft.pureDot.ui.screen.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.Application
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentStartBinding
import com.g7.soft.pureDot.ext.makeLinks
import com.g7.soft.pureDot.repo.UserRepository
import kotlinx.coroutines.launch

// todo make this fragment MVVM arch
class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_start, container, false)

        viewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup listeners
        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
        }
        if (Application.isClientFlavour) {
            binding.continueAsGuestTv.visibility = View.VISIBLE
            binding.continueAsGuestTv.makeLinks(Pair(getString(R.string.part_guest), View.OnClickListener {
                lifecycleScope.launch {
                    UserRepository("").updateIsGuestAccount(requireContext(), true)
                }
                findNavController().navigate(R.id.action_startFragment_to_homeFragment)
            }), doChangeColor = false)
        }
    }


}