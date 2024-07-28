package com.brandon.floppybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

class Bird(context: Context) {
    private val birdDrawable: VectorDrawableCompat?
    var x = 100f
    var y = 500f
    private var velocity = 0f
    private val gravity = 0.6f
    val width = 50f // Adjust based on your bird's size
    val height = 50f

    init {
        birdDrawable = VectorDrawableCompat.create(context.resources, R.drawable.flappy_bird_svg, null)
    }

    fun update() {
        velocity += gravity
        y += velocity
    }

    fun jump() {
        velocity = -10f
    }

    fun draw(canvas: Canvas) {
        birdDrawable?.let { drawable ->
            drawable.setBounds(x.toInt(), y.toInt(), (x + 100).toInt(), (y + 100).toInt())
            drawable.draw(canvas)
        }
    }
}
