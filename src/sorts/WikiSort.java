package sorts;

import static array.visualizer.ArrayVisualizer.compare;
import static array.visualizer.Writes.swap;
import static array.visualizer.Writes.write;

/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>
*/

public class WikiSort {	

	// structure to represent ranges within the array
	static class Range {
		private int start;
		private int end;

		public Range(int start1, int end1) {
			start = start1;
			end = end1;
		}

		public Range() {
			start = 0;
			end = 0;
		}

		private void set(int start1, int end1) {
			start = start1;
			end = end1;
		}

		private int length() {
			return end - start;
		}
	}

	static class Pull {
		private int from, to, count;
		private Range range;
		public Pull() { range = new Range(0, 0); }
		private void reset() {
			range.set(0, 0);
			from = 0;
			to = 0;
			count = 0;
		}
	}

	// calculate how to scale the index value to the range within the array
	// the bottom-up merge sort only operates on values that are powers of two,
	// so scale down to that power of two, then use a fraction to scale back again
	static class Iterator {
		private int size, power_of_two;
		private int numerator, decimal;
		private int denominator, decimal_step, numerator_step;

		// 63 -> 32, 64 -> 64, etc.
		// this comes from Hacker's Delight
		private static int FloorPowerOfTwo(int value) {
			int x = value;
			x = x | (x >> 1);
			x = x | (x >> 2);
			x = x | (x >> 4);
			x = x | (x >> 8);
			x = x | (x >> 16);
			return x - (x >> 1);
		}

		public Iterator(int size2, int min_level) {
			size = size2;
			power_of_two = FloorPowerOfTwo(size);
			denominator = power_of_two/min_level;
			numerator_step = size % denominator;
			decimal_step = size/denominator;
			begin();
		}

		private void begin() {
			numerator = decimal = 0;
		}

		private Range nextRange() {
			int start = decimal;

			decimal += decimal_step;
			numerator += numerator_step;
			if (numerator >= denominator) {
				numerator -= denominator;
				decimal++;
			}

			return new Range(start, decimal);
		}

		private boolean finished() {
			return (decimal >= size);
		}

		private boolean nextLevel() {
			decimal_step += decimal_step;
			numerator_step += numerator_step;
			if (numerator_step >= denominator) {
				numerator_step -= denominator;
				decimal_step++;
			}

			return (decimal_step < size);
		}

		private int length() {
			return decimal_step;
		}
	}

	static class WikiSorter {
		// use a small cache to speed up some of the operations
		// since the cache size is fixed, it's still O(1) memory!
		// just keep in mind that making it too small ruins the point (nothing will fit into it),
		// and making it too large also ruins the point (so much for "low memory"!)
		// note that you can easily modify the above to allocate a dynamically sized cache
		// good choices for the cache size are:
		// (size + 1)/2 – turns into a full-speed standard merge sort since everything fits into the cache
		// sqrt((size + 1)/2) + 1 – this will be the size of the A blocks at the largest level of merges,
		// so a buffer of this size would allow it to skip using internal or in-place merges for anything
		// 512 – chosen from careful testing as a good balance between fixed-size memory use and run time
		// 0 – if the system simply cannot allocate any extra memory whatsoever, no memory works just fine

		// toolbox functions used by the sorter

		// find the index of the first value within the range that is equal to array[index]
		private int BinaryFirst(int[] array, int value, Range range) {
			int start = range.start, end = range.end - 1;
			while (start < end) {
				int mid = start + (end - start)/2;
				if (compare(array[mid], value) < 0)
					start = mid + 1;
				else
					end = mid;
			}
			if (start == range.end - 1 && compare(array[start], value) < 0) start++;
			return start;
		}

		// find the index of the last value within the range that is equal to array[index], plus 1
		private int BinaryLast(int[] array, int value, Range range) {
			int start = range.start, end = range.end - 1;
			while (start < end) {
				int mid = start + (end - start)/2;
				if (compare(value, array[mid]) >= 0)
					start = mid + 1;
				else
					end = mid;
			}
			if (start == range.end - 1 && compare(value, array[start]) >= 0) start++;
			return start;
		}

