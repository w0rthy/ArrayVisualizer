package templates;

import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
The MIT License (MIT)

Copyright (c) 2013 Andrey Astrelin

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/********* Holy Grail Sorting ****************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* (c) 2019 modified by John Reynolds (MusicTheorist)    */
/*                                                       */
/* Holy Grail Sort is a variant of Grail Sort, an        */
/* implementation of Block Merge Sort.                   */
/*                                                       */
/* Grail Sort is a stable sort that works in O(n log n)  */
/* worst time and uses O(1) extra memory. It splits      */
/* sorted ranges of an array into "blocks" of length     */
/* sqrt(n), combines pairs of ranges via swapping        */
/* blocks, and locally merges blocks together using      */
/* an internal buffer. It maintains stability by         */
/* collecting unique values throughout the array called  */
/* "keys", and tagging blocks with these keys to track   */
/* their movements (think of "key-value pairs").         */
/*                                                       */
/* Holy Grail Sort improves on these ideas by using      */
/* Binary Insertion Sort instead of Insertion Sort,      */
/* reducing the number of write operations in select     */
/* cases of Grail Sort's "Rotate" method, and            */
/* implementing a bidirectional internal buffer. These   */
/* optimizations result in an algorithm that is at least */
/* 30% faster than Grail Sort.                           */
/*                                                       */
/* Define int / SortComparator                           */
/* and then call HolyGrailSort() function                */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use HolyGrailSortWithBuffer()                         */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use HolyGrailSortWithDynBuffer()                      */
/*                                                       */
/*********************************************************/

//TODO: Implement grailCheckPairs method to detect presorted array

final class HolyGrailPair {
    private int leftOverLen;
    private int leftOverFrag;
    
    protected HolyGrailPair(int len, int frag) {
        this.leftOverLen = len;
        this.leftOverFrag = frag;
    }
    
    protected int getLeftOverLen() {
        return leftOverLen;
    }
    
    protected int getLeftOverFrag() {
        return leftOverFrag;
    }
}

public abstract class HolyGrailSorting extends Sort {    
    final private int grailStaticBufferLen = 32; //Buffer length changed due to less numbers in this program being sorted than what Mr. Astrelin used for testing.
    
