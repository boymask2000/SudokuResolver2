package com.posbeu.sudokuresolver;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.posbeu.sudokuresolver.core.TableCell;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PopupNumberWindow extends android.widget.PopupWindow {

    private final MainActivity ctx;
    private final View customView;

    public PopupNumberWindow(MainActivity context) {
        super(context);
        ctx = context;


        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);



        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        customView = inflater.inflate(R.layout.custom, null);

setContentView(customView);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        handleButtons(customView);
    }

    public void show(int x, int y) {
        System.out.println("show");
        showAtLocation(ctx.findViewById(R.id.layout), Gravity.CENTER, x, y);
      //  showAtLocation(customView, Gravity.CENTER, x, y);
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

        Button clean = v.findViewById(R.id.clean);
        clean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ctx.getSudoku().setClean();
            }
        });
    }

    private void setFixedVal(int n) {
        if (Heap.selectedCell == null) return;
        TableCell p = Heap.selectedCell;
        ctx.getTable().setFixed(p.getX(), p.getY(), n);

        dismiss();

    }
}
