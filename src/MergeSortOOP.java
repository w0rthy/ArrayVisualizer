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
public class MergeSortOOP {
    public static void mergeSortOP()throws Exception {
        int start = 0;
        int end = array.length;
        int mid = (end+start)/2;
        mergeOP(start,mid,end);
    }

    public static void mergeOP(int start, int mid, int end)throws Exception{
        if(start==mid)
            return;
        mergeOP(start, (mid+start)/2, mid);
        mergeOP(mid, (mid+end)/2, end);
        
        int[] tmp = new int[end-start];
        
        int low = start;
        int high = mid;
        for(int nxt = 0; nxt < tmp.length; nxt++){
            if(low >= mid && high >= end)
                break;
            if(low < mid && high >= end){
                tmp[nxt]=array[low];
                low++;
                comps+=2;
            }
            else if(low >= mid && high < end){
                tmp[nxt]=array[high];
                high++;
                comps+=2;
            }
            else if(array[low]<array[high]){
                tmp[nxt]=array[low];
                low++;
                comps+=3;
            }
            else{
                tmp[nxt]=array[high];
                high++;
                comps+=3;
            }
            aa++;
            marked.set(1,low);
            marked.set(2, high);
            //if(end-start>=array.length/10)
                sleep(0.65);
        }
        //System.arraycopy(tmp, 0, array, start, tmp.length);
        marked.set(2, -5);
        for(int i = 0; i < tmp.length; i++){
            array[start+i]=tmp[i];
            aa++;
            marked.set(1, start+i);
            if(end-start>=array.length/100)
                sleep(0.5);
        }
    }
}
