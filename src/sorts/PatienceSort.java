package sorts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 *
  Copyright (c) rosettacode.org.
  Permission is granted to copy, distribute and/or modify this document
  under the terms of the GNU Free Documentation License, Version 1.2
  or any later version published by the Free Software Foundation;
  with no Invariant Sections, no Front-Cover Texts, and no Back-Cover
  Texts.  A copy of the license is included in the section entitled "GNU
  Free Documentation License".
 *
 */

final public class PatienceSort extends Sort {
    public PatienceSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Patience");
        this.setRunAllID("Patience Sort");
        this.setReportSortID("Patience Sort");
        this.setCategory("Insertion Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    final private class Pile extends Stack<Integer> implements Comparable<Pile> {
        private static final long serialVersionUID = 1L;

        public int compare(Pile y) {
            return Reads.compare(peek(), y.peek());
        }

        @Override
        public int compareTo(Pile y) {
            return peek().compareTo(y.peek());
        }
    }
    
    private void binarySearch(ArrayList<Pile> list, Pile find) {
		int at = list.size() / 2;
		int change = list.size() / 4;
		
		while(list.get(at).compare(find) != 0 && change > 0){
			Highlights.markArray(1, at);
			Delays.sleep(0.5);
			
			if(list.get(at).compare(find) < 0)
				at += change;
			else
				at -= change;
			
			change /= 2;
		}
		
		Highlights.markArray(1, at);
		Delays.sleep(0.5);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        ArrayList<Pile> piles = new ArrayList<>();
        
        // sort into piles
        for (int x = 0; x < length; x++) {
            Pile newPile = new Pile();
            
            Highlights.markArray(2, x);
            Writes.mockWrite(length, newPile.size(), array[x], 1);
            
            newPile.push(array[x]);
            
            int i = Collections.binarySearch(piles, newPile);
            if(!piles.isEmpty()) {
                this.binarySearch(piles, newPile);
            }
            if (i < 0) i = ~i;
            if (i != piles.size()) {
                Writes.mockWrite(length, piles.get(i).size(), array[x], 0);
                piles.get(i).push(array[x]);
            }
            else {
                Writes.mockWrite(length, piles.size(), newPile.get(0), 0);   
                piles.add(newPile);
            }
        }

        Highlights.clearMark(2);

        // priority queue allows us to retrieve least pile efficiently
        PriorityQueue<Pile> heap = new PriorityQueue<>(piles);
        
        for (int c = 0; c < length; c++) {
            Writes.mockWrite(length, heap.size(), 0, 0);
            Pile smallPile = heap.poll();
            
            Writes.mockWrite(length, smallPile.size(), 0, 0);
            Writes.write(array, c, smallPile.pop(), 1, true, false);
            
            if (!smallPile.isEmpty()) {
                Writes.mockWrite(length, heap.size(), smallPile.get(0), 0);
                heap.offer(smallPile);
            }
        }
    }
}