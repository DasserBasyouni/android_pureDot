package com.g7.soft.pureDot.ext

import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CheckBox
import android.widget.TextView

fun CheckBox.makeLinks(
    vararg links: Pair<String, View.OnClickListener?>,
    isUnderlineText: Boolean = true,
    doChangeColor: Boolean = true,
    isFakeBoldText: Boolean = false,
) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                if (doChangeColor)
                    textPaint.color = textPaint.linkColor
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = isUnderlineText
                textPaint.isFakeBoldText = isFakeBoldText
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second?.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1, true)
        if (startIndexOfLink == -1) continue // to verify your texts contains links text
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    if (links.mapNotNull { it.second }.isNotEmpty())
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}