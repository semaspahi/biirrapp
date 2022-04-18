package com.sema.biirrapp.ui.beer

import android.os.Bundle
import androidx.fragment.app.viewModels
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sema.base.common.Resource
import com.sema.base.component.CommonRecyclerAdapter
import com.sema.base.data.model.Beer
import com.sema.base.util.scrollListener
import com.sema.biirrapp.databinding.BeerFragmentBinding
import com.sema.biirrapp.R
import com.sema.biirrapp.databinding.ItemBeerBinding
import com.sema.biirrapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerFragment : BaseFragment<BeerFragmentBinding>() {

    private val viewModel by viewModels<BeerViewModel>()

    private val beerAdapter by lazy {
        CommonRecyclerAdapter<Beer>(R.layout.item_beer) { binding, item, _ ->
            (binding as ItemBeerBinding).beer = item
        }.apply {
            onItemClickListener = { openBeerDetail(it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setupRecyclerView()
        initializeObservers()
        viewModel.getBeers()
    }

    private fun BeerFragmentBinding.setupRecyclerView() {
        rvBeer.apply {
            adapter = beerAdapter
            scrollListener(requireContext(),
                isLastItem = { viewModel.getNextBeers(viewModel.page) })
        }
    }

    private fun initializeObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.beerListFlow.collect {
                when (it.status) {
                    Resource.Status.LOADING -> showLoadingDialog(requireContext())
                    Resource.Status.SUCCESS -> {
                        hideLoadingDialog()
                        it.data?.let { beerAdapter.appendItems(it) }
                    }
                    Resource.Status.ERROR -> {
                        hideLoadingDialog()
                        it.error.handleErrorMessage()
                    }
                }
            }
        }
    }

    private fun openBeerDetail(beer: Beer?) {
        beer?.id?.let{
            val action = BeerFragmentDirections.actionBeerListToBeerDetail(it)
            findNavController().navigate(action)
        }
    }

}