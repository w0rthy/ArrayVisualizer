package templates;

import sorts.MaxHeapSort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
pdqsort.h - Pattern-defeating quicksort.
Copyright (c) 2015 Orson Peters
This software is provided 'as-is', without any express or implied warranty. In no event will the
authors be held liable for any damages arising from the use of this software.
Permission is granted to anyone to use this software for any purpose, including commercial
applications, and to alter it and redistribute it freely, subject to the following restrictions:
1. The origin of this software must not be misrepresented; you must not claim that you wrote the
   original software. If you use this software in a product, an acknowledgment in the product
   documentation would be appreciated but is not required.
2. Altered source versions must be plainly marked as such, and must not be misrepresented as
   being the original software.
3. This notice may not be removed or altered from any source distribution.
 *
 */

final class PDQPair {
    private int pivotPosition;
    private boolean alreadyPartitioned;

    public PDQPair(int pivotPos, boolean presorted) {
        this.pivotPosition = pivotPos;
        this.alreadyPartitioned = presorted;
    }

    public int getPivotPosition() {
        return this.pivotPosition;
    }

    public boolean getPresortBool() {
        return this.alreadyPartitioned;
    }
}

public abstract class PDQSorting extends Sort {
    private MaxHeapSort heapSorter;
    
    final private int insertSortThreshold = 24;
    final private int nintherThreshold = 128;
    final private int partialInsertSortLimit = 8;
    final private int blockSize = 64;
    final private int cachelineSize = 64;

    protected PDQSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    protected void newHeapSorter(MaxHeapSort heapSort) {
        heapSorter = heapSort;
    }
    
