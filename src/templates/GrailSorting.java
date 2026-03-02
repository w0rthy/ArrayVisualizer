package templates;

import sorts.InsertionSort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
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
 *
 */

/********* Grail sorting *********************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* Refactored by MusicTheorist                           */
/*                                                       */
/* Stable sorting that works in O(N*log(N)) worst time   */
/* and uses O(1) extra memory                            */
/*                                                       */
/* Define int / SortComparator                           */
/* and then call GrailSort() function                    */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use GrailSortWithBuffer()                             */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use GrailSortWithDynBuffer()                          */
/*                                                       */
/*********************************************************/

final class GrailState {
    private int leftOverLen;
    private int leftOverFrag;
    
    protected GrailState(int len, int frag) {
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

public abstract class GrailSorting extends Sort {
    private InsertionSort insertSorter;
    
    final private int grailStaticBufferLen = 32; //Buffer length changed due to less numbers in this program being sorted than what Mr. Astrelin used for testing.
    
    protected GrailSorting(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
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
    
    private void grailRotate(int[] array, int pos, int lenA, int lenB) {
        while(lenA != 0 && lenB != 0) {
            if(lenA <= lenB) {
                this.grailMultiSwap(array, pos, pos + lenA, lenA);
                pos += lenA;
                lenB -= lenA;
            } 
            else {
                this.grailMultiSwap(array, pos + (lenA - lenB), pos + lenA, lenB);
                lenA -= lenB;
            }
        }
    }

    private void grailInsertSort(int[] arr, int pos, int len) {
        insertSorter.customInsertSort(arr, pos, len, 0.25, false);
    }

    //boolean argument determines direction
    private int grailBinSearch(int[] arr, int pos, int len, int keyPos, boolean isLeft) {
        int left = -1, right = len;
        while(left < right - 1) {
            int mid = left + ((right - left) >> 1);
            if(isLeft) {
                if(Reads.compare(arr[pos + mid], arr[keyPos]) >= 0) {
                    right = mid;
                } else {
                    left = mid;
                }
            } else {
                if(Reads.compare(arr[pos + mid], arr[keyPos]) > 0) {
                    right = mid;
                } else left = mid;
            }
            Highlights.markArray(1, pos + mid);
            Delays.sleep(0.025);
        }
        return right;
    }

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
                    GrailState results = this.grailSmartMergeWithBuffer(arr, pos + restToProcess, leftOverLen, leftOverFrag, blockLen);
                    leftOverLen = results.getLeftOverLen();
                    leftOverFrag = results.getLeftOverFrag();
                } else {
                    GrailState results = this.grailSmartMergeWithoutBuffer(arr, pos + restToProcess, leftOverLen, leftOverFrag, blockLen);
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
    private GrailState grailSmartMergeWithoutBuffer(int[] arr, int pos, int leftOverLen, int leftOverFrag, int regBlockLen) {
        if(regBlockLen == 0) return new GrailState(leftOverLen, leftOverFrag);

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
                    return new GrailState(len1, leftOverFrag);
                }
                do {
                    pos++;
                    len1--;
                } while(len1 != 0 && Reads.compare(arr[pos], arr[pos + len1]) - typeFrag < 0);
            }
        }
        return new GrailState(len2, typeFrag);
    }

