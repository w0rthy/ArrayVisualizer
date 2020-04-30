package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import main.ArrayVisualizer;
import templates.TimSorting;

/*
 * 
MIT License

Copyright (c) 2019 w0rthy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

/**
 *
 * @author S630690
 */
final public class Writes {
    private volatile long reversals;
    private volatile long swaps;
    private volatile long tempWrites;
    private volatile long writes;
    
    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;
    
    private Delays Delays;
    private Highlights Highlights;
    private Timer Timer;
    
    public Writes(ArrayVisualizer ArrayVisualizer) {
        this.reversals = 0;
        this.swaps = 0;
        this.tempWrites = 0;
        this.writes = 0;
        
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Timer = ArrayVisualizer.getTimer();
        
        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        this.symbols = this.formatter.getDecimalFormatSymbols();
        
        this.symbols.setGroupingSeparator(',');
        this.formatter.setDecimalFormatSymbols(this.symbols);
    }
    
    public void resetStatistics() {
        this.reversals = 0;
        this.swaps = 0;
        this.tempWrites = 0;
        this.writes = 0;
    }
    
    public String getReversals() {
        if(this.reversals < 0) {
            this.reversals = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(reversals == 1) return this.reversals + " Reversal";
            else               return this.formatter.format(this.reversals) + " Reversals";
        }
    }
    
