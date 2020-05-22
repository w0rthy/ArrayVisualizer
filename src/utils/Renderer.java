package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.ArrayVisualizer;
import visuals.Bars;
import visuals.Circular;
import visuals.Hoops;
import visuals.Mesh;
import visuals.Pixels;
import visuals.VisualStyles;

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

final class WindowState {
    private boolean windowUpdated;
    private boolean windowResized;
    
    public WindowState(boolean windowUpdate, boolean windowResize) {
        this.windowUpdated = windowUpdate;
        this.windowResized = windowResize;
    }
    
    public boolean updated() {
        return this.windowUpdated;
    }
    
    public boolean resized() {
        return this.windowResized;
    }
}

final public class Renderer {
    private Bars Bars;
    private Circular Circular;
    private Hoops Hoops;
    private Mesh Mesh;
    private Pixels Pixels;
    
    private volatile double xscl; //TODO: Change to xScale/yScale
    private volatile double yscl;
    
    private volatile int amt;
    
    private int linkedpixdrawx; //TODO: Change names
    private int linkedpixdrawy;
    
    private int frames;
    
    private int doth; //TODO: Change names
    private int dotw;
    private int dots; //TODO: Change name to dotDims/dotDimensions
    
    public Renderer(ArrayVisualizer ArrayVisualizer) {
        ArrayVisualizer.setWindowHeight();
        ArrayVisualizer.setWindowWidth();
        ArrayVisualizer.setThickStroke(new BasicStroke(8));
    }
    
    public double getXScale() {
        return this.xscl;
    }
    public double getYScale() {
        return this.yscl;
    }
    public int getOffset() {
        return this.amt;
    }
    public int getDotWidth() {
        return this.dotw;
    }
    public int getDotHeight() {
        return this.doth;
    }
    public int getDotDimensions() {
        return this.dots;
    }
    public int getLineX() {
        return this.linkedpixdrawx;
    }
    public int getLineY() {
        return this.linkedpixdrawy;
    }
    
    public void setOffset(int amount) {
        this.amt = amount;
    }
    public void setLineX(int x) {
        this.linkedpixdrawx = x;
    }
    public void setLineY(int y) {
        this.linkedpixdrawy = y;
    }
    
    public void updateGraphics(ArrayVisualizer ArrayVisualizer) {
        ArrayVisualizer.createVolatileImage();
        ArrayVisualizer.setMainRender();
        ArrayVisualizer.setExtraRender();
    }
    
    public void initializeVisuals(ArrayVisualizer ArrayVisualizer, VisualStyles VisualStyles) {
        this.Bars = new Bars();
        this.Circular = new Circular();
        this.Hoops = new Hoops();
        this.Mesh = new Mesh();
        this.Pixels = new Pixels();
        
        this.frames = 0;
        this.updateGraphics(ArrayVisualizer);
        ArrayVisualizer.updateFontSize();
        ArrayVisualizer.repositionFrames();
    }
    

    private WindowState checkWindowResizeAndReposition(ArrayVisualizer ArrayVisualizer) {
        boolean windowUpdate = false;
        boolean windowResize = false;
        
        if(ArrayVisualizer.currentHeight() != ArrayVisualizer.windowHeight()) {
            windowUpdate = true;
            windowResize = true;
        }
        if(ArrayVisualizer.currentWidth() != ArrayVisualizer.windowWidth()) {
            windowUpdate = true;
            windowResize = true;
        }
        if(ArrayVisualizer.currentX() != ArrayVisualizer.windowXCoordinate()) {
            windowUpdate = true;
        }
        if(ArrayVisualizer.currentY() != ArrayVisualizer.windowYCoordinate()) {
            windowUpdate = true;
        }
        
        return new WindowState(windowUpdate, windowResize);
    }
    
