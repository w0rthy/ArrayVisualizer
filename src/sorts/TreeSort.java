package sorts;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

//TODO: Code's an absolute mess. Refactor it at some point.
final public class TreeSort extends Sort {
    public TreeSort(Delays delayOps, Highlights markOps, Reads readOps, Writes writeOps) {
        super(delayOps, markOps, readOps, writeOps);
        
        this.setSortPromptID("Tree");
        this.setRunAllID("Tree Sort");
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
    
    // Java program to  
    // implement Tree Sort 
    final class TreeSorter  
    { 

        // Class containing left and 
        // right child of current  
        // node and key value 
        final class Node  
        { 
            int key; 
            Node left, right; 

            public Node(int item)  
            { 
                key = item; 
                left = right = null; 
            } 
        } 

        // Root of BST 
        Node root; 

        //Index for final result
        int index;
        
        //Field to store current length
        int length;
        
        // Constructor 
        TreeSorter()  
        {  
            root = null;  
        } 

        //Method has to be inside TreeSort to access Node class directly
        public Node treeWrite(Node element, int at) {
            Node node = new Node(0);
            
            if(at < length) Highlights.markArray(1, at - 1);
            
            Writes.changeTempWrites(1);
            
            Writes.startLap();
            
            node = element;
            
            Writes.stopLap();
            
            Delays.sleep(0.25);
            
            return node;
        }
        
        // This method mainly 
        // calls insertRec() 
        void insert(int key) 
        { 
            this.root = treeWrite(insertRec(root, key, 1), 1);
        } 

        /* A recursive function to  
     insert a new key in BST */
        Node insertRec(Node root, int key, int depth)  
        { 

            /* If the tree is empty, 
         return a new node */
            if (root == null)  
            { 
                root = treeWrite(new Node(key), 1);
                return root; 
            } 

            /* Otherwise, recur 
         down the tree */
            if (Reads.compare(key, root.key) == -1) 
                root.left = treeWrite(insertRec(root.left, key, depth * 2), depth * 2); 
            else if (Reads.compare(key, root.key) == 1) 
                root.right = treeWrite(insertRec(root.right, key, (depth * 2) + 1), (depth * 2) + 1); 

            /* return the root */
            return root; 
        } 

        // A function to do  
        // inorder traversal of BST 
        void inorderRec(Node root, int[] array)  
        { 
            if (root != null)  
            { 
                inorderRec(root.left, array); 
                Writes.write(array, this.index++, root.key, 1, true, false); 
                inorderRec(root.right, array); 
            } 
        } 

        void treeins(int arr[], int length) 
        { 
            for(int i = 0; i < length; i++) 
            {
                Highlights.markArray(2, i);
                insert(arr[i]); 
            } 
            Highlights.clearMark(2);
        } 

    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        TreeSorter tree = new TreeSorter();
        tree.length = currentLength;
        tree.treeins(array, tree.length); 
        tree.index = 0;
        tree.inorderRec(tree.root, array); 
    }
}