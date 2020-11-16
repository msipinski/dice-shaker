package com.example.dice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DiceView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private var bitmap: Bitmap = diceBitmaps[0]
    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            drawBitmap(bitmap, 0F, 0F, null)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(mSize, mSize)
    }

    var dotsCount = 0
        set(value) {
            require(value in 0..6)
            field = value
            bitmap = diceBitmaps[value]
            invalidate()
        }

    companion object {
        private const val mSize = 256
        private val diceBitmaps: List<Bitmap> = run {
            val paintBlack = Paint().apply { color = Color.BLACK }
            val paintGreen = Paint().apply { color = Color.GREEN }
            //black background
            val bitmapBackground =
                Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888).apply {
                    Canvas(this).drawRoundRect(
                        0F,
                        0F,
                        mSize.toFloat(),
                        mSize.toFloat(),
                        mSize / 8F,
                        mSize / 8F,
                        paintBlack
                    )
                }
            listOf(
                listOf(),
                listOf(1 to 1),
                listOf(0 to 0, 2 to 2),
                listOf(0 to 2, 1 to 1, 2 to 0),
                listOf(0 to 0, 2 to 2, 0 to 2, 2 to 0),
                listOf(0 to 0, 2 to 2, 0 to 2, 2 to 0, 1 to 1),
                listOf(0 to 0, 0 to 1, 0 to 2, 2 to 0, 2 to 1, 2 to 2),
            ).map { points ->
                Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888).apply {
                    Canvas(this).run {
                        drawBitmap(bitmapBackground, 0F, 0F, null)
                        points.forEach { (x, y) ->
                            drawCircle(
                                (x + 1) * (mSize / 4F),
                                (y + 1) * (mSize / 4F),
                                mSize / 12F,
                                paintGreen
                            )
                        }
                    }
                }
            }
        }
    }
}