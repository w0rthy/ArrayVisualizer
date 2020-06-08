package visuals;

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

public enum VisualStyles {
    BARS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
            ArrayVisualizer.getVisuals()[0].drawVisual(array, ArrayVisualizer, Renderer, Highlights);
        }
    },
    CIRCULAR {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
            ArrayVisualizer.getVisuals()[1].drawVisual(array, ArrayVisualizer, Renderer, Highlights);
        }
    },
    HOOPS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
            ArrayVisualizer.getVisuals()[2].drawVisual(array, ArrayVisualizer, Renderer, Highlights);
        }
    },
    MESH {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
            ArrayVisualizer.getVisuals()[3].drawVisual(array, ArrayVisualizer, Renderer, Highlights);
        }
    },
    PIXELS {
        @Override
        public void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights) {
            ArrayVisualizer.getVisuals()[4].drawVisual(array, ArrayVisualizer, Renderer, Highlights);
        }
    };
    
    public VisualStyles getCurrentVisual() {
        return this;
    }

    public abstract void drawVisual(int[] array, ArrayVisualizer ArrayVisualizer, Renderer Renderer, Highlights Highlights);
}