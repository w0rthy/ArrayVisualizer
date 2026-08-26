package array.visualizer.sort;

import array.visualizer.ArrayVisualizer;

public class BitonicRadixSort extends Sort {
    public BitonicRadixSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.name = "Bitonic Radix Sort";
    }

    @Override
    public void sort(int[] arr, int length) {
        int chunkSize = 16; 
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(i + chunkSize, length);
            lsdRadixSubSort(arr, i, end);
        }
        for (int size = chunkSize; size < length; size *= 2) {
            for (int i = 0; i < length; i += size * 2) {
                // Merge adjacent chunks in alternating up/down directions
                bitonicMerge(arr, i, size * 2, 1);
            }
        }
    }


    private void lsdRadixSubSort(int[] arr, int start, int end) {
        int subLength = end - start;
        if (subLength <= 1) return;

        int max = arr[start];
        for (int i = start + 1; i < end; i++) {
            if (arr[i] > max) max = arr[i];
        }

        for (int exp = 1; max / exp > 0; exp *= 10) {
            int[] output = new int[subLength];
            int[] count = new int[10];

            for (int i = start; i < end; i++) {
                count[(arr[i] / exp) % 10]++;
            }
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }
            for (int i = end - 1; i >= start; i--) {
                int digit = (arr[i] / exp) % 10;
                output[count[digit] - 1] = arr[i];
                count[digit]--;
            }
            for (int i = 0; i < subLength; i++) {
                array.update(start + i, output[i]);
            }
        }
    }


    private void bitonicMerge(int[] arr, int low, int cnt, int dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            for (int i = low; i < low + k; i++) {
                if (i + k < arr.length) {
                    compareAndSwap(arr, i, i + k, dir);
                }
            }
            bitonicMerge(arr, low, k, dir);
            bitonicMerge(arr, low + k, k, dir);
        }
    }

    private void compareAndSwap(int[] arr, int i, int j, int dir) {
        if ((dir == 1 && arr[i] > arr[j]) || (dir == 0 && arr[i] < arr[j])) {
            int temp = arr[i];
            array.update(i, arr[j]);
            array.update(j, temp);
        }
    }
}
