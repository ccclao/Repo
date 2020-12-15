package com.aotem.repo.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * @Description:自定义按钮
 * @Author: aotem
 * @Time: 2020/12/15 17:48.
 */
public class DraggableButton extends AppCompatImageView {
    
    private Paint mPaint;
    private Rect mRect;
    private String text;
    private int backgroundColor;
    private int pressedColor;
    
    public DraggableButton(Context context) {
        this(context, null);
    }

    public DraggableButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.getAttrs(context, attrs);
        this.init();
    }

    private void init() {
        mRect = new Rect();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(1);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DraggableButton, Color.parseColor("#81D4fA"));
        this.backgroundColor = ta.getColor(R.styleable.DraggableButton_dragBtn_background, Color.parseColor("#0277BD"));
        this.pressedColor = ta.getColor(R.styleable.DraggableButton_dragBtn_pressed);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = getWidth() / 2;
        //边框
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(backgroundColor);
        canvas.drawCircle(radius, radius , radius - 2, mPaint);
        // 按下时有背景变化
        if (isPressed()){
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(pressedColor);
            canvas.drawCircle(radius, radius , radius - 4, mPaint);
        }
        
        if (!TextUtils.isEmpty(text)){
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(radius / 2);
            mPaint.getTextBounds(text, 0, text.length(), mRect);

            int textHeight = mRect.bottom - mRect.top;
            int textWidth = mRect.right - mRect.left;
            canvas.drawText(text, radius - textWidth / 2, radius + textHeight / 2, mPaint);
        }
        
        super.onDraw(canvas);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        // View的状态有发生改变的触发
        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getPressedColor() {
        return pressedColor;
    }

    public void setPressedColor(int pressedColor) {
        this.pressedColor = pressedColor;
    }
}