		// combine a linear search with a binary search to reduce the number of comparisons in situations
		// where have some idea as to how many unique values there are and where the next value might be
		private int FindFirstForward(int[] array, int value, Range range, int unique) {
			if (range.length() == 0) return range.start;
			int index, skip = Math.max(range.length()/unique, 1);

			for (index = range.start + skip; compare(array[index - 1], value) < 0; index += skip)
				if (index >= range.end - skip)
					return BinaryFirst(array, value, new Range(index, range.end));

			return BinaryFirst(array, value, new Range(index - skip, index));
		}

		private int FindLastForward(int[] array, int value, Range range, int unique) {
			if (range.length() == 0) return range.start;
			int index, skip = Math.max(range.length()/unique, 1);

			for (index = range.start + skip; compare(value, array[index - 1]) >= 0; index += skip)
				if (index >= range.end - skip)
					return BinaryLast(array, value, new Range(index, range.end));

			return BinaryLast(array, value, new Range(index - skip, index));
		}

		private int FindFirstBackward(int[] array, int value, Range range, int unique) {
			if (range.length() == 0) return range.start;
			int index, skip = Math.max(range.length()/unique, 1);

			for (index = range.end - skip; index > range.start && compare(array[index - 1], value) >= 0; index -= skip)
				if (index < range.start + skip)
					return BinaryFirst(array, value, new Range(range.start, index));

			return BinaryFirst(array, value, new Range(index, index + skip));
		}

		private int FindLastBackward(int[] array, int value, Range range, int unique) {
			if (range.length() == 0) return range.start;
			int index, skip = Math.max(range.length()/unique, 1);

			for (index = range.end - skip; index > range.start && compare(value, array[index - 1]) < 0; index -= skip)
				if (index < range.start + skip)
					return BinaryLast(array, value, new Range(range.start, index));

			return BinaryLast(array, value, new Range(index, index + skip));
		}

		// n^2 sorting algorithm used to sort tiny chunks of the full array
		private void InsertionSort(int[] array, Range range) {
			for (int j, i = range.start + 1; i < range.end; i++) {
				int temp = array[i];
				for (j = i; j > range.start && compare(temp, array[j - 1]) < 0; j--)
					write(array, j, array[j - 1], 1, true, false);
				write(array, j, temp, 1, true, false);
			}
		}

		// reverse a range of values within the array
		private void Reverse(int[] array, Range range) {
			for (int index = range.length()/2 - 1; index >= 0; index--) {
				swap(array, range.start + index, range.end - index - 1, 1, true);
			}
		}

		// swap a series of values in the array
		private void BlockSwap(int[] array, int start1, int start2, int block_size) {
			for (int index = 0; index < block_size; index++) {
				swap(array, start1 + index, start2 + index, 1, true);
			}
		}

		// rotate the values in an array ([0 1 2 3] becomes [1 2 3 0] if we rotate by 1)
		// this assumes that 0 <= amount <= range.length()
		private void Rotate(int[] array, int amount, Range range) {
			if (range.length() == 0) return;
			
			int split;
			if (amount >= 0)
				split = range.start + amount;
			else
				split = range.end + amount;
			
			Range range1 = new Range(range.start, split);
			Range range2 = new Range(split, range.end);
			
			Reverse(array, range1);
			Reverse(array, range2);
			Reverse(array, range);
		}

		// merge operation using an internal buffer
		private void MergeInternal(int[] array, Range A, Range B, Range buffer) {
			// whenever we find a value to add to the final array, swap it with the value that's already in that spot
			// when this algorithm is finished, 'buffer' will contain its original contents, but in a different order
			int A_count = 0, B_count = 0, insert = 0;

			if (B.length() > 0 && A.length() > 0) {
				while (true) {
					if (compare(array[B.start + B_count], array[buffer.start + A_count]) >= 0) {
						swap(array, A.start + insert, buffer.start + A_count, 1, true);
						A_count++;
						insert++;
						if (A_count >= A.length()) break;
					} else {
						swap(array, A.start + insert, B.start + B_count, 1, true);
						B_count++;
						insert++;
						if (B_count >= B.length()) break;
					}
				}
			}

			// swap the remainder of A into the final array
			BlockSwap(array, buffer.start + A_count, A.start + insert, A.length() - A_count);
		}

