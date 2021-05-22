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
    
    protected volatile int categoryCount;
    
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
    
    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int length, double speed) throws InterruptedException {
        Delays.setSleepRatio(2.5);
        
        if(length != ArrayVisualizer.getCurrentLength()) {
            ArrayVisualizer.setCurrentLength(length);
        }
        
        ArrayManager.refreshArray(array, ArrayVisualizer.getCurrentLength(), this.ArrayVisualizer);
        
        ArrayVisualizer.setHeading(sort.getRunAllID() + " (Sort " + this.sortNumber + " of " + this.sortCount + ")");
        Delays.setSleepRatio(speed);
        
        Timer.enableRealTimer();
        
        sort.runSort(array, ArrayVisualizer.getCurrentLength(), bucketCount);
        
        ArrayVisualizer.endSort();
        Thread.sleep(1000);
        
        this.sortNumber++;
    }
    
    protected abstract void executeSortList(int[] array) throws Exception;
    protected abstract void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception;

    public synchronized void reportCategorySorts(int[] array) throws Exception {
        this.runThread(array, 0, 0, false);
    }
    
    public synchronized void reportAllSorts(int[] array, int current, int total) throws Exception {
        this.runThread(array, current, total, true);
    }
    
    public int getSortCount() {
        return this.sortCount;
    }
    
    public int getCategoryCount() {
        return this.categoryCount;
    }
}