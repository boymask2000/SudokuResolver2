package com.posbeu.sudokuresolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
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


    public MyThread getThread() {
        return mythread;
    }

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
        this.mainActivity = mainActivity;
        board = new Board(context);

        getDims();


        SurfaceHolder holder = getHolder();

        holder.addCallback(this);
    }


    private void solve() throws InterruptedException {


    }

    @Override
    protected void onDraw(Canvas canvas) {

        doDraw(canvas);
    }

    void doDraw1(Canvas canvas) {

    }

    //***************************************************************************************
    void doDraw(Canvas canvas) {
        int step = screenWidth / 9;
        for (int i = 0; i <= 9; i++) {
            canvas.drawLine(i * step, 0, i * step, screenWidth, mPaint);
            canvas.drawLine(0, i * step, screenWidth, i * step, mPaint);
        }
        fill(canvas, screenWidth, 3 * step);
        fill(canvas, screenWidth, 6 * step);

        Table table = mainActivity.getTable();
        table.draw(canvas, mPaint, screenWidth);

    }


    private void fill(Canvas canvas, int screenWidth, int x) {
        int size = screenWidth;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawRect(x - 1, 0, x + 2, screenWidth, paint);
        canvas.drawRect(0, x - 1, screenWidth, x + 2, paint);
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

        Heap.selectedCell = mainActivity.getTable().getCellByCoord(cella.getX(), cella.getY());

//mainActivity.getSudoku().setClick((int)x,(int)y);
        mainActivity.getTable().setSelectedCell(cella.getX(), cella.getY());
        showPoup();
        return true;
    }

    private void setFixedVal(int n) {
        if (Heap.selectedCell == null) return;
        TableCell p = Heap.selectedCell;
        mainActivity.getTable().setFixed(p.getX(), p.getY(), n);

        mPopupWindow.dismiss();
        mPopupWindow.update();
    }

    private PopupWindow mPopupWindow;

    private void showPoup() {
        LinearLayout lay = findViewById(R.id.buttons);


        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        float density = this.getResources().getDisplayMetrics().density;
// create a focusable PopupWindow with the given layout and correct size


        View customView = inflater.inflate(R.layout.custom, null);
        handleButtons(customView);
        mPopupWindow = new PopupWindow(customView, (int) density * 240, (int) density * 285, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
/*        mPopupWindow.setTouchInterceptor(new OnTouchListener() {



            public boolean onTouch(View v, MotionEvent event)

            {

                if(event.getAction() == MotionEvent.ACTION_OUTSIDE)

                {

                    mPopupWindow.dismiss();

                    return true;

                }

                return false;

            }

        });*/
      mPopupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
    //    mPopupWindow.showAsDropDown(customView);
    }

    private void handleButtons(View v) {

        Button b1 = v.findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(1);
            }
        });
        Button b2 = v.findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(2);
            }
        });
        Button b3 = v.findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(3);
            }
        });
        Button b4 = v.findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(4);
            }
        });
        Button b5 = v.findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(5);
            }
        });
        Button b6 = v.findViewById(R.id.b6);
        b6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(6);
            }
        });
        Button b7 = v.findViewById(R.id.b7);
        b7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(7);
            }
        });
        Button b8 = v.findViewById(R.id.b8);
        b8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(8);
            }
        });
        Button b9 = v.findViewById(R.id.b9);
        b9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setFixedVal(9);
            }
        });
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
