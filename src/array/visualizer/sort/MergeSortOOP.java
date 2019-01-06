/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;

/**
 *
 * @author S630690
 */
public class MergeSortOOP {
    public static void mergeSortOP(final ArrayController ac)throws Exception {
        int start = 0;
        int end = ac.length;
        int mid = (end+start)/2;
        mergeOP(ac, start, mid, end);
    }

    public static void mergeOP(final ArrayController ac, int start, int mid, int end)throws Exception{
        if(start==mid)
            return;
        mergeOP(ac, start, (mid+start)/2, mid);
        mergeOP(ac, mid, (mid+end)/2, end);
        
        int[] tmp = new int[end-start];
        
        int low = start;
        int high = mid;
        for(int nxt = 0; nxt < tmp.length; nxt++){
            if(low >= mid && high >= end)
                break;
            if(low < mid && high >= end){
                tmp[nxt]=ac.array[low];
                low++;
                ac.comps+=2;
            }
            else if(low >= mid && high < end){
                tmp[nxt]=ac.array[high];
                high++;
                ac.comps+=2;
            }
            else if(ac.array[low]<ac.array[high]){
                tmp[nxt]=ac.array[low];
                low++;
                ac.comps+=3;
            }
            else{
                tmp[nxt]=ac.array[high];
                high++;
                ac.comps+=3;
            }
            ac.aa++;
            ac.marked.set(1, low);
            ac.marked.set(2, high);
            //if(end-start>=array.length/10)
                sleep(0.65);
        }
        //System.arraycopy(tmp, 0, array, start, tmp.length);
        ac.marked.set(2, -5);
        for(int i = 0; i < tmp.length; i++){
            ac.array[start+i]=tmp[i];
            ac.aa++;
            ac.marked.set(1, start+i);
            if(end-start>= ac.length/100)
                sleep(0.5);
        }
    }
}
