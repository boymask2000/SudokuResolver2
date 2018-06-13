package com.posbeu.sudokuresolver.core;

import com.posbeu.sudokuresolver.MainActivity;
import com.posbeu.sudokuresolver.Pair;

import java.util.ArrayList;
import java.util.List;


public class Sudoku {

	private Table table = new Table();
	private List<Pair> lista;
	private MainActivity main;

	public Sudoku(MainActivity main) {
		this.main = main;
		table.setMain(main);

		lista = buildPairs();
//
//		table.setFixed(0, 0, 5);


	}

	public void go() { solved=false;
		solve(0);
	}

	private List<Pair> buildPairs() {
		List<Pair> lista = new ArrayList<Pair>();
		for (int i = 0; i < Table.getNumrows(); i++)
			for (int j = 0; j < Table.getNumcols(); j++)
				lista.add(new Pair(i, j));

		return lista;
	}

	int ciclo = 0;
boolean solved=false;
	private void solve(int start) {
		ciclo++;
		// try {
		// Thread.sleep(10);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		int v = start;

		// System.out.println("v:" +v);
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
					solved=true;
					System.out.println("FOUND!!!!!!!!!!!!!!!!!!");

				} else
					solve(start + 1);
			}
			if(solved)return;

			cell.setEmpty();
		}

	}



/*	public void setClick(int x, int y) {

		TableCell c = table.getCellByCoord(x, y);

		table.setSelectedCell(c);
		main.update();

	}*/

	public void setSelectedValue(int i) {
		table.setSelectedValue(i);
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

