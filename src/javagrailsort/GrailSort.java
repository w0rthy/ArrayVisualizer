package javagrailsort;

import static javagrailsort.Tester.swaps;

/********* Grail sorting *********************************/
/*                                                       */
/* (c) 2013 by Andrey Astrelin                           */
/* Refactored by MusicTheorist                           */
/*                                                       */
/* Stable sorting that works in O(N*log(N)) worst time   */
/* and uses O(1) extra memory                            */
/*                                                       */
/* Define SortType / SortComparator                      */
/* and then call GrailSort() function                    */
/*                                                       */
/* For sorting w/ fixed external buffer (512 items)      */
/* use GrailSortWithBuffer()                             */
/*                                                       */
/* For sorting w/ dynamic external buffer (sqrt(length)) */
/* use GrailSortWithDynBuffer()                          */
/*                                                       */
/*********************************************************/

public class GrailSort {

	final private static int grailStaticBufferLen = 512;

	private static SortComparator grail;

	private static void grailSwap(SortType[] arr, int a, int b) {	
		swaps++;
		SortType temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}

	private static void grailMultiSwap(SortType[] arr, int a, int b, int swapsLeft) {
		while(swapsLeft != 0) { 
			grailSwap(arr, a++, b++);
			swapsLeft--;
		}
	}

	private static void grailRotate(SortType[] array, int pos, int lenA, int lenB) {
		while(lenA != 0 && lenB != 0) {
			if(lenA <= lenB) {
				grailMultiSwap(array, pos, pos + lenA, lenA);
				pos += lenA;
				lenB -= lenA;
			} else {
				grailMultiSwap(array, pos + (lenA - lenB), pos + lenA, lenB);
				lenA -= lenB;
			}
		}
	}

	private static void grailInsertSort(SortType[] arr, int pos, int len) {
		for(int i = 1; i < len; i++) {
			int dist = pos + i - 1; SortType item = arr[pos + i];
			while((dist - pos) >= 0 && grail.compare(arr[dist], item) > 0) {
				arr[dist + 1] = arr[dist];
				dist--;
			}
			arr[dist + 1] = item;
		}
	}

	//boolean argument determines direction
	private static int grailBinSearch(SortType[] arr, int pos, int len, int keyPos, boolean isLeft) {
		int left = -1, right = len;
		while(left < right - 1) {
			int mid = left + ((right - left) >> 1);
			if(isLeft) {
				if(grail.compare(arr[pos + mid], arr[keyPos]) >= 0) {
					right = mid;
				} else {
					left = mid;
				}
			} else {
				if(grail.compare(arr[pos + mid], arr[keyPos]) > 0) {
					right = mid;
				} else left = mid;
			}
		}
		return right;
	}

	// cost: 2 * len + numKeys^2 / 2
	private static int grailFindKeys(SortType[] arr, int pos, int len, int numKeys) {
		int dist = 1, foundKeys = 1, firstKey = 0;  // first key is always here

		while(dist < len && foundKeys < numKeys) {
			//Binary Search left
			int loc = grailBinSearch(arr, pos + firstKey, foundKeys, pos + dist, true);
			if(loc == foundKeys || grail.compare(arr[pos + dist], arr[pos + (firstKey + loc)]) != 0) {
				grailRotate(arr, pos + firstKey, foundKeys, dist - (firstKey + foundKeys));
				firstKey = dist - foundKeys;
				grailRotate(arr, pos + (firstKey + loc), foundKeys - loc, 1);
				foundKeys++;
			}
			dist++;
		}
		grailRotate(arr, pos, firstKey, foundKeys);
		return foundKeys;
	}

	// cost: min(len1, len2)^2 + max(len1, len2)
	private static void grailMergeWithoutBuffer(SortType[] arr, int pos, int len1, int len2) {
		if(len1 < len2) {
			while(len1 != 0) {
				//Binary Search left
				int loc = grailBinSearch(arr, pos + len1, len2, pos, true);
				if(loc != 0) {
					grailRotate(arr, pos, len1, loc);
					pos += loc;
					len2 -= loc;
				}
				if(len2 == 0) break;
				do {
					pos++;
					len1--;
				} while(len1 != 0 && grail.compare(arr[pos], arr[pos + len1]) <= 0);
			}
		} else {
			while(len2 != 0) {
				//Binary Search right
				int loc = grailBinSearch(arr, pos, len1, pos + (len1 + len2 - 1), false);
				if(loc != len1) {
					grailRotate(arr, pos + loc, len1 - loc, len2);
					len1 = loc;
				}
				if(len1 == 0) break;
				do {
					len2--;
				} while(len2 != 0 && grail.compare(arr[pos + len1 - 1], arr[pos + len1 + len2 - 1]) <= 0);
			}
		}
	}

