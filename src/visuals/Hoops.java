package visuals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.ArrayVisualizer;
import templates.Visual;
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

final public class Hoops extends Visual {
    public Hoops(ArrayVisualizer ArrayVisualizer) {
        super(ArrayVisualizer);
    }

    @SuppressWarnings("fallthrough")
    public static void markHoops(int logOfLen, int index, Highlights Highlights, Graphics2D mainRender) {
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
    public static void drawFancyFinishHoops(int logOfLen, int index, int position, Graphics2D mainRender) {
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
    
    //TODO: Fix scaling to ensure Hoops close at the center
    //TODO: Too many rings highlighted at once!!
    @Override
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
        this.mainRender.setStroke(new BasicStroke(1.0f)); //thin strokes significantly increased performance
        
                                                     //This StackOverflow thread may be related: https://stackoverflow.com/questions/47102734/performances-issue-when-drawing-dashed-line-in-java

        double diameter = 2.0;
        double diamstep = Math.min(Renderer.getXScale(), Renderer.getYScale());

        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            if(Highlights.fancyFinishActive()) {
                if(i < Highlights.getFancyFinishPosition()) {
                    this.mainRender.setColor(Color.GREEN);
                }
                else this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));

                drawFancyFinishHoops(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), this.mainRender);
            }
            else {
                this.mainRender.setColor(getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
            }
            
            if(ArrayVisualizer.getCurrentLength() != 2) {
                markHoops(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights, this.mainRender);
            }

            int radius = (int) (diameter / 2.0);

            this.mainRender.drawOval(ArrayVisualizer.windowHalfWidth()  - radius,
                                ArrayVisualizer.windowHalfHeight() - radius + 12,
                                (int) diameter,
                                (int) diameter);
            
            diameter += diamstep;
        }
    }
}