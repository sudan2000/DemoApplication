package com.susu.baselibrary.recyclerview.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

/**
 * Author : sudan
 * Time : 2021/11/23
 * Description:
 */
class GridItemDecoration(val color: Int, private val column: Int) : RecyclerView.ItemDecoration() {
    var paint: Paint = Paint()

    init {
        paint.color = color
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        if (childCount <= 0) {
            return
        }
        val rows = (childCount + column - 1) / column
        val pWidth = parent.width
        val cWidth = parent.width / column
        val cHeight = cWidth

        for (index in 0..rows) {

        }
    }
}