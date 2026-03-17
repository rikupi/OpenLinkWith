package com.tasomaniac.openwith.settings

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroupAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.openwith.R as AppR
import com.tasomaniac.openwith.base.R

class OneUiGroupDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val density = context.resources.displayMetrics.density

    private val cardColor = ContextCompat.getColor(context, R.color.oneui_card_background)
    private val rippleColor = ContextCompat.getColor(context, R.color.oneui_ripple)

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.oneui_pref_divider)
        strokeWidth = density
    }

    private val cornerRadius = 26 * density
    private val dividerInset = 16 * density

    private enum class Role { SINGLE, FIRST, MIDDLE, LAST }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as? PreferenceGroupAdapter ?: return
        val itemCount = adapter.itemCount

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(view)
            if (pos == RecyclerView.NO_POSITION) continue

            val pref = adapter.getItem(pos) ?: continue
            if (pref is PreferenceCategory) {
                if (view.getTag(AppR.id.oneui_card_role) != null) {
                    view.background = null
                    view.setTag(AppR.id.oneui_card_role, null)
                }
                continue
            }

            val isFirst = pos == 0 || adapter.getItem(pos - 1) is PreferenceCategory
            val isLast = pos >= itemCount - 1 || adapter.getItem(pos + 1) is PreferenceCategory

            val role = when {
                isFirst && isLast -> Role.SINGLE
                isFirst           -> Role.FIRST
                isLast            -> Role.LAST
                else              -> Role.MIDDLE
            }

            if (view.getTag(AppR.id.oneui_card_role) != role) {
                view.setTag(AppR.id.oneui_card_role, role)
                view.background = createBackground(role)
            }

            if (!isFirst) {
                c.drawLine(
                    view.left + dividerInset, view.top.toFloat(),
                    view.right - dividerInset, view.top.toFloat(),
                    dividerPaint
                )
            }
        }
    }

    private fun createBackground(role: Role): RippleDrawable {
        val r = cornerRadius
        val radii = when (role) {
            Role.SINGLE -> floatArrayOf(r, r, r, r, r, r, r, r)
            Role.FIRST  -> floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f)
            Role.LAST   -> floatArrayOf(0f, 0f, 0f, 0f, r, r, r, r)
            Role.MIDDLE -> floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        }
        val shape = ShapeDrawable(RoundRectShape(radii, null, null))
        shape.paint.color = cardColor
        return RippleDrawable(ColorStateList.valueOf(rippleColor), shape, shape)
    }
}
