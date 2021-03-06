package com.posbeu.sudokuresolver.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.posbeu.sudokuresolver.Heap;
import com.posbeu.sudokuresolver.MainActivity;
import com.posbeu.sudokuresolver.TableChecker;


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
        TableChecker tc = new TableChecker(table);
        return tc.check();
    }


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
        int cSize = screenWidth / 10;
        mPaint.setTextSize(cSize);
        int fattX = screenWidth / 9;
        int fattY = screenWidth / 9;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                TableCell cell = table[i][j];

                int x = i * fattX + 2;
                int y = j * fattY + 2;
                fill(canvas, screenWidth, x, y, Color.WHITE);

                if( ((i/3+j/3))% 2==0)
                {
                    fill(canvas, screenWidth, x, y, Color.LTGRAY);
                }


                if (selectedCell != null && selectedCell.equals(cell)) {

                    fill(canvas, screenWidth, x, y, Color.DKGRAY);
                }
                if (cell.getValMax() == cell.getValMin()) {

                    fill(canvas, screenWidth, x, y, Color.GRAY);
                }
                if (cell.isError()) {

                    fill(canvas, screenWidth, x, y, Color.RED);
                }


                if (!cell.isEmpty()) {

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


}
