package com.posbeu.sudokuresolver.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.posbeu.sudokuresolver.Heap;
import com.posbeu.sudokuresolver.MainActivity;


public class Table {

    private static final int NUMROWS = 9;
    private static final int NUMCOLS = 9;
    private TableCell[][] table = new TableCell[10][10];
    private TableCell selectedCell;
    private MainActivity main;

    public Table() {
        for (int i = 0; i <= 9; i++)
            for (int j = 0; j <= 9; j++)
                table[i][j] = new TableCell(i, j);

    }

    public void setFixed(int i, int j, int k) {

        table[i][j].setFixed(k);

    }

    public static int getNumrows() {
        return NUMROWS;
    }

    public static int getNumcols() {
        return NUMCOLS;
    }

    public TableCell getCell(int i, int j) {

        return table[i][j];
    }

    public boolean check() {
        boolean quad = checkQuad(0, 0);
        quad = quad && checkQuad(0, 3);
        quad = quad && checkQuad(0, 6);
        quad = quad && checkQuad(3, 0);
        quad = quad && checkQuad(3, 3);
        quad = quad && checkQuad(3, 6);
        quad = quad && checkQuad(6, 0);
        quad = quad && checkQuad(6, 3);
        quad = quad && checkQuad(6, 6);
        if (!quad)
            return quad;

        for (int i = 0; i < 10; i++)
            quad = quad && checkCol(i) && checkRow(i);
        return quad;
    }

    private boolean checkQuad(int i, int j) {
        if (table[i][j].isEmpty())
            return true;
        int vals[] = new int[10];
        vals[table[i][j].getCurrentVal()]++;
        vals[table[i + 1][j].getCurrentVal()]++;
        vals[table[i + 2][j].getCurrentVal()]++;
        vals[table[i][j + 1].getCurrentVal()]++;
        vals[table[i + 1][j + 1].getCurrentVal()]++;
        vals[table[i + 2][j + 1].getCurrentVal()]++;
        vals[table[i][j + 2].getCurrentVal()]++;
        vals[table[i + 1][j + 2].getCurrentVal()]++;
        vals[table[i + 2][j + 2].getCurrentVal()]++;

        for (int jj = 1; jj <= 9; jj++)
            if (vals[jj] > 1)
                return false;
        return true;
    }

    private boolean checkCol(int i) {
        int vals[] = new int[10];
        for (int j = 0; j < NUMROWS; j++) {
            TableCell cell = table[j][i];
            if (cell.isEmpty())
                continue;
            vals[cell.getCurrentVal()]++;
            if (vals[cell.getCurrentVal()] > 1) return false;

        }
        for (int j = 0; j < NUMROWS; j++)
            if (vals[j] > 1)
                return false;
        return true;
    }

    private boolean checkRow(int i) {
        int vals[] = new int[10];
        for (int j = 0; j < NUMCOLS; j++) {
            TableCell cell = table[i][j];
            if (cell.isEmpty())
                continue;
            vals[cell.getCurrentVal()]++;
            if (vals[cell.getCurrentVal()] > 1) return false;

        }
        for (int j = 0; j < NUMCOLS; j++)
            if (vals[j] > 1)
                return false;
        return true;
    }

    // public void dump() {
    // for (int i = 0; i < NUMROWS; i++) {
    // for (int j = 0; j < NUMCOLS; j++) {
    // TableCell cell = table[i][j];
    // if (cell.isEmpty())
    // System.out.print(" ");
    // else
    // System.out.print(cell.getCurrentVal());
    // }
    // System.out.println();
    // }
    // }


    public TableCell getCellByCoord(int x, int y) {
        if (x < 0 || y < 0)
            return null;
        if (x > 450 || y > 450)
            return null;
        int xx = x / 50;
        int yy = y / 50;
        return table[xx][yy];
    }

    public void setSelectedCell(int x, int y) {

        selectedCell = table[x][y];
        Heap.selectedCell = selectedCell;
    }

    public void setSelectedCell(TableCell c) {
        System.out.println(c);
        selectedCell = c;
        main.update();

    }

    public void setMain(MainActivity main) {
        this.main = main;

    }

    public void setSelectedValue(int i) {
        if (selectedCell != null)
            selectedCell.setFixed(i);

    }

    public void setFree() {
        if (selectedCell != null)
            selectedCell.setEmpty();

    }

    public void setClean() {
        if (selectedCell != null)
            selectedCell.setClean();

    }

    public void cleanAll() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {

                TableCell cell = table[i][j];
                cell.setClean();

            }
    }

    public void draw(Canvas canvas, Paint mPaint, int screenWidth) {
        //  setTextSizeForWidth(mPaint,300, "1");
        int cSize = screenWidth / 9;
        mPaint.setTextSize(cSize);
        int fattX = screenWidth / 9;
        int fattY = screenWidth / 9;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                TableCell cell = table[i][j];

                int x = i * fattX + 2;
                int y = j * fattY + 2;
                fill(canvas, screenWidth, x, y, Color.WHITE);


                if (selectedCell != null && selectedCell.equals(cell)) {
                    x = i * fattX + 2;
                    y = j * fattY + 2;

                    fill(canvas, screenWidth, x, y, Color.GRAY);
                }


                if (!cell.isEmpty())
                {

                    canvas.drawText("" + cell.getCurrentVal(), i * fattX + 2, (j + 1) * fattY - 4, mPaint);
                }
            }
    }

    private void fill(Canvas canvas, int screenWidth, int x, int y, int color) {
        int size = screenWidth / 9 - 4;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRect(x, y, x + size, y + size, paint);
    }

    public void dump() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {

                TableCell cell = table[i][j];
                System.out.print("-"+i+" "+j+" :"+ cell.getCurrentVal());

            }System.out.println();
    }

}
