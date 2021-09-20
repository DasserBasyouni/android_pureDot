package com.g7.soft.pureDot.ui.screen.transferMoney

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.ContactsAdapter
import com.g7.soft.pureDot.databinding.FragmentTransferMoneyBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.util.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch


class TransferMoneyFragment : Fragment() {
    private lateinit var binding: FragmentTransferMoneyBinding
    private lateinit var viewModelFactory: TransferMoneyViewModelFactory
    internal lateinit var viewModel: TransferMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_transfer_money,
                container,
                false
            )

        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            val currencySymbol = UserRepository("").getCurrencySymbol(requireContext())

            viewModelFactory = TransferMoneyViewModelFactory(
                tokenId = tokenId,
            )
            viewModel =
                ViewModelProvider(this@TransferMoneyFragment, viewModelFactory).get(
                    TransferMoneyViewModel::class.java
                )

            binding.currency = currencySymbol
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@TransferMoneyFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        lifecycleScope.launch {
            val tokenId =
                UserRepository("").getTokenId(requireContext())
            viewModel.getWalletData(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        viewModel.walletResponse.observe(viewLifecycleOwner, {
            viewModel.walletLcee.value!!.response.value = it
        })
        val contactsAdapter = ContactsAdapter(this)
        binding.contactsRv.adapter = contactsAdapter
        viewModel.contactsResponse.observe(viewLifecycleOwner, {
            viewModel.contactsLcee.value!!.response.value = it
            contactsAdapter.submitList(it.data)
        })

        // editText listener
        binding.emailTil.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    lifecycleScope.launch {
                        val tokenId =
                            UserRepository("").getTokenId(requireContext())

                        viewModel.suggestContact(
                            requireActivity().currentLocale.toLanguageTag(),
                            tokenId
                        )
                    }
                }
            }
        })

        // setup on click
        binding.transferBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId =
                    UserRepository("").getTokenId(requireContext())

                viewModel.transferMoney(
                    requireActivity().currentLocale.toLanguageTag(),
                    tokenId = tokenId
                ).observeApiResponse(this@TransferMoneyFragment, {
                    viewModel.walletResponse.value = viewModel.walletResponse.value.apply {
                        ProjectDialogUtils.showSimpleMessage(
                            requireContext(),
                            messageResId = R.string.msg_transfer_success,
                            drawableResId = R.drawable.ic_transfer_money,
                            title = getString(R.string.transfer_money),
                            positiveBtnOnClick = findNavController()::popBackStack
                        )
                    }
                })
            }
        }
    }

}