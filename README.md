# ArrayVisualizer
Sorting Visualizer/Audiolizer

## Videos
[Vertical Pyramid](https://www.youtube.com/watch?v=QOYcpGnHH0g)

[Dynamic Hoops](https://www.youtube.com/watch?v=S0RtR2Yllzk)

[TriMesh](https://www.youtube.com/watch?v=Zc__8qaLfJk)

[Horizontal Pyramid](https://www.youtube.com/watch?v=vmT3XUBoxiQ)

[Static Hoops](https://www.youtube.com/watch?v=jXs1y3tCKQg)

[Christmas Tree](https://www.youtube.com/watch?v=xY1tiHzo8mE)

[Variable Width TriMesh](https://www.youtube.com/watch?v=0tr6AtLu4pg)

[Color Circle](https://www.youtube.com/watch?v=sVYtGyPiGik)

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
=======