	// arr - starting array. arr[0 - regBlockLen..-1] - buffer (if havebuf).
	// regBlockLen - length of regular blocks. First blockCount blocks are stable sorted by 1st elements and key-coded
	// keysPos - arrays of keys, in same order as blocks. keysPos < midkey means stream A
	// aBlockCount are regular blocks from stream A.
	// lastLen is length of last (irregular) block from stream B, that should go before nblock2 blocks.
	// lastLen = 0 requires aBlockCount = 0 (no irregular blocks). lastLen > 0, aBlockCount = 0 is possible.
	private static void grailMergeBuffersLeft(SortType[] arr, int keysPos, int midkey, int pos, 
											  int blockCount, int blockLen, boolean havebuf, int aBlockCount, 
							                  int lastLen) {

		if(blockCount == 0) {
			int aBlocksLen = aBlockCount * blockLen;
			if(havebuf) grailMergeLeft(arr, pos, aBlocksLen, lastLen, 0 - blockLen);
			else grailMergeWithoutBuffer(arr, pos, aBlocksLen, lastLen);
			return;
		}

		int leftOverLen = blockLen;
		int leftOverFrag = grail.compare(arr[keysPos], arr[midkey]) < 0 ? 0 : 1;
		int processIndex = blockLen;
		int restToProcess;

		for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, processIndex += blockLen) {
			restToProcess = processIndex - leftOverLen;
			int nextFrag = grail.compare(arr[keysPos + keyIndex], arr[midkey]) < 0 ? 0 : 1;

			if(nextFrag == leftOverFrag) {
				if(havebuf) grailMultiSwap(arr, pos + restToProcess - blockLen, pos + restToProcess, leftOverLen);
				restToProcess = processIndex;
				leftOverLen = blockLen;
			} else {
				if(havebuf) {
					int[] grailState = grailSmartMergeWithBuffer(arr, pos + restToProcess, leftOverLen, leftOverFrag, blockLen);
					leftOverLen = grailState[0];
					leftOverFrag = grailState[1];
				} else {
					int[] grailState = grailSmartMergeWithoutBuffer(arr, pos + restToProcess, leftOverLen, leftOverFrag, blockLen);
					leftOverLen = grailState[0];
					leftOverFrag = grailState[1];
				}
			}
		}
		restToProcess = processIndex - leftOverLen;

