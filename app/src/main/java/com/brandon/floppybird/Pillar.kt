package com.brandon.floppybird

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import kotlin.random.Random

class Pillar(context: Context, var x: Float) {
    private val paint = Paint()
    val gapHeight = 200f
    var topHeight = Random.nextFloat() * 400f + 100f
    val width = 100f // Adjust based on your pillar's width

    init {
        paint.color = android.graphics.Color.GREEN
    }

    fun update() {
        x -= 5f
        if (x < -100f) {
            x = 900f
            topHeight = Random.nextFloat() * 400f + 100f
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(x, 0f, x + 100f, topHeight, paint)
        canvas.drawRect(x, topHeight + gapHeight, x + 100f, canvas.height.toFloat(), paint)
    }
}