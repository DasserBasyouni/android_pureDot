package com.g7.soft.pureDot.ui.screen.addMoney

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
import com.g7.soft.pureDot.databinding.FragmentAddMoneyBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class AddMoneyFragment : Fragment() {
    private lateinit var binding: FragmentAddMoneyBinding
    internal lateinit var viewModel: AddMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_add_money,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(AddMoneyViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup onClick
        binding.addMoneyBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.addMoney(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                )
                    .observeApiResponse(this@AddMoneyFragment, {
                        findNavController().popBackStack()
                    })
            }
        }
    }

}