    //returns the leftover length, then the leftover fragment
    private GrailState grailSmartMergeWithBuffer(int[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen) {
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
        return new GrailState(length, fragment);
    }


    /***** Sort With Extra Buffer *****/

    //returns the leftover length, then the leftover fragment
    private GrailState grailSmartMergeWithXBuf(int[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen) {
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
        return new GrailState(length, fragment);
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
                GrailState results = this.grailSmartMergeWithXBuf(arr, pos + restToProcess, leftOverLen, leftOverFrag, regBlockLen);
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
    private void grailBuildBlocks(int[] arr, int pos, int len, int buildLen, 
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
            for(int dist = 1; dist < len; dist += 2) {
                extraDist = 0;
                if(Reads.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) extraDist = 1;
                this.grailSwap(arr, pos + (dist - 3), pos + (dist - 1 + extraDist));
                this.grailSwap(arr, pos + (dist - 2), pos + (dist - extraDist));
            }
            if(len % 2 != 0) this.grailSwap(arr, pos + (len - 1), pos + (len - 3));
            pos -= 2;
            part = 2;
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
    }

    // keys are on the left of arr. Blocks of length buildLen combined. We'll combine them in pairs
    // buildLen and nkeys are powers of 2. (2 * buildLen / regBlockLen) keys are guaranteed
    private void grailCombineBlocks(int[] arr, int keyPos, int pos, int len, int buildLen,
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
            while(--len >= 0) {
                this.grailSwap(arr, pos + len, pos + len - regBlockLen);
            }
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
        insertSorter = new InsertionSort(this.Delays, this.Highlights, this.Reads, this.Writes);
        
        if(len <= 16) {
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

        if(bufferEnabled) {
            this.grailBuildBlocks(arr, pos + dist, len - dist, buildLen, buffer, bufferPos, bufferLen);
        }
        else {
            this.grailBuildBlocks(arr, pos + dist, len - dist, buildLen, null, bufferPos, 0);
        }

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
            this.grailCombineBlocks(arr, pos, pos + dist, len - dist, buildLen, regBlockLen, buildBufEnabled, 
                    buildBufEnabled && regBlockLen <= bufferLen ? buffer : null, bufferPos);
            
            Highlights.clearMark(2);
        }

        this.grailInsertSort(arr, pos, dist);
        this.grailMergeWithoutBuffer(arr, pos, dist, len - dist);    
    }
    
    private void grailInPlaceMerge(int[] arr, int pos, int len1, int len2) {
        if(len1 < 3 || len2 < 3) {
            this.grailMergeWithoutBuffer(arr, pos, len1, len2);
            return;
        }

        int midpoint;
        if(len1 < len2) midpoint = len1 + len2 / 2;
        else midpoint = len1 / 2;

        //Left binary search
        int len1Left, len1Right;
        len1Left = len1Right = this.grailBinSearch(arr, pos, len1, pos + midpoint, true);

        //Right binary search
        if(len1Right < len1 && Reads.compare(arr[pos + len1Right], arr[pos + midpoint]) == 0) {
            len1Right = this.grailBinSearch(arr, pos + len1Left, len1 - len1Left, pos + midpoint, false) + len1Left;
        }

        int len2Left, len2Right;
        len2Left = len2Right = this.grailBinSearch(arr, pos + len1, len2, pos + midpoint, true);

        if(len2Right < len2 && Reads.compare(arr[pos + len1 + len2Right], arr[pos + midpoint]) == 0) {
            len2Right = this.grailBinSearch(arr, pos + len1 + len2Left, len2 - len2Left, pos + midpoint, false) + len2Left;
        }

        if(len1Left == len1Right) this.grailRotate(arr, pos + len1Right, len1 - len1Right, len2Right);
        else {
            this.grailRotate(arr, pos + len1Left, len1 - len1Left, len2Left);

            if(len2Right != len2Left) {
                this.grailRotate(arr, pos + (len1Right + len2Left), len1 - len1Right, len2Right - len2Left);
            }
        }

        this.grailInPlaceMerge(arr, pos + (len1Right + len2Right), len1 - len1Right, len2 - len2Right);
        this.grailInPlaceMerge(arr, pos, len1Left, len2Left);
    }
    protected void grailInPlaceMergeSort(int[] arr, int start, int len) {
        for(int dist = start + 1; dist < len; dist += 2) {
            if(Reads.compare(arr[dist - 1], arr[dist]) > 0) this.grailSwap(arr, dist - 1, dist);
        }
        for(int part = 2; part < len; part *= 2) {
            int left = start, right = len - 2 * part;

            while(left <= right) {
                this.grailInPlaceMerge(arr, left, part, part);
                left += 2 * part;
            }

            int rest = len - left;
            if(rest > part) this.grailInPlaceMerge(arr, left, part, rest - part);
        }
    }
}
