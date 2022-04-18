package com.sema.biirrapp.ui

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.jintin.bindingextension.BindingFragment
import com.sema.base.common.Error
import com.sema.biirrapp.R

open class BaseFragment<VB : ViewBinding> : BindingFragment<VB>() {

    private var loadingDialog: Dialog? = null

    fun showLoadingDialog(context: Context) {
        loadingDialog = Dialog(context)
        loadingDialog?.let {
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            it.setContentView(R.layout.custom_loading_dialog)
            it.show()
        }
    }

    open fun hideLoadingDialog() = loadingDialog?.dismiss()

    fun showToast(message: CharSequence) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

    fun Error?.handleErrorMessage() {
        this?.data?.message?.let { showToast(it) }
            ?: showToast(getString(R.string.generic_error_message))
    }
}