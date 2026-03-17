package com.tasomaniac.openwith.settings

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroupAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tasomaniac.openwith.base.R

class OneUiGroupDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val density = context.resources.displayMetrics.density

    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.oneui_card_background)
    }

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.oneui_pref_divider)
        strokeWidth = density
    }

    private val cornerRadius = 26 * density
    private val dividerInset = 16 * density

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as? PreferenceGroupAdapter ?: return
        val itemCount = adapter.itemCount

        val path = Path()
        val rect = RectF()

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(view)
            if (pos == RecyclerView.NO_POSITION) continue

            val pref = adapter.getItem(pos) ?: continue
            if (pref is PreferenceCategory) continue

            val isFirst = pos == 0 || adapter.getItem(pos - 1) is PreferenceCategory
            val isLast = pos >= itemCount - 1 || adapter.getItem(pos + 1) is PreferenceCategory

            val left = view.left.toFloat()
            val right = view.right.toFloat()
            val top = view.top.toFloat()
            val bottom = view.bottom.toFloat()

            rect.set(left, top, right, bottom)

            val r = cornerRadius
            val radii = when {
                isFirst && isLast -> floatArrayOf(r, r, r, r, r, r, r, r)
                isFirst            -> floatArrayOf(r, r, r, r, 0f, 0f, 0f, 0f)
                isLast             -> floatArrayOf(0f, 0f, 0f, 0f, r, r, r, r)
                else               -> floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            }

            path.reset()
            path.addRoundRect(rect, radii, Path.Direction.CW)
            c.drawPath(path, cardPaint)

            if (!isFirst) {
                c.drawLine(left + dividerInset, top, right - dividerInset, top, dividerPaint)
            }
        }
    }
}
