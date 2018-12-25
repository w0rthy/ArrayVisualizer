/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Analysis.*;

/**
 *
 * @author S630690
 */
public class GravitySort {
    public static void gravitySort() throws Exception {
        int max = analyzemax();
        int[][] abacus = new int[array.length][max];
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i]; j++)
                abacus[i][abacus[0].length-j-1] = 1;
        }
        //apply gravity
        for(int i = 0; i < abacus[0].length; i++){
            for(int j = 0; j < abacus.length; j++){
                if(abacus[j][i]==1){
                    //Drop it
                    int droppos = j;
                    while(droppos+1 < abacus.length && abacus[droppos][i] == 1)
                        droppos++;
                    if(abacus[droppos][i]==0){
                        abacus[j][i]=0;
                        abacus[droppos][i]=1;
                        aa+=2;
                    }
                }
            }
            
            int count = 0;
            for(int x = 0; x < abacus.length; x++){
                count = 0;
                for(int y = 0; y < abacus[0].length; y++)
                    count+=abacus[x][y];
                array[x] = count;
                sleep(0.002);
            }
            marked.set(1,array.length-i-1);
            sleep(2);
        }
    }
}
