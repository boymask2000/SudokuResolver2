package com.posbeu.sudokuresolver;

import com.posbeu.sudokuresolver.core.TableCell;

public class TableChecker {
    private static final int NUMROWS = 9;
    private static final int NUMCOLS = 9;
    private final TableCell[][] table;

    public TableChecker(TableCell[][] table) {
        this.table = table;

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
}
