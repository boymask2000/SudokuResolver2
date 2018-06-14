package com.posbeu.sudokuresolver.core;

import java.util.Objects;

public class TableCell {
	private boolean empty = true;
	private int valMin = 1;
	private int valMax = 9;

	private int x;
	private int y;

	private int currentVal;

	public TableCell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TableCell tableCell = (TableCell) o;
		return getX() == tableCell.getX() &&
				getY() == tableCell.getY();
	}

	@Override
	public int hashCode() {

		return Objects.hash(getX(), getY());
	}

	public int getValMin() {
		return valMin;
	}

	public void setValMin(int valMin) {
		this.valMin = valMin;
	}

	public int getValMax() {
		return valMax;
	}

	public void setValMax(int valMax) {
		this.valMax = valMax;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCurrentVal() {
		if(empty)return 0;
		return currentVal;
	}

	public void setCurrentVal(int currentVal)  {

		this.currentVal = currentVal;

		empty = false;
	}

	public void setFixed(int k) {
		valMax = k;
		valMin = k;	this.currentVal = k;
		empty = false;

	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty() {
		if(valMax==valMin)return;
		empty = true;
		valMax = 9;
		valMin = 1;
		currentVal=0;

	}
	public void setClean() {
	
		empty = true;
		valMax = 9;
		valMin = 1;
		currentVal=0;

	}



}
