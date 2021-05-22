package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

final public class TreeSort extends Sort {
    public TreeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Tree");
        this.setRunAllID("Tree Sort (Unbalanced)");
        this.setReportSortID("Treesort");
        this.setCategory("Insertion Sorts");
        this.isComparisonBased(true);
        this.isBucketSort(false);
        this.isRadixSort(false);
        this.isUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.isBogoSort(false);
    }
    
    // Code retrieved from https://www.geeksforgeeks.org/tree-sort/
	
	final private class Node { 
		int key; 
		Node left, right; 

		public Node(int item) { 
			key = item; 
			left = right = null; 
		} 
	} 
	
	private Node root; 
	private int index, length;

	private Node treeWrite(Node element, int at) {
		Node node = new Node(0);
		
		if(at > 0 && at < this.length) Highlights.markArray(1, at - 1);
		Writes.changeTempWrites(1);
		Writes.startLap();
		node = element;
		Writes.stopLap();
		
		Delays.sleep(0.25);
		
		return node;
	}

	private void insert(int key) {
		this.root = this.treeWrite(insertRec(this.root, key, 1), 1);
	} 
	
	private Node insertRec(Node root, int key, int depth) { 
		if (root == null) { 
			root = treeWrite(new Node(key), 1);
			return root; 
		} 

		if (Reads.compare(key, root.key) == -1) 
			root.left  = treeWrite(insertRec(root.left, key, depth * 2), depth * 2); 
		else if (Reads.compare(key, root.key) == 1) 
			root.right = treeWrite(insertRec(root.right, key, (depth * 2) + 1), (depth * 2) + 1); 

		return root; 
	} 

	private void traverseRec(Node root, int[] array) { 
		if (root != null) { 
			this.traverseRec(root.left, array); 
			Writes.write(array, this.index++, root.key, 1, true, false); 
			this.traverseRec(root.right, array); 
		} 
	} 

	private void treeIns(int arr[]) {
		for(int i = 0; i < this.length; i++) {
			Highlights.markArray(2, i);
			this.insert(arr[i]);
		} 
		Highlights.clearMark(2);
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.length = currentLength;
        this.treeIns(array); 
        this.index = 0;
        this.traverseRec(this.root, array); 
    }
}