package utils;

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

public enum Shuffles {
    RANDOM {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = currentLen; i > 1; i--){
                int temp = (int)(Math.random()*i);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i-1], array[temp]);
                Writes.swap(array, i-1, temp, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    RANDOM1 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < currentLen; i++){
                int temp = (int)(Math.random()*(currentLen - i)) + i;
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    RANDOM2 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 1; i <= currentLen; i++){
                int temp = (int)(Math.random()*i);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i-1], array[temp]);
                Writes.swap(array, i-1, temp, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    RANDOMWRONG {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < currentLen; i++){
                int temp = (int)(Math.random()*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    ALMOSTR {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < Math.max(currentLen / 20, 1); i++){
                int temp = (int)(Math.random()*currentLen);
                int temp2 = (int)(Math.random()*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[temp], array[temp2]);
                Writes.swap(array, temp, temp2, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[left], array[right]);
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    ALMOST2R {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < Math.max(Math.min((currentLen + 3) / 4, 64), 1); i++){
                int temp = (int)(Math.random()*currentLen);
                int temp2 = (int)(Math.random()*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[temp], array[temp2]);
                Writes.swap(array, temp, temp2, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[left], array[right]);
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    NEARLYR {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for (int n = 6; n > 0; n--){
                int u = 0;
                int i = n;
                while(i<currentLen){
                    if ((int)(Math.random()*2)==0){
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i-n], array[i]);
                        Writes.swap(array, i - n, i, 0, true, false);
                        if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);
                    }
                    i++;
                    u = (u + 1) % n;
                    if (u == 0){
                        i += n;
                    }
                }
                u = 0;
                i = (2*n);
                while(i<currentLen){
                    if ((int)(Math.random()*2)==0){
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i-n], array[i]);
                        Writes.swap(array, i - n, i, 0, true, false);
                        if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);
                    }
                    i++;
                    u = (u + 1) % n;
                    if (u == 0){
                        i += n;
                    }
                }
            }
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[left], array[right]);
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    REVERSE {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[left], array[right]);
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    SIMILAR {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < currentLen - 8; i++) {
                array[i] = currentLen / 2;
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            for(int i = currentLen - 8; i < currentLen; i++) {
                array[i] = (int) (Math.random() < 0.5 ? currentLen * 0.75 : currentLen * 0.25);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            for(int i = 0; i < currentLen; i++){
                int temp = (int)(Math.random()*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    ALMOST {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < Math.max(currentLen / 20, 1); i++){
                int temp = (int)(Math.random()*currentLen);
                int temp2 = (int)(Math.random()*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[temp], array[temp2]);
                Writes.swap(array, temp, temp2, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
        }
    },
    ALMOST2 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < Math.max(Math.min((currentLen + 3) / 4, 64), 1); i++){
                int temp = (int)(Math.random()*currentLen);
                int temp2 = (int)(Math.random()*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[temp], array[temp2]);
                Writes.swap(array, temp, temp2, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(2);
            }
        }
    },
    NEARLY {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for (int n = 6; n > 0; n--){
                int u = 0;
                int i = n;
                while(i<currentLen){
                    if ((int)(Math.random()*2)==0){
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i-n], array[i]);
                        Writes.swap(array, i - n, i, 0, true, false);
                        if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);
                    }
                    i++;
                    u = (u + 1) % n;
                    if (u == 0){
                        i += n;
                    }
                }
                u = 0;
                i = (2*n);
                while(i<currentLen){
                    if ((int)(Math.random()*2)==0){
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i-n], array[i]);
                        Writes.swap(array, i - n, i, 0, true, false);
                        if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(0.5);
                    }
                    i++;
                    u = (u + 1) % n;
                    if (u == 0){
                        i += n;
                    }
                }
            }
        }
    },
    ALREADY {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < currentLen; i++) {
                if(ArrayVisualizer.shuffleEnabled()) {
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    FEW2 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            int few = 2;
            for(int i = 0; i < currentLen; i++) {
		array[i] = ((((((i*few)/currentLen)*2)+1)*currentLen)/(few*2));
                int temp = (int)(Math.random()*(i+1));
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    FEW4 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            int few = 4;
            for(int i = 0; i < currentLen; i++) {
		array[i] = ((((((i*few)/currentLen)*2)+1)*currentLen)/(few*2));
                int temp = (int)(Math.random()*(i+1));
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    FEW8 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            int few = 8;
            for(int i = 0; i < currentLen; i++) {
		array[i] = ((((((i*few)/currentLen)*2)+1)*currentLen)/(few*2));
                int temp = (int)(Math.random()*(i+1));
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    FEW16 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            int few = 16;
            for(int i = 0; i < currentLen; i++) {
		array[i] = ((((((i*few)/currentLen)*2)+1)*currentLen)/(few*2));
                int temp = (int)(Math.random()*(i+1));
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
/*    TESTING2 {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for (int left = 0, right = currentLen - 1; left < right; left++, right--) {
                // swap the values at the left and right indices
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[left], array[right]);
                Writes.swap(array, left, right, 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
            for (int i = currentLen/3; i < 2*(currentLen/3); i++){
                array[i] = currentLen/3;
            }
        }
    },*/
    TESTING {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            
            for(int i = 0; i < currentLen/2; i++) {
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[i+(currentLen/2)]);
                Writes.swap(array, i, i+(currentLen/2), 0, true, false);
                
                if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
            }
        }
    },
    BITREV {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
                int j = 0;
                int k = i;
                for (int l = (int)(Math.ceil(Math.log(currentLen)/Math.log(2.0))); l > 0; l--){
                    j *= 2;
                    j += k % 2;
                    k /= 2;
                }
                if (j > i && j < currentLen){
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[j]);
                    Writes.swap(array, i, j, 0, true, false);
                    if(ArrayVisualizer.shuffleEnabled()) Delays.sleep(1);
                }
            }
        }
    },
    SHUFCUBIC {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
		array[i] = 3*(int)(((Math.pow((((i+0.0)/currentLen)*2.0)-1.0, 3)+1.0)/6.0)*currentLen);
                int temp = (int)(Math.random()*(i+1));
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    SHUFQUINT {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
		array[i] = 3*(int)(((Math.pow((((i+0.0)/currentLen)*2.0)-1.0, 5)+1.0)/6.0)*currentLen);
                int temp = (int)(Math.random()*(i+1));
                if(ArrayVisualizer.shuffleEnabled()) Reads.play2(array[i], array[temp]);
                Writes.swap(array, i, temp, 0, true, false);
                if(ArrayVisualizer.shuffleEnabled()) {
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    SORTCUBIC {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
		array[i] = 3*(int)(((Math.pow((((i+0.0)/currentLen)*2.0)-1.0, 3)+1.0)/6.0)*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) {
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    SORTQUINT {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
		array[i] = 3*(int)(((Math.pow((((i+0.0)/currentLen)*2.0)-1.0, 5)+1.0)/6.0)*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) {
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    REVCUBIC {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
		array[i] = 3*(int)(((Math.pow((((currentLen-i-1.0)/currentLen)*2.0)-1.0, 3)+1.0)/6.0)*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) {
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    },
    REVQUINT {
        @Override
        public void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes) {
            int currentLen = ArrayVisualizer.getCurrentLength();
            for(int i = 0; i < currentLen; i++) {
		array[i] = 3*(int)(((Math.pow((((currentLen-i-1.0)/currentLen)*2.0)-1.0, 5)+1.0)/6.0)*currentLen);
                if(ArrayVisualizer.shuffleEnabled()) {
                if(ArrayVisualizer.shuffleEnabled()) Reads.play(array[i]);
                    Highlights.markArray(1, i);
                    Delays.sleep(1);
                }
            }
        }
    };

    public abstract void shuffleArray(int[] array, ArrayVisualizer ArrayVisualizer, Delays Delays, Highlights Highlights, Writes Writes);
}