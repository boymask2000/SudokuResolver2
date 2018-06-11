package com.posbeu.sudokuresolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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

    private final Bitmap resizedPhotoBitMap;
    private final List<Chunk> lista;


    private MyThread mythread;
    private int screenWidth;
    private int screenHeight;

    private Board board;
    private int numMosse = 0;
    private boolean goSolve = false;

    private Chunk first = null;
    private int i = 0;

    public SurfacePanel(Context ctx, AttributeSet attrSet) {
        super(ctx, attrSet);
        context = ctx;
        board = new Board(context);

        getDims();

        Bitmap photoBitMap = Heap.getBitmap();

        SurfaceHolder holder = getHolder();
        resizedPhotoBitMap = getResizedBitmap(photoBitMap);
        Heap.setBitmap(resizedPhotoBitMap);
        holder.addCallback(this);
        lista = randomize(board.prepareImage(resizedPhotoBitMap));

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
        if (i >= lista.size()) return;
        Chunk c = lista.get(i++);
        //   for (Chunk c : lista) {

        int pos = c.getPosCorretta();
        if (pos != c.getPosAttuale()) {
            for (Chunk d : lista) {
                if (d.getPosAttuale() == pos) {

                    MotionEvent motionEvent1 = MotionEvent.obtain(
                            SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN,
                            c.getX() + 1,
                            c.getY() + 1,
                            0
                    );

                    this.dispatchTouchEvent(motionEvent1);

                    Thread.sleep(1000);
                    MotionEvent motionEvent2 = MotionEvent.obtain(
                            SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN,
                            d.getX() + 1,
                            d.getY() + 1,
                            0
                    );

                    this.dispatchTouchEvent(motionEvent2);

                    break;
                }
            }
        }
    }

    void doDraw(Canvas canvas) {


        List<Chunk> ll = new ArrayList<Chunk>();

        for (Chunk c : lista) {
            c.draw(canvas);
            if (c.isSelected()) ll.add(c);
        }
        for (Chunk c : ll) {
            c.draw(canvas);

        }


        if (goSolve) try {
            solve();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       /* for (Moving m : transiti) {
            m.nextStep();
        }*/
    }

    public void goSolve() {
        goSolve = true;
    }

    public Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) screenWidth) / width;
        float scaleHeight = ((float) screenHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //    bm.recycle();
        return resizedBitmap;
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
        for (Chunk c : lista) {
            if (c.getPosAttuale() != c.getPosCorretta()) return;
        }
        PopupMessage.info(context, "Bravo! Completato in " + numMosse + " mosse");
    }

    public Chunk getChunk(float x, float y) {
        for (Chunk c : lista) {
            if (c.isInside(x, y))
                return c;
        }
        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mythread = new MyThread(holder, context, this);

        mythread.setRunning(true);

        mythread.start();
    }

}