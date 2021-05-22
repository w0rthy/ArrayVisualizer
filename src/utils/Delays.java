package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.ArrayVisualizer;
import templates.JErrorPane;

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

final public class Delays {
    private volatile double SLEEPRATIO;
    private volatile boolean SKIPPED;
    
    private double addamt;
    private double delay;
    private double nanos;
    
    private volatile double currentDelay;
    
    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;
    
    private Sounds Sounds;
    
    public Delays(ArrayVisualizer ArrayVisualizer) {
        this.SLEEPRATIO = 1.0;
        this.SKIPPED = false;
        this.addamt = 0;
        
        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        this.symbols = this.formatter.getDecimalFormatSymbols();
        
        this.symbols.setGroupingSeparator(',');
        this.formatter.setDecimalFormatSymbols(this.symbols);
        
        this.Sounds = ArrayVisualizer.getSounds();
    }
    
    public String displayCurrentDelay() {
        String currDelay = "";
        
        if(this.currentDelay == 0 || this.SKIPPED) {
            currDelay = "0";
        }
        else if(this.currentDelay < 0.001) {
            currDelay = "< 0.001";
        }
        else {
            currDelay = formatter.format(this.currentDelay);
        }
        
        return currDelay;
    }
    public double getCurrentDelay() {
        return this.currentDelay;
    }
    public void setCurrentDelay(double value) {
        this.delay = value;
    }
    public void updateCurrentDelay(double oldRatio, double newRatio) {
        this.delay = (this.delay * oldRatio) / newRatio;
        this.currentDelay = this.delay;
        this.Sounds.changeNoteDelayAndFilter((int) this.currentDelay);
        this.addamt = 0;
    
        if(this.currentDelay < 0) {
            this.delay = this.currentDelay = 0;
        }
    }
    
    public double getSleepRatio() {
        return this.SLEEPRATIO;
    }
    public void setSleepRatio(double sleepRatio) {
        this.SLEEPRATIO = sleepRatio;
    }
    
    public boolean skipped() {
        return this.SKIPPED;
    }
    public void changeSkipped(boolean Bool) {
        this.SKIPPED = Bool;
        if(this.SKIPPED) this.Sounds.changeNoteDelayAndFilter(1);
    }
    
    public void sleep(double millis){
        if(millis <= 0) {
            return;
        }
        
        this.delay += (millis * (1 / this.SLEEPRATIO));
        this.currentDelay = (millis * (1 / this.SLEEPRATIO));
        
        this.Sounds.changeNoteDelayAndFilter((int) this.currentDelay);
        
        try {
            // With this for loop, you can change the speed of sorts without waiting for the current delay to finish.
            if(!this.SKIPPED) {
                while(this.delay >= 1) {
                    Thread.sleep(1);
                    this.delay--;
                }
            }
            else {
                this.delay = 0;
            }
        } catch(Exception ex) {
            JErrorPane.invokeErrorMessage(ex);
        }
        
        this.currentDelay = 0;
    }
}