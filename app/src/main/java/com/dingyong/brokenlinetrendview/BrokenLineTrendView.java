package com.dingyong.brokenlinetrendview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 * ScoreTrendView
 */
public class BrokenLineTrendView extends View {
    /**
     * 控件的宽
     */
    private float mWith;
    /**
     * 控件的高
     */
    private float mHeight;
    /**
     * 折线的颜色
     */
    private int mBrokenLineColor;
    /**
     * 虚线的颜色
     */
    private int mDottedLineColor  = getResources().getColor(R.color.color_dotted_line);
    /**
     * 文字的颜色
     */
    private int mTexColor = getResources().getColor(R.color.color_text);
    /**
     * 折线的粗细
     */
    private float mBrokenLineWith = DensityUtil.dip2px(getContext(),1.5f);
    /**
     * 字体大小
     */
    private int mTextSize = DensityUtil.dip2px(getContext(), 12);

    /**
     * 最大的Y轴点
     */
    private Double mMaxYPoint;
    /**
     * 最小的Y轴点
     */
    private Double mMinYPoint;
    /**
     * Y轴的虚线paint
     */
    private Paint mDottedPaint;
    /**
     * 文字的paint
     */
    private Paint mTextPaint;
    /**
     * 折线的paint
     */
    private Paint mBrokenLinePaint;
    /**
     * 折线点的paint
     */
    private Paint mBrokenPointPaint;
    private Paint mScorePaint;
    /**
     * 顶部的remark Paint
     */
    private Paint mRemarkPaint;

    /**
     * 数据源
     */
    private BrokenLineTrendData mBrokenLineTrendData;
    /**
     * X轴坐标轴的数据
     */
    private List<String> mXLineDataList = new ArrayList<>();
    /**
     * Y轴坐标点的数据
     */
    private List<BrokenLineDimension> mDataList = new ArrayList<>();
    /**
     * Y轴的坐标轴的数据
     */
    private List<Double> mYLineDataList = new ArrayList<>();

    /**
     * 折线点的坐标list
     */
    private List<List<Point>> mScorePointsList;

    /**
     * 选中中的index
     */
    private int mSelectIndex = 0;
    /**
     * padding 值
     */
    private int mLeftPadding = DensityUtil.dip2px(getContext(), 15);
    private int mRightPadding = DensityUtil.dip2px(getContext(), 15);
    private int mTopPadding = DensityUtil.dip2px(getContext(), 15);
    private int mBottomPadding = DensityUtil.dip2px(getContext(), 15);
    private int mTextBottomPadding = DensityUtil.dip2px(getContext(), 8);

    /**
     * Y轴区域部分的高度
     */
    private float mYHeight;
    /**
     * Y轴区域部分的高度
     */
    private OnItemClick mOnItemClick;
    /**
     * 记录Y轴的虚线的纵坐标的List
     */
    private List<Float> dottedLineList = new ArrayList<>();


    public BrokenLineTrendView(Context context) {
        this(context, null);
    }

