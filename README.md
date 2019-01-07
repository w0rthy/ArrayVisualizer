# ArrayVisualizer
Sorting Visualizer with 6 different views and 14 included sorting algorithms

Outdated videos:

Classic:
https://www.youtube.com/watch?v=QmOtL6pPcI0

Color Circle:
https://www.youtube.com/watch?v=IjIViETya5k

Dots:
https://www.youtube.com/watch?v=zcO8uxg_Spw

Spiral Dots:
https://www.youtube.com/watch?v=jrHLeKwMzfI

# How to use

### Build 
```
mkdir -p dist target
javac src/array/visualizer/ArrayVisualizer.java -sourcepath src -d target/
jar -cvfm dist/ArrayVisualizer.jar manifest.mf -C target/ ./
```

### Run
```
java -jar dist/ArrayVisualizer.jar
```