    public void updateVisuals(ArrayVisualizer ArrayVisualizer) {
        WindowState WindowState = checkWindowResizeAndReposition(ArrayVisualizer);
        
        if(WindowState.updated()) {
            ArrayVisualizer.repositionFrames();
            ArrayVisualizer.updateCoordinates();
            
            /*
            if(v != null && v.isVisible())
                v.reposition();
            */
            
            if(WindowState.resized()) {
                ArrayVisualizer.updateDimensions();
                this.updateGraphics(ArrayVisualizer);
            }
            
            ArrayVisualizer.updateFontSize();
        }
        
        ArrayVisualizer.renderBackground();
        
        //CURRENT = WINDOW
        //WINDOW = C VARIABLES
        
        this.xscl = (double) (ArrayVisualizer.currentWidth() - 40)  / ArrayVisualizer.getCurrentLength();
        this.yscl = (double) (ArrayVisualizer.currentHeight() - 96) / ArrayVisualizer.getCurrentLength();
        
        this.amt = 0; //TODO: rename to barCount
        
        this.linkedpixdrawx = 0;
        this.linkedpixdrawy = 0;
        
        this.frames++;

        this.dotw = (int) (2 * (ArrayVisualizer.currentWidth()  / 640.0));
        this.doth = (int) (2 * (ArrayVisualizer.currentHeight() / 480.0));
        this.dots = (this.dotw + this.doth) / 2; //TODO: Does multiply/divide by 2 like this cancel out??
        
        ArrayVisualizer.resetMainStroke();
    }
    
    public Color getIntColor(int i, int length) {
        return Color.getHSBColor(((float) i / length), 1.0F, 0.8F);
    }
    
    public void markBar(Graphics2D bar, boolean color, boolean rainbow, boolean analysis) {
        if(color || rainbow) {
            if(analysis) bar.setColor(Color.WHITE);
            else         bar.setColor(Color.BLACK);
        }
        else if(analysis)    bar.setColor(Color.BLUE);
        else                 bar.setColor(Color.RED);
    }
    private void markBarFancy(Graphics2D bar, boolean color, boolean rainbow) {
        if(!color && !rainbow) bar.setColor(Color.RED);
        else                   bar.setColor(Color.BLACK);
    }
    
    public void lineMark(Graphics2D line, double width, boolean color, boolean analysis) {
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
        if(color) line.setColor(Color.BLACK);
        else if(analysis) line.setColor(Color.BLUE);
        else line.setColor(Color.RED);
    }
    //TODO: Change name to markLineFancy
    public void lineFancy(Graphics2D line, double width) {
        line.setColor(Color.GREEN);
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
    }
    //TODO: Change name to clearLine
    public void lineClear(Graphics2D line, boolean color, int[] array, int i, int length, double width) {
        if(color) line.setColor(getIntColor(array[i], length)); 
        else line.setColor(Color.WHITE);
        line.setStroke(new BasicStroke((float) (3f * (width / 1280f))));
    }
    
    public void setRectColor(Graphics2D rect, boolean color, boolean analysis) {
        if(color) rect.setColor(Color.WHITE);
        else if(analysis) rect.setColor(Color.BLUE);
        else rect.setColor(Color.RED);
    }
    
    @SuppressWarnings("fallthrough")
    //The longer the array length, the more bars marked. Makes the visual easier to see when bars are thinner.
    public void colorMarkedBars(int logOfLen, int index, Highlights Highlights, Graphics2D mainRender, boolean colorEnabled, boolean rainbowEnabled, boolean analysis) {
        switch(logOfLen) {
        case 14: if(Highlights.containsPosition(index - 10)
                 || Highlights.containsPosition(index - 9)
                 || Highlights.containsPosition(index - 8))  markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
        case 13: if(Highlights.containsPosition(index - 7)
                 || Highlights.containsPosition(index - 6)
                 || Highlights.containsPosition(index - 5))  markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
        case 12: if(Highlights.containsPosition(index - 4)
                 || Highlights.containsPosition(index - 3))  markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
        case 11: if(Highlights.containsPosition(index - 2))  markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
        case 10: if(Highlights.containsPosition(index - 1))  markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
        default: if(Highlights.containsPosition(index))      markBar(mainRender, colorEnabled, rainbowEnabled, analysis);
        }
    }
    
