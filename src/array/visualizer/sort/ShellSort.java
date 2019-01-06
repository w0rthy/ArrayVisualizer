package array.visualizer.sort;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

public class ShellSort {
    public static void shellSort(int gap, int divrate){
        double sleepamt = 1d;
        while(gap>0){
            for(int j = 0; j <= gap-1; j++){
                for(int i = j+gap; i < array.length; i+=gap){
                    int pos = i;
                    int prev = pos-gap;
                    while(prev>=0){
                        if(array[pos] < array[prev]){
                            comps++;
                            swap(array, pos, prev);
                            sleep(sleepamt);
                        }else{
                            aa+=2;
                            break;
                        }
                        pos = prev;
                        prev = pos-gap;
                    }
                }
            }
            
            if(gap==1) //Done
                break;
            
            gap = Math.max(gap/divrate,1); //Ensure that we do gap 1
            //sleepamt /= divrate;
        }
    }
}
