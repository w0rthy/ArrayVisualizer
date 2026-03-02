package templates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;

public abstract class Visual {
    protected Graphics2D mainRender;
    protected Graphics2D extraRender;
    
    public Visual(ArrayVisualizer ArrayVisualizer) {
        this.updateRender(ArrayVisualizer);
    }
    
    public void updateRender(ArrayVisualizer ArrayVisualizer) {
        this.mainRender = ArrayVisualizer.getMainRender();
        this.extraRender = ArrayVisualizer.getExtraRender();
    }
    
    public static Color getIntColor(int i, int length) {
        return Color.getHSBColor(((float) i / length), 1.0F, 0.8F);
    }
    
    public static void markBar(Graphics2D bar, boolean color, boolean rainbow, boolean analysis) {
        if(color || rainbow) {
            /*
            if(analysis) bar.setColor(Color.WHITE);
            else         bar.setColor(Color.BLACK);
            */
            if(analysis) bar.setColor(Color.LIGHT_GRAY);
            else         bar.setColor(Color.WHITE);
        }
        else if(analysis)    bar.setColor(Color.BLUE);
        else                 bar.setColor(Color.RED);
    }
    private static void markBarFancy(Graphics2D bar, boolean color, boolean rainbow) {
        if(!color && !rainbow) bar.setColor(Color.RED);
        else                   bar.setColor(Color.BLACK);
    }
    
    public static void lineMark(Graphics2D line, double width, boolean color, boolean analysis) {
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
        if(color) line.setColor(Color.BLACK);
        else if(analysis) line.setColor(Color.BLUE);
        else line.setColor(Color.RED);
    }
    //TODO: Change name to markLineFancy
    public static void lineFancy(Graphics2D line, double width) {
        line.setColor(Color.GREEN);
        line.setStroke(new BasicStroke((float) (9f * (width / 1280f))));
    }
    //TODO: Change name to clearLine
    public static void lineClear(Graphics2D line, boolean color, int[] array, int i, int length, double width) {
        if(color) line.setColor(getIntColor(array[i], length)); 
        else line.setColor(Color.WHITE);
        line.setStroke(new BasicStroke((float) (3f * (width / 1280f))));
    }
    
    public static void setRectColor(Graphics2D rect, boolean color, boolean analysis) {
        if(color) rect.setColor(Color.WHITE);
        else if(analysis) rect.setColor(Color.BLUE);
        else rect.setColor(Color.RED);
    }
    
    @SuppressWarnings("fallthrough")
    //The longer the array length, the more bars marked. Makes the visual easier to see when bars are thinner.
    public static void colorMarkedBars(int logOfLen, int index, Highlights Highlights, Graphics2D mainRender, boolean colorEnabled, boolean rainbowEnabled, boolean analysis) {
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
    public static void drawFancyFinish(int logOfLen, int index, int position, Graphics2D mainRender, boolean colorEnabled, boolean rainbowEnabled) {
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
    public static void drawFancyFinishLine(int logOfLen, int index, int position, Graphics2D mainRender, double width, boolean colorEnabled) {
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
    
    public abstract void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights);
}