    @SuppressWarnings("fallthrough")
    public void markHoops(int logOfLen, int index, Highlights Highlights, Graphics2D mainRender) {
        switch(logOfLen) {
        case 14: if(Highlights.containsPosition(index - 13)) mainRender.setColor(Color.BLACK);
        case 13: if(Highlights.containsPosition(index - 12)) mainRender.setColor(Color.BLACK);
        case 12: if(Highlights.containsPosition(index - 11)) mainRender.setColor(Color.BLACK);
        case 11: if(Highlights.containsPosition(index - 10)) mainRender.setColor(Color.BLACK);
        case 10: if(Highlights.containsPosition(index - 9))  mainRender.setColor(Color.BLACK);
        case 9:  if(Highlights.containsPosition(index - 8))  mainRender.setColor(Color.BLACK);
        case 8:  if(Highlights.containsPosition(index - 7))  mainRender.setColor(Color.BLACK);
        case 7:  if(Highlights.containsPosition(index - 6))  mainRender.setColor(Color.BLACK);
        case 6:  if(Highlights.containsPosition(index - 5))  mainRender.setColor(Color.BLACK);
        case 5:  if(Highlights.containsPosition(index - 4))  mainRender.setColor(Color.BLACK);
        case 4:  if(Highlights.containsPosition(index - 3))  mainRender.setColor(Color.BLACK);
        case 3:  if(Highlights.containsPosition(index - 2))  mainRender.setColor(Color.BLACK);
        case 2:  if(Highlights.containsPosition(index - 1))  mainRender.setColor(Color.BLACK);
        default: if(Highlights.containsPosition(index))      mainRender.setColor(Color.BLACK);
        }
    }
    
    @SuppressWarnings("fallthrough")
    public void drawFancyFinish(int logOfLen, int index, int position, Graphics2D mainRender, boolean colorEnabled, boolean rainbowEnabled) {
        switch(logOfLen) {
        case 14: if(index == position - 13) markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 13: if(index == position - 12) markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 12: if(index == position - 11) markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 11: if(index == position - 10) markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 10: if(index == position - 9)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 9:  if(index == position - 8)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 8:  if(index == position - 7)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 7:  if(index == position - 6)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 6:  if(index == position - 5)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 5:  if(index == position - 4)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 4:  if(index == position - 3)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 3:  if(index == position - 2)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        case 2:  if(index == position - 1)  markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        default: if(index == position)      markBarFancy(mainRender, colorEnabled, rainbowEnabled);
        }
    }
    
    @SuppressWarnings("fallthrough")
    public void drawFancyFinishLine(int logOfLen, int index, int position, Graphics2D mainRender, double width, boolean colorEnabled) {
        switch(logOfLen) {
        case 14: if(index == position - 13) lineMark(mainRender, width, colorEnabled, false);
        case 13: if(index == position - 12) lineMark(mainRender, width, colorEnabled, false);
        case 12: if(index == position - 11) lineMark(mainRender, width, colorEnabled, false);
        case 11: if(index == position - 10) lineMark(mainRender, width, colorEnabled, false);
        case 10: if(index == position - 9)  lineMark(mainRender, width, colorEnabled, false);
        case 9:  if(index == position - 8)  lineMark(mainRender, width, colorEnabled, false);
        case 8:  if(index == position - 7)  lineMark(mainRender, width, colorEnabled, false);
        case 7:  if(index == position - 6)  lineMark(mainRender, width, colorEnabled, false);
        case 6:  if(index == position - 5)  lineMark(mainRender, width, colorEnabled, false);
        case 5:  if(index == position - 4)  lineMark(mainRender, width, colorEnabled, false);
        case 4:  if(index == position - 3)  lineMark(mainRender, width, colorEnabled, false);
        case 3:  if(index == position - 2)  lineMark(mainRender, width, colorEnabled, false);
        case 2:  if(index == position - 1)  lineMark(mainRender, width, colorEnabled, false);
        default: if(index == position)      lineMark(mainRender, width, colorEnabled, false);
        }
    }

