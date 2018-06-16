package com.posbeu.sudokuresolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.posbeu.sudokuresolver.core.Table;
import com.posbeu.sudokuresolver.core.TableCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
    private boolean goSolve = false;
    private int i = 0;
    private Paint mPaint = new Paint();

    public SurfacePanel(Context ctx, AttributeSet attrSet, MainActivity mainActivity) {
        super(ctx, attrSet);
        context = ctx;
        this.mainActivity = mainActivity;
        board = new Board(context);
//setBackgroundColor(Color.RED);

        getDims();
        setClipBounds(new Rect(0,0,screenWidth,screenWidth));

        SurfaceHolder holder = getHolder();

        holder.addCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        doDraw(canvas);
    }

    //***************************************************************************************
    void doDraw(Canvas canvas) {
        int step = screenWidth / 9;
        for (int i = 0; i <= 9; i++) {
            canvas.drawLine(i * step, 0, i * step, screenWidth, mPaint);
            canvas.drawLine(0, i * step, screenWidth, i * step, mPaint);
        }
        //     fill(canvas, screenWidth, 3 * step);
        //    fill(canvas, screenWidth, 6 * step);

        Table table = mainActivity.getTable();
        table.draw(canvas, mPaint, screenWidth);

    }


    private void fill(Canvas canvas, int screenWidth, int x) {
        int delta = 3;
        int size = screenWidth;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawRect(x - delta, 0, x + delta, screenWidth, paint);
        canvas.drawRect(0, x - delta, screenWidth, x + delta, paint);
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

        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;

        float x = event.getX();
        float y = event.getY();

        Pair cella = getCella(x, y);
        if (cella == null) return false;

        Heap.selectedCell = mainActivity.getTable().getCellByCoord(cella.getX(), cella.getY());

        mainActivity.getTable().setSelectedCell(cella.getX(), cella.getY());
        showPoup(x, y);


/*
        Table table = mainActivity.getTable();
        if(!table.check())
        {
          //  TableCell c = table.getCellByCoord(cella.getX(), cella.getY());
           c.setError(true);
        }
*/


        return true;
    }


    private void showPoup(float x, float y) {
        PopupNumberWindow popup = new PopupNumberWindow(mainActivity);
        popup.show((int) x, (int) y);
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
        invalidate();
    }
}
