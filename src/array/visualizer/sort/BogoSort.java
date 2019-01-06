package array.visualizer.sort;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.utils.Swaps.*;

public class BogoSort implements Sort {
    public static boolean bogoIsSorted(final ArrayController ac){
        for(int i = 1; i < ac.length; i++){
            ac.comps++;
            ac.aa++;
            ac.marked.set(1, i);
            ac.marked.set(2, i-1);
            sleep(1);
            if(ac.array[i]<ac.array[i-1])
                return false;
        }
        return true;
    }
    
    public static void bogoSort(final ArrayController ac){
        while(!bogoIsSorted(ac)){
            for(int i = 0; i < ac.length; i++){
                swap(ac, i, (int)(Math.random()* ac.length));
                sleep(1);
            }
        }
    }
    
    public static void bogobogoSort(){
        
    }

    @Override
    public String name()
    {
        return "Bogo Sort";
    }

    @Override
    public void sort(ArrayController ac)
    {
        bogoSort(ac);
    }
}
