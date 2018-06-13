package com.posbeu.sudokuresolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.posbeu.sudokuresolver.core.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gposabella on 30/05/2016.
 */
public class SurfacePanel extends SurfaceView implements SurfaceHolder.Callback {
    private final Context context;
    private final MainActivity mainActivity;


    private MyThread mythread;
    private int screenWidth;
    private int screenHeight;

    private Board board;
    private int numMosse = 0;
    private boolean goSolve = false;


    private int i = 0;
    private Paint mPaint = new Paint();

    public SurfacePanel(Context ctx, AttributeSet attrSet, MainActivity mainActivity) {
        super(ctx, attrSet);
        context = ctx;
        this.mainActivity=mainActivity;
        board = new Board(context);

        getDims();


        SurfaceHolder holder = getHolder();

        holder.addCallback(this);
    }


    private void solve() throws InterruptedException {


    }

    //***************************************************************************************
    void doDraw(Canvas canvas) {
        int step = screenWidth / 9;
        for (int i = 0; i <= 9; i++) {
            canvas.drawLine(i * step, 0, i * step, screenWidth, mPaint);
            canvas.drawLine(0, i * step, screenWidth, i * step, mPaint);
        }

        Table table = mainActivity.getTable();
        table.draw(canvas,mPaint,screenWidth);

    }

    public void goSolve() {
        goSolve = true;
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight() {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)//&& !hasMenuKey)
        {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void getDims() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();
        Point size = new Point();


        display.getSize(size);

        int v1 = getStatusBarHeight();
        int v2 = getNavigationBarHeight();

        screenWidth = size.x;
        screenHeight = size.y - v1 - v2;
        float f = (float) screenHeight / screenWidth;

        if (screenHeight >= screenWidth) Heap.setFact(f);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mythread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try {
                mythread.join();

                retry = false;
            } catch (Exception e) {

                Log.v("Exception Occured", e.getMessage());
            }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        Pair cella = getCella(x, y);

        Heap.selectedCell = cella;

//mainActivity.getSudoku().setClick((int)x,(int)y);
        mainActivity.getTable().setSelectedCell(cella.getX(),cella.getY());

        return true;
    }

    private Pair getCella(float x, float y) {
        int posx = (int) (x * 9 / screenWidth);
        int posy = (int) (y * 9 / screenWidth);

        if (posx < 0 || posx > 8) return null;
        if (posy < 0 || posy > 8) return null;



        return new Pair(posx, posy);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mythread = new MyThread(holder, context, this);

        mythread.setRunning(true);

        mythread.start();
    }

    public void update() {
    }
}
