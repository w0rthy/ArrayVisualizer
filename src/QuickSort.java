/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Swaps.*;

/**
 *
 * @author S630690
 */
public class QuickSort {
    public static void quickSort(int[] a, int p, int r) throws InterruptedException
    {
        if(p<r)
        {
            int q=partition(a,p,r);
                sleep(1);
            quickSort(a,p,q);
            quickSort(a,q+1,r);
        }
    }

    public static int partition(int[] a, int p, int r) throws InterruptedException {

        int x = a[p];
        int i = p-1 ;
        int j = r+1 ;

        while (true) {
            //sleep(0.);
            i++;
            while ( i< r && a[i] < x){
                i++;
                marked.set(1, i);
                sleep(0.45);
                comps+=2;
            }
            j--;
            while (j>p && a[j] > x){
                j--;
                marked.set(2,j);
                sleep(0.45);
                comps+=2;
            }

            if (i < j)
                swap(a, i, j);
            else
                return j;
        }
    }
}
