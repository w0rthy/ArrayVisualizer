package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

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

final public class Delays {
    private volatile double SLEEPRATIO;
    private volatile boolean SKIPPED;
    
    private double addamt;
    private double delay;
    
    public Delays() {
        this.SLEEPRATIO = 1.0;
        this.SKIPPED = false;
        this.addamt = 0.0;
    }
    
    public double getCurrentDelay() {
        return this.delay;
    }
    public void setCurrentDelay(double value) {
        this.delay = value;
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
    }
    
    public void sleep(double millis){
        if(millis <= 0) {
            return;
        }
        
        this.delay = (millis * (1 / this.SLEEPRATIO));
        this.addamt += this.delay;
        
        long amt = (long) this.delay;
        if(this.addamt >= 1){
            amt += (int) this.addamt;
            this.addamt -= (int) this.addamt;
        }
        
        try {
            // With this for loop, you can change the speed of sorts without waiting for the current delay to finish.
            for(int i = 0; i < amt / this.SLEEPRATIO; i++) {
                Thread.sleep(1);
            }
        } catch(Exception ex) {
            Logger.getLogger(ArrayVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}