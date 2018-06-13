package com.posbeu.sudokuresolver;

import android.graphics.Bitmap;

/**
 * Created by giovanni on 6/2/16.
 */
public class Heap {
    private static Bitmap bitmap = null;


    private static MainActivity activity;
    private static float fact;
    private static Board board;
    public static Pair selectedCell;


    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap m) {
        bitmap = m;
    }


    public static void setMainActivity(MainActivity m) {
        activity=m;
    }
    public static MainActivity getActivity() {
        return activity;
    }

    public static void setFact(float fact) {
        Heap.fact = fact;
    }

    public static float getFact() {
        return fact;
    }

    public static void setBoard(Board board) {
        Heap.board = board;
    }

    public static Board getBoard() {
        return board;
    }
}
