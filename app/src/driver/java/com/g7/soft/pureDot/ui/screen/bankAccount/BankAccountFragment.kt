package com.g7.soft.pureDot.ui.screen.bankAccount

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
import com.g7.soft.pureDot.databinding.FragmentBankAccountBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.screen.MainActivity
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.android.synthetic.driver.activity_main.*
import kotlinx.coroutines.launch

class BankAccountFragment : Fragment() {
    private lateinit var binding: FragmentBankAccountBinding
    internal lateinit var viewModel: BankAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_bank_account,
                container,
                false
            )

        viewModel = ViewModelProvider(this).get(BankAccountViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set screen title
        (requireActivity() as MainActivity).toolbar_title.text =
            getString(R.string.update_bank_account_details)

        // set onClick listener
        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.save(requireActivity().currentLocale.toLanguageTag(), tokenId = tokenId)
                    .observeApiResponse(this@BankAccountFragment, {
                        findNavController().popBackStack()
                    })
            }
        }
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}