package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

// Copyright Malte Skarupke 2016.
// Distributed under the Boost Software License, Version 1.0.
// (See http://www.boost.org/LICENSE_1_0.txt)

SORT DISABLED

final class PartitionInfo {
    private int count;
    private int offset;
    private int next_offset;
    
    public PartitionInfo() {
        this.setCount(0);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public void incrementCount() {
        ++this.count;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNextOffset() {
        return this.next_offset;
    }

    public void setNextOffset(int next_offset) {
        this.next_offset = next_offset;
    }
}

public class SkaSort extends Sort {
    final private static int StdSortThreshold = 128;
    final private static int AmericanFlagSortThreshold = 1024;
    
    public SkaSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Ska");
        this.setRunAllID("Ska Sort");
        this.setReportSortID("Skasort");
        this.setCategory("Distributive Sorts");
        this.isComparisonBased(false);
        this.isBucketSort(true);
        this.isRadixSort(true);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    template<typename It, typename Func>
    inline void unroll_loop_four_times(It begin, size_t iteration_count, Func && to_call)
    {
        size_t loop_count = iteration_count / 4;
        size_t remainder_count = iteration_count - loop_count * 4;
        for (; loop_count > 0; --loop_count)
        {
            to_call(begin);
            ++begin;
            to_call(begin);
            ++begin;
            to_call(begin);
            ++begin;
            to_call(begin);
            ++begin;
        }
        switch(remainder_count)
        {
        case 3:
            to_call(begin);
            ++begin;
        case 2:
            to_call(begin);
            ++begin;
        case 1:
            to_call(begin);
        }
    }

    template<typename It, typename F>
    inline It custom_std_partition(It begin, It end, F && func)
    {
        for (;; ++begin)
        {
            if (begin == end)
                return end;
            if (!func(*begin))
                break;
        }
        It it = begin;
        for(++it; it != end; ++it)
        {
            if (!func(*it))
                continue;

            std::iter_swap(begin, it);
            ++begin;
        }
        return begin;
    }
    
    private void ska_byte_sort(int[] array, int begin, int end) {
        PartitionInfo partitions[] = new PartitionInfo[256];
        for (int it = begin; it != end; ++it) {
            partitions[array[it]].incrementCount();
        }
        int remaining_partitions[] = new int[256];
        int total = 0;
        int num_partitions = 0;
        for (int i = 0; i < 256; ++i) {
            int count = partitions[i].getCount();
            if (count != 0){
                partitions[i].setOffset(total);
                total += count;
                remaining_partitions[num_partitions] = i;
                ++num_partitions;
            }
            partitions[i].setNextOffset(total);
        }
        for (int last_remaining = remaining_partitions[num_partitions], int end_partition = remaining_partitions[1]; last_remaining > end_partition;) {
            last_remaining = custom_std_partition(remaining_partitions, last_remaining, [&](uint8_t partition)
            {
                size_t & begin_offset = partitions[partition].offset;
                size_t & end_offset = partitions[partition].next_offset;
                if (begin_offset == end_offset)
                    return false;

                unroll_loop_four_times(begin + begin_offset, end_offset - begin_offset, [partitions = partitions, begin, &extract_key, sort_data](It it)
                {
                    uint8_t this_partition = current_byte(extract_key(*it), sort_data);
                    size_t offset = partitions[this_partition].offset++;
                    std::iter_swap(it, begin + offset);
                });
                return begin_offset != end_offset;
            });
        }
        if (Offset + 1 != NumBytes || next_sort)
        {
            for (int it = remaining_partitions + num_partitions; it != remaining_partitions; --it)
            {
                int partition = partitions[it - 1];
                int start_offset = (partition == 0 ? 0 : partitions[partition - 1].next_offset);
                int end_offset = partitions[partition].next_offset;
                int partition_begin = begin + start_offset;
                int partition_end = begin + end_offset;
                int num_elements = end_offset - start_offset;
                if (!StdSortIfLessThanThreshold(array, partition_begin, partition_end, num_elements)) {
                    this.sort(array, partition_begin, partition_end, num_elements);
                }
            }
        }
    }
    
    private boolean StdSortIfLessThanThreshold(int[] array, int begin, int end, int num_elements) {
        if (num_elements <= 1)
            return true;
        if (num_elements >= StdSortThreshold)
            return false;
        BranchedPDQSort pdqSort = new BranchedPDQSort(Delays, Highlights, Reads, Writes);
        pdqSort.customSort(array, begin, end);
        return true;
    }
    
    private void sort(int[] array, int begin, int end, int num_elements) {
        if (num_elements < AmericanFlagSortThreshold) {
            AmericanFlagSort flagSort = new AmericanFlagSort(Delays, Highlights, Reads, Writes);
            flagSort.runSort(array, num_elements, 128);
        }
        else {
            this.ska_byte_sort(array, begin, end);
        }
    }
    
    private void inplace_radix_sort(int[] array, int begin, int end) {
        this.sort(array, begin, end, end - begin);
    }
    
    private void ska_sort(int[] array, int begin, int end) {
        this.inplace_radix_sort(array, begin, end);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.ska_sort(array, 0, length);
    }
}
