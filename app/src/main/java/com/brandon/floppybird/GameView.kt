package com.brandon.floppybird

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {
    private var thread: Thread? = null
    private var isPlaying = false
    private val bird: Bird
    private val pillars: List<Pillar>
    private var score = 0
    private var gameTime = 0L
    private val backgroundPaint: Paint = Paint()
    private var isGameOver = false

    init {
        holder.addCallback(this)
        bird = Bird(context)
        pillars = List(3) { Pillar(context, it * 300f) }
        backgroundPaint.color = Color.WHITE

    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            control()
        }
    }

    private fun update() {
        bird.update()
        pillars.forEach { it.update() }
        checkCollision()
        gameTime++
        if (gameTime % 100 == 0L) score++
    }

    private fun draw() {
        if (holder.surface.isValid) {
            val canvas: Canvas = holder.lockCanvas()
            canvas.drawColor(Color.WHITE)
            bird.draw(canvas)
            // Draw pillars
            pillars.forEach { it.draw(canvas) }
            // Draw score and time
            drawScoreAndTime(canvas)
            if (isGameOver) {
                canvas.drawText("Game Over", width / 2f, height / 2f, backgroundPaint)
                canvas.drawText("Tap to restart", width / 2f, height / 2f + 100f, backgroundPaint)
            }
            holder.unlockCanvasAndPost(canvas)
        }

    }

    private fun control() {
        Thread.sleep(17) // ~60 FPS
    }

    private fun checkCollision() {
        if (bird.y < 0 || bird.y + bird.height > height) {
            gameOver()
            return
        }

        // Check collision with pillars
        for (pillar in pillars) {
            if (bird.x < pillar.x + pillar.width &&
                bird.x + bird.width > pillar.x &&
                (bird.y < pillar.topHeight ||
                        bird.y + bird.height > pillar.topHeight + pillar.gapHeight)
            ) {
                gameOver()
                return
            }
        }
    }

    private fun drawScoreAndTime(canvas: Canvas) {
        val paint = Paint()
        paint.textSize = 50f
        canvas.drawText("Score: $score", 50f, 50f, paint)
        canvas.drawText("Time: ${gameTime / 60}", 50f, 100f, paint)
    }

    fun pause() {
        isPlaying = false
        thread?.join()
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }

    private fun gameOver() {
        isGameOver = true
        pause()
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        resume()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> bird.jump()
        }
        return true
    }
}