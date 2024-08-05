package com.example.imagefilter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

public class DraggableTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final int INVALID_POINTER_ID = -1;
    private float x1, y1, x2, y2;
    private int ptrId1, ptrId2;
    private float mAngle;

    private float _xDelta;
    private float _yDelta;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    private float mScaleFactor = 1.f;

    private boolean isTwoFingerGesture = false;

    private float mPosX;
    private float mPosY;

    private float originTextWidth;
    private float originTextHeight;

    private OnDoubleClickListener mOnDoubleClickListener;


    public DraggableTextView(Context context) {
        super(context);
        init(context);
    }

    public DraggableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DraggableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());

        mAngle = 0;
        ptrId1 = INVALID_POINTER_ID;
        ptrId2 = INVALID_POINTER_ID;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        updateTextSize();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateTextSize();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                ptrId1 = event.getPointerId(event.getActionIndex());
                _xDelta = mPosX - event.getRawX();
                _yDelta = mPosY - event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                ptrId1 = INVALID_POINTER_ID;
                isTwoFingerGesture = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                ptrId2 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isTwoFingerGesture = true;
                ptrId2 = event.getPointerId(event.getActionIndex());
                if (event.getPointerCount()==2){
                    x1 = event.getX(event.findPointerIndex(ptrId1));
                    y1 = event.getY(event.findPointerIndex(ptrId1));
                    x2 = event.getX(event.findPointerIndex(ptrId2));
                    y2 = event.getY(event.findPointerIndex(ptrId2));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                    if(ptrId1 != INVALID_POINTER_ID && ptrId2 != INVALID_POINTER_ID && event.getPointerCount()==2){
                        float nX1, nY1, nX2, nY2;
                        nX1 = event.getX(event.findPointerIndex(ptrId1));
                        nY1 = event.getY(event.findPointerIndex(ptrId1));
                        nX2 = event.getX(event.findPointerIndex(ptrId2));
                        nY2 = event.getY(event.findPointerIndex(ptrId2));
                        float prevAngle = calculateAngle(x1, y1, x2, y2);
                        float newAngle = calculateAngle(nX1, nY1, nX2, nY2);
                        float deltaAngle = newAngle-prevAngle;
                        setNewAngle(deltaAngle);
                        invalidate();
                    }
                    else if (!isTwoFingerGesture){
                        mPosX = event.getRawX() + _xDelta;
                        mPosY = event.getRawY() + _yDelta;
                        invalidate();
                    }
                break;}
        return true;
    }

    private void updateTextSize(){
        int viewWidth = getWidth();
        originTextWidth = getPaint().measureText(getText().toString());
        if (originTextWidth > viewWidth) {
            originTextWidth = viewWidth;

        }
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        float spacingMultiplier = 1;
        float spacingAddition = 0;
        boolean includePadding = false;

        StaticLayout myStaticLayout = new StaticLayout(
                getText(),
                getPaint(),
                viewWidth, alignment,
                spacingMultiplier,
                spacingAddition,
                includePadding);
        originTextHeight = myStaticLayout.getHeight();
    }

    private float calculateAngle(float x1, float y1, float x2, float y2) {
        double deltaX = (x1 - x2);
        double deltaY = (y1 - y2);
        double radians = Math.atan2(deltaY, deltaX);
        float angle = (float) Math.toDegrees(radians);
        if (angle<0)
            angle+=360;
        return angle;
    }
    private void setNewAngle(float deltaAngle){
        if (Float.isNaN(deltaAngle)) return;
        mAngle = deltaAngle;
        mAngle = mAngle%360;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mPosX, mPosY);
        float pivotX = (originTextWidth * mScaleFactor) / 2;
        float pivotY = (originTextHeight * mScaleFactor) / 2;
        canvas.rotate(mAngle, pivotX, pivotY);
        canvas.scale(mScaleFactor, mScaleFactor, pivotX, pivotY);
        super.onDraw(canvas);
        canvas.restore();
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            invalidate();
            return true;
        }
    }

    public void setOnDoubleClickListener(OnDoubleClickListener listener){
        this.mOnDoubleClickListener = listener;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mOnDoubleClickListener!=null){
                mOnDoubleClickListener.onDoubleClick(DraggableTextView.this);
            }
            return true;
        }
    }

    interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }
}