    public BrokenLineTrendView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BrokenLineTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig(context, attrs);
    }

    private void initConfig(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BrokenLineTrendView);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.BrokenLineTrendView_trend_view_color_dotted_line:
                    mDottedLineColor = array.getColor(attr, mBrokenLineColor);
                    break;
                case R.styleable.BrokenLineTrendView_trend_view_color_text:
                    mTexColor = array.getColor(attr, mTexColor);
                    break;
                case R.styleable.BrokenLineTrendView_trend_view_text_size:
                    mTextSize = array.getDimensionPixelSize(attr, mTextSize);
                    break;
            }
        }
        array.recycle();
        initPaint();
        initData();
    }

    private void initPaint() {
        mDottedPaint = new Paint();
        mDottedPaint.setAntiAlias(true);
        mDottedPaint.setStyle(Paint.Style.STROKE);
        mDottedPaint.setColor(mDottedLineColor);
        mDottedPaint.setStrokeWidth(mBrokenLineWith);
        //mDottedPaint.setStrokeCap(Paint.Cap.SQUARE);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTexColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);


        mBrokenLinePaint = new Paint();
        mBrokenLinePaint.setAntiAlias(true);
        mBrokenLinePaint.setStyle(Paint.Style.STROKE);
        mBrokenLinePaint.setColor(Color.GREEN);
        mBrokenLinePaint.setStrokeWidth(mBrokenLineWith);
        mBrokenLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mBrokenPointPaint = new Paint();
        mBrokenPointPaint.setAntiAlias(true);
        mBrokenPointPaint.setStyle(Paint.Style.STROKE);
        mBrokenPointPaint.setColor(Color.GREEN);
        mBrokenPointPaint.setStrokeWidth(mBrokenLineWith);
        mBrokenPointPaint.setStrokeCap(Paint.Cap.ROUND);


        mScorePaint = new Paint();
        mScorePaint = new Paint();
        mScorePaint.setColor(Color.WHITE);
        mScorePaint.setTextSize(DensityUtil.dip2px(getContext(), 12));
        mScorePaint.setAntiAlias(true);

        mRemarkPaint = new Paint();
        mRemarkPaint.setAntiAlias(true);
        mRemarkPaint.setStyle(Paint.Style.FILL);
    }

    private void initData() {
        mBrokenLineTrendData = new BrokenLineTrendData();
        mDataList = mBrokenLineTrendData.mDimensionList;
        mXLineDataList = mBrokenLineTrendData.mXLineDataList;
        mYLineDataList = mBrokenLineTrendData.mYLineDataList;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWith = w;
        mHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawXLineText(canvas);
        drawRemark(canvas);
        drawYLineText(canvas);
        drawDottedLine(canvas);
        drawBrokenLine(canvas);
        drawBrokenPoint(canvas);

    }


    /**
     * 绘制虚线
     * @param canvas Canvas
     */
    private void drawDottedLine(Canvas canvas) {
        int count = mYLineDataList.size();
        if (count > 0) {
            canvas.save();
            mDottedPaint.setPathEffect(new DashPathEffect(new float[]{20, 10}, 4));
            mDottedPaint.setStrokeWidth(1f);
            float height = mYHeight - mBottomPadding;
            float one = 1.00f / count;
            float startX = mWith * 0.1f;
            float stopX = mWith - mRightPadding;
            float startY;
            float stopY;
            for (int i = 0; i < count; i++) {
                startY = stopY = (i + 1) * one * height;
                Path path = new Path();
                path.reset();
                path.moveTo(startX, startY);
                path.lineTo(stopX, stopY);
                canvas.drawPath(path, mDottedPaint);
            }
            canvas.restore();
        }

    }


    /**
     * 绘制Y轴
     * @param canvas canvas
     */
    private void drawYLineText(Canvas canvas) {
        canvas.save();
        float height = mYHeight - mBottomPadding;//
        int count = mYLineDataList.size();
        if (count > 0) {
            float one = 1.00f / count;
            float startY;
            float tempWidth = 0;
            for (int i = 0; i < count; i++) {
                mTextPaint.setColor(mTexColor);
                startY = (i + 1) * one * height;
                String text = String.valueOf(mYLineDataList.get(count - 1 - i));
                float textMaxHeight = getTextHeight(mTextPaint);
                float textWidth = mTextPaint.measureText(text);
                canvas.drawText(text, mLeftPadding, startY + textMaxHeight / 4, mTextPaint);
                dottedLineList.add((count - i) * one * height);
                if (tempWidth < textWidth) {
                    tempWidth = textWidth;
                }
            }
        }
        canvas.restore();
    }

    /**
     * 绘制X轴的text
     * @param canvas canvas
     */
    private void drawXLineText(Canvas canvas) {
        int count = mXLineDataList.size();//拿到X轴数据,进行循环,
        if (count > 0) {
            canvas.save();
            float newWidth = mWith - (mWith * 0.1f) * 2;
            float startX;
            for (int i = 0; i < count; i++) {//在循环中计算相应的坐标点
                mTextPaint.setColor(mTexColor);
                String text = mXLineDataList.get(i);
                startX = ((newWidth * i) / (count - 1) + mWith * 0.1f);
                Rect rectText = new Rect();
                mTextPaint.getTextBounds(text, 0, text.length(), rectText);
                float textSize = rectText.width();//文字的宽
                float textHeight = rectText.height();//文字的高
                if (mSelectIndex == i) {//绘制选中后的text和框框
                    mTextPaint.setStyle(Paint.Style.STROKE);
                    int XDataSelectColor = mBrokenLineTrendData.mSelectColor;
                    mTextPaint.setColor(XDataSelectColor);
                    RectF rectF = new RectF();
                    rectF.left = startX - DensityUtil.dip2px(getContext(), 8);
                    rectF.right = startX + textSize + DensityUtil.dip2px(getContext(), 8);
                    rectF.bottom = mHeight - mBottomPadding;
                    rectF.top = rectF.bottom - mTextBottomPadding - textHeight - DensityUtil.dip2px(getContext(), 4);
                    canvas.drawRoundRect(rectF, 10, 10, mTextPaint);
                    mYHeight = rectF.top;//得到Y轴区域的高度,拿这个高度去计算Y轴需要多高
                }
                canvas.drawText(text, startX, mHeight - mBottomPadding - mTextBottomPadding, mTextPaint);

            }
            canvas.restore();
        }
    }

    /**
     * 绘制折线
     * @param canvas canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        int dataSize = mDataList.size();
        if (dataSize > 0) {
            mScorePointsList = new ArrayList<>();
            for (BrokenLineDimension dimension : mDataList) {
                List<Point> pointList = new ArrayList<>();
                float max = dottedLineList.get(dottedLineList.size() - 1);
                float min = dottedLineList.get(0);
                float newWith = mWith - (mWith * 0.1f) * 2;
                int coordinateX;
                List<Double> doubles = dimension.mDatasList;
                int count = doubles.size();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        Point point = new Point();
                        coordinateX = (int) (((newWith * i) / (count - 1) + mWith * 0.1f) + DensityUtil.dip2px(getContext(), 8));
                        point.x = coordinateX;
                        point.y = (int) ((mMaxYPoint - doubles.get(i)) * (min - max) / (mMaxYPoint - mMinYPoint) + max);
                        pointList.add(point);
                    }
                }
                int size = doubles.size();
                if (size > 0) {
                    canvas.save();
                    Path path = new Path();
                    path.reset();
                    mBrokenLinePaint.setStrokeWidth(mBrokenLineWith);
                    mBrokenLineColor = dimension.mBrokenLineColor;
                    mBrokenLinePaint.setColor(mBrokenLineColor);
                    for (int i = 0; i < size; i++) {
                        if (i == 0) {
                            path.moveTo(pointList.get(0).x, pointList.get(0).y);
                        } else {
                            path.lineTo(pointList.get(i).x, pointList.get(i).y);
                        }
                    }
                    canvas.drawPath(path, mBrokenLinePaint);
                }
                mScorePointsList.add(pointList);
            }
            canvas.restore();
        }
    }


    /**
     * 绘制折线点
     * @param canvas canvas
     */
    private void drawBrokenPoint(Canvas canvas) {
        int dataSize = mDataList.size();
        if (dataSize > 0) {
            canvas.save();
            for (int j = 0; j < dataSize; j++) {
                BrokenLineDimension dimension = mDataList.get(j);
                List<Double> doubles = dimension.mDatasList;
                int count = doubles.size();
                for (int i = 0; i < count; i++) {
                    int brokenPointColor = dimension.mBrokenPointColor;
                    int brokenPointInColor = dimension.mBrokenPointIntColor;
                    int brokenPointOutColor = dimension.mBrokenPointOutColor;

                    mBrokenPointPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 2));
                    mBrokenPointPaint.setStyle(Paint.Style.STROKE);

                    mBrokenPointPaint.setColor(brokenPointColor);
                    canvas.drawCircle(mScorePointsList.get(j).get(i).x, mScorePointsList.get(j).get(i).y, DensityUtil.dip2px(getContext(), 2), mBrokenPointPaint);
                    if (mSelectIndex == i) {
                        mBrokenPointPaint.setColor(Color.WHITE);
                        mBrokenPointPaint.setStyle(Paint.Style.FILL);
                        mBrokenPointPaint.setColor(brokenPointOutColor);
                        canvas.drawCircle(mScorePointsList.get(j).get(i).x, mScorePointsList.get(j).get(i).y, DensityUtil.dip2px(getContext(), 6), mBrokenPointPaint);
                        mBrokenPointPaint.setColor(brokenPointInColor);
                        canvas.drawCircle(mScorePointsList.get(j).get(i).x, mScorePointsList.get(j).get(i).y, DensityUtil.dip2px(getContext(), 3), mBrokenPointPaint);
                        /*drawFloatTextBackground(canvas, mScorePointsList.get(j).get(i).x, mScorePointsList.get(j).get(i).y - DensityUtil.dip2px(getContext(), 8));
                        String score = String.valueOf(doubles.get(i));
                        float width = mScorePaint.measureText(score);
                        float height = getTextHeight(mScorePaint);
                        canvas.drawText(score, mScorePointsList.get(j).get(i).x - width / 2, mScorePointsList.get(j).get(i).y - DensityUtil.dip2px(getContext(), 8) - height / 5 * 3, mScorePaint);*/
                    }
                    mBrokenPointPaint.setColor(0xffffffff);
                    canvas.drawCircle(mScorePointsList.get(j).get(i).x, mScorePointsList.get(j).get(i).y, DensityUtil.dip2px(getContext(), 1.5f), mBrokenPointPaint);
                    mBrokenPointPaint.setStyle(Paint.Style.STROKE);
                    mBrokenPointPaint.setColor(brokenPointColor);
                    canvas.drawCircle(mScorePointsList.get(j).get(i).x, mScorePointsList.get(j).get(i).y, DensityUtil.dip2px(getContext(), 2.5f), mBrokenPointPaint);
                    mBrokenPointPaint.setColor(brokenPointColor);
                }
            }
            canvas.restore();
        }
    }

    private void drawFloatTextBackground(Canvas canvas, int x, int y) {
        Path path = new Path();
        path.reset();
        mBrokenLinePaint.setStyle(Paint.Style.FILL);
        mBrokenLinePaint.setColor(mBrokenLineColor);
        //p1
        Point point = new Point(x, y);
        path.moveTo(point.x, point.y);

        //p2
        point.x = point.x + DensityUtil.dip2px(getContext(), 5);
        point.y = point.y - DensityUtil.dip2px(getContext(), 5);
        path.lineTo(point.x, point.y);

        //p3
        point.x = point.x + DensityUtil.dip2px(getContext(), 12);
        path.lineTo(point.x, point.y);

        //p4
        point.y = point.y - DensityUtil.dip2px(getContext(), 17);
        path.lineTo(point.x, point.y);

        //p5
        point.x = point.x - DensityUtil.dip2px(getContext(), 34);
        path.lineTo(point.x, point.y);

        //p6
        point.y = point.y + DensityUtil.dip2px(getContext(), 17);
        path.lineTo(point.x, point.y);

        //p7
        point.x = point.x + DensityUtil.dip2px(getContext(), 12);
        path.lineTo(point.x, point.y);

        //p8

        path.lineTo(x, y);
        path.close();
        canvas.drawPath(path, mBrokenLinePaint);


    }

    /**
     * 绘制顶部的remark
     * @param canvas canvas
     */
    private void drawRemark(Canvas canvas) {
        canvas.save();
        mRemarkPaint.setStrokeWidth(DensityUtil.dip2px(getContext(),2));
        int size = mDataList.size();
        int paddingRight = DensityUtil.dip2px(getContext(), 40);//右边Padding值
        int totalPadding = paddingRight * (size - 1);//总的Padding值
        int oneWidth = DensityUtil.dip2px(getContext(), 90);//一个itme的长度
        int width = oneWidth * size + totalPadding;//一个item的总长度
        int x = (int) (mWith - width) / 2;//确定开始是坐标位置
        int y = mTopPadding;
        for (int i = 0; i < size; i++) {
            BrokenLineDimension dimension = mDataList.get(i);
            mRemarkPaint.setColor(dimension.mBrokenLineColor);
            int startX = x + (i * oneWidth) + paddingRight;//开始的x点
            int endX = startX + oneWidth - paddingRight;
            canvas.drawLine(startX, y, endX, y, mRemarkPaint);//绘制线条
            float textWidth = mTextPaint.measureText(dimension.remark);
            int offset = (int) ((oneWidth - textWidth - paddingRight) / 2);
            canvas.drawText(dimension.remark, startX + offset , y * 2, mTextPaint);//绘制文字
        }
        canvas.restore();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.getParent().requestDisallowInterceptTouchEvent(true);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);//事件的分发
                break;
            case MotionEvent.ACTION_UP:
                onClick(event);
                this.getParent().requestDisallowInterceptTouchEvent(false);////事件的分发
                break;
        }
        return true;
    }

    private void onClick(MotionEvent event) {
        boolean isValidTouch = validateTouch(event.getX(), event.getY());
        if (isValidTouch) {
            invalidate();
        }
    }

    /**
     * 判断点击区域
     * @param x  x
     * @param y y
     * @return true false
     */
    private boolean validateTouch(float x, float y) {
        //对折线点进行判断
        for (int j = 0; j < mScorePointsList.size(); j++) {
            List<Point> list = mScorePointsList.get(j);
            for (int i = 0; i < list.size(); i++) {
                if (x > list.get(i).x - DensityUtil.dip2px(getContext(), 8) * 2 && x < list.get(i).x + DensityUtil.dip2px(getContext(), 8) * 2) {
                    if (y > list.get(i).y - DensityUtil.dip2px(getContext(), 8) * 2 && y < list.get(i).y + DensityUtil.dip2px(getContext(), 8) * 2) {
                        mSelectIndex = i;
                        if (mOnItemClick != null) {
                            mOnItemClick.onBrokenLinePointClick(i, mDataList.get(j), mXLineDataList);
                        }
                        return true;
                    }
                }
            }
        }
        //对X轴上的点进行判断
        int count = mXLineDataList.size();
        if (count > 0) {
            float newWidth = mWith - (mWith * 0.1f) * 2;
            int coordinateX;
            for (int i = 0; i < count; i++) {
                String text = mXLineDataList.get(i);
                coordinateX = (int) ((newWidth * i) / (count - 1) + mWith * 0.1f);
                float textSize = mTextPaint.measureText(text);
                float textHeight = getTextHeight(mTextPaint);
                mTextPaint.setStyle(Paint.Style.STROKE);
                mTextPaint.setColor(mBrokenLineColor);
                RectF rectF = new RectF();
                rectF.left = coordinateX - DensityUtil.dip2px(getContext(), 8);
                rectF.right = coordinateX + textSize + DensityUtil.dip2px(getContext(), 8);
                rectF.bottom = mHeight - mBottomPadding;
                rectF.top = rectF.bottom - mTextBottomPadding - textHeight - DensityUtil.dip2px(getContext(), 4);
                if (x > rectF.left && x < rectF.right) {
                    if (y > rectF.top - DensityUtil.dip2px(getContext(), 8) && y < rectF.bottom + DensityUtil.dip2px(getContext(), 8)) {
                        mSelectIndex = i;
                        if (mOnItemClick != null) {
                            mOnItemClick.onXLinePointClick(i, mDataList, mXLineDataList);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public interface OnItemClick {
        void onBrokenLinePointClick(int position, BrokenLineDimension dimension, List<String> xData);
        void onXLinePointClick(int position, List<BrokenLineDimension> data, List<String> xData);
    }

    /*************************
     * 对外部提供的方法
     **************************************************************/
    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public void setBrokenLineTrendData(BrokenLineTrendData brokenLineTrendData) {
        this.mBrokenLineTrendData = brokenLineTrendData;
        mDataList = mBrokenLineTrendData.mDimensionList;
        mXLineDataList = mBrokenLineTrendData.mXLineDataList;
        mYLineDataList = mBrokenLineTrendData.mYLineDataList;
        mMaxYPoint = getMaxY();
        mMinYPoint = getMinY();

        invalidate();
    }

    private double getMaxY() {
        Double max = 0d;
        for (Double aDouble : mYLineDataList) {
            if (max < aDouble) {
                max = aDouble;
            }
        }
        return max;
    }

    private double getMinY() {
        Double min = 0d;
        for (Double aDouble : mYLineDataList) {
            if (min > aDouble) {
                min = aDouble;
            }
        }
        return min;
    }


    /**
     * 设置数据源
     * @param data  List<BrokenLineDimension>
     */
    public void setChangeData(List<BrokenLineDimension> data) {
        this.mDataList = data;
        invalidate();
    }

    /**
     * X轴的数据
     * @param xLineData List<String> xLineData
     */
    public void setChangeXData(List<String> xLineData) {
        this.mXLineDataList = xLineData;
        invalidate();
    }

    /**
     * Y轴的数据坐标轴
     * @param yLineData List<Double> yLineData
     */
    public void setChangeYData(List<Double> yLineData) {
        this.mYLineDataList = yLineData;
        mMaxYPoint = getMaxY();
        mMinYPoint = getMinY();
        invalidate();
    }

    /**<p>
     * 虚线的颜色
     * </p>
     * @param dottedLineColor color
     */
    public void setDottedLineColor(int dottedLineColor) {
        mDottedLineColor = dottedLineColor;
        invalidate();
    }

    public void setTexColor(int texColor) {
        mTexColor = texColor;
        invalidate();
    }

    public void setBrokenLineWith(float brokenLineWith) {
        mBrokenLineWith = brokenLineWith;
        invalidate();
    }


    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public float getBrokenLineWith() {
        return mBrokenLineWith;
    }

    public int getTexColor() {
        return mTexColor;
    }

    public int getDottedLineColor() {
        return mDottedLineColor;
    }

    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) (fontMetrics.descent - fontMetrics.ascent);
    }


}
