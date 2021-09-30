package com.g7.soft.pureDot.ui.screen.complain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.CommentsAdapter
import com.g7.soft.pureDot.constant.ApiConstant
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.databinding.FragmentComplainBinding
import com.g7.soft.pureDot.ext.observeApiResponse
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import com.g7.soft.pureDot.utils.ProjectDialogUtils
import com.zeugmasolutions.localehelper.currentLocale
import kotlinx.coroutines.launch

class ComplainFragment : Fragment() {
    internal lateinit var binding: FragmentComplainBinding
    private lateinit var viewModelFactory: ComplainViewModelFactory
    internal lateinit var viewModel: ComplainViewModel
    private val args: ComplainFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_complain, container, false)

        viewModelFactory = ComplainViewModelFactory(
            complain = args.complain,
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComplainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetch data
        lifecycleScope.launch {
            val tokenId = UserRepository("").getTokenId(requireContext())
            viewModel.getComplainComments(requireActivity().currentLocale.toLanguageTag(), tokenId)
        }

        // setup observers
        val commentsAdapter = CommentsAdapter(this)
        binding.reviewsRv.adapter = commentsAdapter
        viewModel.reviewsResponse.observe(viewLifecycleOwner, {
            viewModel.reviewsLcee.value!!.response.value = it
            commentsAdapter.submitList(it.data)
        })

        // add decoration divider
        binding.reviewsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )

        // setup click listener
        binding.rateServiceBtn.setOnClickListener {
            lifecycleScope.launch {
                val tokenId = UserRepository("").getTokenId(requireContext())

                if (args.complain.status == ApiConstant.ComplainStatus.RESOLVED.value) {
                    ProjectDialogUtils.showComplaintServiceRating(this@ComplainFragment)
                } else {
                    viewModel.addComplaintReply(
                        requireActivity().currentLocale.toLanguageTag(),
                        tokenId = tokenId
                    ).observeApiResponse(this@ComplainFragment, { commentModel ->
                        viewModel.reviewsResponse.value = viewModel.reviewsResponse.value.also {
                            if (commentModel != null) it?.data?.add(commentModel)
                        }
                    }, validationObserve = {
                        binding.messageTil.error =
                            if (it == ProjectConstant.Companion.ValidationError.EMPTY_COMMENT)
                                getString(R.string.error_comment) else null
                    })
                }
            }
        }

    }

}