package com.aotem.repo.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.robotpos.R.styleable;

/**
 * FileName: CompassView
 * Author: lao
 * Date: 2020/11/19 10:12
 * Description: compass罗盘
 */
public class CompassView extends View {
    private static final double CONVERSION_ANGLE_CONST = 0.017453292519943295D;
    private static final String TAG = "CompassView";
    private Paint mPaint;
    private Paint mKeyPaint;
    private Paint mTextPaint;
    private Paint mTmpPaint;
    private Paint mOrientationTextPoint;
    private Paint mAngleTextPoint;
    private Paint mMainPaint;
    private Paint mMidpointTextPoint;
    private float rotate = 0.0F;
    private float textW = 0.0F;
    private float textH = 0.0F;
    private float textOriW = 0.0F;
    private float textOriH = 0.0F;
    private float textAngleW1;
    private float textAngleW2;
    private int width;
    private int height;
    private static final float DIVIDE_COUNT = 120.0F;
    private double lineRateSize = 0.045454545454545456D;
    private int lineColor;
    private int keyLineColor;
    private int mainLinecolor;
    private int edgeTextColor;
    private int orientationTextColor;
    private int angleTextColor;
    private int edgeTextSize;
    private int orientationTextSize;
    private int angleTextSize;
    private int oriTextMargin;
    private int rowPitch;
    private int mainLineLength;
    private boolean isDebug = true;
    private int edgeTextMargin;

    public void setRotate(float rotate) {
        this.rotate = (float)((int)(-rotate));
        this.invalidate();
    }

    public CompassView(Context context) {
        super(context);
        this.init();
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.getAttrs(context, attrs);
        this.init();
    }

