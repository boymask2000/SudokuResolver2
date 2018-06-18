package com.posbeu.sudokuresolver.core;

import com.posbeu.sudokuresolver.MainActivity;
import com.posbeu.sudokuresolver.Pair;

import java.util.ArrayList;
import java.util.List;


public class Sudoku {

    private Table table ;
    private List<Pair> lista;
    private MainActivity main;

    public Sudoku(MainActivity main) {
        this.main = main;
        table=main.getTable();
        table.setMain(main);

        lista = buildPairs();
    }

    public void go() {
        solved = false;

        solve(0);
        main.update();
    }

    private List<Pair> buildPairs() {
        List<Pair> lista = new ArrayList<Pair>();
        for (int i = 0; i < Table.getNumrows(); i++)
            for (int j = 0; j < Table.getNumcols(); j++)
                lista.add(new Pair(i, j));

        return lista;
    }

    int ciclo = 0;
    boolean solved = false;

    private void solve(int start) {
        ciclo++;

        int v = start;

        int i = lista.get(v).getX();
        int j = lista.get(v).getY();

        TableCell cell = table.getCell(i, j);
        int vMin = cell.getValMin();
        int vMax = cell.getValMax();


        for (int k = vMin; k <= vMax; k++) {
            cell.setCurrentVal(k);

            if (ciclo == 1000) {
                ciclo = 0;

                main.update();
            }

            if (table.check()) {

                if (v == 80) {
                    main.update();
                    solved = true;

                } else
                    solve(start + 1);
            }
            if (solved) return;

            cell.setEmpty();
        }

    }

    public void setSetFree() {
        table.setFree();

    }

    public void setClean() {
        table.setClean();

    }

    public void cleanAll() {
        table.cleanAll();

    }

    public void update() {
    }
}

