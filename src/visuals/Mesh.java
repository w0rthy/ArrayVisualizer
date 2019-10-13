package visuals;

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

final public class Mesh {
    public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Graphics2D mainRender, Graphics2D extraRender, Highlights Highlights) {
        int trih = Renderer.getTriangleHeight(ArrayVisualizer.getCurrentLength(), ArrayVisualizer.windowHeight() / 20); //Height of triangles to use, Width will be scaled accordingly

        int tripercol = (ArrayVisualizer.windowHeight() / trih) * 2; //Triangles per column
        int triperrow = Renderer.getTrianglesPerRow(ArrayVisualizer.getCurrentLength(), tripercol); //Triangles per row

        double triw = (double) ArrayVisualizer.windowWidth() / triperrow; //Width of triangles to use

        double curx = 0;
        int cury = 15;

        int[] triptsx = new int[3];
        int[] triptsy = new int[3];

        for(int i = 0; i < ArrayVisualizer.getCurrentLength(); i++){
            if(Highlights.containsPosition(i) && ArrayVisualizer.getCurrentLength() != 2) {
                if(ArrayVisualizer.analysisEnabled()) mainRender.setColor(Color.WHITE);
                else                                  mainRender.setColor(Color.BLACK);
            }
            else {
                //TODO: Clean up this visual trick
                if(Highlights.fancyFinishActive() && (i < Highlights.getFancyFinishPosition() && i > Highlights.getFancyFinishPosition() - ArrayVisualizer.getLogBaseTwoOfLength())) {
                    mainRender.setColor(Color.GREEN);
                }
                else mainRender.setColor(Renderer.getIntColor(array[i], ArrayVisualizer.getCurrentLength()));
            }
            //If i/triperrow is even, then triangle points right, else left
            boolean direction = false;
            if((i / triperrow) % 2 == 0) direction = true;
            else direction = false;

            //Make the triangle
            if(!direction) {
                //Pointing right
                triptsx[0] = (int) curx;
                triptsx[1] = (int) curx;
                curx      +=       triw;
                triptsx[2] = (int) curx;

                triptsy[0] = cury;
                triptsy[2] = cury + (trih / 2);
                triptsy[1] = cury +  trih;
            }else{
                //Pointing left
                triptsx[2] = (int) curx;
                curx      +=       triw;
                triptsx[0] = (int) curx;
                triptsx[1] = (int) curx;

                triptsy[0] = cury;
                triptsy[2] = cury + (trih / 2);
                triptsy[1] = cury +  trih;
            }

            //Draw it
            mainRender.fillPolygon(triptsx, triptsy, triptsx.length);

            //If at the end of a row, reset curx
            //(i != 0 || i != currentLen - 1)
            if((i + 1) % triperrow == 0){
                curx = 0;
                cury += trih / 2;
            }
        }
    }
}