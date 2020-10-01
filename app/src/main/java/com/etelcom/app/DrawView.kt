package com.etelcom.app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.fragment_new_file.view.*


class DrawView(c: Context, attrSet: AttributeSet) : View(c) {
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private val mPath: Path
    private val mBitmapPaint: Paint
    private val mPaint: Paint

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
        canvas.drawPath(mPath, mPaint)
    }

    private var mX = 0f
    private var mY = 0f
    private fun touch_start(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touch_move(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touch_up() {
        mPath.lineTo(mX, mY)
        // commit the path to our offscreen
        mCanvas!!.drawPath(mPath, mPaint)
        // kill this so we don't double draw
        mPath.reset()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touch_start(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touch_move(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touch_up()
                invalidate()
            }
        }
        return true
    }

    fun clear() {
        mBitmap!!.eraseColor(Color.TRANSPARENT)
        invalidate()
        System.gc()
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }

    init {
        mPath = Path()
        mBitmapPaint = Paint(Paint.DITHER_FLAG)
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = -0x1000000
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 5f
    }
}