    protected HolyGrailSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
    }
    
    protected int getStaticBuffer() {
        return this.grailStaticBufferLen;
    }
    
    private void grailSwap(int[] arr, int a, int b) {
        Writes.swap(arr, a, b, 1, true, false);
    }

    private void grailMultiSwap(int[] arr, int a, int b, int swapsLeft) {        
        while(swapsLeft != 0) { 
            this.grailSwap(arr, a++, b++);
            swapsLeft--;
        }
    }
    
    // Copy element at pos, shift elements from pos + 1 to pos + len to the left by 1,
    // and paste copied element at pos + len.
    private void grailForwardShift(int[] array, int index, int shiftsLeft) {
        int temp = array[index];
        
        while(shiftsLeft > 0) {
            Writes.write(array, index, array[index + 1], 1, true, false);
            index++;
            shiftsLeft--;
        }
        Writes.write(array, index, temp, 1, true, false);    
    }
    
    // Copy element at pos + len, shift elements from pos to pos + len - 1 to the right by 1,
    // and paste copied element at pos.
    private void grailBackwardShift(int[] array, int index, int shiftsLeft) {    
        int temp = array[index + shiftsLeft];

        while(shiftsLeft > 0) {
            Writes.write(array, index + shiftsLeft, array[(index - 1) + shiftsLeft], 1, true, false);
            shiftsLeft--;
        }
        Writes.write(array, index, temp, 1, true, false);
    }
    
    private void grailRotate(int[] array, int leftIndex, int rightIndex, int target) {
        while(rightIndex != 0 && target != 0) {
            if(rightIndex <= target) {
                if(rightIndex != 1) {
                    this.grailMultiSwap(array, leftIndex, leftIndex + rightIndex, rightIndex);
                    leftIndex += rightIndex;
                    target -= rightIndex;
                }
                else {
                    Highlights.clearMark(2);
                    this.grailForwardShift(array, leftIndex, target);
                    target = 0;
                }
            } 
            else {
                if(target != 1) {
                    this.grailMultiSwap(array, leftIndex + (rightIndex - target), leftIndex + rightIndex, target);
                    rightIndex -= target;
                }
                else {
                    Highlights.clearMark(2);
                    this.grailBackwardShift(array, leftIndex, rightIndex);
                    rightIndex = 0;
                }
            }
        }
    }

    // Thanks to https://jeffreystedfast.blogspot.com/2007/02/binary-insertion-sort.html for
    // a great reference on InsertSort optimizations!!
    
    //TODO: Fairly certain the use of "pos" is inconsistent between Insert and Bin Search
    @SuppressWarnings("fallthrough")
    private void grailInsertSort(int[] arr, int pos, int len) {
        for(int i = 1; i < len; i++) {
            int insertPos = grailBinSearch(arr, pos, pos + i, pos + i, false);
            Highlights.markArray(3, insertPos);
            
            if(insertPos < i) {
                int item = arr[pos + i];

                // Used TimSort's Binary Insert as a reference here.
                int shifts = (pos + i) - insertPos;
                switch(shifts) {
                    case 2:  Writes.write(arr, insertPos + 2, arr[insertPos + 1], 1, true, false);
                    case 1:  Writes.write(arr, insertPos + 1, arr[insertPos],     1, true, false);
                             break;
                    default: Writes.reversearraycopy(arr, insertPos, arr, insertPos + 1, shifts, 1, true, false);
                }
                Writes.write(arr, insertPos, item, 1, true, false);
            }
            
            Highlights.clearMark(3);
        }
    }

    // Binary Search is not general purpose, FIX IT
    
    //boolean argument determines direction
    private int grailBinSearch(int[] arr, int pos, int len, int keyPos, boolean isLeft) {
        int left = -1, right = len;
        while(left < right - 1) {
            int mid = left + ((right - left) >> 1);
            if(isLeft) {
                if(Reads.compare(arr[pos + mid], arr[keyPos]) >= 0) {
                    right = mid;
                } else left = mid;
            } else {
                if(Reads.compare(arr[pos + mid], arr[keyPos]) > 0) {
                    right = mid;
                } else left = mid;
            }
            Highlights.markArray(1, pos + mid);
        }
        return right;
    }

    //TODO: *Somehow* make this more efficient.
    // cost: 2 * len + numKeys^2 / 2
    private int grailFindKeys(int[] arr, int pos, int len, int numKeys) {
        int dist = 1, foundKeys = 1, firstKey = 0;  // first key is always here

        while(dist < len && foundKeys < numKeys) {
            //Binary Search left
            int loc = this.grailBinSearch(arr, pos + firstKey, foundKeys, pos + dist, true);
            if(loc == foundKeys || Reads.compare(arr[pos + dist], arr[pos + (firstKey + loc)]) != 0) {
                this.grailRotate(arr, pos + firstKey, foundKeys, dist - (firstKey + foundKeys));
                firstKey = dist - foundKeys;
                this.grailRotate(arr, pos + (firstKey + loc), foundKeys - loc, 1);
                foundKeys++;
            }
            dist++;
        }
        this.grailRotate(arr, pos, firstKey, foundKeys);
        
        Highlights.clearMark(2);
        
        return foundKeys;
    }

    private boolean grailCheckOddPairs(int[] arr, int pos, int len) {
        for(int dist = 2; dist < (len - 2); dist += 2) {
            Highlights.markArray(1, pos + dist - 1);
            Highlights.markArray(2, pos + dist);
            Delays.sleep(1);
            
            if(Reads.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) {
                return false;
            }
        }
        return true;
    }
    
    // cost: min(len1, len2)^2 + max(len1, len2)
    private void grailMergeWithoutBuffer(int[] arr, int pos, int len1, int len2) {
        if(len1 < len2) {
            while(len1 != 0) {
                //Binary Search left
                int loc = this.grailBinSearch(arr, pos + len1, len2, pos, true);
                if(loc != 0) {
                    this.grailRotate(arr, pos, len1, loc);
                    pos += loc;
                    len2 -= loc;
                }
                if(len2 == 0) break;
                do {
                    pos++;
                    len1--;
                } while(len1 != 0 && Reads.compare(arr[pos], arr[pos + len1]) <= 0);
            }
        } else {
            while(len2 != 0) {
                //Binary Search right
                int loc = this.grailBinSearch(arr, pos, len1, pos + (len1 + len2 - 1), false);
                if(loc != len1) {
                    this.grailRotate(arr, pos + loc, len1 - loc, len2);
                    len1 = loc;
                }
                if(len1 == 0) break;
                do {
                    len2--;
                } while(len2 != 0 && Reads.compare(arr[pos + len1 - 1], arr[pos + len1 + len2 - 1]) <= 0);
            }
        }
    }

    // arr - starting array. arr[0 - regBlockLen..-1] - buffer (if havebuf).
    // regBlockLen - length of regular blocks. First blockCount blocks are stable sorted by 1st elements and key-coded
    // keysPos - arrays of keys, in same order as blocks. keysPos < midkey means stream A
    // aBlockCount are regular blocks from stream A.
    // lastLen is length of last (irregular) block from stream B, that should go before nblock2 blocks.
    // lastLen = 0 requires aBlockCount = 0 (no irregular blocks). lastLen > 0, aBlockCount = 0 is possible.
    private void grailMergeBuffersLeft(int[] arr, int keysPos, int midkey, int pos, 
            int blockCount, int blockLen, boolean havebuf, int aBlockCount, 
            int lastLen) {

        if(blockCount == 0) {
            int aBlocksLen = aBlockCount * blockLen;
            if(havebuf) this.grailMergeLeft(arr, pos, aBlocksLen, lastLen, 0 - blockLen);
            else this.grailMergeWithoutBuffer(arr, pos, aBlocksLen, lastLen);
            return;
        }

        int leftOverLen = blockLen;
        int leftOverFrag = Reads.compare(arr[keysPos], arr[midkey]) < 0 ? 0 : 1;
        int processIndex = blockLen;
        int restToProcess;

        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, processIndex += blockLen) {
            restToProcess = processIndex - leftOverLen;
            int nextFrag = Reads.compare(arr[keysPos + keyIndex], arr[midkey]) < 0 ? 0 : 1;

            if(nextFrag == leftOverFrag) {
                if(havebuf) this.grailMultiSwap(arr, pos + restToProcess - blockLen, pos + restToProcess, leftOverLen);
                restToProcess = processIndex;
                leftOverLen = blockLen;
            } else {
                if(havebuf) {
                    GrailPair results = this.grailSmartMergeWithBuffer(arr, pos + restToProcess, leftOverLen, leftOverFrag, blockLen);
                    leftOverLen = results.getLeftOverLen();
                    leftOverFrag = results.getLeftOverFrag();
                } else {
                    GrailPair results = this.grailSmartMergeWithoutBuffer(arr, pos + restToProcess, leftOverLen, leftOverFrag, blockLen);
                    leftOverLen = results.getLeftOverLen();
                    leftOverFrag = results.getLeftOverFrag();
                }
            }
        }
        restToProcess = processIndex - leftOverLen;

        if(lastLen != 0) {
            if(leftOverFrag != 0) {
                if(havebuf) {
                    this.grailMultiSwap(arr, pos + restToProcess - blockLen, pos + restToProcess, leftOverLen);
                }
                restToProcess = processIndex;
                leftOverLen = blockLen * aBlockCount;
                leftOverFrag = 0;
            } else {
                leftOverLen += blockLen * aBlockCount;
            }
            if(havebuf) {
                this.grailMergeLeft(arr, pos + restToProcess, leftOverLen, lastLen, -blockLen);
            }
            else {
                this.grailMergeWithoutBuffer(arr, pos + restToProcess, leftOverLen, lastLen);
            }
        } else {
            if(havebuf) {
                this.grailMultiSwap(arr, pos + restToProcess, pos + (restToProcess - blockLen), leftOverLen);
            }
        }
    }
    
    // arr[dist..-1] - buffer, arr[0, leftLen - 1] ++ arr[leftLen, leftLen + rightLen - 1]
    // -> arr[dist, dist + leftLen + rightLen - 1]
    private void grailMergeLeft(int[] arr, int pos, int leftLen, int rightLen, int dist) {
        int left = 0;
        int right = leftLen;

        rightLen += leftLen;

        while(right < rightLen) {
            if(left == leftLen || Reads.compare(arr[pos + left], arr[pos + right]) > 0) {
                this.grailSwap(arr, pos + (dist++), pos + (right++));
            } 
            else this.grailSwap(arr, pos + (dist++), pos + (left++));       
            Highlights.markArray(3, pos + left);
            Highlights.markArray(4, pos + right);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        
        if(dist != left) this.grailMultiSwap(arr, pos + dist, pos + left, leftLen - left);
    }
    private void grailMergeRight(int[] arr, int pos, int leftLen, int rightLen, int dist) {
        int mergedPos = leftLen + rightLen + dist - 1;
        int right = leftLen + rightLen - 1;
        int left = leftLen - 1;

        while(left >= 0) {
            if(right < leftLen || Reads.compare(arr[pos + left], arr[pos + right]) > 0) {
                this.grailSwap(arr, pos + (mergedPos--), pos + (left--));
            } 
            else this.grailSwap(arr, pos + (mergedPos--), pos + (right--));
            Highlights.markArray(3, pos + left);
            Highlights.markArray(4, pos + right);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        
        if(right != mergedPos) {
            while(right >= leftLen) this.grailSwap(arr, pos + (mergedPos--), pos + (right--));
        }
    }

    //returns the leftover length, then the leftover fragment
    private GrailPair grailSmartMergeWithoutBuffer(int[] arr, int pos, int leftOverLen, int leftOverFrag, int regBlockLen) {
        if(regBlockLen == 0) return new GrailPair(leftOverLen, leftOverFrag);

        int len1 = leftOverLen;
        int len2 = regBlockLen;
        int typeFrag = 1 - leftOverFrag; //1 if inverted

        if(len1 != 0 && Reads.compare(arr[pos + (len1 - 1)], arr[pos + len1]) - typeFrag >= 0) {

            while(len1 != 0) {
                int foundLen;
                if (typeFrag != 0) {
                    //Binary Search left
                    foundLen = this.grailBinSearch(arr, pos + len1, len2, pos, true);
                } else { 
                    //Binary Search right
                    foundLen = this.grailBinSearch(arr, pos + len1, len2, pos, false);
                }
                if(foundLen != 0) {
                    this.grailRotate(arr, pos, len1, foundLen);
                    pos += foundLen;
                    len2 -= foundLen;
                }
                if(len2 == 0) {
                    return new GrailPair(len1, leftOverFrag);
                }
                do {
                    pos++;
                    len1--;
                } while(len1 != 0 && Reads.compare(arr[pos], arr[pos + len1]) - typeFrag < 0);
            }
        }
        return new GrailPair(len2, typeFrag);
    }

    //returns the leftover length, then the leftover fragment
    private GrailPair grailSmartMergeWithBuffer(int[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen) {
        int dist = 0 - blockLen, left = 0, right = leftOverLen, leftEnd = right, rightEnd = right + blockLen;
        int typeFrag = 1 - leftOverFrag;  // 1 if inverted

        while(left < leftEnd && right < rightEnd) {
            if(Reads.compare(arr[pos + left], arr[pos + right]) - typeFrag < 0) {
                this.grailSwap(arr, pos + (dist++), pos + (left++));
            }
            else this.grailSwap(arr, pos + (dist++), pos + (right++));
            Highlights.markArray(3, pos + left);
            Highlights.markArray(4, pos + right);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);
        
        int length, fragment = leftOverFrag;
        if(left < leftEnd) {
            length = leftEnd - left;
            while(left < leftEnd) this.grailSwap(arr, pos + (--leftEnd), pos + (--rightEnd));
        } else {
            length = rightEnd - right;
            fragment = typeFrag;
        }
        return new GrailPair(length, fragment);
    }


    /***** Sort With Extra Buffer *****/

    //returns the leftover length, then the leftover fragment
    private GrailPair grailSmartMergeWithXBuf(int[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen) {
        int dist = 0 - blockLen, left = 0, right = leftOverLen, leftEnd = right, rightEnd = right + blockLen;
        int typeFrag = 1 - leftOverFrag;  // 1 if inverted
        
        Highlights.clearMark(2);
        
        while(left < leftEnd && right < rightEnd) {
            if(Reads.compare(arr[pos + left], arr[pos + right]) - typeFrag < 0) {
                Writes.write(arr, pos + dist++, arr[pos + left++], 1, true, false);
            }
            else Writes.write(arr, pos + dist++, arr[pos + right++], 1, true, false);
            Highlights.markArray(2, pos + left);
            Highlights.markArray(3, pos + right);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        
        int length, fragment = leftOverFrag;
        if(left < leftEnd) {
            length = leftEnd - left;
            while(left < leftEnd) Writes.write(arr, pos + --rightEnd, arr[pos + --leftEnd], 1, true, false);
        } else {
            length = rightEnd - right;
            fragment = typeFrag;
        }
        return new GrailPair(length, fragment);
    }

    // arr[dist..-1] - free, arr[0, leftEnd - 1] ++ arr[leftEnd, leftEnd + rightEnd - 1]
    // -> arr[dist, dist + leftEnd + rightEnd - 1]
    private void grailMergeLeftWithXBuf(int[] arr, int pos, int leftEnd, int rightEnd, int dist) {
        int left = 0;
        int right = leftEnd;
        rightEnd += leftEnd;

        Highlights.clearMark(2);
        
        while(right < rightEnd) {
            if(left == leftEnd || Reads.compare(arr[pos + left], arr[pos + right]) > 0) {
                Writes.write(arr, pos + dist++, arr[pos + right++], 1, true, false);
            }
            else Writes.write(arr, pos + dist++, arr[pos + left++], 1, true, false);
            Highlights.markArray(2, pos + left);
            Highlights.markArray(3, pos + right);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        
        if(dist != left) {
            while(left < leftEnd) Writes.write(arr, pos + dist++, arr[pos + left++], 1, true, false);
        }
    }

    // arr - starting array. arr[0 - regBlockLen..-1] - buffer (if havebuf).
    // regBlockLen - length of regular blocks. First blockCount blocks are stable sorted by 1st elements and key-coded
    // keysPos - where keys are in array, in same order as blocks. keysPos < midkey means stream A
    // aBlockCount are regular blocks from stream A.
    // lastLen is length of last (irregular) block from stream B, that should go before aCountBlock blocks.
    // lastLen = 0 requires aBlockCount = 0 (no irregular blocks). lastLen > 0, aBlockCount = 0 is possible.
    private void grailMergeBuffersLeftWithXBuf(int[] arr, int keysPos, int midkey, int pos,
            int blockCount, int regBlockLen, int aBlockCount, int lastLen) {

        Highlights.clearMark(2);
        
        if(blockCount == 0) {
            int aBlocksLen = aBlockCount * regBlockLen;
            this.grailMergeLeftWithXBuf(arr, pos, aBlocksLen, lastLen, 0 - regBlockLen);
            return;
        }

        int leftOverLen = regBlockLen;
        int leftOverFrag = Reads.compare(arr[keysPos], arr[midkey]) < 0 ? 0 : 1;
        int processIndex = regBlockLen;

        int restToProcess;
        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, processIndex += regBlockLen) {
            restToProcess = processIndex - leftOverLen;
            int nextFrag = Reads.compare(arr[keysPos + keyIndex], arr[midkey]) < 0 ? 0 : 1;

            if(nextFrag == leftOverFrag) {
                Writes.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen, 1, true, false);
                
                restToProcess = processIndex;
                leftOverLen = regBlockLen;
            } else {
                GrailPair results = this.grailSmartMergeWithXBuf(arr, pos + restToProcess, leftOverLen, leftOverFrag, regBlockLen);
                leftOverLen = results.getLeftOverLen(); 
                leftOverFrag = results.getLeftOverFrag();
            }
        }
        restToProcess = processIndex - leftOverLen;

        if(lastLen != 0) {
            if(leftOverFrag != 0) {
                Writes.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen, 1, true, false);
                
                restToProcess = processIndex;
                leftOverLen = regBlockLen * aBlockCount;
                leftOverFrag = 0;
            } else {
                leftOverLen += regBlockLen * aBlockCount;
            }
            this.grailMergeLeftWithXBuf(arr, pos + restToProcess, leftOverLen, lastLen, 0 - regBlockLen);
        } else {
            Writes.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen, 1, true, false);
        }
    }

    /***** End Sort With Extra Buffer *****/

    // build blocks of length buildLen
    // input: [-buildLen, -1] elements are buffer
    // output: first buildLen elements are buffer, blocks 2 * buildLen and last subblock sorted
    
    // if array is already sorted, return false
    private boolean grailBuildBlocks(int[] arr, int pos, int len, int buildLen, 
            int[] extbuf, int bufferPos, int extBufLen) {

        int buildBuf = buildLen < extBufLen ? buildLen : extBufLen;
        while((buildBuf & (buildBuf - 1)) != 0) buildBuf &= buildBuf - 1;  // max power or 2 - just in case

        int extraDist, part;
        if(buildBuf != 0) {
            Writes.arraycopy(arr, pos - buildBuf, extbuf, bufferPos, buildBuf, 1, true, true);
            
            for(int dist = 1; dist < len; dist += 2) {
                extraDist = 0;
                if(Reads.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) extraDist = 1;
                Writes.write(arr, pos + dist - 3, arr[pos + dist - 1 + extraDist], 1, true, false);
                Writes.write(arr, pos + dist - 2, arr[pos + dist - extraDist], 1, true, false);
            }
            if(len % 2 != 0) Writes.write(arr, pos + len - 3, arr[pos + len - 1], 1, true, false);
            pos -= 2;

            for(part = 2; part < buildBuf; part *= 2) {
                int left = 0;
                int right = len - 2 * part;
                while(left <= right) {
                    this.grailMergeLeftWithXBuf(arr, pos + left, part, part, 0 - part);
                    left += 2 * part;
                }
                int rest = len - left;

                if(rest > part) {
                    this.grailMergeLeftWithXBuf(arr, pos + left, part, rest - part, 0 - part);
                } else {
                    for(; left < len; left++) Writes.write(arr, pos + left - part, arr[pos + left], 1, true, false);
                }
                pos -= part;
            }    
            Writes.arraycopy(extbuf, bufferPos, arr, pos + len, buildBuf, 1, true, false);
        } 
        else {
            boolean evenPairsSorted = true;
            
            for(int dist = 1; dist < len; dist += 2) {
                extraDist = 0;
                if(Reads.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) {
                    evenPairsSorted = false;
                    extraDist = 1;
                }
                this.grailSwap(arr, pos + (dist - 3), pos + (dist - 1 + extraDist));
                this.grailSwap(arr, pos + (dist - 2), pos + (dist - extraDist));
            }
            if(len % 2 != 0) this.grailSwap(arr, pos + (len - 1), pos + (len - 1) - 2);
            pos -= 2;
            part = 2;
        
            if(evenPairsSorted) {
                pos += 2;
                if(this.grailCheckOddPairs(arr, pos, len)) {
                    while((len - 1) - 2 >= 0) {
                        this.grailSwap(arr, pos + (len - 1), pos + ((len--) - 1) - 2);
                    }
                    if(Reads.compare(arr[pos], arr[pos + 1]) > 0) {
                        this.grailSwap(arr, pos, pos + 1);
                    }
                    return false;
                }
                pos -= 2;
            }
        }

        for(; part < buildLen; part *= 2) {
            int left = 0;
            int right = len - 2 * part;
            while(left <= right) {
                this.grailMergeLeft(arr, pos + left, part, part, 0 - part);
                left += 2 * part;
            }
            int rest = len - left;
            if(rest > part) {
                this.grailMergeLeft(arr, pos + left, part, rest - part, 0 - part);
            } else {
                this.grailRotate(arr, pos + left - part, part, rest);
            }
            pos -= part;
        }
        int restToBuild = len % (2 * buildLen);
        int leftOverPos = len - restToBuild;

        if(restToBuild <= buildLen) this.grailRotate(arr, pos + leftOverPos, restToBuild, buildLen);
        else this.grailMergeRight(arr, pos + leftOverPos, buildLen, restToBuild - buildLen, buildLen);

        while(leftOverPos > 0) {
            leftOverPos -= 2 * buildLen;
            this.grailMergeRight(arr, pos + leftOverPos, buildLen, buildLen, buildLen);
        }
        
        return true;
    }

    // keys are on the left of arr. Blocks of length buildLen combined. We'll combine them in pairs
    // buildLen and nkeys are powers of 2. (2 * buildLen / regBlockLen) keys are guaranteed
    private void grailCombineBlocksForward(int[] arr, int keyPos, int pos, int len, int buildLen,
            int regBlockLen, boolean havebuf, int[] buffer, int bufferPos) {
        
        int combineLen = len / (2 * buildLen);
        int leftOver = len % (2 * buildLen);
        if(leftOver <= buildLen) {
            len -= leftOver;
            leftOver = 0;
        }

        if(buffer != null) Writes.arraycopy(arr, pos - regBlockLen, buffer, bufferPos, regBlockLen, 1, true, true);

        for(int i = 0; i <= combineLen; i++) {
            if(i == combineLen && leftOver == 0) break;

            int blockPos = pos + i * 2 * buildLen;
            int blockCount = (i == combineLen ? leftOver : 2 * buildLen) / regBlockLen;

            this.grailInsertSort(arr, keyPos, blockCount + (i == combineLen ? 1 : 0));
            
            int midkey = buildLen / regBlockLen;

            for(int index = 1; index < blockCount; index++) {
                int leftIndex = index - 1;

                for(int rightIndex = index; rightIndex < blockCount; rightIndex++) {
                    int rightComp = Reads.compare(arr[blockPos + leftIndex * regBlockLen],
                            arr[blockPos + rightIndex * regBlockLen]);
                    if(rightComp > 0 || (rightComp == 0 && Reads.compare(arr[keyPos + leftIndex], arr[keyPos + rightIndex]) > 0)) {
                        leftIndex = rightIndex;
                    }
                }
                if(leftIndex != index - 1) {
                    this.grailMultiSwap(arr, blockPos + (index - 1) * regBlockLen, blockPos + leftIndex * regBlockLen, regBlockLen);
                    this.grailSwap(arr, keyPos + (index - 1), keyPos + leftIndex);
                    if(midkey == index - 1 || midkey == leftIndex) {
                        midkey ^= (index - 1) ^ leftIndex;
                    }
                }
            }

            int aBlockCount = 0;
            int lastLen = 0;
            if(i == combineLen) lastLen = leftOver % regBlockLen;

            if(lastLen != 0) {
                while(aBlockCount < blockCount && Reads.compare(arr[blockPos + blockCount * regBlockLen],
                        arr[blockPos + (blockCount - aBlockCount - 1) * regBlockLen]) < 0) {
                    aBlockCount++;
                }
            }

            if(buffer != null) {
                this.grailMergeBuffersLeftWithXBuf(arr, keyPos, keyPos + midkey, blockPos,
                        blockCount - aBlockCount, regBlockLen, aBlockCount, lastLen);
            }
            else this.grailMergeBuffersLeft(arr, keyPos, keyPos + midkey, blockPos, 
                    blockCount - aBlockCount, regBlockLen, havebuf, aBlockCount, lastLen);
        }
        if(buffer != null) {
            for(int i = len; --i >= 0;) Writes.write(arr, pos + i, arr[pos + i - regBlockLen], 1, true, false);
            Writes.arraycopy(buffer, bufferPos, arr, pos - regBlockLen, regBlockLen, 1, true, false);
        }
        else if(havebuf) {
            //if(combineLen != 0) {
                while(--len >= 0) {
                    int temp = arr[pos + len];
                    arr[pos + len] = arr[pos + len - regBlockLen];
                    arr[pos + len - regBlockLen] = temp;
                }
            //}
        }
    }

    private void grailCombineBlocksBackward(int[] arr, int keyPos, int pos, int len, int buildLen,
            int regBlockLen, boolean havebuf, int[] buffer, int bufferPos) {
        
        int combineLen = len / (2 * buildLen);
        int leftOver = len % (2 * buildLen);
        if(leftOver <= buildLen) {
            len -= leftOver;
            leftOver = 0;
        }

        if(buffer != null) Writes.arraycopy(arr, pos - regBlockLen, buffer, bufferPos, regBlockLen, 1, true, true);

        for(int i = combineLen; i >= 0; i--) {
            if(i == 0 && leftOver == 0) break;

            int blockPos = pos + i * 2 * buildLen;
            int blockCount = (i == 0 ? leftOver : 2 * buildLen) / regBlockLen;

            this.grailInsertSort(arr, keyPos, blockCount + (i == 0 ? 1 : 0));
            
            int midkey = buildLen / regBlockLen;

            for(int index = 1; index < blockCount; index++) {
                int leftIndex = index - 1;

                for(int rightIndex = index; rightIndex < blockCount; rightIndex++) {
                    int rightComp = Reads.compare(arr[blockPos + leftIndex * regBlockLen],
                            arr[blockPos + rightIndex * regBlockLen]);
                    if(rightComp > 0 || (rightComp == 0 && Reads.compare(arr[keyPos + leftIndex], arr[keyPos + rightIndex]) > 0)) {
                        leftIndex = rightIndex;
                    }
                }
                if(leftIndex != index - 1) {
                    this.grailMultiSwap(arr, blockPos + (index - 1) * regBlockLen, blockPos + leftIndex * regBlockLen, regBlockLen);
                    this.grailSwap(arr, keyPos + (index - 1), keyPos + leftIndex);
                    if(midkey == index - 1 || midkey == leftIndex) {
                        midkey ^= (index - 1) ^ leftIndex;
                    }
                }
            }

            int aBlockCount = 0;
            int lastLen = 0;
            if(i == 0) lastLen = leftOver % regBlockLen;

            if(lastLen != 0) {
                while(aBlockCount < blockCount && Reads.compare(arr[blockPos + blockCount * regBlockLen],
                        arr[blockPos + (blockCount - aBlockCount - 1) * regBlockLen]) < 0) {
                    aBlockCount++;
                }
            }

            if(buffer != null) {
                this.grailMergeBuffersLeftWithXBuf(arr, keyPos, keyPos + midkey, blockPos,
                        blockCount - aBlockCount, regBlockLen, aBlockCount, lastLen);
            }
            else this.grailMergeBuffersLeft(arr, keyPos, keyPos + midkey, blockPos, 
                    blockCount - aBlockCount, regBlockLen, havebuf, aBlockCount, lastLen);
        }
    }

    protected void grailLazyStableSort(int[] arr, int pos, int len) {
        for(int dist = 1; dist < len; dist += 2) {
            if(Reads.compare(arr[pos + dist - 1], arr[pos + dist]) > 0) {
                this.grailSwap(arr, pos + (dist - 1), pos + dist);
            }
            Highlights.markArray(3, pos + dist - 1);
            Highlights.markArray(4, pos + dist);
        }
        Highlights.clearMark(3);
        Highlights.clearMark(4);

        for(int part = 2; part < len; part *= 2) {
            int left = 0;
            int right = len - 2 * part;

            while(left <= right) {
                this.grailMergeWithoutBuffer(arr, pos + left, part, part);
                left += 2 * part;
            }

            int rest = len - left;
            if(rest > part) {
                this.grailMergeWithoutBuffer(arr, pos + left, part, rest - part);
            }
        }
    }

    protected void grailCommonSort(int[] arr, int pos, int len, int[] buffer, int bufferPos, int bufferLen) {
        if(len <= 32) {
            this.grailInsertSort(arr, pos, len);
            return;
        }
        
        int blockLen = 1;
        while(blockLen * blockLen < len) blockLen *= 2;   
        
        int numKeys = (len - 1) / blockLen + 1;
        int keysFound = this.grailFindKeys(arr, pos, len, numKeys + blockLen);
        
        boolean bufferEnabled = true;

        if(keysFound < numKeys + blockLen) {
            if(keysFound < 4) {
                this.grailLazyStableSort(arr, pos, len);
                return;
            }
            numKeys = blockLen;
            while(numKeys > keysFound) numKeys /= 2;
            bufferEnabled = false;
            blockLen = 0;
        }

        int dist = blockLen + numKeys;
        int buildLen = bufferEnabled ? blockLen : numKeys;

        boolean continueSort = true;

        if(bufferEnabled) {
            continueSort = this.grailBuildBlocks(arr, pos + dist, len - dist, buildLen, buffer, bufferPos, bufferLen);
        }
        else {
            continueSort = this.grailBuildBlocks(arr, pos + dist, len - dist, buildLen, null, bufferPos, 0);
        }
        
        System.out.println(continueSort);
        
        if(continueSort) {
            boolean mergeForward = true;

            // 2 * buildLen are built
            while(len - dist > (buildLen *= 2)) {
                int regBlockLen = blockLen;
                boolean buildBufEnabled = bufferEnabled;

                if(!bufferEnabled) {
                    if(numKeys > 4 && numKeys / 8 * numKeys >= buildLen) {
                        regBlockLen = numKeys / 2;
                        buildBufEnabled = true;
                    } else {
                        int calcKeys = 1;
                        int i = buildLen * keysFound / 2;
                        while(calcKeys < numKeys && i != 0) {
                            calcKeys *= 2;
                            i /= 8;
                        }
                        regBlockLen = (2 * buildLen) / calcKeys;
                    }
                }
                if(mergeForward) {
                    this.grailCombineBlocksForward(arr, pos, pos + dist, len - dist, buildLen, regBlockLen, buildBufEnabled, 
                            buildBufEnabled && regBlockLen <= bufferLen ? buffer : null, bufferPos);
                }
                else {
                    this.grailCombineBlocksBackward(arr, pos, pos + dist, len - dist - regBlockLen, buildLen, regBlockLen, buildBufEnabled, 
                            buildBufEnabled && regBlockLen <= bufferLen ? buffer : null, bufferPos);
                }

                //mergeForward = !mergeForward;

                Highlights.clearMark(2);
            }

            this.grailInsertSort(arr, pos, dist);
        }
        
        //TODO: O(n) best-case DOES NOT WORK for key counts LESS THAN (2 * sqrt(n))
        this.grailMergeWithoutBuffer(arr, pos, dist, len - dist);
        
        /*
        this.grailInsertSort(arr, pos, numKeys);
        this.grailMergeWithoutBuffer(arr, pos, numKeys, len - blockLen);
        
        BinaryInsertionSort sort = new BinaryInsertionSort(Delays, Highlights, Reads, Writes);
        sort.customBinaryInsert(arr, pos + len - blockLen, len, 0.1);
        
        //this.grailInsertSort(arr, pos + len - blockLen, blockLen);
        this.grailMergeWithoutBuffer(arr, pos, len - blockLen, blockLen);
        */
    }
}