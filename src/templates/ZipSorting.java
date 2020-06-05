package templates;

import sorts.InsertionSort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/* translated from Richard Cookman's original MIT licensed C++ implementation
 * https://github.com/ceorron/stable-inplace-sorting-algorithms#zip_sort
 */

public abstract class ZipSorting extends Sort {
    private InsertionSort insertSorter;

    protected ZipSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }

    protected void zipRotate(int[] arr, int first, int middle, int last, double sleep, boolean mark, boolean temp) {
        if(middle == last)
            return;
        int next = middle;
        while(first != next) {
            Writes.swap(arr, first++, next++, sleep, mark, temp);
            if(next == last) next = middle;
            else if(first == middle) middle = next;
        }
    }

    protected void zipMerge(int[] arr, int left, int right, int end, double sleep, boolean mark, boolean temp) {
        //the swap buffer, 2048 bytes / sizeof(int), as per documentation
        short buffer_count = 512;
        int[] swapbufr = new int[buffer_count];

        //all of the middle sections
        int mdlstart = right;
        int mdltop = right;

        while((left != right) & (right != end)) {
            if(mdltop != right) {
                //if we have a middle section test the middle against the right - long run optimisation
                if(Reads.compare(arr[right], arr[mdltop]) == -1) {
                    if(mdltop != mdlstart) {
                        //move in a buffers worth at a time
                        short count = 0;
                        do {
                            //construct(swapbufr[count++], std::move(*left));
                            if(mark) Highlights.markArray(1, left);
                            Writes.write(swapbufr, count++, arr[left], sleep, false, true);
                            //construct(*left, std::move(*right));
                            if(mark) Highlights.markArray(2, right);
                            Writes.write(arr, left, arr[right], sleep, mark, temp);
                            ++right;
                            ++left;
                        } while(((count < buffer_count) & (left != mdlstart) & (right != end)) && Reads.compare(arr[right], arr[mdltop]) == -1);

                        //move the new smallest into the correct place in the middle section
                        //move the middle section right
                        int mdlend = right;
                        int mdl = mdlend - count;
                        do {
                            --mdlend;
                            --mdl;
                            //construct(*mdlend, std::move(*mdl));
                            if(mark) Highlights.markArray(2, mdl);
                            Writes.write(arr, mdlend, arr[mdl], sleep, mark, temp);
                        } while(mdl != mdltop);

                        //move in the new data
                        for(short i = 0; i < count; ++i, ++mdltop)
                            //construct(*mdltop, std::move(swapbufr[i]));
                            Writes.write(arr, mdltop, swapbufr[i], sleep, mark, temp);

                        if((count >= buffer_count) | (left == mdlstart) | (right == end))
                            //if we exit because we reach the end of the input we can move across
                            --left;
                        else {
                            //swap the middle with the left
                            Writes.swap(arr, left, mdltop, sleep, mark, temp);
                            ++mdltop;
                            if(mdltop == right)
                                mdltop = mdlstart;
                        }
                    } else {
                        Writes.swap(arr, left, right, sleep, mark, temp);
                        ++right;
                    }
                } else {
                    Writes.swap(arr, left, mdltop, sleep, mark, temp);
                    ++mdltop;
                    if(mdltop == right)
                        mdltop = mdlstart;
                }
            } else {
                //test the right against the left
                if(Reads.compare(arr[right], arr[left]) == -1) {
                    Writes.swap(arr, left, right, sleep, mark, temp);
                    ++right;
                }
            }

            ++left;
            if(left == mdlstart) {
                //if the left reaches the middle, re-order the middle section so smallest first
                zipRotate(arr, mdlstart, mdltop, right, sleep, mark, temp);
                mdlstart = right;
                mdltop = right;
            }
        }

        if(left != right) {
            //if the right has reached the end before the left
            zipRotate(arr, mdlstart, mdltop, right, sleep, mark, temp);
            zipRotate(arr, left, mdlstart, right, sleep, mark, temp);
        }
    }

    protected void zipSort(int[] arr, int beg, int end, boolean hybrid, double sleep, boolean mark, boolean temp) {
        insertSorter = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        int sze = end - beg;
        if(sze <= 1)
            return;

        int len = 1;

        if(hybrid){
            //sort small runs with insertion sort before doing merge
            int insert_count = 16;
            {
                int len = insert_count;
                int count = 0;
                for(int bg = beg; bg != end; count+=len) {
                    int ed = (count + len > sze ? end : bg + len);
                    insertSorter.customInsertSort(arr, bg, ed, sleep, temp);
                    bg = ed;
                }
            }
            if(sze <= insert_count)
                return;

            len = insert_count;
        }

        //go through all of the lengths starting at 1 doubling
        while(len < sze) {
            int pos = 0;
            //go through all of the sorted sublists, zip them together
            while(pos + len < sze) {
                //make the two halves
                int cleft = beg + pos;
                int cright = cleft + len;
                int cend = (pos + (len * 2) > sze ? end : cright + len);

                //do zip merge
                zipMerge(arr, cleft, cright, cend, sleep, mark, temp);
                pos += (len * 2);
            }
            len *= 2;
        }
    }
}
