package javagrailsort;

import java.util.Comparator;

import static javagrailsort.Tester.comps;

class SortType {
	public int key;
	public int value;

	public SortType() {
		this.key = 0;
		this.value = 0;
	}

	public SortType(int key, int value) {
		this.key = key;
		this.value = value;
	}
}

class SortComparator implements Comparator<SortType> {
	public int compare(SortType a, SortType b) {
		comps++;
		if (a.key < b.key) return -1;
		else if (a.key > b.key) return 1;
		else return 0;
	}
}