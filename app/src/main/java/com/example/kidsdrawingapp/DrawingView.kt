package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context : Context , attrs:AttributeSet) : View(context , attrs) {

    private var mDrawCustomPath : CustomPath? = null
    private var mDrawPaint : Paint? = null

    private var mCanvas : Canvas? = null
    private var mDrawCanvasBitmap : Bitmap? = null
    private var mCanvasPaint : Paint? = null

    private var color = Color.BLACK
    private var brushSize : Float? = 0.toFloat()


    init {
        setupDrawingVariables()
    }

    internal inner class CustomPath(var color : Int , var brushThickness : Float) : Path(){

    }

    private fun setupDrawingVariables(){

        mDrawPaint = Paint()
        mDrawCustomPath = CustomPath(color , brushSize!!)

        //drawPaint => 1/color  2/style  3/stokeJoin  4/stokeCap
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        brushSize = 20.toFloat()

        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    //onDraw function => Paint + Canvas
    //What should happen if we want to DRAW
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(mDrawCanvasBitmap!! , 0f , 0f , mCanvasPaint)
        canvas?.drawPath(mDrawCustomPath!! , mDrawPaint!!)

        mDrawPaint!!.strokeWidth = mDrawCustomPath!!.brushThickness
        mDrawPaint!!.color = mDrawCustomPath!!.color
    }

    //onSizeChange function => canvas
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //ARGB related to COLOR Schema
        mDrawCanvasBitmap = Bitmap.createBitmap(w , h, Bitmap.Config.ARGB_8888)

        mCanvas = Canvas(mDrawCanvasBitmap!!)

    }

    //onTouchEvent function => Path
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX = event?.x
        val touchY = event?.y

        //have three actions if user touch the screen
        //1- put user finger on screen
        //2- move user finger over screen
        //3- release user finger out of screen

        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                mDrawCustomPath!!.color = color
                mDrawCustomPath!!.brushThickness = brushSize!!

                mDrawCustomPath!!.reset()
                mDrawCustomPath!!.moveTo(touchX!! , touchY!!)
            }
            MotionEvent.ACTION_MOVE ->{
                mDrawCustomPath!!.lineTo(touchX!! , touchY!!)
            }
            MotionEvent.ACTION_UP ->{
                //reset
                mDrawCustomPath = CustomPath(color , brushSize!!)
            }
            else -> return false
        }
        invalidate()
        return true
    }
}