    public String getSwaps() {
        if(this.swaps < 0) {
            this.swaps = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.swaps == 1) return this.swaps + " Swap";
            else                return this.formatter.format(this.swaps) + " Swaps";
        }
    }
    
    public String getTempWrites() {
        if(this.tempWrites < 0) {
            this.tempWrites = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.tempWrites == 1) return this.tempWrites + " Write to Auxiliary Array(s)";
            else                     return this.formatter.format(this.tempWrites) + " Writes to Auxiliary Array(s)";
        }
    }
    
    public String getWrites() {
        if(this.writes < 0) {
            this.writes = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.writes == 1) return this.writes + " Write to Main Array";
            else                 return this.formatter.format(this.writes) + " Writes to Main Array";
        }
    }
    
    public void changeTempWrites(int value) {
        this.tempWrites += value;
    }
    
    public void changeWrites(int value) {
        this.writes += value;
    }
    
    private void updateSwap(boolean auxwrite) {
        this.swaps++;
        if(auxwrite) this.tempWrites += 2;
        else             this.writes += 2;
    }

    private void markSwap(int a, int b) {
        Highlights.markArray(1, a);
        Highlights.markArray(2, b);
    }

    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean auxwrite) {
        if(mark) this.markSwap(a, b);

        if(Timer.timerEnabled()) Timer.startLap();

        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;

        if(Timer.timerEnabled()) Timer.stopLap();

        this.updateSwap(auxwrite);
        Delays.sleep(pause);
    }

    public void multiSwap(int[] array, int pos, int to, double sleep, boolean mark, boolean auxwrite) {
        if(to - pos > 0) {
            for(int i = pos; i < to; i++) {
                this.swap(array, i, i + 1, 0, mark, auxwrite);
                Delays.sleep(sleep);
            }
        }
        else {
            for(int i = pos; i > to; i--) {
                this.swap(array, i, i - 1, 0, mark, auxwrite);
                Delays.sleep(sleep);
            }
        }
    }
    
    public void reversal(int[] array, int start, int length, double sleep, boolean mark, boolean auxwrite) {
        this.reversals++;
        
        for(int i = start; i < start + ((length - start + 1) / 2); i++) {
            this.swap(array, i, start + length - i, sleep, mark, auxwrite);
        }
    }

    public void write(int[] array, int at, int equals, double pause, boolean mark, boolean auxwrite) {
        if(mark) Highlights.markArray(1, at);
        
        if(auxwrite) tempWrites++;
        else             writes++;
        
        if(Timer.timerEnabled()) Timer.startLap();
        
        array[at] = equals;

        if(Timer.timerEnabled()) Timer.stopLap();
        
        Delays.sleep(pause);
    }

    public void multiDimWrite(int[][] array, int x, int y, int equals, double pause, boolean mark, boolean auxwrite) {
        if(mark) Highlights.markArray(1, x);
        
        if(auxwrite) tempWrites++;
        else             writes++;

        if(Timer.timerEnabled()) Timer.startLap();
        
        array[x][y] = equals;

        if(Timer.timerEnabled()) Timer.stopLap();
        
        Delays.sleep(pause);
    }

    //Simulates a write in order to better estimate time for values being written to an ArrayList
    public void mockWrite(int length, int pos, int val, double pause) {
        int[] mockArray = new int[length];
        
        this.tempWrites++;

        if(Timer.timerEnabled()) Timer.startLap();
        
        mockArray[pos] = val;

        if(Timer.timerEnabled()) Timer.stopLap();
        
        Delays.sleep(pause);
    }

    public void transcribe(int[] array, ArrayList<Integer>[] registers, int start, boolean mark, boolean auxwrite) {
        int total = start;

        for(int index = 0; index < registers.length; index++) {
            for(int i = 0; i < registers[index].size(); i++) {
                this.write(array, total++, registers[index].get(i), 0, mark, auxwrite);
                if(mark) Delays.sleep(1);
            }
            registers[index].clear();
        }
    }

    public void transcribeMSD(int[] array, ArrayList<Integer>[] registers, int start, int min, boolean mark, boolean auxwrite) {
        int total = start;
        int temp = 0;

        for(ArrayList<Integer> list : registers) {
            total += list.size();
        }
        
        for(int index = registers.length - 1; index >= 0; index--) {
            for(int i = registers[index].size() - 1; i >= 0; i--) {
                this.write(array, total + min - temp++ - 1, registers[index].get(i), 0, mark, auxwrite);
                if(mark) Delays.sleep(1 + (2 / registers[index].size()));
            }
        }
    }

    public void fancyTranscribe(int[] array, int length, ArrayList<Integer>[] registers) {
        int[] tempArray = new int[length];
        boolean[] tempWrite = new boolean[length];
        int radix = registers.length;

        this.transcribe(tempArray, registers, 0, false, true);
        tempWrites -= length;

        for(int i = 0; i < length; i++) {
            int register = i % radix;
            int pos = (int) ((register * (length / radix)) + (i / radix));
            
            if(!tempWrite[pos]) {
                this.write(array, pos, tempArray[pos], 0, false, false);
                tempWrite[pos] = true;
            }
            
            Highlights.markArray(register, pos);
            if(register == 0) Delays.sleep(radix);
        }
        for(int i = 0; i < length; i++) {
            if(!tempWrite[i]){
                this.write(array, i, tempArray[i], 0, false, false);
            }
        }
    
        Highlights.clearAllMarks();
    }
    
    //Methods mocking System.arraycopy (reversearraycopy is for TimSort's MergeHi and BinaryInsert, and WikiSort's Rotate)
    public void arraycopy(int[] src, int srcPos, int[] dest, int destPos, int length, double sleep, boolean mark, boolean temp) {
        for(int i = 0; i < length; i++) {
            this.write(dest, destPos + i, src[srcPos + i], sleep, false, temp);
            
            if(mark) {
                if(temp) Highlights.markArray(1, srcPos  + i);
                else     Highlights.markArray(1, destPos + i);
            }
        }
    }
    
    public void reversearraycopy(int[] src, int srcPos, int[] dest, int destPos, int length, double sleep, boolean mark, boolean temp) {
        for(int i = length - 1; i >= 0; i--) {
            this.write(dest, destPos + i, src[srcPos + i], sleep, false, temp);
            
            if(mark) {
                if(temp) Highlights.markArray(1, srcPos  + i);
                else     Highlights.markArray(1, destPos + i);
            }
        }
    }
    
    //TODO: These methods should be solely controlled by Timer class
    public void addTime(long milliseconds) {
        if(Timer.timerEnabled()) Timer.manualAddTime(milliseconds);
    }
    
    public void setTime(long milliseconds) {
        if(Timer.timerEnabled()) Timer.manualSetTime(milliseconds);
    }
    
    public void startLap() {
        if(Timer.timerEnabled()) Timer.startLap();
    }
    
    public void stopLap() {
        if(Timer.timerEnabled()) Timer.stopLap();
    }
}
