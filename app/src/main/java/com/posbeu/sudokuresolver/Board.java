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


    public List<Chunk> prepareImage(Bitmap bit) {
        NROW = (int) (NCOL * Heap.getFact());
        List<Chunk> lista = new ArrayList<Chunk>();

        chunkHeight = bit.getHeight() / NROW;
        chunkWidth = bit.getWidth() / NCOL;

        int pos = 0;
        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for (int x = 0; x < NROW; x++) {
            int xCoord = 0;
            for (int y = 0; y < NCOL; y++) {
                Bitmap b = Bitmap.createBitmap(bit, xCoord, yCoord, chunkWidth, chunkHeight);
                Chunk c = new Chunk(b, xCoord, yCoord, chunkWidth, chunkHeight);
                c.setPosCorretta(pos);
                c.setPosAttuale(pos++);
                //  lista.add(Bitmap.createBitmap(bit, xCoord, yCoord, chunkWidth, chunkHeight));
                lista.add(c);
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
        return lista;
    }
}