    @SuppressWarnings("fallthrough")
    public void drawFancyFinishHoops(int logOfLen, int index, int position, Graphics2D mainRender) {
        switch(logOfLen) {
        case 14: if(index == position - 13) mainRender.setColor(Color.BLACK);
        case 13: if(index == position - 12) mainRender.setColor(Color.BLACK);
        case 12: if(index == position - 11) mainRender.setColor(Color.BLACK);
        case 11: if(index == position - 10) mainRender.setColor(Color.BLACK);
        case 10: if(index == position - 9)  mainRender.setColor(Color.BLACK);
        case 9:  if(index == position - 8)  mainRender.setColor(Color.BLACK);
        case 8:  if(index == position - 7)  mainRender.setColor(Color.BLACK);
        case 7:  if(index == position - 6)  mainRender.setColor(Color.BLACK);
        case 6:  if(index == position - 5)  mainRender.setColor(Color.BLACK);
        case 5:  if(index == position - 4)  mainRender.setColor(Color.BLACK);
        case 4:  if(index == position - 3)  mainRender.setColor(Color.BLACK);
        case 3:  if(index == position - 2)  mainRender.setColor(Color.BLACK);
        case 2:  if(index == position - 1)  mainRender.setColor(Color.BLACK);
        default: if(index == position)      mainRender.setColor(Color.BLACK);
        }
    }
    
    public int getTriangleHeight(int length, double height) {
        switch(length) {
        case 2:   height *= 20;   break;
        case 4:   height *= 13;   break;
        case 8:   height *= 8;    break;
        case 16:
        case 32:  height *= 4.4;  break; 
        case 64:  height *= 2.3;  break;
        case 128: height *= 2.35; break;
        case 256: height *= 1.22; break;
        default:  height *= 1;
        }
        
        return (int) height;
    }
    
    public int getTrianglesPerRow(int length, int trianglesPerColumn) {
        int trianglesPerRow;
        
        switch(length) {
        case 32: 
        case 64:  trianglesPerRow = 4; break;
        case 128:
        case 256: trianglesPerRow = 8; break;
        default:  trianglesPerRow = Math.max(length / trianglesPerColumn, 2); 
        }
        
        return trianglesPerRow;
    }
    
    public void drawCircle(int[] array, ArrayVisualizer ArrayVisualizer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        Circular.drawVisual(array, ArrayVisualizer, this, mainRender, extraRender, Highlights);
    }
    public void drawHoops(int[] array, ArrayVisualizer ArrayVisualizer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        Hoops.drawVisual(array, ArrayVisualizer, this, mainRender, extraRender, Highlights);
    }
    public void drawMesh(int[] array, ArrayVisualizer ArrayVisualizer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        Mesh.drawVisual(array, ArrayVisualizer, this, mainRender, extraRender, Highlights);
    }
    public void drawBars(int[] array, ArrayVisualizer ArrayVisualizer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        Bars.drawVisual(array, ArrayVisualizer, this, mainRender, extraRender, Highlights);
    }
    public void drawPixels(int[] array, ArrayVisualizer ArrayVisualizer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        Pixels.drawVisual(array, ArrayVisualizer, this, mainRender, extraRender, Highlights);
    }
    
    public void drawVisual(VisualStyles VisualStyles, int[] array, ArrayVisualizer ArrayVisualizer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        VisualStyles.drawVisual(array, ArrayVisualizer, this, mainRender, extraRender, Highlights);
    }
}