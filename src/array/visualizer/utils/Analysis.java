/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.utils;

import static array.visualizer.ArrayVisualizer.*;

/**
 *
 * @author S630690
 */
public class Analysis {
    public static int analyze(int base)throws Exception {
        int a = 0;
        for(int i = 0; i < array.length; i++){
            marked.set(1, i);
            aa++;
            sleep(1);
            if((int)(Math.log(array[i])/Math.log(base))>a){
                a=(int)(Math.log(array[i])/Math.log(base));
            }
        }
        return a;
    }

    public static int analyze(int[] ar, int base) {
        int a = 0;
        for(int i = 0; i < ar.length; i++)
            if((int)(Math.log(ar[i])/Math.log(base))>a){
                a=(int)(Math.log(ar[i])/Math.log(base));
            }
        return a;
    }
    
    public static int analyzemax() throws Exception{
        int a = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i]>a)
                a=array[i];
            marked.set(1, i);
            aa++;
            sleep(1.0);
        }
        return a;
    }
}
