package com.example.myfirstapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class MyTestView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private Paint mPaint;

    public MyTestView(Context context) {
        super(context);
        init(null, 0);
    }

    public MyTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyTestView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.MyTestView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.MyTestView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.MyTestView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.MyTestView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.MyTestView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);

    }

    private void drawBoard(Canvas canvas){

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        int nRow = 8;

        mPaint.setStyle(Paint.Style.FILL);
        int darkSquareColor = ContextCompat.getColor(getContext(), R.color.boardSquareDark);
        int lightSquareColor = ContextCompat.getColor(getContext(), R.color.boardSquareLight);
        for (int row=0; row<nRow; row++){
            for (int col=0; col<nRow; col++){
                if ((col + row) % 2 == 0) {
                    mPaint.setColor(darkSquareColor);
                } else {
                    mPaint.setColor(lightSquareColor);
                }
                    canvas.drawRect(
                        paddingLeft + col * contentWidth / nRow,
                        paddingTop + row * contentHeight / nRow,
                        paddingLeft + (col + 1) * contentWidth / nRow,
                        paddingTop + (row + 1) * contentHeight / nRow,
                        mPaint
                    );
            }
        }



        // Border around the board
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(
                paddingLeft,
                paddingTop,
                paddingLeft + contentWidth,
                paddingTop + contentHeight,
                mPaint
        );
    }
}
