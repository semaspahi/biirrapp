package com.sema.base.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ToggleButton
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * http://kotlinextensions.com/
 */

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}

@SuppressLint("ClickableViewAccessibility")
fun View.disableTouch() {
    isClickable = false
    isFocusable = false
    setOnTouchListener { _, _ -> false }
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNotNull(): Boolean {
    contract {
        returns(true) implies (this@isNotNull != null)
    }
    return this != null
}

fun Fragment.dp(number: Number): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        number.toFloat(),
        resources.displayMetrics
    )

fun Activity.dp(number: Number): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        number.toFloat(),
        resources.displayMetrics
    )

fun View.dp(number: Number): Float =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        number.toFloat(),
        resources.displayMetrics
    )

infix fun SwitchCompat.onCheckedChanged(function: (CompoundButton, Boolean) -> Unit) {
    setOnCheckedChangeListener(function)
}

infix fun ToggleButton.onCheckedChanged(function: (CompoundButton, Boolean) -> Unit) {
    setOnCheckedChangeListener(function)
}

infix fun View?.onClick(function: () -> Unit) {
    this?.setOnClickListener { function() }
}

infix fun EditText.addTextChangedListener(function: (Boolean, Int) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            function(if (text == null || text.toString().isEmpty()) false else text.toString()
                .isNotEmpty(), text.toString().length)
        }
    })
}


fun EditText.afterTextChanged(afterTextChanged: (String?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.adapterDataObserver(function: () -> Unit) {

    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            update()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            update()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            update()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            update()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            update()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            update()
        }

        fun update() {
            function.invoke()
        }

    })
}

fun View?.showView(visibility: Boolean) {
    this?.visibility = if (visibility) View.VISIBLE else View.GONE
}

infix fun TabLayout.addOnTabSelectedListener(function: (TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            function(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    })
}

fun RecyclerView.scrollBottomOnKeyboardOpen() {
    addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
        if (bottom < oldBottom) {
            scrollBy(0, oldBottom - bottom);
        }
    }
}

fun RecyclerView.scrollListener(
    context: Context,
    shrinkButton: ((Boolean) -> Unit?)? = null,
    isLastItem: ((Boolean) -> Unit?)? = null,
) {

    var isListGoingUp = false
    var visible = true

    layoutManager = LinearLayoutManager(context).also { lm ->
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                isListGoingUp = dy <= 0

                if (!isListGoingUp) {
                    recyclerView.adapter?.itemCount?.let {
                        if(it - 1 == lm.findLastVisibleItemPosition()) isLastItem?.invoke(true)
                    }
                }
            }

            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                recyclerView.adapter?.itemCount?.let {
                    if (newState != RecyclerView.SCROLL_STATE_IDLE && !isListGoingUp && visible && it > 0) {
                        visible = false
                        shrinkButton?.invoke(true)
                    }

                    if (isListGoingUp && !visible) {
                        shrinkButton?.invoke(false)
                        visible = true
                    }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
}