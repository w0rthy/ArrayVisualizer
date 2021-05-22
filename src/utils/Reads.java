package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import main.ArrayVisualizer;

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

final public class Reads {
    private volatile long comparisons;
    
    private ArrayVisualizer ArrayVisualizer;
    
    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;
    
    private Delays Delays;
    private Highlights Highlights;
    private Timer Timer;
    
    public Reads(ArrayVisualizer arrayVisualizer) {
        this.ArrayVisualizer = arrayVisualizer;
        
        this.comparisons = 0;
        
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Timer = ArrayVisualizer.getTimer();
        
        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        this.symbols = this.formatter.getDecimalFormatSymbols();
        
        this.symbols.setGroupingSeparator(',');
        this.formatter.setDecimalFormatSymbols(this.symbols);
    }
    
    public void resetStatistics() {
        this.comparisons = 0;
    }
    
    public void addComparison() {
        this.comparisons++;
    }
    
    public String displayComparisons() {
        if(this.comparisons < 0) {
            this.comparisons = Long.MIN_VALUE;
            return "Over " + this.formatter.format(Long.MAX_VALUE);
        }
        else {
            if(this.comparisons == 1) return this.comparisons + " Comparison";
            else                      return this.formatter.format(this.comparisons) + " Comparisons";
        }
    }
    
    public long getComparisons() {
        return this.comparisons;
    }
    
    public void setComparisons(long value) {
        this.comparisons = value;
    }
    
    public int compare(int left, int right) {
        this.comparisons++;
        
        int cmpVal = 0;
        
        Timer.startLap();

        if(left > right)      cmpVal =  1;
        else if(left < right) cmpVal = -1;
        else                  cmpVal =  0;

        Timer.stopLap();
        
        return cmpVal;
    }

    public int analyzeMax(int[] array, int length, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        
        int max = 0;

        for(int i = 0; i < length; i++) {
            Timer.startLap();
            
            if(array[i] > max) max = array[i];
            
            Timer.stopLap();
            
            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }
        
        ArrayVisualizer.toggleAnalysis(false);
        
        return max;
    }
    
    public int analyzeMaxLog(int[] array, int length, int base, double sleep, boolean mark) {
        ArrayVisualizer.toggleAnalysis(true);
        
        int max = 0;
        
        for(int i = 0; i < length; i++) { 
            int log = (int) (Math.log(array[i]) / Math.log(base));
            
            Timer.startLap();
            
            if(log > max) max = log;
            
            Timer.stopLap();
            
            if(mark) {
                Highlights.markArray(1, i);
                Delays.sleep(sleep);
            }
        }
        
        ArrayVisualizer.toggleAnalysis(false);
        
        return max;
    }
    
    public int analyzeBit(int[] array, int length) {
        ArrayVisualizer.toggleAnalysis(true);
        
        // Find highest bit of highest value
        int max = 0;
        
        for(int i = 0; i < length; i++) {
            Timer.startLap();
            
            max = Math.max(max, array[i]);
            
            Timer.stopLap();
            
            Highlights.markArray(1, i);
            Delays.sleep(0.75);
        }
        Timer.startLap();
        
        int analysis = 31 - Integer.numberOfLeadingZeros(max);
        
        Timer.stopLap();
        
        ArrayVisualizer.toggleAnalysis(false);
        return analysis;
    }
    
    public int getDigit(int a, int power, int radix) {
        return (int) (a / Math.pow(radix, power)) % radix;
    }
    
    public boolean getBit(int n, int k) {
        // Find boolean value of bit k in n
        return ((n >> k) & 1) == 1;
    }
}