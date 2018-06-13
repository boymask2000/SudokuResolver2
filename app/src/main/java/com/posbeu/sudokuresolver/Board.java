package com.posbeu.sudokuresolver;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gposabella on 31/05/2016.
 */
public class Board {
    private static int NROW = 10;

    public static void setNCOL(int NCOL) {
        Board.NCOL = NCOL;
    }

    private static int NCOL = 10;

    private final Context context;
    private int chunkHeight;
    private int chunkWidth;

    public Board(Context context) {
        this.context = context;
        Heap.setBoard(this);
    }



}
