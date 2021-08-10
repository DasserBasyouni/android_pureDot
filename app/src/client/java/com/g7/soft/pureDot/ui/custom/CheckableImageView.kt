package com.g7.soft.pureDot.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageView

class CheckableImageView(context: Context?, attrs: AttributeSet?) : AppCompatImageView(
    context!!, attrs
), Checkable {
    private var mChecked = false
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        return drawableState
    }

    override fun toggle() {
        isChecked = !mChecked
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked == checked) return
        mChecked = checked
        refreshDrawableState()
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}