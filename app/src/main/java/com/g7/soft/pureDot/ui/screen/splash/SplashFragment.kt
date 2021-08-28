package com.g7.soft.pureDot.ui.screen.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.databinding.FragmentSplashBinding
import com.g7.soft.pureDot.repo.ClientRepository
import kotlinx.coroutines.launch

// todo make this fragment MVVM arch
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_splash, container, false)

        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.post(this::animateDesiredScreen)
    }

    private fun animateDesiredScreen() {
        lifecycleScope.launch {
            val userData = ClientRepository("").getLocalUserData(requireContext())
            val tokenId = userData.tokenId
            val isGuestAccount = userData.isGuestAccount
            Log.e("Z_", "ud: $tokenId, $isGuestAccount")

            if (!tokenId.isNullOrEmpty() || isGuestAccount)
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            else
                findNavController().navigate(R.id.action_splashFragment_to_startFragment)
        }
    }
}