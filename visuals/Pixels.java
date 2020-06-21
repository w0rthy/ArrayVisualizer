package visuals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

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

final public class Pixels {
    private boolean drawRect;
    
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        //TODO: Fix zeroth line approaching infinity
        if(ArrayVisualizer.linesEnabled()) {
            for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
                int y = 0;
                int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();

                if(width > 0) {
                    y = (int) ((ArrayVisualizer.windowHeight() - 20) - (Math.max(array[i], 1) * Renderer.getYScale()));
                    
                    if(i > 0) {
                        if(Highlights.fancyFinishActive()) {
                            if(i < Highlights.getFancyFinishPosition()) {
                                Renderer.lineFancy(mainRender, ArrayVisualizer.currentWidth());
                            }
                            else Renderer.lineClear(mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());

                            Renderer.drawFancyFinishLine(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled());
                        }
                        else if(Highlights.containsPosition(i) && ArrayVisualizer.getCurrentLength() != 2) {
                            Renderer.lineMark(mainRender, ArrayVisualizer.currentWidth(), ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                        }
                        else Renderer.lineClear(mainRender, ArrayVisualizer.colorEnabled(), array, i, ArrayVisualizer.getCurrentLength(), ArrayVisualizer.currentWidth());

                        mainRender.drawLine(Renderer.getOffset() + 20, y, Renderer.getLineX() + 20, Renderer.getLineY());
                    }
                    Renderer.setLineX(Renderer.getOffset());
                    Renderer.setLineY(y);
                }
                Renderer.setOffset(Renderer.getOffset() + width);
            }
        }
        else {
            for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
                if(i < Highlights.getFancyFinishPosition()) {
                    mainRender.setColor(Color.GREEN);
                }
                else if(i == Highlights.getFancyFinishPosition() && Highlights.fancyFinishActive()) {
                    if(ArrayVisualizer.colorEnabled()) {
                        mainRender.setColor(Color.WHITE);
                    }
                    else mainRender.setColor(Color.RED);
                }
                else if(ArrayVisualizer.colorEnabled()) {
                    mainRender.setColor(Renderer.getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
                }
                else mainRender.setColor(Color.WHITE);

                int y = 0;
                int width = (int) (Renderer.getXScale() * (i + 1)) - Renderer.getOffset();

                if(Highlights.containsPosition(i) && ArrayVisualizer.getCurrentLength() != 2) {
                    Renderer.setRectColor(extraRender, ArrayVisualizer.colorEnabled(), ArrayVisualizer.analysisEnabled());
                    drawRect = true;
                }
                else drawRect = false;

                if(width > 0) {
                    y = (int) ((ArrayVisualizer.windowHeight() - 20) - (array[i] * Renderer.getYScale()));
                    mainRender.fillRect(Renderer.getOffset() + 20, y, Renderer.getDotDimensions(), Renderer.getDotDimensions());
                    
                    if(drawRect) {
                        extraRender.setStroke(ArrayVisualizer.getThickStroke());
                        
                        if(Highlights.fancyFinishActive()) {
                            extraRender.fillRect(Renderer.getOffset() + 10, y - 10, Renderer.getDotDimensions() + 20, Renderer.getDotDimensions() + 20);
                        }
                        else {
                            extraRender.drawRect(Renderer.getOffset() + 10, y - 10, Renderer.getDotDimensions() + 20, Renderer.getDotDimensions() + 20);
                        }
                        
                        extraRender.setStroke(new BasicStroke(3f * (ArrayVisualizer.currentWidth() / 1280f))); //TODO: This BasicStroke should have a getDefaultStroke() method
                    }
                }
                Renderer.setOffset(Renderer.getOffset() + width);
            }
        }
    }
}