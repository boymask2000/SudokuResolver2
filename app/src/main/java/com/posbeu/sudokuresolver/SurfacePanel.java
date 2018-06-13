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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gposabella on 30/05/2016.
 */
public class SurfacePanel extends SurfaceView implements SurfaceHolder.Callback {
    private final Context context;





    private MyThread mythread;
    private int screenWidth;
    private int screenHeight;

    private Board board;
    private int numMosse = 0;
    private boolean goSolve = false;

    private Chunk first = null;
    private int i = 0;
    private Paint mPaint = new Paint();

    public SurfacePanel(Context ctx, AttributeSet attrSet) {
        super(ctx, attrSet);
        context = ctx;
        board = new Board(context);

        getDims();



        SurfaceHolder holder = getHolder();

        holder.addCallback(this);
    }

    private List<Chunk> randomize(List<Chunk> ll) {

        Random rand = new Random(100);
        List<Chunk> out = new ArrayList<Chunk>();
        List<Integer> indexes = new ArrayList<>();
        //List<Integer> values = new ArrayList<Integer>();
        for (int i = 0; i < ll.size(); i++) {
            indexes.add(i);
        }
        int count = 0;
        while (!indexes.isEmpty()) {
            int ii = (int) (Math.random() * indexes.size());

            int index = indexes.get(ii);

            indexes.remove(ii);
            out.add(ll.get(index));
            count++;
            if (count % 2 == 0)
                scambia(out.get(count - 1), out.get(count - 2), false);
        }
        return out;
    }


    private void solve() throws InterruptedException {


    }

    //***************************************************************************************
    void doDraw(Canvas canvas) {
        int step = screenWidth / 9;
        for(int i=0; i<=9; i++) {
            canvas.drawLine(i * step, 0, i * step, screenWidth, mPaint);
            canvas.drawLine(0, i * step, screenWidth,i * step, mPaint);
        }
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


    private void scambia(Chunk c1, Chunk c2, boolean slow) {

        int xf = c1.getX();
        int yf = c1.getY();
        int posf = c1.getPosAttuale();
        c1.setX(c2.getX());
        c1.setY(c2.getY());
        c1.setPosAttuale(c2.getPosAttuale());
        c2.setX(xf);
        c2.setY(yf);
        c2.setPosAttuale(posf);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {



        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                numMosse++;
                Chunk p = getChunk(x, y);
                if (p == null) return true;
                p.setSelected(true);
                if (first == null) first = p;
                else {
                    if (first == p) {
                        p.setSelected(false);
                        first.setSelected(false);
                        return true;
                    }

                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                    }
                    scambia(first, p, true);
                    checkFine();
                    p.setSelected(false);
                    first.setSelected(false);
                    first = null;
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:


                break;
            default:
                break;
        }

        return true;
    }

    private void checkFine() {

        PopupMessage.info(context, "Bravo! Completato in " + numMosse + " mosse");
    }

    public Chunk getChunk(float x, float y) {

        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mythread = new MyThread(holder, context, this);

        mythread.setRunning(true);

        mythread.start();
    }

}