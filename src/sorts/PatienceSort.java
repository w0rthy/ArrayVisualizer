package sorts;

import static array.visualizer.ArrayVisualizer.calcReal;
import static array.visualizer.ArrayVisualizer.comps;
import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.ArrayVisualizer.realTimer;
import static array.visualizer.ArrayVisualizer.sleep;
import static array.visualizer.Writes.mockWrite;
import static array.visualizer.Writes.write;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

/*
  Copyright (c) rosettacode.org.
  Permission is granted to copy, distribute and/or modify this document
  under the terms of the GNU Free Documentation License, Version 1.2
  or any later version published by the Free Software Foundation;
  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
  Texts.  A copy of the license is included in the section entitled "GNU
  Free Documentation License".
 */
public class PatienceSort {
	private static void binarySearch(ArrayList<Pile> list, Pile find, int[] array) throws Exception {
		int at = list.size()/2;
		int change = list.size()/4;
		while(list.get(at).compareTo(find) != 0 && change > 0){
			marked.set(1, at);
			sleep(0.5);
			if(list.get(at).compareTo(find) < 0)
				at += change;
			else
				at -= change;
			change /= 2;
		}
		marked.set(1, at);
		sleep(0.5);
	}

	public static void patienceSort (int[] n) throws Exception {
		ArrayList<Pile> piles = new ArrayList<Pile>();
		// sort into piles
		for (int x = 0; x < n.length; x++) {
			Pile newPile = new Pile();
			newPile.push(n[x]);
			marked.set(2, x);
			mockWrite(n, 1);
			int i = Collections.binarySearch(piles, newPile);
			if(!piles.isEmpty()) {
				binarySearch(piles, newPile, n);
			}
			if (i < 0) i = ~i;
			if (i != piles.size()) {
				piles.get(i).push(n[x]);
				mockWrite(n, 0);
			}
			else {
				piles.add(newPile);
				mockWrite(n, 0);
			}
		}
		
		marked.set(2, -5);
		
		// priority queue allows us to retrieve least pile efficiently
		PriorityQueue<Pile> heap = new PriorityQueue<Pile>(piles);
		for (int c = 0; c < n.length; c++) {
			Pile smallPile = heap.poll();
			mockWrite(n, 0);
			write(n, c, smallPile.pop(), 1, false, false);
			mockWrite(n, 0);
			marked.set(1, c);
			if (!smallPile.isEmpty()) {
				heap.offer(smallPile);
				mockWrite(n, 0);
			}
		}
		assert(heap.isEmpty());
	}

	private static class Pile extends Stack<Integer> implements Comparable<Pile> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public int compareTo(Pile y) {
			comps++;
			long time = System.nanoTime();
			int cmp = peek().compareTo(y.peek());
			if(calcReal) realTimer += (System.nanoTime() - time) / 1e+6;
			return cmp; 
		}
	}
}