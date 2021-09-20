package com.g7.soft.pureDot.ui.screen.myAccount

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.SettingsAdapter
import com.g7.soft.pureDot.databinding.FragmentMyAccountBinding
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class MyAccountFragment : Fragment() {
    private lateinit var binding: FragmentMyAccountBinding
    internal lateinit var viewModel: MyAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_my_account, container, false)

        lifecycleScope.launch {
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            viewModel = ViewModelProvider(this@MyAccountFragment).get(MyAccountViewModel::class.java)

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@MyAccountFragment
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.getUserData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.userDataResponse.observe(viewLifecycleOwner, {
            viewModel.userDataLcee.value!!.response.value = it

            // setup settings
            binding.settingsRv.adapter = SettingsAdapter(this, it.data)
        })

        // add decoration divider
        binding.settingsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.editBtn.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notification, menu)
    }
}