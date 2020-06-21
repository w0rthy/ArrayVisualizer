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

final public class Hoops {
    //TODO: Fix scaling to ensure Hoops close at the center
    //TODO: Too many rings highlighted at once!!
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        mainRender.setStroke(new BasicStroke(1.0f)); //thin strokes significantly increased performance
        
                                                     //This StackOverflow thread may be related: https://stackoverflow.com/questions/47102734/performances-issue-when-drawing-dashed-line-in-java

        double diameter = 2.0;
        double diamstep = Math.min(Renderer.getXScale(), Renderer.getYScale());

        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++) {
            if(Highlights.fancyFinishActive()) {
                if(i < Highlights.getFancyFinishPosition()) {
                    mainRender.setColor(Color.GREEN);
                }
                else mainRender.setColor(Renderer.getIntColor(array[i], ArrayVisualizer.getCurrentLength()));

                Renderer.drawFancyFinishHoops(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights.getFancyFinishPosition(), mainRender);
            }
            else {
                mainRender.setColor(Renderer.getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
            }
            
            if(ArrayVisualizer.getCurrentLength() != 2) {
                Renderer.markHoops(ArrayVisualizer.getLogBaseTwoOfLength(), i, Highlights, mainRender);
            }

            int radius = (int) (diameter / 2.0);

            mainRender.drawOval(ArrayVisualizer.windowHalfWidth()  - radius,
                                ArrayVisualizer.windowHalfHeight() - radius + 12,
                                (int) diameter,
                                (int) diameter);
            
            diameter += diamstep;
        }
    }
}