		if(lastLen != 0) {
			if(leftOverFrag != 0) {
				if(havebuf) grailMultiSwap(arr, pos + restToProcess - blockLen, pos + restToProcess, leftOverLen);
				restToProcess = processIndex;
				leftOverLen = blockLen * aBlockCount;
				leftOverFrag = 0;
			} else {
				leftOverLen += blockLen * aBlockCount;
			}
			if(havebuf) grailMergeLeft(arr, pos + restToProcess, leftOverLen, lastLen, -blockLen);
			else grailMergeWithoutBuffer(arr, pos + restToProcess, leftOverLen, lastLen);
		} else {
			if(havebuf) grailMultiSwap(arr, pos + restToProcess, pos + (restToProcess - blockLen), leftOverLen);
		}
	}

	// arr[dist..-1] - buffer, arr[0, leftLen - 1] ++ arr[leftLen, leftLen + rightLen - 1]
	// -> arr[dist, dist + leftLen + rightLen - 1]
	private static void grailMergeLeft(SortType[] arr, int pos, int leftLen, int rightLen, int dist) {
		int left = 0;
		int right = leftLen;

		rightLen += leftLen;

		while(right < rightLen) {
			if(left == leftLen || grail.compare(arr[pos + left], arr[pos + right]) > 0) {
				grailSwap(arr, pos + (dist++), pos + (right++));
			} else {
				grailSwap(arr, pos + (dist++), pos + (left++));
			}
		}
		if(dist != left) grailMultiSwap(arr, pos + dist, pos + left, leftLen - left);
	}
	private static void grailMergeRight(SortType[] arr, int pos, int leftLen, int rightLen, int dist) {
		int mergedPos = leftLen + rightLen + dist - 1;
		int right = leftLen + rightLen - 1;
		int left = leftLen - 1;

		while(left >= 0) {
			if(right < leftLen || grail.compare(arr[pos + left], arr[pos + right]) > 0) {
				grailSwap(arr, pos + (mergedPos--), pos + (left--));
			} else {
				grailSwap(arr, pos + (mergedPos--), pos + (right--));
			}
		}
		if(right != mergedPos) {
			while(right >= leftLen) grailSwap(arr, pos + (mergedPos--), pos + (right--));
		}
	}

	//returns the leftover length, then the leftover fragment
	private static int[] grailSmartMergeWithoutBuffer(SortType[] arr, int pos, int leftOverLen, int leftOverFrag, int regBlockLen) {
		if(regBlockLen == 0) return new int[] {leftOverLen, leftOverFrag};

		int len1 = leftOverLen;
		int len2 = regBlockLen;
		int typeFrag = 1 - leftOverFrag; //1 if inverted

		if(len1 != 0 && grail.compare(arr[pos + (len1 - 1)], arr[pos + len1]) - typeFrag >= 0) {
			
			while(len1 != 0) {
				int foundLen;
				if (typeFrag != 0) {
					//Binary Search left
					foundLen = grailBinSearch(arr, pos + len1, len2, pos, true);
				} else { 
					//Binary Search right
					foundLen = grailBinSearch(arr, pos + len1, len2, pos, false);
				}
				if(foundLen != 0) {
					grailRotate(arr, pos, len1, foundLen);
					pos += foundLen;
					len2 -= foundLen;
				}
				if(len2 == 0) {
					return new int[] {len1, leftOverFrag};
				}
				do {
					pos++;
					len1--;
				} while(len1 != 0 && grail.compare(arr[pos], arr[pos + len1]) - typeFrag < 0);
			}
		}
		return new int[] {len2, typeFrag};
	}

	//returns the leftover length, then the leftover fragment
	private static int[] grailSmartMergeWithBuffer(SortType[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen) {
		int dist = 0 - blockLen, left = 0, right = leftOverLen, leftEnd = right, rightEnd = right + blockLen;
		int typeFrag = 1 - leftOverFrag;  // 1 if inverted

		while(left < leftEnd && right < rightEnd) {
			if(grail.compare(arr[pos + left], arr[pos + right]) - typeFrag < 0) {
				grailSwap(arr, pos + (dist++), pos + (left++));
			}
			else grailSwap(arr, pos + (dist++), pos + (right++));
		}
		int length, fragment = leftOverFrag;
		if(left < leftEnd) {
			length = leftEnd - left;
			while(left < leftEnd) grailSwap(arr, pos + (--leftEnd), pos + (--rightEnd));
		} else {
			length = rightEnd - right;
			fragment = typeFrag;
		}
		return new int[] {length, fragment};
	}


	/***** Sort With Extra Buffer *****/

	//returns the leftover length, then the leftover fragment
	private static int[] grailSmartMergeWithXBuf(SortType[] arr, int pos, int leftOverLen, int leftOverFrag, int blockLen) {
		int dist = 0 - blockLen, left = 0, right = leftOverLen, leftEnd = right, rightEnd = right + blockLen;
		int typeFrag = 1 - leftOverFrag;  // 1 if inverted

		while(left < leftEnd && right < rightEnd) {
			if(grail.compare(arr[pos + left], arr[pos + right]) - typeFrag < 0) {
				arr[pos + (dist++)] = arr[pos + (left++)];
			}
			else arr[pos + (dist++)] = arr[pos + (right++)];
		}
		int length, fragment = leftOverFrag;
		if(left < leftEnd) {
			length = leftEnd - left;
			while(left < leftEnd) arr[pos + (--rightEnd)] = arr[pos + (--leftEnd)];
		} else {
			length = rightEnd - right;
			fragment = typeFrag;
		}
		return new int[] {length, fragment};
	}

	// arr[dist..-1] - free, arr[0, leftEnd - 1] ++ arr[leftEnd, leftEnd + rightEnd - 1]
	// -> arr[dist, dist + leftEnd + rightEnd - 1]
	private static void grailMergeLeftWithXBuf(SortType[] arr, int pos, int leftEnd, int rightEnd, int dist) {
		int left = 0;
		int right = leftEnd;
		rightEnd += leftEnd;

		while(right < rightEnd) {
			if(left == leftEnd || grail.compare(arr[pos + left], arr[pos + right]) > 0) {
				arr[pos + (dist++)] = arr[pos + (right++)];
			}
			else arr[pos + (dist++)] = arr[pos + (left++)];
		}
		if(dist != left) {
			while(left < leftEnd) arr[pos + (dist++)] = arr[pos + (left++)];
		}
	}

	// arr - starting array. arr[0 - regBlockLen..-1] - buffer (if havebuf).
	// regBlockLen - length of regular blocks. First blockCount blocks are stable sorted by 1st elements and key-coded
	// keysPos - where keys are in array, in same order as blocks. keysPos < midkey means stream A
	// aBlockCount are regular blocks from stream A.
	// lastLen is length of last (irregular) block from stream B, that should go before aCountBlock blocks.
	// lastLen = 0 requires aBlockCount = 0 (no irregular blocks). lastLen > 0, aBlockCount = 0 is possible.
	private static void grailMergeBuffersLeftWithXBuf(SortType[] arr, int keysPos, int midkey, int pos,
													  int blockCount, int regBlockLen, int aBlockCount, int lastLen) {

		if(blockCount == 0) {
			int aBlocksLen = aBlockCount * regBlockLen;
			grailMergeLeftWithXBuf(arr, pos, aBlocksLen, lastLen, 0 - regBlockLen);
			return;
		}

		int leftOverLen = regBlockLen;
		int leftOverFrag = grail.compare(arr[keysPos], arr[midkey]) < 0 ? 0 : 1;
		int processIndex = regBlockLen;

		int restToProcess;
		for(int keyIndex = 1; keyIndex < blockCount; keyIndex++, processIndex += regBlockLen) {
			restToProcess = processIndex - leftOverLen;
			int nextFrag = grail.compare(arr[keysPos + keyIndex], arr[midkey]) < 0 ? 0 : 1;

			if(nextFrag == leftOverFrag) {
				System.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen);
				restToProcess = processIndex;
				leftOverLen = regBlockLen;
			} else {
				int[] grailState = grailSmartMergeWithXBuf(arr, pos + restToProcess, leftOverLen, leftOverFrag, regBlockLen);
				leftOverLen = grailState[0]; 
				leftOverFrag = grailState[1];
			}
		}
		restToProcess = processIndex - leftOverLen;

		if(lastLen != 0) {
			if(leftOverFrag != 0) {
				System.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen);
				restToProcess = processIndex;
				leftOverLen = regBlockLen * aBlockCount;
				leftOverFrag = 0;
			} else {
				leftOverLen += regBlockLen * aBlockCount;
			}
			grailMergeLeftWithXBuf(arr, pos + restToProcess, leftOverLen, lastLen, 0 - regBlockLen);
		} else {
			System.arraycopy(arr, pos + restToProcess, arr, pos + restToProcess - regBlockLen, leftOverLen);
		}
	}

	/***** End Sort With Extra Buffer *****/

	// build blocks of length buildLen
	// input: [-buildLen, -1] elements are buffer
	// output: first buildLen elements are buffer, blocks 2 * buildLen and last subblock sorted
	private static void grailBuildBlocks(SortType[] arr, int pos, int len, int buildLen, 
										 SortType[] extbuf, int bufferPos, int extBufLen) {

		int buildBuf = buildLen < extBufLen ? buildLen : extBufLen;
		while((buildBuf & (buildBuf - 1)) != 0) buildBuf &= buildBuf - 1;  // max power or 2 - just in case

		int extraDist, part;
		if(buildBuf != 0) {
			System.arraycopy(arr, pos - buildBuf, extbuf, bufferPos, buildBuf);
			for(int dist = 1; dist < len; dist += 2) {
				extraDist = 0;
				if(grail.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) extraDist = 1;
				arr[pos + dist - 3] = arr[pos + dist - 1 + extraDist];
				arr[pos + dist - 2] = arr[pos + dist - extraDist];
			}
			if(len % 2 != 0) arr[pos + len - 3] = arr[pos + len - 1];
			pos -= 2;

			for(part = 2; part < buildBuf; part *= 2) {
				int left = 0;
				int right = len - 2 * part;
				while(left <= right) {
					grailMergeLeftWithXBuf(arr, pos + left, part, part, 0 - part);
					left += 2 * part;
				}
				int rest = len - left;

				if(rest > part) {
					grailMergeLeftWithXBuf(arr, pos + left, part, rest - part, 0 - part);
				} else {
					for(; left < len; left++)	arr[pos + left - part] = arr[pos + left];
				}
				pos -= part;
			}
			System.arraycopy(extbuf, bufferPos, arr, pos + len, buildBuf);
		} else {
			for(int dist = 1; dist < len; dist += 2) {
				extraDist = 0;
				if(grail.compare(arr[pos + (dist - 1)], arr[pos + dist]) > 0) extraDist = 1;
				grailSwap(arr, pos + (dist - 3), pos + (dist - 1 + extraDist));
				grailSwap(arr, pos + (dist - 2), pos + (dist - extraDist));
			}
			if(len % 2 != 0) grailSwap(arr, pos + (len - 1), pos + (len - 3));
			pos -= 2;
			part = 2;
		}

		for(; part < buildLen; part *= 2) {
			int left = 0;
			int right = len - 2 * part;
			while(left <= right) {
				grailMergeLeft(arr, pos + left, part, part, 0 - part);
				left += 2 * part;
			}
			int rest = len - left;
			if(rest > part) {
				grailMergeLeft(arr, pos + left, part, rest - part, 0 - part);
			} else {
				grailRotate(arr, pos + left - part, part, rest);
			}
			pos -= part;
		}
		int restToBuild = len % (2 * buildLen);
		int leftOverPos = len - restToBuild;

		if(restToBuild <= buildLen) grailRotate(arr, pos + leftOverPos, restToBuild, buildLen);
		else grailMergeRight(arr, pos + leftOverPos, buildLen, restToBuild - buildLen, buildLen);

		while(leftOverPos > 0) {
			leftOverPos -= 2 * buildLen;
			grailMergeRight(arr, pos + leftOverPos, buildLen, buildLen, buildLen);
		}
	}

	// keys are on the left of arr. Blocks of length buildLen combined. We'll combine them in pairs
	// buildLen and nkeys are powers of 2. (2 * buildLen / regBlockLen) keys are guaranteed
	private static void grailCombineBlocks(SortType[] arr, int keyPos, int pos, int len, int buildLen,
										              int regBlockLen, boolean havebuf, SortType[] buffer, int bufferPos) {

		int combineLen = len / (2 * buildLen);
		int leftOver = len % (2 * buildLen);
		if(leftOver <= buildLen) {
			len -= leftOver;
			leftOver = 0;
		}

		if(buffer != null) System.arraycopy(arr, pos - regBlockLen, buffer, bufferPos, regBlockLen);

		for(int i = 0; i <= combineLen; i++) {
			if(i == combineLen && leftOver == 0) break;

			int blockPos = pos + i * 2 * buildLen;
			int blockCount = (i == combineLen ? leftOver : 2 * buildLen) / regBlockLen;

			grailInsertSort(arr, keyPos, blockCount + (i == combineLen ? 1 : 0));

			int midkey = buildLen / regBlockLen;

			for(int index = 1; index < blockCount; index++) {
				int leftIndex = index - 1;

				for(int rightIndex = index; rightIndex < blockCount; rightIndex++) {
					int rightComp = grail.compare(arr[blockPos + leftIndex * regBlockLen],
									              arr[blockPos + rightIndex * regBlockLen]);
					if(rightComp > 0 || (rightComp == 0 && grail.compare(arr[keyPos + leftIndex], arr[keyPos + rightIndex]) > 0)) {
						leftIndex = rightIndex;
					}
				}
				if(leftIndex != index - 1) {
					grailMultiSwap(arr, blockPos + (index - 1) * regBlockLen, blockPos + leftIndex * regBlockLen, regBlockLen);
					grailSwap(arr, keyPos + (index - 1), keyPos + leftIndex);
					if(midkey == index - 1 || midkey == leftIndex) {
						midkey ^= (index - 1) ^ leftIndex;
					}
				}
			}

			int aBlockCount = 0;
			int lastLen = 0;
			if(i == combineLen) lastLen = leftOver % regBlockLen;

			if(lastLen != 0) {
				while(aBlockCount < blockCount && grail.compare(arr[blockPos + blockCount * regBlockLen],
						arr[blockPos + (blockCount - aBlockCount - 1) * regBlockLen]) < 0) {
					aBlockCount++;
				}
			}

			if(buffer != null) {
				grailMergeBuffersLeftWithXBuf(arr, keyPos, keyPos + midkey, blockPos,
						blockCount - aBlockCount, regBlockLen, aBlockCount, lastLen);
			}
			else grailMergeBuffersLeft(arr, keyPos, keyPos + midkey, blockPos, 
					blockCount - aBlockCount, regBlockLen, havebuf, aBlockCount, lastLen);
		}
		if(buffer != null) {
			for(int i = len; --i >= 0;) arr[pos + i] = arr[pos + i - regBlockLen];
			System.arraycopy(buffer, bufferPos, arr, pos - regBlockLen, regBlockLen);
		}
		else if(havebuf) {
			while(--len >= 0) grailSwap(arr, pos + len, pos + len - regBlockLen);
		}
	}

	private static void grailLazyStableSort(SortType[] arr, int pos, int len) {
		for(int dist = 1; dist < len; dist += 2) {
			if(grail.compare(arr[pos + dist - 1], arr[pos + dist]) > 0) {
				grailSwap(arr, pos + (dist - 1), pos + dist);
			}
		}

		for(int part = 2; part < len; part *= 2) {
			int left = 0;
			int right = len - 2 * part;

			while(left <= right) {
				grailMergeWithoutBuffer(arr, pos + left, part, part);
				left += 2 * part;
			}

			int rest = len - left;
			if(rest > part) {
				grailMergeWithoutBuffer(arr, pos + left, part, rest - part);
			}
		}
	}

	private static void grailCommonSort(SortType[] arr, int pos, int len, 
			SortType[] buffer, int bufferPos, int bufferLen) {

		int blockLen = 1;
		if(len < 16) {
			grailInsertSort(arr, pos, len);
			return;
		}

		while(blockLen * blockLen < len) blockLen *= 2;		
		int numKeys = (len - 1) / blockLen + 1;
		int keysFound = grailFindKeys(arr, pos, len, numKeys + blockLen);
		boolean bufferEnabled = true;

		if(keysFound < numKeys + blockLen) {
			if(keysFound < 4) {
				grailLazyStableSort(arr, pos, len);
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
			grailBuildBlocks(arr, pos + dist, len - dist, buildLen, buffer, bufferPos, bufferLen);
		}
		else {
			grailBuildBlocks(arr, pos + dist, len - dist, buildLen, null, bufferPos, 0);
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
			grailCombineBlocks(arr, pos, pos + dist, len - dist, buildLen, regBlockLen, buildBufEnabled, 
					buildBufEnabled && regBlockLen <= bufferLen ? buffer : null, bufferPos);
		}

		grailInsertSort(arr, pos, dist);
		grailMergeWithoutBuffer(arr, pos, dist, len - dist);
	}

	public static void grailSortWithoutBuffer(SortType[] arr) {
		grail = new SortComparator();
		grailCommonSort(arr, 0, arr.length, null, 0, 0);
	}
	public static void grailSortWithoutBuffer(SortType[] arr, SortComparator cmp) {
		grail = cmp;
		grailCommonSort(arr, 0, arr.length, null, 0, 0);
	}

	public static void grailSortWithBuffer(SortType[] arr) {
		grail = new SortComparator();
		SortType[] ExtBuf = new SortType[grailStaticBufferLen];
		grailCommonSort(arr, 0, arr.length, ExtBuf, 0, grailStaticBufferLen);
	}
	public static void grailSortWithBuffer(SortType[] arr, SortComparator cmp) {
		grail = cmp;
		SortType[] ExtBuf = new SortType[grailStaticBufferLen];
		grailCommonSort(arr, 0, arr.length, ExtBuf, 0, grailStaticBufferLen);
	}

	public static void grailSortWithDynBuffer(SortType[] arr) {
		grail = new SortComparator();
		int tempLen = 1;
		while(tempLen * tempLen < arr.length) tempLen *= 2;
		SortType[] ExtBuf = new SortType[tempLen];
		grailCommonSort(arr, 0, arr.length, ExtBuf, 0, tempLen);
	}
	public static void grailSortWithDynBuffer(SortType[] arr, SortComparator cmp) {
		grail = cmp;
		int tempLen = 1;
		while(tempLen * tempLen < arr.length) tempLen *= 2;
		SortType[] ExtBuf = new SortType[tempLen];
		grailCommonSort(arr, 0, arr.length, ExtBuf, 0, tempLen);
	}
}