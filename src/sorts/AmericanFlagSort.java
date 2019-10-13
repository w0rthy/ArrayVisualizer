package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
Copyright 2017 Justin Wetherell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 *
 */

/*
 * An American flag sort is an efficient, in-place variant of radix sort that
 * distributes items into hundreds of buckets. Non-comparative sorting
 * algorithms such as radix sort and American flag sort are typically used to
 * sort large objects such as strings, for which comparison is not a unit-time
 * operation. 
 * <p>
 * Family: Bucket.<br>
 * Space: In-place.<br>
 * Stable: False.<br>
 * <p>
 * Average case = O(n*k/d)<br>
 * Worst case = O(n*k/d)<br>
 * Best case = O(n*k/d)<br>
 * <p>
 * NOTE: n is the number of digits and k is the average bucket size
 * <p>
 * @see <a href="https://en.wikipedia.org/wiki/American_flag_sort">American Flag Sort (Wikipedia)</a>
 * <br>
 * @author Justin Wetherell <phishman3579@gmail.com>
 */

final public class AmericanFlagSort extends Sort {	    
	    private int NUMBER_OF_BUCKETS = 128; // ex. 10 for base 10 numbers
	    
	    public AmericanFlagSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
	        super(delayOps, markOps, readOps, writeOps);
	        
	        this.setSortPromptID("American Flag");
	        this.setRunAllID("American Flag Sort, " + this.NUMBER_OF_BUCKETS + " Buckets");
	        this.setReportSortID("American Flag Sort");
	        this.setCategory("Distributive Sorts");
	        this.isComparisonBased(false);
	        this.isBucketSort(true);
	        this.isRadixSort(true);
	        this.isUnreasonablySlow(false);
	        this.setUnreasonableLimit(0);
	        this.isBogoSort(false);
	    }
	    
	    // Slightly different than Reads.analyzeMaxLog.
	    private int getMaxNumberOfDigits(int[] array, int length) {
	        int max = Integer.MIN_VALUE;
            int temp = 0;
            
            for (int i = 0; i < length; i++) {
                temp = (int) (Math.log(array[i]) / Math.log(this.NUMBER_OF_BUCKETS)) + 1;
                
                if (temp > max)
                    max = temp;
            }
            return max;
        }

        private int getDigit(int integer, int divisor) {
            return (integer / divisor) % this.NUMBER_OF_BUCKETS;
        }
        
        private void sort(int[] array, int start, int length, int divisor) {
            // First pass - find counts
            int[] count = new int[this.NUMBER_OF_BUCKETS];
            int[] offset = new int[this.NUMBER_OF_BUCKETS];
            int digit = 0;
            
            for (int i = start; i < length; i++) {
                Highlights.markArray(1, i);
                Delays.sleep(0.75);
                
                int d = array[i];
                digit = this.getDigit(d, divisor);
                
                Writes.write(count, digit, count[digit] + 1, 0, false, true);
            }
            
            Writes.write(offset, 0, start + 0, 0, false, true);
            
            for (int i = 1; i < this.NUMBER_OF_BUCKETS; i++) {
                Writes.write(offset, i, count[i - 1] + offset[i - 1], 0, false, true);
            }
            
            // Second pass - move into position
            for (int b = 0; b < this.NUMBER_OF_BUCKETS; b++) {
                while (count[b] > 0) {
                    int origin = offset[b];
                    int from = origin;
                    int num = array[from];
                    
                    Writes.write(array, from, -1, 0.5, true, false);
                    
                    do {
                        digit = this.getDigit(num, divisor);
                        int to = offset[digit];
                        
                        Writes.write(offset, digit, offset[digit] + 1, 0, false, true);
                        Writes.write(count, digit, count[digit] - 1, 0, false, true);
                        
                        int temp = array[to];
                        Writes.write(array, to, num, 0.75, true, false);
                        
                        num = temp;
                        from = to;
                    } while (from != origin);
                }
            }
            if (divisor > 1) {
                // Sort the buckets
                for (int i = 0; i < this.NUMBER_OF_BUCKETS; i++) {
                    int begin = (i > 0) ? offset[i - 1] : start;
                    int end = offset[i];
                    
                    if (end - begin > 1)
                        this.sort(array, begin, end, divisor / this.NUMBER_OF_BUCKETS);
                }
            }
        }

        @Override
        public void runSort(int[] array, int currentLen, int bucketCount) {
            this.NUMBER_OF_BUCKETS = bucketCount;
            
            int numberOfDigits = this.getMaxNumberOfDigits(array, currentLen); // Max number of digits
            int max = 1;
            
            for (int i = 0; i < numberOfDigits - 1; i++)
                max *= this.NUMBER_OF_BUCKETS;
            
            this.sort(array, 0, currentLen, max);
        }
	}