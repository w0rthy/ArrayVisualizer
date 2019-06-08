package sorts;

import static array.visualizer.ArrayVisualizer.marked;
import static array.visualizer.Writes.write;

/*
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS
 * LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE.
 * TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED HERE IN
 * CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 */

// Code refactored from the Python implementation found here: https://en.wikipedia.org/wiki/Pigeonhole_sort

public class PigeonholeSort {
	public static void pigeonSort(int[] a, int length) {
		int mi = 0;
		int size = length;
		int[] holes = new int[size];
		int j = 0;
		for(int x = 0; x < length; x++) {
			write(holes, x - mi, holes[x - mi] + 1, 1, false, true);
			marked.set(1, j);
			j++;
		}
		int i = 0;
		for(int count = 0; count < size; count++) {
			while(holes[count] > 0) {
				write(holes, count, holes[count] - 1, 0, false, true);
				write(a, i, count + mi, 1, false, false);
				marked.set(1, i);
				i++;
			}
		}
	}
}