    public CompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.getAttrs(context, attrs);
        this.init();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, styleable.CompassView);
        ta.getInt(styleable.CompassView_cv_divideCount, 120);
        this.angleTextColor = ta.getColor(styleable.CompassView_cv_angleTextColor, Color.parseColor("#ffffff"));
        this.edgeTextColor = ta.getInt(styleable.CompassView_cv_edgeTextColor, Color.parseColor("#00ffff"));
        this.keyLineColor = ta.getInt(styleable.CompassView_cv_keyLineColor, Color.parseColor("#ffffee"));
        this.lineColor = ta.getInt(styleable.CompassView_cv_lineColor, Color.parseColor("#00cccc"));
        this.orientationTextColor = ta.getInt(styleable.CompassView_cv_orientationTextColor, Color.parseColor("#00cccc"));
        this.mainLinecolor = ta.getInt(styleable.CompassView_cv_mainLineColor, Color.parseColor("#ffffee"));
        this.isDebug = ta.getBoolean(styleable.CompassView_cv_isDebug, false);

        ta.recycle();
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setColor(this.lineColor);
        this.mPaint.setStrokeWidth(1.0F);
        this.mPaint.setAntiAlias(true);
        this.mKeyPaint = new Paint();
        this.mKeyPaint.setStrokeWidth(1.0F);
        this.mKeyPaint.setColor(this.keyLineColor);
        this.mKeyPaint.setAntiAlias(true);
        this.mMainPaint = new Paint();
        this.mMainPaint.setStrokeWidth(4.0F);
        this.mMainPaint.setColor(this.mainLinecolor);
        this.mMainPaint.setAntiAlias(true);
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(this.edgeTextColor);
        this.mTextPaint.setStrokeWidth(2.0F);
        this.mTextPaint.setTextSize((float)this.edgeTextSize);
        this.mTextPaint.setAntiAlias(true);

        if (this.isDebug) {
            this.mTmpPaint = new Paint();
            this.mTmpPaint.setColor(Color.parseColor("#ff0000"));
            this.mTmpPaint.setStrokeWidth(8.0F);
            this.mTmpPaint.setAntiAlias(true);
        }

        this.mOrientationTextPoint = new Paint();
        this.mOrientationTextPoint.setColor(this.orientationTextColor);
        this.mOrientationTextPoint.setStrokeWidth(3.0F);
        this.mOrientationTextPoint.setTextSize((float)this.orientationTextSize);
        this.mOrientationTextPoint.setAntiAlias(true);
        this.mAngleTextPoint = new Paint();
        this.mAngleTextPoint.setColor(this.angleTextColor);
        this.mAngleTextPoint.setStrokeWidth(2.0F);
        this.mAngleTextPoint.setTextSize((float)this.angleTextSize);
        this.mAngleTextPoint.setAntiAlias(true);
        this.mMidpointTextPoint = new Paint();
        this.mMidpointTextPoint.setColor(this.angleTextColor);
        this.mMidpointTextPoint.setStrokeWidth(2.0F);
        this.mMidpointTextPoint.setTextSize((float)(this.angleTextSize * 22 / 28));
        this.mMidpointTextPoint.setAntiAlias(true);
        this.initWH();
    }

    void initSize() {
        this.edgeTextSize = 38 - (1080 - this.width) / 30;
        this.edgeTextMargin = 50 - (1080 - this.width) / 30;
        this.orientationTextSize = 42 - (1080 - this.width) / 30;
        this.oriTextMargin = 92 - (1080 - this.width) / 12;
        this.angleTextSize = 60 - (1080 - this.width) * 4 / 75;
        this.rowPitch = 56 - (1080 - this.width) / 20;
        this.mainLineLength = 35 - (1080 - this.width) / 30;
    }

    private void initWH() {
        String test = "120°";
        Rect rect = new Rect();
        this.mTextPaint.getTextBounds(test, 0, test.length(), rect);
        this.textW = (float)rect.width();
        this.textH = (float)rect.height();
        test = "120";
        rect = new Rect();
        this.mAngleTextPoint.getTextBounds(test, 0, test.length(), rect);
        this.textAngleW1 = (float)rect.width();
        test = "东北";
        rect = new Rect();
        this.mMidpointTextPoint.getTextBounds(test, 0, test.length(), rect);
        this.textAngleW2 = (float)rect.width();
        test = "东";
        rect = new Rect();
        this.mOrientationTextPoint.getTextBounds(test, 0, test.length(), rect);
        this.textOriW = (float)rect.width();
        this.textOriH = (float)rect.height();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = this.getMeasuredWidth();
        this.height = this.getMeasuredHeight();
        Log.d("lwz", "width: " + this.width + " ;height :" + this.height);
        this.height = this.width;
        this.initSize();
        this.init();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float ax = 3.0F;

        for(int i = 0; (float)i < 120.0F; ++i) {
            float rotateAngle = (ax * (float)i - this.rotate + 360.0F) % 360.0F;
            canvas.drawLine(this.getRotatePointX(rotateAngle, 80.0F, (float)(this.height / 2)), this.getRotatePointY(rotateAngle, 80.0F, (float)(this.height / 2)), this.getRotatePointX(rotateAngle, (float)((double)this.width * this.lineRateSize + 80.0D), (float)(this.height / 2)), this.getRotatePointY(rotateAngle, (float)((double)this.width * this.lineRateSize + 80.0D), (float)(this.height / 2)), this.mPaint);
            if ((rotateAngle + this.rotate) % 30.0F == 0.0F) {
                canvas.drawLine(this.getRotatePointX(rotateAngle, 75.0F, (float)(this.height / 2)), this.getRotatePointY(rotateAngle, 75.0F, (float)(this.height / 2)), this.getRotatePointX(rotateAngle, (float)((double)this.width * this.lineRateSize + 80.0D), (float)(this.height / 2)), this.getRotatePointY(rotateAngle, (float)((double)this.width * this.lineRateSize + 80.0D), (float)(this.height / 2)), this.mKeyPaint);
                float genAngle = ((270.0F - rotateAngle < 0.0F ? 270.0F - rotateAngle + 360.0F : 270.0F - rotateAngle) - this.rotate + 360.0F) % 360.0F;
                String strA;
                if (genAngle < 10.0F) {
                    strA = " " + (int)genAngle + "°";
                } else if (genAngle < 100.0F) {
                    strA = " " + (int)genAngle + "°";
                } else {
                    strA = "" + (int)genAngle + "°";
                }

                canvas.drawText(strA, this.getRotatePointX(rotateAngle, (float)(80 - this.edgeTextMargin), (float)(this.height / 2)) - this.textW / 2.0F, this.getRotatePointY(rotateAngle, (float)(80 - this.edgeTextMargin), (float)(this.height / 2)) + this.textH / 2.0F, this.mTextPaint);
                if (this.isDebug) {
                    canvas.drawPoint(this.getRotatePointX(rotateAngle, (float)(80 - this.edgeTextMargin), (float)(this.height / 2)), this.getRotatePointY(rotateAngle, (float)(80 - this.edgeTextMargin), (float)(this.height / 2)), this.mTmpPaint);
                }

                String strOri;
                if ((int)genAngle == 0) {
                    strOri = "北";
                    this.drawOriText(canvas, strOri, rotateAngle);
                } else if (genAngle == 90.0F) {
                    strOri = "东";
                    this.drawOriText(canvas, strOri, rotateAngle);
                } else if (genAngle == 180.0F) {
                    strOri = "南";
                    this.drawOriText(canvas, strOri, rotateAngle);
                } else if (genAngle == 270.0F) {
                    strOri = "西";
                    this.drawOriText(canvas, strOri, rotateAngle);
                }
            }
        }

        canvas.drawText((int)Math.abs(this.rotate) + "°", (float)(this.width / 2) - this.textAngleW1 / 2.0F, (float)(this.height / 2), this.mAngleTextPoint);
        canvas.drawLine((float)(this.width / 2), (float)(80 - this.mainLineLength), (float)(this.width / 2), (float)((double)this.width * this.lineRateSize + 80.0D), this.mMainPaint);
        String currentOri;
        if ((int)Math.abs(this.rotate) == 0) {
            currentOri = "北";
        } else if ((int)Math.abs(this.rotate) == 90) {
            currentOri = "东";
        } else if ((int)Math.abs(this.rotate) == 180) {
            currentOri = "南";
        } else if ((int)Math.abs(this.rotate) == 270) {
            currentOri = "西";
        } else if ((int)Math.abs(this.rotate) < 90) {
            currentOri = "东北";
        } else if ((int)Math.abs(this.rotate) < 180) {
            currentOri = "东南";
        } else if ((int)Math.abs(this.rotate) < 270) {
            currentOri = "西南";
        } else {
            currentOri = "西北";
        }

        canvas.drawText(currentOri, (float)(this.width / 2) - this.textAngleW2 / 2.0F, (float)(this.height / 2 + this.rowPitch), this.mMidpointTextPoint);
        if (this.isDebug) {
            canvas.drawPoint((float)(this.width / 2), (float)(this.height / 2 + this.rowPitch / 2), this.mTmpPaint);
        }

    }

    private void drawOriText(Canvas canvas, String strOri, float rotateAngle) {
        canvas.drawText(strOri, this.getRotatePointX(rotateAngle, (float)(80 + this.oriTextMargin), (float)(this.height / 2)) - this.textOriW / 2.0F, this.getRotatePointY(rotateAngle, (float)(80 + this.oriTextMargin), (float)(this.height / 2)) + this.textOriH / 2.0F, this.mOrientationTextPoint);
        if (this.isDebug) {
            canvas.drawPoint(this.getRotatePointX(rotateAngle, (float)(80 + this.oriTextMargin), (float)(this.height / 2)), this.getRotatePointY(rotateAngle, (float)(80 + this.oriTextMargin), (float)(this.height / 2)), this.mTmpPaint);
        }

    }

    private float getRotatePointX(float a, float x, float y) {
        return (float)((double)(x - (float)(this.width / 2)) * Math.cos(0.017453292519943295D * (double)a) + (double)(y - (float)(this.height / 2)) * Math.sin(0.017453292519943295D * (double)a)) + (float)(this.width / 2);
    }

    private float getRotatePointY(float a, float x, float y) {
        return (float)((double)(y - (float)(this.height / 2)) * Math.cos(0.017453292519943295D * (double)a) - (double)(x - (float)(this.width / 2)) * Math.sin(0.017453292519943295D * (double)a)) + (float)(this.height / 2);
    }
}
