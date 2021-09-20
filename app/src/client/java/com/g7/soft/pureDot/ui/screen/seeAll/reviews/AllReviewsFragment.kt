package com.g7.soft.pureDot.ui.screen.seeAll.reviews

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.g7.soft.pureDot.R
import com.g7.soft.pureDot.adapter.PagedReviewsAdapter
import com.g7.soft.pureDot.constant.ProjectConstant
import com.g7.soft.pureDot.data.PaginationDataSource
import com.g7.soft.pureDot.databinding.FragmentAllReviewsBinding
import com.g7.soft.pureDot.model.ReviewModel
import com.g7.soft.pureDot.repo.UserRepository
import com.g7.soft.pureDot.ui.DividerItemDecorator
import kotlinx.coroutines.launch


class AllReviewsFragment : Fragment() {
    private lateinit var binding: FragmentAllReviewsBinding
    private lateinit var viewModelFactory: AllReviewsViewModelFactory
    internal lateinit var viewModel: AllReviewsViewModel
    private val args: AllReviewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_all_reviews, container, false)

        lifecycleScope.launch {
            val tokenId =
                UserRepository("").getTokenId(requireContext())

            viewModelFactory = AllReviewsViewModelFactory(
                itemId = args.itemId,
                isProduct = args.isProduct,
                tokenId = tokenId
            )
            viewModel =
                ViewModelProvider(
                    this@AllReviewsFragment,
                    viewModelFactory
                ).get(AllReviewsViewModel::class.java)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this@AllReviewsFragment
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup pagination
        viewModel.reviewsPagedList =
            PaginationDataSource.initializedPagedListBuilder<ReviewModel>(
                config = PagedList.Config.Builder()
                    .setPageSize(ProjectConstant.ITEMS_PER_PAGE)
                    .setEnablePlaceholders(true)
                    .build(),
                fragment = this@AllReviewsFragment,
            ).build()

        // setup observers
        val reviewsAdapter = PagedReviewsAdapter(this)
        binding.reviewsRv.adapter = reviewsAdapter
        viewModel.reviewsPagedList?.observe(viewLifecycleOwner, {
            reviewsAdapter.submitList(it)
        })

        // add decoration divider
        binding.reviewsRv.addItemDecoration(
            DividerItemDecorator(
                ContextCompat.getDrawable(requireContext(), R.drawable.reviews_divider_layer)!!
            )
        )
    }
}