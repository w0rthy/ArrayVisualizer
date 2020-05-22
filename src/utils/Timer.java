package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

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

final public class Timer {
    private DecimalFormat formatter;
    private DecimalFormatSymbols symbols;
    
    private volatile String minuteFormat;
    private volatile String secondFormat;
    
    private volatile int elapsedTime;
    private volatile double realTimer;
    private volatile boolean REALTIMER;
    private volatile double sortRunTime;
    private volatile boolean timerEnabled;
    
    private long timeStart;
    private long timeStop;
    
    public Timer() {
        this.REALTIMER = true;
        
        this.timeStart = 0;
        this.timeStop = 0;
        
        this.formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        this.symbols = this.formatter.getDecimalFormatSymbols();
        
        this.symbols.setGroupingSeparator(',');
        this.formatter.setDecimalFormatSymbols(this.symbols);
    }
    
    public String getVisualTime() {
        if(this.timerEnabled) {
            this.elapsedTime = (int) ((System.nanoTime() - this.sortRunTime) / 1e+9);
            
            secondFormat = "" + ((this.elapsedTime % 60) / 10) + (this.elapsedTime % 10);
            minuteFormat = (this.elapsedTime / 60) + ":" + secondFormat;
        }
        
        if(!this.timerEnabled && this.elapsedTime == 0) return "-:--";
        else if(this.elapsedTime >= 60)                 return minuteFormat;
        else if(this.elapsedTime >= 1)                  return "0:" + secondFormat;
        else                                            return "0:00";
    }
    
    public String getRealTime() {
        if(!this.REALTIMER) {
            return "Disabled";
        }
        else if(this.realTimer == 0) {
            if(this.timerEnabled) return "0.000ms";
            else                  return "---ms";
        }
        else if(this.realTimer < 0.001)      return "< 0.001ms";
        else if(this.realTimer >= 60000.000) return "~" + this.formatter.format((int) (this.realTimer / 60000)) + "m" + (int) ((this.realTimer % 60000) / 1000) + "s";
        else if(this.realTimer >= 1000.000)  return "~" + this.formatter.format(this.realTimer / 1000) + "s";
        else                                 return "~" + this.formatter.format(this.realTimer) + "ms";
    }   
    
    public void toggleRealTimer(boolean Bool) {
        this.REALTIMER = Bool;
    }
    
    public void enableRealTimer() {
        if(REALTIMER) this.timerEnabled = true;
        this.sortRunTime = System.nanoTime();
        this.realTimer = 0;
    }

    public void disableRealTimer() {
        this.timerEnabled = false;
    }
    
    public boolean timerEnabled() {
        return this.timerEnabled;
    }
    
    public void startLap() {
        if(this.timerEnabled) this.timeStart = System.nanoTime();
    }
    
    public void stopLap() {
        this.timeStop = System.nanoTime();
        if(this.timerEnabled) this.realTimer += (timeStop - timeStart) * 1e-6d;
    }

    void manualAddTime(long milliseconds) {
        this.realTimer += milliseconds;
    }
    
    public void manualSetTime(long milliseconds) {
        this.realTimer = milliseconds;
    }
}