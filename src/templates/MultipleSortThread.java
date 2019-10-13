package templates;

import main.ArrayManager;
import main.ArrayVisualizer;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Sounds;
import utils.Timer;
import utils.Writes;

public abstract class MultipleSortThread {
    protected ArrayManager ArrayManager;
    protected ArrayVisualizer ArrayVisualizer;
    protected Delays Delays;
    protected Highlights Highlights;
    protected Reads Reads;
    protected Writes Writes;
    protected Sounds Sounds;
    protected Timer Timer;
    
    protected volatile int sortCount;
    protected volatile int sortNumber;
    
    public MultipleSortThread(ArrayVisualizer ArrayVisualizer) {
        this.ArrayVisualizer = ArrayVisualizer;
        this.ArrayManager = ArrayVisualizer.getArrayManager();
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Reads = ArrayVisualizer.getReads();
        this.Writes = ArrayVisualizer.getWrites();
        this.Sounds = ArrayVisualizer.getSounds();
        this.Timer = ArrayVisualizer.getTimer();
    }
    
    protected synchronized void RunIndividualSort(Sort sort, int bucketCount, int[] array, double speed) throws InterruptedException {
        Delays.setSleepRatio(2.5);
        ArrayManager.refreshArray(array, ArrayVisualizer.getCurrentLength(), this.ArrayVisualizer);
        
        ArrayVisualizer.setHeading(sort.getRunAllID() + " (Sort " + this.sortNumber + " of " + this.sortCount + ")");
        Delays.setSleepRatio(speed);
        
        Timer.enableRealTimer();
        
        sort.runSort(array, ArrayVisualizer.getCurrentLength(), bucketCount);
        
        ArrayVisualizer.endSort();
        Thread.sleep(1000);
        
        this.sortNumber++;
    }
}