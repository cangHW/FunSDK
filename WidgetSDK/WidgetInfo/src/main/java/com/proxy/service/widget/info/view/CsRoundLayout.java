package com.proxy.service.widget.info.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.proxy.service.widget.info.R;

/**
 * @author: cangHX
 * @date: 2023/11/20 11:47
 * @desc:
 */
public class CsRoundLayout extends RelativeLayout {

    private final RectF rectF = new RectF();
    private final float[] radiusF = new float[8];
    private final Path path = new Path();

    private float radiusLT = 0f;
    private float radiusRT = 0f;
    private float radiusLB = 0f;
    private float radiusRB = 0f;

    private int maxHeight = -1;

    public CsRoundLayout(@NonNull Context context) {
        this(context, null);
    }

    public CsRoundLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CsRoundLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (isInLayout()) {
            return;
        }
        if (isInEditMode()) {
            return;
        }
        if (attrs == null) {
            return;
        }
        @SuppressLint("CustomViewStyleable") TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.cs_widget_RoundLayout, 0, 0);
        int radius = array.getDimensionPixelSize(R.styleable.cs_widget_RoundLayout_round_radius, -1);
        int radius_l_t = array.getDimensionPixelSize(R.styleable.cs_widget_RoundLayout_round_radius_top_left, -1);
        int radius_r_t = array.getDimensionPixelSize(R.styleable.cs_widget_RoundLayout_round_radius_top_right, -1);
        int radius_r_b = array.getDimensionPixelSize(R.styleable.cs_widget_RoundLayout_round_radius_bottom_right, -1);
        int radius_l_b = array.getDimensionPixelSize(R.styleable.cs_widget_RoundLayout_round_radius_bottom_left, -1);
        array.recycle();

        if (radius >= 0) {
            radiusLT = radius;
            radiusRT = radius;
            radiusLB = radius;
            radiusRB = radius;
        }

        if (radius_l_t >= 0) {
            radiusLT = radius_l_t;
        }

        if (radius_r_t >= 0) {
            radiusRT = radius_r_t;
        }

        if (radius_r_b >= 0) {
            radiusRB = radius_r_b;
        }

        if (radius_l_b >= 0) {
            radiusLB = radius_l_b;
        }

        radiusF[0] = radiusLT;
        radiusF[1] = radiusLT;
        radiusF[2] = radiusRT;
        radiusF[3] = radiusRT;
        radiusF[4] = radiusRB;
        radiusF[5] = radiusRB;
        radiusF[6] = radiusLB;
        radiusF[7] = radiusLB;
    }

    public void setRadius(int radius) {
        setRadius(radius, radius, radius, radius);
    }

    public void setRadius(int radiusLT, int radiusRT, int radiusRB, int radiusLB) {
        radiusF[0] = radiusLT;
        radiusF[1] = radiusLT;
        radiusF[2] = radiusRT;
        radiusF[3] = radiusRT;
        radiusF[4] = radiusRB;
        radiusF[5] = radiusRB;
        radiusF[6] = radiusLB;
        radiusF[7] = radiusLB;
        requestLayout();
        postInvalidate();
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = heightMeasureSpec;
        if (maxHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        rectF.left = 0f;
        rectF.top = 0f;
        rectF.right = r - l;
        rectF.bottom = b - t;

        path.reset();
        path.addRoundRect(rectF, radiusF, Path.Direction.CCW);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int num = canvas.save();
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(num);
    }

    @Override
    public void onDrawForeground(@NonNull Canvas canvas) {
        int num = canvas.save();
        canvas.clipPath(path);
        super.onDrawForeground(canvas);
        canvas.restoreToCount(num);
    }

    @Override
    public void draw(Canvas canvas) {
        int num = canvas.save();
        canvas.clipPath(path);
        super.draw(canvas);
        canvas.restoreToCount(num);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        int num = canvas.save();
//        canvas.clipPath(path);
//        super.onDraw(canvas);
//        canvas.restoreToCount(num);
//    }
}
