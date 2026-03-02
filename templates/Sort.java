package templates;

import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

public abstract class Sort {
    private String sortPromptID;
    private String runAllID;
    private String reportSortID;
    
    private String category;
    
    private boolean comparisonBased;
    private boolean bucketSort;
    private boolean radixSort;
    private boolean unreasonablySlow;
    private boolean bogoSort;
    private int unreasonableLimit;
    
    protected Delays Delays;
    protected Highlights Highlights;
    protected Reads Reads;
    protected Writes Writes;
    
    protected Sort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        this.setSortPromptID("");
        this.setRunAllID("");
        this.setReportSortID("");
        this.setCategory("");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);        //Used to slightly change base dialogue and to verify that a radix sort is also labeled a bucket sort.
        this.isUnreasonablySlow(false); //This boolean is true when a sort is incredibly slow even when the user clicks the "Skip Sort" button.
        this.setUnreasonableLimit(0);   //The length of the array such that the user will be warned if this sort is picked and it is marked as unreasonably slow.
        this.isBogoSort(false);         //Used to slightly change warning dialogue and to verify that a Bogo-based sort is marked as unreasonably slow.
        
        this.Delays = delayOps;
        this.Highlights = markOps;
        this.Reads = readOps;
        this.Writes = writeOps;
    }
    
    public String getSortPromptID() {
        return this.sortPromptID;
    }
    public String getRunAllID() {
        return this.runAllID;
    }
    public String getReportSortID() {
        return this.reportSortID;
    }
    public String getCategory() {
        return this.category;
    }
    public boolean comparisonBased() {
        return this.comparisonBased;
    }
    public boolean usesBuckets() {
        return this.bucketSort;
    }
    public boolean radixSort() {
        return this.radixSort;
    }
    public boolean getUnreasonablySlow() {
        return this.unreasonablySlow;
    }
    public int getUnreasonableLimit() {
        return this.unreasonableLimit;
    }
    public boolean bogoSort() {
        return this.bogoSort;
    }
    
    protected void setSortPromptID(String ID) {
        this.sortPromptID = ID;
    }
    protected void setRunAllID(String ID) {
        this.runAllID = ID;
    }
    protected void setReportSortID(String ID) {
        this.reportSortID = ID;
    }
    protected void setCategory(String ID) {
        this.category = ID;
    }
    protected void isComparisonBased(boolean Bool) {
        this.comparisonBased = Bool;
    }
    public void isBucketSort(boolean Bool) {
        this.bucketSort = Bool;
    }
    protected void isRadixSort(boolean Bool) {
        this.radixSort = Bool;
    }
    protected void isUnreasonablySlow(boolean Bool) {
        this.unreasonablySlow = Bool;
    }
    public void setUnreasonableLimit(int number) {
        this.unreasonableLimit = number;
    }
    protected void isBogoSort(boolean Bool) {
        this.bogoSort = Bool;
    }
    
    public abstract void runSort(int[] array, int currentLength, int bucketCount); //bucketCount will be zero for comparison-based sorts
}