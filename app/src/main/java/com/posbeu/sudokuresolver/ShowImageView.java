package com.posbeu.sudokuresolver;

/**
 * Created by giovanni on 6/3/16.
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class ShowImageView extends View {

    private Bitmap titleGraphic;

    private int screenW;
    private int screenH;
    private boolean playButtonPressed;
    private Activity myContext;

    public ShowImageView(Activity context) {
        super(context);
        myContext = context;
        titleGraphic = Heap.getBitmap();

    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(titleGraphic, (screenW - titleGraphic.getWidth()) / 2, 0, null);

    }


    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {

            case MotionEvent.ACTION_DOWN:
                myContext.finish();
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        invalidate();
        return true;
    }
}