    // Returns floor(log2(n)), assumes n > 0.
    public static int pdqLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0) ++log;
        return log;
    }

    // We do not record laps here in order to better estimate Branchless PDQ's running time
    private int pdqLessThan(int a, int b) {
        Reads.addComparison();    
        return 1 & (Boolean.hashCode(a < b) >> 1);
    }

    // Sorts [begin, end) using insertion sort with the given comparison function.
    private void pdqInsertSort(int[] array, int begin, int end, boolean Branchless) {
        if (begin == end) return;

        double sleep;
        if(Branchless) sleep = 0.25;
        else sleep = 0.05;

        for (int cur = begin + 1; cur != end; ++cur) {
            int sift = cur;
            int siftMinusOne = cur - 1;

            // Compare first so we can avoid 2 moves for an element already positioned correctly.
            if (Reads.compare(array[sift], array[siftMinusOne]) < 0) {
                int tmp = array[sift];
                do {
                    Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                } while (sift != begin && Reads.compare(tmp, array[--siftMinusOne]) < 0);

                Writes.write(array, sift, tmp, sleep, true, false);
            }
        }
    }

    // Sorts [begin, end) using insertion sort with the given comparison function. Assumes
    // array[begin - 1] is an element smaller than or equal to any element in [begin, end).
    private void pdqUnguardInsertSort(int[] array, int begin, int end, boolean Branchless) {
        if (begin == end) return;

        double sleep;
        if(Branchless) sleep = 0.25;
        else sleep = 0.05;

        for (int cur = begin + 1; cur != end; ++cur) {
            int sift = cur;
            int siftMinusOne = cur - 1;

            // Compare first so we can avoid 2 moves for an element already positioned correctly.
            if (Reads.compare(array[sift], array[siftMinusOne]) < 0) {
                int tmp = array[sift];

                do {
                    Writes.write(array, sift--, array[siftMinusOne], sleep, true, false); 
                } while (Reads.compare(tmp, array[--siftMinusOne]) < 0);

                Writes.write(array, sift, tmp, sleep, true, false);
            }
        }
    }

    // Attempts to use insertion sort on [begin, end). Will return false if more than
    // partialInsertSortLimit elements were moved, and abort sorting. Otherwise it will
    // successfully sort and return true.
    private boolean pdqPartialInsertSort(int[] array, int begin, int end, boolean Branchless) {
        if (begin == end) return true;

        double sleep;
        if(Branchless) sleep = 0.25;
        else sleep = 0.05;

        int limit = 0;
        for (int cur = begin + 1; cur != end; ++cur) {
            if (limit > partialInsertSortLimit) return false;

            int sift = cur;
            int siftMinusOne = cur - 1;

            // Compare first so we can avoid 2 moves for an element already positioned correctly.
            if (Reads.compare(array[sift], array[siftMinusOne]) < 0) {
                int tmp = array[sift];

                do { 
                    Writes.write(array, sift--, array[siftMinusOne], sleep, true, false);
                } while (sift != begin && Reads.compare(tmp, array[--siftMinusOne]) < 0);

                Writes.write(array, sift, tmp, sleep, true, false);
                limit += cur - sift;
            }
        }
        return true;
    }

    private void pdqSortTwo(int[] array, int a, int b) {
        if (Reads.compare(array[b], array[a]) < 0) {
            Writes.swap(array, a, b, 1, true, false);
        }
        Highlights.clearMark(2);
    }

    // Sorts the elements array[a], array[b] and array[c] using comparison function compare.
    private void pdqSortThree(int[] array, int a, int b, int c) {
        this.pdqSortTwo(array, a, b);
        this.pdqSortTwo(array, b, c);
        this.pdqSortTwo(array, a, b);
    }

    // With Branchless PDQSort, in order to better estimate the gains in speed from branchless partioning, we treat the writes to the offset arrays
    // and specialized less than comparison as negligible, and only record time from elements being swapped into position. By no means is this
    // exact, yet it is a much closer estimate than what was happening before with recording time for every block being written.
    private void pdqSwapOffsets(int[] array, int first, int last, int[] leftOffsets, int leftOffsetsPos, 
                                int[] rightOffsets, int rightOffsetsPos, int num, boolean useSwaps) {
        if (useSwaps) {
            // This case is needed for the descending distribution, where we need
            // to have proper swapping for pdqsort to remain O(n).
            for (int i = 0; i < num; ++i) {
                Writes.swap(array, first + leftOffsets[leftOffsetsPos + i], last - rightOffsets[rightOffsetsPos + i], 1, true, false);
            }
            Highlights.clearMark(2);
        } else if (num > 0) {
            int left = first + leftOffsets[leftOffsetsPos];
            int right = last - rightOffsets[rightOffsetsPos];
            int tmp = array[left];
            Writes.write(array, left, array[right], 1, true, false);
            for (int i = 1; i < num; ++i) {
                left = first + leftOffsets[leftOffsetsPos + i];
                Writes.write(array, right, array[left], 1, true, false);
                right = last - rightOffsets[rightOffsetsPos + i];
                Writes.write(array, left, array[right], 1, true, false);
            }
            Writes.write(array, right, tmp, 1, true, false);
        }
    }

    // Partitions [begin, end) around pivot array[begin] using comparison function compare. Elements equal
    // to the pivot are put in the right-hand partition. Returns the position of the pivot after
    // partitioning and whether the passed sequence already was correctly partitioned. Assumes the
    // pivot is a median of at least 3 elements and that [begin, end) is at least
    // insertSortThreshold long. Uses branchless partitioning.

    // We do not record laps throughout the vast majority of this method in order to better estimate Branchless PDQ's running time
    private PDQPair pdqPartRightBranchless(int[] array, int begin, int end) {
        // Move pivot into local for speed.
        int pivot = array[begin];
        int first = begin;
        int last = end;

        // Find the first element greater than or equal than the pivot (the median of 3 guarantees
        // this exists).
        while (this.pdqLessThan(array[++first], pivot) == 1);

        // Find the first element strictly smaller than the pivot. We have to guard this search if
        // there was no element before *first.
        if (first - 1 == begin) while (first < last && this.pdqLessThan(array[--last], pivot) == 0);
        else                    while (                this.pdqLessThan(array[--last], pivot) == 0);

        // If the first pair of elements that should be swapped to partition are the same element,
        // the passed in sequence already was correctly partitioned.
        boolean alreadyParted = first >= last;
        if (!alreadyParted) {
            Writes.swap(array, first, last, 1, true, false);
            ++first;
            Highlights.clearMark(2);
        }

        // The following branchless partitioning is derived from "BlockQuicksort: How Branch
        // Mispredictions don’t affect Quicksort" by Stefan Edelkamp and Armin Weiss.
        int[] leftOffsets = new int[blockSize + cachelineSize];
        int[] rightOffsets = new int [blockSize + cachelineSize];
        int leftNum, rightNum, leftStart, rightStart;
        leftNum = rightNum = leftStart = rightStart = 0;

        while (last - first > 2 * blockSize) {
            // Fill up offset blocks with elements that are on the wrong side.
            if (leftNum == 0) {
                leftStart = 0;
                int it = first;
                Highlights.clearMark(1);
                for (int i = 0; i < blockSize;) {
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                }
                Highlights.clearMark(2);
            }
            if (rightNum == 0) {
                rightStart = 0;
                int it = last;
                Highlights.clearMark(1);
                for (int i = 0; i < blockSize;) {
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                    rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                    Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
                }
                Highlights.clearMark(2);
            }

            // Swap elements and update block sizes and first/last boundaries.
            int num = Math.min(leftNum, rightNum);
            this.pdqSwapOffsets(array, first, last, leftOffsets, leftStart, rightOffsets, rightStart, num, leftNum == rightNum);
            leftNum -= num; rightNum -= num;
            leftStart += num; rightStart += num;
            if (leftNum == 0) first += blockSize;
            if (rightNum == 0) last -= blockSize;
        }

        int leftSize = 0, rightSize = 0;
        int unknownLeft = (last - first) - ((rightNum != 0 || leftNum != 0) ? blockSize : 0);
        if (rightNum != 0) {
            // Handle leftover block by assigning the unknown elements to the other block.
            leftSize = unknownLeft;
            rightSize = blockSize;
        } else if (leftNum != 0) {
            leftSize = blockSize;
            rightSize = unknownLeft;
        } else {
            // No leftover block, split the unknown elements in two blocks.
            leftSize = unknownLeft / 2;
            rightSize = unknownLeft - leftSize;
        }

        // Fill offset buffers if needed.
        if (unknownLeft != 0 && leftNum == 0) {
            leftStart = 0;
            int it = first;
            Highlights.clearMark(1);
            for (int i = 0; i < leftSize;) {
                leftOffsets[leftNum] = i++; leftNum += Math.abs(this.pdqLessThan(array[it++], pivot) - 1);
                Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
            }
            Highlights.clearMark(2);
        }
        if (unknownLeft != 0 && rightNum == 0) {
            rightStart = 0;
            int it = last;
            Highlights.clearMark(1);
            for (int i = 0; i < rightSize;) {
                rightOffsets[rightNum] = ++i; rightNum += this.pdqLessThan(array[--it], pivot);
                Writes.changeTempWrites(1); Highlights.markArray(2, it); Delays.sleep(0.5);
            }
            Highlights.clearMark(2);
        }

        int num = Math.min(leftNum, rightNum);
        this.pdqSwapOffsets(array, first, last, leftOffsets, leftStart, rightOffsets, rightStart, num, leftNum == rightNum);
        leftNum -= num; rightNum -= num;
        leftStart += num; rightStart += num;
        if (leftNum == 0) first += leftSize;
        if (rightNum == 0) last -= rightSize;

        int leftOffsetsPos = 0;
        int rightOffsetsPos = 0;

        // We have now fully identified [first, last)'s proper position. Swap the last elements.
        if (leftNum != 0) {
            leftOffsetsPos += leftStart;
            while (leftNum-- != 0) Writes.swap(array, first + leftOffsets[leftOffsetsPos + leftNum], --last, 1, true, false);
            Highlights.clearMark(2);
            first = last;
        }
        if (rightNum != 0) {
            rightOffsetsPos += rightStart;
            while (rightNum-- != 0) Writes.swap(array, last - rightOffsets[rightOffsetsPos + rightNum], first++, 1, true, false);
            Highlights.clearMark(2);
            last = first;
        }

        // Put the pivot in the right place.
        int pivotPos = first - 1;
        Writes.write(array, begin, array[pivotPos], 1, true, false);
        Writes.write(array, pivotPos, pivot, 1, true, false);

        return new PDQPair(pivotPos, alreadyParted);
    }

    // Partitions [begin, end) around pivot array[begin] using comparison function compare. Elements equal
    // to the pivot are put in the right-hand partition. Returns the position of the pivot after
    // partitioning and whether the passed sequence already was correctly partitioned. Assumes the
    // pivot is a median of at least 3 elements and that [begin, end) is at least
    // insertSortThreshold long.

    private PDQPair pdqPartRight(int[] array, int begin, int end) {
        // Move pivot into local for speed.
        int pivot = array[begin];
        int first = begin;
        int last = end;

        // Find the first element greater than or equal than the pivot (the median of 3 guarantees
        // this exists).
        while (Reads.compare(array[++first], pivot) < 0);

        // Find the first element strictly smaller than the pivot. We have to guard this search if
        // there was no element before *first.
        if (first - 1 == begin) while (first < last && !(Reads.compare(array[--last], pivot) < 0));
        else                    while (                !(Reads.compare(array[--last], pivot) < 0));

        // If the first pair of elements that should be swapped to partition are the same element,
        // the passed in sequence already was correctly partitioned.
        boolean alreadyParted = first >= last;

        // Keep swapping pairs of elements that are on the wrong side of the pivot. Previously
        // swapped pairs guard the searches, which is why the first iteration is special-cased
        // above.
        while (first < last) {
            Writes.swap(array, first, last, 1, true, false);
            while (Reads.compare(array[++first], pivot) < 0);
            while (!(Reads.compare(array[--last], pivot) < 0));
        }
        Highlights.clearMark(2);

        // Put the pivot in the right place.
        int pivotPos = first - 1;
        Writes.write(array, begin, array[pivotPos], 1, true, false);
        Writes.write(array, pivotPos, pivot, 1, true, false);

        return new PDQPair(pivotPos, alreadyParted);
    }

    // Similar function to the one above, except elements equal to the pivot are put to the left of
    // the pivot and it doesn't check or return if the passed sequence already was partitioned.
    // Since this is rarely used (the many equal case), and in that case pdqsort already has O(n)
    // performance, no block quicksort is applied here for simplicity.

    private int pdqPartLeft(int[] array, int begin, int end) {
        // Move pivot into local for speed.
        int pivot = array[begin];
        int first = begin;
        int last = end;

        while (Reads.compare(pivot, array[--last]) < 0);

        if (last + 1 == end) while (first < last && !(Reads.compare(pivot, array[++first]) < 0));
        else                 while (                !(Reads.compare(pivot, array[++first]) < 0));

        while (first < last) {
            Writes.swap(array, first, last, 1, true, false);
            while (Reads.compare(pivot, array[--last]) < 0);
            while (!(Reads.compare(pivot, array[++first]) < 0));
        }
        Highlights.clearMark(2);

        int pivotPos = last;
        Writes.write(array, begin, array[pivotPos], 1, true, false);
        Writes.write(array, pivotPos, pivot, 1, true, false);

        return pivotPos;
    }

    protected void pdqLoop(int[] array, int begin, int end, boolean Branchless, int badAllowed) {
        boolean leftmost = true;

        // Use a while loop for tail recursion elimination.
        while (true) {
            int size = end - begin;

            // Insertion sort is faster for small arrays.
            if (size < insertSortThreshold) {
                if (leftmost) this.pdqInsertSort(array, begin, end, Branchless);
                else this.pdqUnguardInsertSort(array, begin, end, Branchless);
                return;
            }

            // Choose pivot as median of 3 or pseudomedian of 9.
            int halfSize = size / 2;
            if (size > nintherThreshold) {
                this.pdqSortThree(array, begin, begin + halfSize, end - 1);
                this.pdqSortThree(array, begin + 1, begin + (halfSize - 1), end - 2);
                this.pdqSortThree(array, begin + 2, begin + (halfSize + 1), end - 3);
                this.pdqSortThree(array, begin + (halfSize - 1), begin + halfSize, begin + (halfSize + 1));
                Writes.swap(array, begin, begin + halfSize, 1, true, false);
                Highlights.clearMark(2);
            } else this.pdqSortThree(array, begin + halfSize, begin, end - 1);

            // If array[begin - 1] is the end of the right partition of a previous partition operation
            // there is no element in [begin, end) that is smaller than array[begin - 1]. Then if our
            // pivot compares equal to array[begin - 1] we change strategy, putting equal elements in
            // the left partition, greater elements in the right partition. We do not have to
            // recurse on the left partition, since it's sorted (all equal).
            if (!leftmost && !(Reads.compare(array[begin - 1], array[begin]) < 0)) {
                begin = this.pdqPartLeft(array, begin, end) + 1;
                continue;
            }

            // Partition and get results.
            PDQPair partResult =
                    Branchless ? this.pdqPartRightBranchless(array, begin, end)
                               : this.pdqPartRight(array, begin, end);
                    
                    int pivotPos = partResult.getPivotPosition();
                    boolean alreadyParted = partResult.getPresortBool();

                    // Check for a highly unbalanced partition.
                    int leftSize = pivotPos - begin;
                    int rightSize = end - (pivotPos + 1);
                    boolean highUnbalance = leftSize < size / 8 || rightSize < size / 8;

                    // If we got a highly unbalanced partition, we shuffle elements to break many patterns.
                    if (highUnbalance) {
                        // If we had too many bad partitions, switch to heapsort to guarantee O(n log n).
                        if (--badAllowed == 0) {
                            heapSorter.customHeapSort(array, begin, end, 1);
                            return;
                        }

                        if (leftSize >= insertSortThreshold) {
                            Writes.swap(array, begin,           begin + leftSize / 4, 1, true, false);
                            Writes.swap(array, pivotPos-1,   pivotPos - leftSize / 4, 1, true, false);

                            if (leftSize > nintherThreshold) {
                                Writes.swap(array, begin+1,           begin + (leftSize / 4 + 1), 1, true, false);
                                Writes.swap(array, begin+2,           begin + (leftSize / 4 + 2), 1, true, false);
                                Writes.swap(array, pivotPos-2,     pivotPos - (leftSize / 4 + 1), 1, true, false);
                                Writes.swap(array, pivotPos-3,     pivotPos - (leftSize / 4 + 2), 1, true, false);
                            }
                        }

                        if (rightSize >= insertSortThreshold) {
                            Writes.swap(array, pivotPos+1,   pivotPos + (1 + rightSize / 4), 1, true, false);
                            Writes.swap(array, end-1,                   end - rightSize / 4, 1, true, false);

                            if (rightSize > nintherThreshold) {
                                Writes.swap(array, pivotPos+2,   pivotPos + (2 + rightSize / 4), 1, true, false);
                                Writes.swap(array, pivotPos+3,   pivotPos + (3 + rightSize / 4), 1, true, false);
                                Writes.swap(array, end-2,             end - (1 + rightSize / 4), 1, true, false);
                                Writes.swap(array, end-3,             end - (2 + rightSize / 4), 1, true, false);
                            }
                        }
                        Highlights.clearMark(2);
                    } else {
                        // If we were decently balanced and we tried to sort an already partitioned
                        // sequence, try to use insertion sort.
                        if (alreadyParted && pdqPartialInsertSort(array, begin, pivotPos, Branchless)
                                          && pdqPartialInsertSort(array, pivotPos + 1, end, Branchless)) 
                            return;
                    }

                    // Sort the left partition first using recursion and do tail recursion elimination for
                    // the right-hand partition.
                    this.pdqLoop(array, begin, pivotPos, Branchless, badAllowed);
                    begin = pivotPos + 1;
                    leftmost = false;
        }
    }
}