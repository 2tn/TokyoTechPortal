package com.example.yusuke.opencvsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Yusuke on 2016/01/12.
 */
public class OverlayView extends View {
    Paint paint;
    private float[] line = null;
    private float[] dot = null;
    private Bitmap bmp = null;
    private final static int LINE = 0;
    private final static int DOT = 1;
    private final static int BITMAP = 2;
    private int type = -1;


    public OverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public OverlayView(Context context) {
        super(context);
        initialize();
    }

    public void initialize() {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    public void setline(float[] line) {
        this.line = line;
        type = LINE;
        invalidate();
    }

    public void setdot(float[] dot) {
        this.dot = dot;
        type = DOT;
        invalidate();
    }

    public void setbitmap(Bitmap bmp) {
        this.bmp = bmp;
        type = BITMAP;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (type) {
            case LINE:
                canvas.drawLines(line, paint);
                line = null;
                break;
            case DOT:
                canvas.drawPoints(dot, paint);
                dot = null;
                break;
            case BITMAP:
                canvas.drawBitmap(bmp, 0, 0, paint);
                bmp = null;
                break;
            default:
        }
    }
}
