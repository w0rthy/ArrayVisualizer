/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer;

import static array.visualizer.ArrayVisualizer.*;
import static array.visualizer.Transcriptions.*;
import java.util.ArrayList;

/**
 *
 * @author S630690
 */
public class ShatterSorts {
    public static void shatterPartition(int num) throws Exception {
        int shatters = (int)Math.ceil(array.length/(double)num);
        ArrayList<Integer>[] registers = new ArrayList[shatters];
        for(int i = 0; i < shatters; i++)
            registers[i] = new ArrayList<Integer>();
        for(int i = 0; i < array.length; i++){
            registers[array[i]/num].add(array[i]);
            aa++;
        }
        transcribe(registers, array);
    }
    
    public static void shatterSort(int num) throws Exception {
        int shatters = (int)Math.ceil(array.length/(double)num);
        shatterPartition(num);
        int[] tmp = new int[num];
        for(int i = 0; i < shatters; i++){
            for(int j = 0; j < num; j++){
                if(i*num+j>=array.length)
                    tmp[j] = -1;
                else
                    tmp[j]=array[i*num+j];
                aa++;
            }
            for(int j = 0; j < tmp.length; j++){
                int tmpj = tmp[j];
                if(i*num+(tmpj%num)>=array.length || tmpj == -1)
                    break;
                array[i*num+(tmpj%num)]=tmpj;
                aa++;
                marked.set(1, i*num+(tmpj%num));
                sleep(sleepTime(0.02));
            }
        }
    }
    
    public static void simpleShatterSort(int num, int rate) throws Exception {
        for(int i = num; i > 1; i = i/rate)
            shatterPartition(i);
        shatterPartition(1);
    }
}
