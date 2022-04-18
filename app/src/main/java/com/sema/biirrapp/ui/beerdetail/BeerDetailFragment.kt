package com.sema.biirrapp.ui.beerdetail

import android.os.Bundle
import androidx.fragment.app.viewModels
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sema.base.common.Resource
import com.sema.biirrapp.databinding.BeerDetailFragmentBinding
import com.sema.biirrapp.ui.BaseFragment

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerDetailFragment : BaseFragment<BeerDetailFragmentBinding>() {

    private val viewModel by viewModels<BeerDetailViewModel>()
    private val args: BeerDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBeer(args.beerId)
        binding.initializeObservers()
    }
    private fun BeerDetailFragmentBinding.initializeObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.beerDetailFlow.collect {
                when (it.status) {
                    Resource.Status.LOADING -> showLoadingDialog(requireContext())
                    Resource.Status.SUCCESS -> {
                        hideLoadingDialog()
                        it.data?.let { beer = it.first() }
                    }
                    Resource.Status.ERROR -> {
                        hideLoadingDialog()
                        it.error.handleErrorMessage()
                    }
                }
            }
        }
    }
}