		// merge operation without a buffer
		private void MergeInPlace(int[] array, Range A, Range B) {
			if (A.length() == 0 || B.length() == 0) return;

			/*
		 this just repeatedly binary searches into B and rotates A into position.
		 the paper suggests using the 'rotation-based Hwang and Lin algorithm' here,
		 but I decided to stick with this because it had better situational performance

		 (Hwang and Lin is designed for merging subarrays of very different sizes,
		 but WikiSort almost always uses subarrays that are roughly the same size)

		 normally this is incredibly suboptimal, but this function is only called
		 when none of the A or B blocks in any subarray contained 2√A unique values,
		 which places a hard limit on the number of times this will ACTUALLY need
		 to binary search and rotate.

		 according to my analysis the worst case is √A rotations performed on √A items
		 once the constant factors are removed, which ends up being O(n)

		 again, this is NOT a general-purpose solution – it only works well in this case!
		 kind of like how the O(n^2) insertion sort is used in some places
			 */

			A = new Range(A.start, A.end);
			B = new Range(B.start, B.end);

			while (true) {
				// find the first place in B where the first item in A needs to be inserted
				int mid = BinaryFirst(array, array[A.start], B);

				// rotate A into place
				int amount = mid - A.end;
				Rotate(array, -amount, new Range(A.start, mid));
				if (B.end == mid) break;

				// calculate the new A and B ranges
				B.start = mid;
				A.set(A.start + amount, B.start);
				A.start = BinaryLast(array, array[A.start], A);
				if (A.length() == 0) break;
			}
		}

		private void NetSwap(int[] array, int order[], Range range, int x, int y) {
			int compare = compare(array[range.start + x], array[range.start + y]);
			if (compare > 0 || (order[x] > order[y] && compare == 0)) {
				swap(array, range.start + x, range.start + y, 1, true);
				swap(order, x, y, 0, false);
			}
		}

