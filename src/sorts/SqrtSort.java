package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
The MIT License (MIT)

Copyright (c) 2014 Andrey Astrelin

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
 *
 */

/********* Sqrt sorting **********************************/
/*                                                       */
/* (c) 2014 by Andrey Astrelin                           */
/* Refactored by MusicTheorist                           */
/*                                                       */
/* Stable sorting that works in O(N*log(N)) worst time   */
/* and uses O(sqrt(N)) extra memory                      */
/*                                                       */
/* Define SORT_TYPE and SORT_CMP                         */
/* and then call SqrtSort() function                     */
/*                                                       */
/*********************************************************/

final class SqrtState {
    private int leftOverLen;
    private int leftOverFrag;
    
    protected SqrtState(int len, int frag) {
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

final public class SqrtSort extends Sort {
    private InsertionSort insertSorter;
    
    public SqrtSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Sqrt");
        this.setRunAllID("Square Root Sort (Block Merge Sort");
        this.setReportSortID("Sqrtsort");
        this.setCategory("Hybrid Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    private void sqrtSwap(int[] arr, int a, int b, boolean auxwrite) {
        Writes.swap(arr, a, b, 1, true, auxwrite);
        Highlights.clearMark(2);
    }

    private void sqrtMultiSwap(int[] arr, int a, int b, int swapsLeft, boolean auxwrite) {        
        while(swapsLeft != 0) { 
            this.sqrtSwap(arr, a++, b++, auxwrite);
            swapsLeft--;
        }
    }

    private void sqrtInsertSort(int[] arr, int pos, int len, boolean auxwrite) {
        insertSorter.customInsertSort(arr, pos, len, 0.25, auxwrite);
    }
    
    private void sqrtMergeRight(int[] arr, int pos, int leftLen, int rightLen, int dist, boolean auxwrite) {
        int mergedPos = leftLen + rightLen + dist - 1;
        int right = leftLen + rightLen - 1;
        int left = leftLen - 1;

        while(left >= 0) {
            Highlights.markArray(2, pos + left);
            Highlights.markArray(3, pos + right);
            
            if(right < leftLen || Reads.compare(arr[pos + left], arr[pos + right]) > 0) {
                Writes.write(arr, pos + (mergedPos--), arr[pos + (left--)], 1, true, auxwrite);
            } 
            else Writes.write(arr, pos + (mergedPos--), arr[pos + (right--)], 1, true, auxwrite);
        }
        
        Highlights.clearMark(3);
        
        if(right != mergedPos) {
            while(right >= leftLen) {
                Writes.write(arr, pos + (mergedPos--), arr[pos + (right--)], 1, true, auxwrite);
                Highlights.markArray(2, pos + right);
            }
        }
        
        Highlights.clearMark(2);
    }

    // arr[dist..-1] - free, arr[0, leftEnd - 1] ++ arr[leftEnd, leftEnd + rightEnd - 1]
    // -> arr[dist, dist + leftEnd + rightEnd - 1]
    private void sqrtMergeLeftWithXBuf(int[] arr, int pos, int leftEnd, int rightEnd, int dist, boolean auxwrite) {
        int left = 0;
        int right = leftEnd;
        rightEnd += leftEnd;

        while(right < rightEnd) {
            if(left == leftEnd || Reads.compare(arr[pos + left], arr[pos + right]) > 0) {
                Writes.write(arr, pos + dist++, arr[pos + right++], 1, true, auxwrite);
            }
            else Writes.write(arr, pos + dist++, arr[pos + left++], 1, true, auxwrite);
            
            Highlights.markArray(2, pos + left);
            Highlights.markArray(3, pos + right);
        }
        
        Highlights.clearMark(3);
        
        if(dist != left) {
            while(left < leftEnd) {
                Writes.write(arr, pos + dist++, arr[pos + left++], 1, true, auxwrite);
                Highlights.markArray(2, pos + left);
            }
        }
        
        Highlights.clearMark(2);
    }

    // arr[0,L1-1] ++ arr2[0,L2-1] -> arr[-L1,L2-1],  arr2 is "before" arr1
    private void sqrtMergeDown(int[] arr, int arrPos, int[] buffer, int bufPos, int leftLen, int rightLen, boolean auxwrite) {
        int arrMerge = 0, bufMerge = 0;
        int dist = 0 - rightLen;
        
        while(bufMerge < rightLen) {
            if(arrMerge == leftLen || Reads.compare(arr[arrPos + arrMerge], buffer[bufPos + bufMerge]) >= 0) {
                Writes.write(arr, arrPos + dist++, buffer[bufPos + bufMerge++], 1, true, auxwrite);
            }
            else Writes.write(arr, arrPos + dist++, arr[arrPos + arrMerge++], 1, true, auxwrite);
            
            Highlights.markArray(2, arrPos + arrMerge);
            Highlights.markArray(3, bufPos + bufMerge);
        }
        
        Highlights.clearMark(3);
        
        if(dist != arrMerge) {
            while(arrMerge < leftLen) {
                Writes.write(arr, arrPos + dist++, arr[arrPos + arrMerge++], 1, true, auxwrite);
                Highlights.markArray(2, arrPos + arrMerge);
            }
        }
        
        Highlights.clearMark(2);
    }
    
    //returns the leftover length, then the leftover fragment
    private SqrtState sqrtSmartMergeWithXBuf(int[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen, boolean auxwrite) {
        int dist = 0 - blockLen, left = 0, right = leftOverLen, leftEnd = right, rightEnd = right + blockLen;
        int typeFrag = 1 - leftOverFrag;  // 1 if inverted

        while(left < leftEnd && right < rightEnd) {
            if(Reads.compare(arr[pos + left], arr[pos + right]) - typeFrag < 0) {
                Writes.write(arr, pos + dist++, arr[pos + left++], 1, true, auxwrite);
            }
            else Writes.write(arr, pos + dist++, arr[pos + right++], 1, true, auxwrite);
            
            Highlights.markArray(2, pos + left);
            Highlights.markArray(3, pos + right);
        }
        
        Highlights.clearMark(3);
     
        int length, fragment = leftOverFrag;
        
        if(left < leftEnd) {
            length = leftEnd - left;
            
            while(left < leftEnd) {
                Writes.write(arr, pos + --rightEnd, arr[pos + --leftEnd], 1, true, auxwrite);
                Highlights.markArray(2, pos + leftEnd);
            }
        } 
        else {
            length = rightEnd - right;
            fragment = typeFrag;
        }
        
        Highlights.clearMark(2);
        
        return new SqrtState(length, fragment);
    }

    // arr - starting array. arr[0 - regBlockLen..-1] - buffer (if havebuf).
    // regBlockLen - length of regular blocks. First blockCount blocks are stable sorted by 1st elements and key-coded
    // keysPos - where keys are in array, in same order as blocks. keysPos < midkey means stream A
    // aBlockCount are regular blocks from stream A.
    // lastLen is length of last (irregular) block from stream B, that should go before aCountBlock blocks.
    // lastLen = 0 requires aBlockCount = 0 (no irregular blocks). lastLen > 0, aBlockCount = 0 is possible.
    private void sqrtMergeBuffersLeftWithXBuf(int[] keys, int midkey, int[] arr, int pos, int blockCount, int regBlockLen,
                                              int aBlockCount, int lastLen, boolean auxwrite) {

        if(blockCount == 0) {
            int aBlocksLen = aBlockCount * regBlockLen;
            this.sqrtMergeLeftWithXBuf(arr, pos, aBlocksLen, lastLen, 0 - regBlockLen, auxwrite);
            return;
        }

        int leftOverLen = regBlockLen;
        int leftOverFrag = Reads.compare(keys[0], midkey) < 0 ? 0 : 1;
        int processIndex = regBlockLen;

        int restToProcess;
        
        for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, processIndex += regBlockLen) {
            restToProcess = processIndex - leftOverLen;
            int nextFrag = Reads.compare(keys[keyIndex], midkey) < 0 ? 0 : 1;

            if(nextFrag == leftOverFrag) {
                Writes.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen, 1, true, auxwrite);
                
                restToProcess = processIndex;
                leftOverLen = regBlockLen;
            }
            else {
                SqrtState results = this.sqrtSmartMergeWithXBuf(arr, pos + restToProcess, leftOverLen, leftOverFrag, regBlockLen, auxwrite);
                
                leftOverLen = results.getLeftOverLen();
                leftOverFrag = results.getLeftOverFrag();
            }
        }
        
        restToProcess = processIndex - leftOverLen;

        if(lastLen != 0) {
            if(leftOverFrag != 0) {
                Writes.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen, 1, true, auxwrite);
                
                restToProcess = processIndex;
                leftOverLen = regBlockLen * aBlockCount;
                leftOverFrag = 0;
            } 
            else {
                leftOverLen += regBlockLen * aBlockCount;
            }
            this.sqrtMergeLeftWithXBuf(arr, pos + restToProcess, leftOverLen, lastLen, 0 - regBlockLen, auxwrite);
        } 
        else {
            Writes.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen, 1, true, auxwrite);
        }
    }
    
    // build blocks of length buildLen
    // input: [-buildLen,-1] elements are buffer
    // output: first buildLen elements are buffer, blocks 2 * buildLen and last subblock sorted
    private void sqrtBuildBlocks(int[] arr, int pos, int len, int buildLen, boolean auxwrite) {
        int extraDist, part;
        
        for(int dist = 1; dist < len; dist += 2) {
            extraDist = 0;
            if(Reads.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) extraDist = 1;
            
            Writes.write(arr, pos + dist - 3, arr[pos + dist - 1 + extraDist], 1, true, auxwrite);
            Writes.write(arr, pos + dist - 2, arr[pos + dist - extraDist], 1, true, auxwrite);
        }
        if(len % 2 != 0) Writes.write(arr, pos + len - 3, arr[pos + len - 1], 1, true, auxwrite);
        
        pos -= 2;
        
        for(part = 2; part < buildLen; part *= 2) {
            int left = 0;
            int right = len - 2 * part;
            
            while(left <= right) {
                this.sqrtMergeLeftWithXBuf(arr, pos + left, part, part, 0 - part, auxwrite);
                left += 2 * part;
            }
            
            int rest = len - left;

            if(rest > part) {
                this.sqrtMergeLeftWithXBuf(arr, pos + left, part, rest - part, 0 - part, auxwrite);
            } 
            else {
                while(left < len) Writes.write(arr, pos + left - part, arr[pos + left++], 1, true, auxwrite);
            }
            
            pos -= part;
        }
        int restToBuild = len % (2 * buildLen);
        int leftOverPos = len - restToBuild;
        
        if(restToBuild <= buildLen) {
            Writes.arraycopy(arr, pos + leftOverPos, arr, pos + leftOverPos + buildLen, restToBuild, 1, true, auxwrite);
        }
        else this.sqrtMergeRight(arr, pos + leftOverPos, buildLen, restToBuild - buildLen, buildLen, auxwrite);
        
        while(leftOverPos > 0) {
            leftOverPos -= 2 * buildLen;
            this.sqrtMergeRight(arr, pos + leftOverPos, buildLen, buildLen, buildLen, auxwrite);
        }
    }

    // keys are on the left of arr. Blocks of length buildLen combined. We'll combine them in pairs
    // buildLen and numKeys are powers of 2. (2 * buildLen / regBlockLen) keys are guaranteed
    private void sqrtCombineBlocks(int[] arr, int pos, int len, int buildLen, int regBlockLen, int[] tags, boolean auxwrite) {
        int combineLen = len / (2 * buildLen);
        int leftOver = len % (2 * buildLen);

        if(leftOver <= buildLen) {
            len -= leftOver;
            leftOver = 0;
        }
        
        int leftIndex = 0;
        
        for(int i = 0; i <= combineLen; i++) {
            if(i == combineLen && leftOver == 0) break;
            
            int blockPos = pos + i * 2 * buildLen;
            int blockCount = (i == combineLen ? leftOver : 2 * buildLen) / regBlockLen;
            
            int tagIndex = blockCount + (i == combineLen ? 1 : 0);
            for(int j = 0; j <= tagIndex; j++) Writes.write(tags, j, j, 1, true, true);
            
            int midkey = buildLen / regBlockLen;
            
            for(tagIndex = 1; tagIndex < blockCount; tagIndex++) {
                leftIndex = tagIndex - 1;
                
                for(int rightIndex = tagIndex; rightIndex < blockCount; rightIndex++) {
                    int rightComp = Reads.compare(arr[blockPos + leftIndex * regBlockLen],
                                            arr[blockPos + rightIndex * regBlockLen]);
                    if(rightComp > 0 || (rightComp == 0 && tags[leftIndex] > tags[rightIndex])) leftIndex = rightIndex;
                }
                
                if(leftIndex != tagIndex - 1) {
                    this.sqrtMultiSwap(arr, blockPos + (tagIndex - 1) * regBlockLen, blockPos + leftIndex * regBlockLen, regBlockLen, auxwrite);
                    this.sqrtSwap(tags, tagIndex - 1, leftIndex, true);
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
            this.sqrtMergeBuffersLeftWithXBuf(tags, midkey, arr, blockPos, blockCount - aBlockCount, regBlockLen, aBlockCount, lastLen, auxwrite);
        }
        for(leftIndex = len - 1; leftIndex >= 0; leftIndex--) Writes.write(arr, pos + leftIndex, arr[pos + leftIndex - regBlockLen], 1, true, auxwrite);
    }

    private void sqrtCommonSort(int[] arr, int pos, int len, int[] extBuf, int extBufPos, int[] tags, boolean auxwrite) {
        insertSorter = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        
        if(len <= 16) {
            this.sqrtInsertSort(arr, pos, len, auxwrite);
            Highlights.clearAllMarks();
            return;
        }
        
        int blockLen = 1;
        while((blockLen * blockLen) < len) blockLen *= 2;
        
        Writes.arraycopy(arr, pos, extBuf, extBufPos, blockLen, 1, true, auxwrite);
        
        this.sqrtCommonSort(extBuf, extBufPos, blockLen, arr, pos, tags, !auxwrite);
        
        this.sqrtBuildBlocks(arr, pos + blockLen, len - blockLen, blockLen, auxwrite);
        
        int buildLen = blockLen;
        
        while(len > (buildLen *= 2)) {
            this.sqrtCombineBlocks(arr, pos + blockLen, len - blockLen, buildLen, blockLen, tags, auxwrite);
        }
        this.sqrtMergeDown(arr, pos + blockLen, extBuf, extBufPos, len - blockLen, blockLen, auxwrite);
        
        Highlights.clearAllMarks();
    }

    @Override
    public void runSort(int[] array, int len, int bucketCount) {
        int bufferLen = 1;

        while(bufferLen * bufferLen < len) bufferLen *= 2;
        int numKeys = (len - 1) / bufferLen + 2;
        
        int[] extBuf = new int[bufferLen];        
        int[] tags = new int[numKeys];

        this.sqrtCommonSort(array, 0, len, extBuf, 0, tags, false);
    }
}