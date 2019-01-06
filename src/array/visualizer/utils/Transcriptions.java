/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package array.visualizer.utils;

import array.visualizer.ArrayController;

import static array.visualizer.ArrayVisualizer.*;

import java.util.ArrayList;

/**
 *
 * @author S630690
 */
public class Transcriptions {
    public static void transcribe(ArrayList<Integer>[] registers, final ArrayController ac) {
        int total = 0;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                sleep(1);
                ac.array[total] = registers[ai].get(i);
                ac.marked.set(1, total);
                total++;
                ac.aa++;
            }
            registers[ai].clear();
        }
    }
    
    public static void transcribenm(ArrayList<Integer>[] registers, int[] array) {
        int total = 0;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                array[total] = registers[ai].get(i);
                total++;
            }
            registers[ai].clear();
        }
    }
    
    public static void transcribend(final ArrayController ac, ArrayList<Integer>[] registers, int min) {
        int total = 0;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                sleep((min+i)%5/4);
                ac.array[total+min] = registers[ai].get(i);
                ac.marked.set(1, total+min);
                total++;
                ac.aa++;
            }
            registers[ai].clear();
        }
    }
    
    public static void transcribermsd(final ArrayController ac, ArrayList<Integer>[] registers, int min) {
        int total = 0;
        for(ArrayList<Integer> ai : registers)
            total+=ai.size();
        int tmp = 0;
        for(int ai = registers.length-1; ai >= 0; ai--){
            for(int i = registers[ai].size()-1; i >= 0; i--){
                sleep(1+2/registers[ai].size());
                ac.array[total+min-tmp-1] = registers[ai].get(i);
                ac.marked.set(1, total+min-tmp-1);
                tmp++;
                ac.aa++;
            }
        }
    }
    
    public static void transcribe(ArrayList<Integer>[] registers, final ArrayController ac, int start)  {
        int total = start;
        for(int ai = 0; ai < registers.length; ai++){
            for(int i = 0; i < registers[ai].size(); i++){
                sleep(1);
                ac.array[total] = registers[ai].get(i);
                ac.marked.set(1, total);
                total++;
                ac.aa++;
            }
            registers[ai].clear();
        }
    }

    public static void fancyTranscribe(final ArrayController ac, ArrayList<Integer>[] registers) {
        int[] tmp = new int[ac.length];
        boolean[] tmpwrite = new boolean[ac.length];
        int radix = registers.length;
        transcribenm(registers, tmp);
        for(int i = 0; i < tmp.length; i++){
            int register = i%radix;
            if(register == 0)
                sleep(radix);//radix
            int pos = (int)(((double)register*((double)tmp.length/radix))+((double)i/radix));
            if(tmpwrite[pos]==false){
                ac.array[pos]=tmp[pos];
                ac.aa++;
                tmpwrite[pos] = true;
            }
            ac.marked.set(register, pos);
        }
        for(int i = 0; i < tmpwrite.length; i++)
            if(tmpwrite[i]==false){
                ac.array[i]=tmp[i];
                ac.aa++;
            }
        clearmarked();
    }
}
