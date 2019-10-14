package visuals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import main.ArrayVisualizer;
import utils.Highlights;
import utils.Renderer;

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

final public class Circular {
    final private double CIRC_HEIGHT_RATIO = (9/6.0843731432) * (16/9d);
    final private double CIRC_WIDTH_RATIO = (16/6.0843731432) * (16/9d);
    
    private boolean drawRect;
    
    // The reason we use cosine with height (expressed in terms of y) and sine with width (expressed in terms of x) is because our circles are rotated 90 degrees.
    // After that rotation, sine is on the x-axis and cosine is on the y-axis.
    
    // If we we use sine with height and cosine with width, the sorts would start from the right side of the circle,
    // just like the unit circle from trigonometry.
    
    private static double getSinOfDegrees(double d, int halfCirc) {
        return Math.sin((d * Math.PI) / halfCirc);
    }
    
    private static double getCosOfDegrees(double d, int halfCirc) {
        return Math.cos((d * Math.PI) / halfCirc);
    }
    
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++){
            if(i < Highlights.getFancyFinishPosition()) {
                mainRender.setColor(Color.getHSBColor((1f/3f), 1f, 0.8f));
            }
            else if(!ArrayVisualizer.colorEnabled() && (ArrayVisualizer.spiralEnabled() || ArrayVisualizer.distanceEnabled() || ArrayVisualizer.pixelsEnabled())) {
                mainRender.setColor(Color.WHITE);
            }
            else mainRender.setColor(Renderer.getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
            
            if(Highlights.fancyFinishActive()) {
                Renderer.drawFancyFinish(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), mainRender, ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.colorEnabled());
            }
            else {
                if(ArrayVisualizer.pointerActive()) {
                    if(Highlights.containsPosition(i)) {
                        if(ArrayVisualizer.analysisEnabled()) {
                            extraRender.setColor(Color.GRAY);
                        }
                        else {
                            extraRender.setColor(Color.WHITE);
                        }

                        //Create new Polygon for the pointer
                        Polygon pointer = new Polygon();

                        //Calculate radians
                        double degrees = 360 * ((double) i / ArrayVisualizer.getCurrentLength());
                        double radians = Math.toRadians(degrees);
                        
                        int pointerWidthRatio  = (int) (ArrayVisualizer.windowHalfWidth()  / CIRC_WIDTH_RATIO);
                        int pointerHeightRatio = (int) (ArrayVisualizer.windowHalfHeight() / CIRC_HEIGHT_RATIO);
                        
                        //First step: draw a triangle
                        int[] pointerXValues = {pointerWidthRatio - 10,
                                                pointerWidthRatio,
                                                pointerWidthRatio + 10};
                        
                        int[] pointerYValues = {pointerHeightRatio - 10,
                                                pointerHeightRatio + 10,
                                                pointerHeightRatio - 10};
                        
                        //Second step: rotate triangle (https://en.wikipedia.org/wiki/Rotation_matrix)
                        for(int j = 0; j < pointerXValues.length; j++) {
                            double x = pointerXValues[j] - pointerWidthRatio;
                            double y = pointerYValues[j] - pointerHeightRatio;
                            
                            pointerXValues[j] = (int) (pointerWidthRatio
                                                    + x*Math.cos(radians)
                                                    - y*Math.sin(radians));
                            pointerYValues[j] = (int) (pointerHeightRatio
                                                    + x*Math.sin(radians)
                                                    + y*Math.cos(radians));
                        }
                        
                        for(int j = 0; j < pointerXValues.length; j++) {
                            pointer.addPoint(pointerXValues[j], pointerYValues[j]);
                        }
                                                
                        extraRender.fillPolygon(pointer);                        
                    }
                }
                else if(ArrayVisualizer.getCurrentLength() != 2){
                    Renderer.colorMarkedBars(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights, mainRender, ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                }
            }

            if(ArrayVisualizer.distanceEnabled()) {
                //TODO: Rewrite this abomination
                double len = ((ArrayVisualizer.getCurrentLength() / 2d) - Math.min(Math.min(Math.abs(i - array[i]), Math.abs(i - array[i] + ArrayVisualizer.getCurrentLength())), Math.abs(i - array[i] - ArrayVisualizer.getCurrentLength()))) / (ArrayVisualizer.getCurrentLength() / 2d);

                if(ArrayVisualizer.pixelsEnabled()) {
                    int linkedpixX = ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowWidth()  - 64) / CIRC_WIDTH_RATIO  * len)) + Renderer.getDotWidth()  / 2;
                    int linkedpixY = ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowHeight() - 96) / CIRC_HEIGHT_RATIO * len)) + Renderer.getDotHeight() / 2;

                    if(ArrayVisualizer.linesEnabled()) {
                        if(i > 0) {
                            if(Highlights.fancyFinishActive()) {
                                if(i < Highlights.getFancyFinishPosition()) {
                                    Renderer.lineFancy(mainRender, ArrayVisualizer.currentWidth());
                                }
                                else {
                                    Renderer.lineClear(mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());
                                }

                                Renderer.drawFancyFinishLine(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled());
                            }
                            else {
                                if(Highlights.containsPosition(i)) {
                                    Renderer.lineMark(mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                                }
                                else Renderer.lineClear(mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());
                            }
                            mainRender.drawLine(linkedpixX, linkedpixY, Renderer.getLineX(), Renderer.getLineY());
                        }
                        Renderer.setLineX(linkedpixX);
                        Renderer.setLineY(linkedpixY);
                    }
                    else {
                        if(Highlights.containsPosition(i)) {
                            Renderer.setRectColor(extraRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                            drawRect = true;
                        }
                        else drawRect = false;

                        if(drawRect) {
                            extraRender.setStroke(ArrayVisualizer.getThickStroke());
                            if(Highlights.fancyFinishActive()) {
                                extraRender.fillRect((linkedpixX - Renderer.getDotWidth() / 2) - 10, (linkedpixY - Renderer.getDotHeight() / 2) - 10, Renderer.getDotWidth() + 20, Renderer.getDotHeight() + 20);
                            }
                            else {
                                extraRender.drawRect((linkedpixX - Renderer.getDotWidth() / 2) - 10, (linkedpixY - Renderer.getDotHeight() / 2) - 10, Renderer.getDotWidth() + 20, Renderer.getDotHeight() + 20);
                            }
                            extraRender.setStroke(new BasicStroke(3f * (ArrayVisualizer.currentWidth() / 1280f)));
                        }
                        mainRender.fillRect(linkedpixX - Renderer.getDotWidth() / 2, linkedpixY - Renderer.getDotHeight() / 2, Renderer.getDotWidth(), Renderer.getDotHeight());
                    }
                }
                else {
                    Polygon p = new Polygon();
                    
                    p.addPoint(ArrayVisualizer.windowHalfWidth(),
                               ArrayVisualizer.windowHalfHeight());
                    
                    p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * (((ArrayVisualizer.currentWidth() - 64)  / CIRC_WIDTH_RATIO)  * len)),
                               ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * (((ArrayVisualizer.currentHeight() - 96) / CIRC_HEIGHT_RATIO) * len)));
                    
                    p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * (((ArrayVisualizer.currentWidth()  - 64) / CIRC_WIDTH_RATIO)  * len)),
                               ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * (((ArrayVisualizer.currentHeight() - 96) / CIRC_HEIGHT_RATIO) * len)));
                    
                    mainRender.fillPolygon(p);
                }
            }
            else if(ArrayVisualizer.spiralEnabled()) {
                if(ArrayVisualizer.pixelsEnabled()) {
                    if(ArrayVisualizer.linesEnabled()) {
                        if(i > 0) {
                            if(Highlights.fancyFinishActive()) {
                                if(i < Highlights.getFancyFinishPosition()) {
                                    Renderer.lineFancy(mainRender, ArrayVisualizer.currentWidth());
                                }
                                else Renderer.lineClear(mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());

                                Renderer.drawFancyFinishLine(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled());
                            }
                            else {
                                if(Highlights.containsPosition(i)) {
                                    Renderer.lineMark(mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                                }
                                else Renderer.lineClear(mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());
                            }
                            mainRender.drawLine(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowWidth()  - 64) / 3.0) * array[i]) / ArrayVisualizer.getCurrentLength())), 
                                                ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowHeight() - 96) / 2.0) * array[i]) / ArrayVisualizer.getCurrentLength())), 
                                                Renderer.getLineX(),
                                                Renderer.getLineY());
                        }
                        Renderer.setLineX(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowWidth()  - 64) / 3.0) * array[i]) / ArrayVisualizer.getCurrentLength())));
                        Renderer.setLineY(ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowHeight() - 96) / 2.0) * array[i]) / ArrayVisualizer.getCurrentLength())));      
                    }
                    else {
                        if(Highlights.containsPosition(i)) {
                            Renderer.setRectColor(extraRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                            drawRect = true;
                        }
                        else drawRect = false;

                        int rectx = ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * (((((ArrayVisualizer.windowWidth()  - 64) / 3.0) * array[i]) / ArrayVisualizer.getCurrentLength())));
                        int recty = ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * (((((ArrayVisualizer.windowHeight() - 96) / 2.0) * array[i]) / ArrayVisualizer.getCurrentLength())));

                        mainRender.fillRect(rectx, recty, Renderer.getDotWidth(), Renderer.getDotHeight());

                        if(drawRect) {
                            extraRender.setStroke(ArrayVisualizer.getThickStroke());
                            if(Highlights.fancyFinishActive()) {
                                extraRender.fillRect(rectx - 10, recty - 10, Renderer.getDotWidth() + 20, Renderer.getDotHeight() + 20);
                            }
                            else {
                                extraRender.drawRect(rectx - 10, recty - 10, Renderer.getDotWidth() + 20, Renderer.getDotHeight() + 20);
                            }
                            extraRender.setStroke(new BasicStroke(3f * (ArrayVisualizer.currentWidth() / 1280f)));
                        }
                    }
                }
                else {
                    if(Highlights.containsPosition(i)) {
                        Renderer.markBar(mainRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.rainbowEnabled(), ArrayVisualizer.analysisEnabled());
                    }

                    Polygon p = new Polygon();
                    
                    p.addPoint(ArrayVisualizer.windowHalfWidth(),
                               ArrayVisualizer.windowHalfHeight());
                    
                    p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowWidth()  - 64) / 3.0) * array[i]) / ArrayVisualizer.getCurrentLength())),
                               ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowHeight() - 96) / 2.0) * array[i]) / ArrayVisualizer.getCurrentLength())));
                    
                    p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowWidth()  - 64) / 3.0) * array[Math.min(i + 1, ArrayVisualizer.getCurrentLength() - 1)]) / ArrayVisualizer.getCurrentLength())),
                               ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * ((((ArrayVisualizer.windowHeight() - 96) / 2.0) * array[Math.min(i + 1, ArrayVisualizer.getCurrentLength() - 1)]) / ArrayVisualizer.getCurrentLength())));
                    
                    mainRender.fillPolygon(p);
                }
            }
            else {
                Polygon p = new Polygon();
                
                p.addPoint(ArrayVisualizer.windowHalfWidth(),
                           ArrayVisualizer.windowHalfHeight());
                
                p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowWidth()  - 64) / CIRC_WIDTH_RATIO)),
                           ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowHeight() - 96) / CIRC_HEIGHT_RATIO)));
                
                p.addPoint(ArrayVisualizer.windowHalfWidth()  + (int) (Circular.getSinOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowWidth()  - 64) / CIRC_WIDTH_RATIO)),
                           ArrayVisualizer.windowHalfHeight() - (int) (Circular.getCosOfDegrees(i + 1, ArrayVisualizer.halfCircle()) * ((ArrayVisualizer.windowHeight() - 96) / CIRC_HEIGHT_RATIO)));
                
                mainRender.fillPolygon(p);
            }
        }
    }   
}