		// bottom-up merge sort combined with an in-place merge algorithm for O(1) memory use
		private void Sort(int[] array, int len) {
			int size = len;

			// if the array is of size 0, 1, 2, or 3, just sort them like so:
			if (size < 4) {
				if (size == 3) {
					// hard-coded insertion sort
					if (compare(array[1], array[0]) < 0) {
						swap(array, 0, 1, 1, true);
					}
					if (compare(array[2], array[1]) < 0) {
						swap(array, 1, 2, 1, true);
						if (compare(array[1], array[0]) < 0) {
							swap(array, 0, 1, 1, true);
						}
					}
				} else if (size == 2) {
					// swap the items if they're out of order
					if (compare(array[1], array[0]) < 0) {
						swap(array, 0, 1, 1, true);
					}
				}
				return;
			}

			// sort groups of 4-8 items at a time using an unstable sorting network,
			// but keep track of the original item orders to force it to be stable
			// http://pages.ripco.net/~jgamble/nw.html
			Iterator iterator = new Iterator(size, 4);
			while (!iterator.finished()) {
				int order[] = { 0, 1, 2, 3, 4, 5, 6, 7 };
				Range range = iterator.nextRange();

				if (range.length() == 8) {
					NetSwap(array, order, range, 0, 1); NetSwap(array, order, range, 2, 3);
					NetSwap(array, order, range, 4, 5); NetSwap(array, order, range, 6, 7);
					NetSwap(array, order, range, 0, 2); NetSwap(array, order, range, 1, 3);
					NetSwap(array, order, range, 4, 6); NetSwap(array, order, range, 5, 7);
					NetSwap(array, order, range, 1, 2); NetSwap(array, order, range, 5, 6);
					NetSwap(array, order, range, 0, 4); NetSwap(array, order, range, 3, 7);
					NetSwap(array, order, range, 1, 5); NetSwap(array, order, range, 2, 6);
					NetSwap(array, order, range, 1, 4); NetSwap(array, order, range, 3, 6);
					NetSwap(array, order, range, 2, 4); NetSwap(array, order, range, 3, 5);
					NetSwap(array, order, range, 3, 4);

				} else if (range.length() == 7) {
					NetSwap(array, order, range, 1, 2); NetSwap(array, order, range, 3, 4); NetSwap(array, order, range, 5, 6);
					NetSwap(array, order, range, 0, 2); NetSwap(array, order, range, 3, 5); NetSwap(array, order, range, 4, 6);
					NetSwap(array, order, range, 0, 1); NetSwap(array, order, range, 4, 5); NetSwap(array, order, range, 2, 6);
					NetSwap(array, order, range, 0, 4); NetSwap(array, order, range, 1, 5);
					NetSwap(array, order, range, 0, 3); NetSwap(array, order, range, 2, 5);
					NetSwap(array, order, range, 1, 3); NetSwap(array, order, range, 2, 4);
					NetSwap(array, order, range, 2, 3);

				} else if (range.length() == 6) {
					NetSwap(array, order, range, 1, 2); NetSwap(array, order, range, 4, 5);
					NetSwap(array, order, range, 0, 2); NetSwap(array, order, range, 3, 5);
					NetSwap(array, order, range, 0, 1); NetSwap(array, order, range, 3, 4); NetSwap(array, order, range, 2, 5);
					NetSwap(array, order, range, 0, 3); NetSwap(array, order, range, 1, 4);
					NetSwap(array, order, range, 2, 4); NetSwap(array, order, range, 1, 3);
					NetSwap(array, order, range, 2, 3);

				} else if (range.length() == 5) {
					NetSwap(array, order, range, 0, 1); NetSwap(array, order, range, 3, 4);
					NetSwap(array, order, range, 2, 4);
					NetSwap(array, order, range, 2, 3); NetSwap(array, order, range, 1, 4);
					NetSwap(array, order, range, 0, 3);
					NetSwap(array, order, range, 0, 2); NetSwap(array, order, range, 1, 3);
					NetSwap(array, order, range, 1, 2);

				} else if (range.length() == 4) {
					NetSwap(array, order, range, 0, 1); NetSwap(array, order, range, 2, 3);
					NetSwap(array, order, range, 0, 2); NetSwap(array, order, range, 1, 3);
					NetSwap(array, order, range, 1, 2);
				}
			}
			if (size < 8) return;

			// we need to keep track of a lot of ranges during this sort!
			Range buffer1 = new Range(), buffer2 = new Range();
			Range blockA = new Range(), blockB = new Range();
			Range lastA = new Range(), lastB = new Range();
			Range firstA = new Range();
			Range A = new Range(), B = new Range();

			Pull[] pull = new Pull[2];
			pull[0] = new Pull();
			pull[1] = new Pull();

			// then merge sort the higher levels, which can be 8-15, 16-31, 32-63, 64-127, etc.
			while (true) {

				int block_size = (int)Math.sqrt(iterator.length());
				int buffer_size = iterator.length()/block_size + 1;

				// as an optimization, we really only need to pull out the internal buffers once for each level of merges
				// after that we can reuse the same buffers over and over, then redistribute it when we're finished with this level
				int index, last, count, pull_index = 0;
				buffer1.set(0, 0);
				buffer2.set(0, 0);

				pull[0].reset();
				pull[1].reset();

				// find two internal buffers of size 'buffer_size' each
				int find = buffer_size + buffer_size;
				boolean find_separately = false;

				if (find > iterator.length()) {
					// we can't fit both buffers into the same A or B subarray, so find two buffers separately
					find = buffer_size;
					find_separately = true;
				}

				// we need to find either a single contiguous space containing 2√A unique values (which will be split up into two buffers of size √A each),
				// or we need to find one buffer of < 2√A unique values, and a second buffer of √A unique values,
				// OR if we couldn't find that many unique values, we need the largest possible buffer we can get

				// in the case where it couldn't find a single buffer of at least √A unique values,
				// all of the Merge steps must be replaced by a different merge algorithm (MergeInPlace)

				iterator.begin();
				while (!iterator.finished()) {
					A = iterator.nextRange();
					B = iterator.nextRange();

					// check A for the number of unique values we need to fill an internal buffer
					// these values will be pulled out to the start of A
					for (last = A.start, count = 1; count < find; last = index, count++) {
						index = FindLastForward(array, array[last], new Range(last + 1, A.end), find - count);
						if (index == A.end) break;
					}
					index = last;

					if (count >= buffer_size) {
						// keep track of the range within the array where we'll need to "pull out" these values to create the internal buffer
						pull[pull_index].range.set(A.start, B.end);
						pull[pull_index].count = count;
						pull[pull_index].from = index;
						pull[pull_index].to = A.start;
						pull_index = 1;

						if (count == buffer_size + buffer_size) {
							// we were able to find a single contiguous section containing 2√A unique values,
							// so this section can be used to contain both of the internal buffers we'll need
							buffer1.set(A.start, A.start + buffer_size);
							buffer2.set(A.start + buffer_size, A.start + count);
							break;
						} else if (find == buffer_size + buffer_size) {
							// we found a buffer that contains at least √A unique values, but did not contain the full 2√A unique values,
							// so we still need to find a second separate buffer of at least √A unique values
							buffer1.set(A.start, A.start + count);
							find = buffer_size;
						} else if (find_separately) {
							// found one buffer, but now find the other one
							buffer1 = new Range(A.start, A.start + count);
							find_separately = false;
						} else {
							// we found a second buffer in an 'A' subarray containing √A unique values, so we're done!
							buffer2.set(A.start, A.start + count);
							break;
						}
					} else if (pull_index == 0 && count > buffer1.length()) {
						// keep track of the largest buffer we were able to find
						buffer1.set(A.start, A.start + count);

						pull[pull_index].range.set(A.start, B.end);
						pull[pull_index].count = count;
						pull[pull_index].from = index;
						pull[pull_index].to = A.start;
					}

					// check B for the number of unique values we need to fill an internal buffer
					// these values will be pulled out to the end of B
					for (last = B.end - 1, count = 1; count < find; last = index - 1, count++) {
						index = FindFirstBackward(array, array[last], new Range(B.start, last), find - count);
						if (index == B.start) break;
					}
					index = last;

					if (count >= buffer_size) {
						// keep track of the range within the array where we'll need to "pull out" these values to create the internal buffer
						pull[pull_index].range.set(A.start, B.end);
						pull[pull_index].count = count;
						pull[pull_index].from = index;
						pull[pull_index].to = B.end;
						pull_index = 1;

						if (count == buffer_size + buffer_size) {
							// we were able to find a single contiguous section containing 2√A unique values,
							// so this section can be used to contain both of the internal buffers we'll need
							buffer1.set(B.end - count, B.end - buffer_size);
							buffer2.set(B.end - buffer_size, B.end);
							break;
						} else if (find == buffer_size + buffer_size) {
							// we found a buffer that contains at least √A unique values, but did not contain the full 2√A unique values,
							// so we still need to find a second separate buffer of at least √A unique values
							buffer1.set(B.end - count, B.end);
							find = buffer_size;
						} else if (find_separately) {
							// found one buffer, but now find the other one
							buffer1 = new Range(B.end - count, B.end);
							find_separately = false;
						} else {
							// buffer2 will be pulled out from a 'B' subarray, so if the first buffer was pulled out from the corresponding 'A' subarray,
							// we need to adjust the end point for that A subarray so it knows to stop redistributing its values before reaching buffer2
							if (pull[0].range.start == A.start) pull[0].range.end -= pull[1].count;

							// we found a second buffer in an 'B' subarray containing √A unique values, so we're done!
							buffer2.set(B.end - count, B.end);
							break;
						}
					} else if (pull_index == 0 && count > buffer1.length()) {
						// keep track of the largest buffer we were able to find
						buffer1.set(B.end - count, B.end);

						pull[pull_index].range.set(A.start, B.end);
						pull[pull_index].count = count;
						pull[pull_index].from = index;
						pull[pull_index].to = B.end;
					}
				}

				// pull out the two ranges so we can use them as internal buffers
				for (pull_index = 0; pull_index < 2; pull_index++) {
					int length = pull[pull_index].count;

					if (pull[pull_index].to < pull[pull_index].from) {
						// we're pulling the values out to the left, which means the start of an A subarray
						index = pull[pull_index].from;
						for (count = 1; count < length; count++) {
							index = FindFirstBackward(array, array[index - 1], new Range(pull[pull_index].to, pull[pull_index].from - (count - 1)), length - count);
							Range range = new Range(index + 1, pull[pull_index].from + 1);
							Rotate(array, range.length() - count, range);
							pull[pull_index].from = index + count;
						}
					} else if (pull[pull_index].to > pull[pull_index].from) {
						// we're pulling values out to the right, which means the end of a B subarray
						index = pull[pull_index].from + 1;
						for (count = 1; count < length; count++) {
							index = FindLastForward(array, array[index], new Range(index, pull[pull_index].to), length - count);
							Range range = new Range(pull[pull_index].from, index - 1);
							Rotate(array, count, range);
							pull[pull_index].from = index - 1 - count;
						}
					}
				}

				// adjust block_size and buffer_size based on the values we were able to pull out
				buffer_size = buffer1.length();
				block_size = iterator.length()/buffer_size + 1;

				// the first buffer NEEDS to be large enough to tag each of the evenly sized A blocks,
				// so this was originally here to test the math for adjusting block_size above
				//if ((iterator.length() + 1)/block_size > buffer_size) throw new RuntimeException();

				// now that the two internal buffers have been created, it's time to merge each A+B combination at this level of the merge sort!
				iterator.begin();
				while (!iterator.finished()) {
					A = iterator.nextRange();
					B = iterator.nextRange();

					// remove any parts of A or B that are being used by the internal buffers
					int start = A.start;
					if (start == pull[0].range.start) {
						if (pull[0].from > pull[0].to) {
							A.start += pull[0].count;

							// if the internal buffer takes up the entire A or B subarray, then there's nothing to merge
							// this only happens for very small subarrays, like √4 = 2, 2 * (2 internal buffers) = 4,
							// which also only happens when cache_size is small or 0 since it'd otherwise use MergeExternal
							if (A.length() == 0) continue;
						} else if (pull[0].from < pull[0].to) {
							B.end -= pull[0].count;
							if (B.length() == 0) continue;
						}
					}
					if (start == pull[1].range.start) {
						if (pull[1].from > pull[1].to) {
							A.start += pull[1].count;
							if (A.length() == 0) continue;
						} else if (pull[1].from < pull[1].to) {
							B.end -= pull[1].count;
							if (B.length() == 0) continue;
						}
					}

					if (compare(array[B.end - 1], array[A.start]) < 0) {
						// the two ranges are in reverse order, so a simple rotation should fix it
						Rotate(array, A.length(), new Range(A.start, B.end));
					} else if (compare(array[A.end], array[A.end - 1]) < 0) {
						// these two ranges weren't already in order, so we'll need to merge them!

						// break the remainder of A into blocks. firstA is the uneven-sized first A block
						blockA.set(A.start, A.end);
						firstA.set(A.start, A.start + blockA.length() % block_size);

						// swap the first value of each A block with the value in buffer1
						int indexA = buffer1.start;
						for (index = firstA.end; index < blockA.end; index += block_size)  {
							swap(array, indexA, index, 1, true);
							indexA++;
						}

						// start rolling the A blocks through the B blocks!
						// whenever we leave an A block behind, we'll need to merge the previous A block with any B blocks that follow it, so track that information as well
						lastA.set(firstA.start, firstA.end);
						lastB.set(0, 0);
						blockB.set(B.start, B.start + Math.min(block_size, B.length()));
						blockA.start += firstA.length();
						indexA = buffer1.start;

						// if the first unevenly sized A block fits into the cache, copy it there for when we go to Merge it
						// otherwise, if the second buffer is available, block swap the contents into that
						if (buffer2.length() > 0)
							BlockSwap(array, lastA.start, buffer2.start, lastA.length());

						if (blockA.length() > 0) {
							while (true) {
								// if there's a previous B block and the first value of the minimum A block is <= the last value of the previous B block,
								// then drop that minimum A block behind. or if there are no B blocks left then keep dropping the remaining A blocks.
								if ((lastB.length() > 0 && compare(array[lastB.end - 1], array[indexA]) >= 0) || blockB.length() == 0) {
									// figure out where to split the previous B block, and rotate it at the split
									int B_split = BinaryFirst(array, array[indexA], lastB);
									int B_remaining = lastB.end - B_split;

									// swap the minimum A block to the beginning of the rolling A blocks
									int minA = blockA.start;
									for (int findA = minA + block_size; findA < blockA.end; findA += block_size)
										if (compare(array[findA], array[minA]) < 0)
											minA = findA;
									BlockSwap(array, blockA.start, minA, block_size);

									// swap the first item of the previous A block back with its original value, which is stored in buffer1
									swap(array, blockA.start, indexA, 1, true);
									indexA++;

									// locally merge the previous A block with the B values that follow it
									// if lastA fits into the external cache we'll use that (with MergeExternal),
									// or if the second internal buffer exists we'll use that (with MergeInternal),
									// or failing that we'll use a strictly in-place merge algorithm (MergeInPlace)
									if (buffer2.length() > 0)
										MergeInternal(array, lastA, new Range(lastA.end, B_split), buffer2);
									else
										MergeInPlace(array, lastA, new Range(lastA.end, B_split));

									if (buffer2.length() > 0) {
										// copy the previous A block into the cache or buffer2, since that's where we need it to be when we go to merge it anyway
										BlockSwap(array, blockA.start, buffer2.start, block_size);

										// this is equivalent to rotating, but faster
										// the area normally taken up by the A block is either the contents of buffer2, or data we don't need anymore since we memcopied it
										// either way, we don't need to retain the order of those items, so instead of rotating we can just block swap B to where it belongs
										BlockSwap(array, B_split, blockA.start + block_size - B_remaining, B_remaining);
									} else {
										// we are unable to use the 'buffer2' trick to speed up the rotation operation since buffer2 doesn't exist, so perform a normal rotation
										Rotate(array, blockA.start - B_split, new Range(B_split, blockA.start + block_size));
									}

									// update the range for the remaining A blocks, and the range remaining from the B block after it was split
									lastA.set(blockA.start - B_remaining, blockA.start - B_remaining + block_size);
									lastB.set(lastA.end, lastA.end + B_remaining);

									// if there are no more A blocks remaining, this step is finished!
									blockA.start += block_size;
									if (blockA.length() == 0)
										break;

								} else if (blockB.length() < block_size) {
									// move the last B block, which is unevenly sized, to before the remaining A blocks, by using a rotation
									// the cache is disabled here since it might contain the contents of the previous A block
									Rotate(array, -blockB.length(), new Range(blockA.start, blockB.end));

									lastB.set(blockA.start, blockA.start + blockB.length());
									blockA.start += blockB.length();
									blockA.end += blockB.length();
									blockB.end = blockB.start;
								} else {
									// roll the leftmost A block to the end by swapping it with the next B block
									BlockSwap(array, blockA.start, blockB.start, block_size);
									lastB.set(blockA.start, blockA.start + block_size);

									blockA.start += block_size;
									blockA.end += block_size;
									blockB.start += block_size;
									blockB.end += block_size;

									if (blockB.end > B.end)
										blockB.end = B.end;
								}
							}
						}

						// merge the last A block with the remaining B values
						if (buffer2.length() > 0)
							MergeInternal(array, lastA, new Range(lastA.end, B.end), buffer2);
						else
							MergeInPlace(array, lastA, new Range(lastA.end, B.end));
					}
				}

				// when we're finished with this merge step we should have the one or two internal buffers left over, where the second buffer is all jumbled up
				// insertion sort the second buffer, then redistribute the buffers back into the array using the opposite process used for creating the buffer

				// while an unstable sort like quick sort could be applied here, in benchmarks it was consistently slightly slower than a simple insertion sort,
				// even for tens of millions of items. this may be because insertion sort is quite fast when the data is already somewhat sorted, like it is here
				InsertionSort(array, buffer2);

				for (pull_index = 0; pull_index < 2; pull_index++) {
					int unique = pull[pull_index].count * 2;
					if (pull[pull_index].from > pull[pull_index].to) {
						// the values were pulled out to the left, so redistribute them back to the right
						Range buffer = new Range(pull[pull_index].range.start, pull[pull_index].range.start + pull[pull_index].count);
						while (buffer.length() > 0) {
							index = FindFirstForward(array, array[buffer.start], new Range(buffer.end, pull[pull_index].range.end), unique);
							int amount = index - buffer.end;
							Rotate(array, buffer.length(), new Range(buffer.start, index));
							buffer.start += (amount + 1);
							buffer.end += amount;
							unique -= 2;
						}
					} else if (pull[pull_index].from < pull[pull_index].to) {
						// the values were pulled out to the right, so redistribute them back to the left
						Range buffer = new Range(pull[pull_index].range.end - pull[pull_index].count, pull[pull_index].range.end);
						while (buffer.length() > 0) {
							index = FindLastBackward(array, array[buffer.end - 1], new Range(pull[pull_index].range.start, buffer.start), unique);
							int amount = buffer.start - index;
							Rotate(array, amount, new Range(index, buffer.end));
							buffer.start -= amount;
							buffer.end -= (amount + 1);
							unique -= 2;
						}
					}
				}

				// double the size of each A and B subarray that will be merged in the next level
				if (!iterator.nextLevel()) break;
			}
		}
	}
	public static void startWikiSort (int[] array, int length) {
		WikiSorter Wiki = new WikiSorter();

		Wiki.Sort(array, length);
	}
}