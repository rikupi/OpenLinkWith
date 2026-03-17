package com.tasomaniac.openwith.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.openwith.base.R

/**
 * OneUI-style card decoration for regular RecyclerView lists.
 * @param headerCount Number of header items at the top to exclude from card styling.
 */
class OneUiListDecoration(
    context: Context,
    private val headerCount: Int = 0
) : RecyclerView.ItemDecoration() {

    private val density = context.resources.displayMetrics.density

    private val cardColor = ContextCompat.getColor(context, R.color.oneui_card_background)
    private val rippleColor = ContextCompat.getColor(context, R.color.oneui_ripple)

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.oneui_pref_divider)
        strokeWidth = density
    }

    private val cornerRadius = 26 * density
    private val dividerInset = 16 * density
    private val cardMargin = (8 * density).toInt()

    private enum class Role { SINGLE, FIRST, MIDDLE, LAST, HEADER }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = (parent.adapter?.itemCount ?: return) - headerCount

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(view)
            if (pos == RecyclerView.NO_POSITION) continue

            if (pos < headerCount) {
                if (view.tag != Role.HEADER) {
                    view.tag = Role.HEADER
                    view.background = null
                    view.foreground = null
                }
                continue
            }

            val contentPos = pos - headerCount
            val isFirst = contentPos == 0
            val isLast = contentPos >= itemCount - 1

            val role = when {
                isFirst && isLast -> Role.SINGLE
                isFirst           -> Role.FIRST
                isLast            -> Role.LAST
                else              -> Role.MIDDLE
            }

            if (view.tag != role) {
                view.tag = role
                view.foreground = null
                view.background = createBackground(role)
            }

        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = (parent.adapter?.itemCount ?: return) - headerCount

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(view)
            if (pos == RecyclerView.NO_POSITION) continue
            if (pos < headerCount) continue

            val contentPos = pos - headerCount
            if (contentPos > 0) {
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
            else        -> floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        }
        val shape = ShapeDrawable(RoundRectShape(radii, null, null))
        shape.paint.color = cardColor
        return RippleDrawable(ColorStateList.valueOf(rippleColor), shape, shape)
    }
}
