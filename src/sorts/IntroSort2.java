package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class IntroSort2 extends Sort {
    public IntroSort2(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Intro2");
        this.setRunAllID("Intro Sort 2");
        this.setReportSortID("Intro Sort 2");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }

    private int compare(int[] array, int start, int end, double sleep, int dir) {
int flag = 0;
                if((dir != 0) ? (Reads.compare(array[start], array[end]) == 1) : (Reads.compare(array[start], array[end]) == -1)) {
                    Writes.swap(array, start, end, sleep, true, false);
flag = 1;
                }
                
                Highlights.markArray(1, start);
                Highlights.markArray(2, end);
                
                Delays.sleep(sleep);
return flag;
    }

    private int bubblepass(int[] array, int start, int end, double sleep, int dir) {
    int lastswap = start; for(int i=start+1; i<end; i++) if((compare(array, 
i-1, i, sleep, dir)!=0) ? true : false) lastswap = i;
return lastswap;
    }

    private void swap2(int[] array, double sleep, int X, int Y){
        Writes.swap(array, X, Y, sleep, true, false);;
    }

    private void bubblesort(int[] array, int start, int end, double sleep, int dir) {
	int lastswap = end;
	while(lastswap > start+1){
            lastswap = bubblepass(array, start, lastswap, sleep, dir);
        }
    }

    private void introsort2(int[] a, int start, int end, double sleep) {
        int length = end-start;
	int[] stack = new int[((int)Math.floor(Math.log(length+0.0)/Math.log(2.0)*2))];
	int[] stack2 = new int[((int)Math.floor(Math.log(length+0.0)/Math.log(2.0)*2))];
	int[] stack3 = new int[((int)Math.floor(Math.log(length+0.0)/Math.log(2.0)*2))];
	stack[0] = 0;
	stack2[1] = length;
	stack3[1] = 0;
	int stackend = 1;
	while(stackend != 0){
		if(stack2[stackend]-stack[stackend-1] <= 1){
			stackend -= 1;
		}
		else{
			if(stack3[stackend] >= ((int)Math.floor(Math.log(length+0.0)/Math.log(2.0)*2))){
				int min = stack[stackend-1];
				int max = stack2[stackend];
				stackend--;
				for(int i=(min+((max-min)/2)); i>min;){
					i--;
					int v = i;
					while(v < (min+((max-min)/2))){
						if((v-min)*2+2+min == max){
							swap2(a, sleep, v, (v-min)*2+1+min);
							v = (v-min)*2+1+min;
						}
						else{
							if(Reads.compare(a[(v-min)*2+1+min], a[(v-min)*2+2+min]) == 1){
								swap2(a, sleep, v, (v-min)*2+1+min);
								v = (v-min)*2+1+min;
							}
							else{
								swap2(a, sleep, v, (v-min)*2+2+min);
								v = (v-min)*2+2+min;
							}
						}
					}
					while(v > i){
						if(Reads.compare(a[((v-min)-1)/2+min], a[v]) == -1){
							swap2(a, sleep, v, ((v-min)-1)/2+min);
							v = ((v-min)-1)/2+min;
						}
						else{
							break;
						}
					}
				}
				while(max-1 > min){
					max--;
					swap2(a, sleep, min, max);
					int v = min;
					while(v < (min+((max-min)/2))){
						if((v-min)*2+2+min == max){
							swap2(a, sleep, v, (v-min)*2+1+min);
							v = (v-min)*2+1+min;
						}
						else{
							if(Reads.compare(a[(v-min)*2+1+min], a[(v-min)*2+2+min]) == 1){
								swap2(a, sleep, v, (v-min)*2+1+min);
								v = (v-min)*2+1+min;
							}
							else{
								swap2(a, sleep, v, (v-min)*2+2+min);
								v = (v-min)*2+2+min;
							}
						}
					}
					while(v > min){
						if(Reads.compare(a[((v-min)-1)/2+min], a[v]) == -1){
							swap2(a, sleep, v, ((v-min)-1)/2+min);
							v = ((v-min)-1)/2+min;
						}
						else{
							break;
						}
					}
				}
			}
			else{
				int temp1 = stack[stackend-1];
				int temp3 = stack2[stackend]-1;
				int temp2 = temp1+((temp3-temp1)/2);
				if(Reads.compare(a[temp1], a[temp2]) < 1){
					if(Reads.compare(a[temp2], a[temp3]) < 1){
						swap2(a, sleep, temp1, temp2);
					}
					else{
						if(Reads.compare(a[temp1], a[temp3]) < 1){
							swap2(a, sleep, temp1, temp3);
						}
						else{
						}
					}
				}
				else{
					if(Reads.compare(a[temp2], a[temp3]) < 1){
						if(Reads.compare(a[temp1], a[temp3]) < 1){
						}
						else{
							swap2(a, sleep, temp1, temp3);
						}
					}
					else{
						swap2(a, sleep, temp1, temp2);
					}
				}
				int x = stack[stackend-1]+1;
				int y = stack2[stackend];
				int state = 0;
				int state2 = 0;
				while(true){
					if(x == y){
						break;
					}
					if(state == 0){
						if(Reads.compare(a[x], a[stack[stackend-1]]) == 1){
							state = 1;
							if(state2 != 0){
								swap2(a, sleep, x, y-1);
								state2 = 0;
								x++;
							}
							else{
								state2 = 1;
							}
						}
						else{
							x++;
							if(state2 == 0){
								state = 1;
							}
						}
					}
					else{
						if(Reads.compare(a[y-1], a[stack[stackend-1]]) == -1){
							state = 0;
							if(state2 != 0){
								swap2(a, sleep, x, y-1);
								state2 = 0;
								y--;
							}
							else{
								state2 = 1;
							}
						}
						else{
							y--;
							if(state2 == 0){
								state = 0;
							}
						}
					}
				}
				swap2(a, sleep, x-1, stack[stackend-1]);
				stackend++;
				stack2[stackend] = stack2[stackend-1];
				stack[stackend-1] = x;
				stack2[stackend-1] = x-1;
				stack3[stackend-1]++;
				stack3[stackend]=stack3[stackend-1];
			}
		}
	}
    }
    
    public void customSort(int[] array, int start, int end) {
        this.introsort2(array, start, end, 1);
    }
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.introsort2(array, 0, length, 1);
    }
}