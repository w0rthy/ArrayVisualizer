public class cardSort {
	private static long startTime = System.currentTimeMillis();
	public void sortCard(int arr []){
		
        for (int i = 1; i < arr.length; ++i) { 
            int key = arr[i]; 
            int j = i - 1; 
            while (j >= 0 && arr[j] > key) { 
                arr[j + 1] = arr[j]; 
                j = j - 1; 
            } 
            arr[j + 1] = key; 
        } 
	}
	
	static void printArray(int arr[]) 
    { 
        int n = arr.length; 
        for (int i = 0; i < n; ++i) 
            System.out.print(arr[i] + " "); 
  
        System.out.println(); 
    } 

	public static void main(String[] args) {
		cardSort ob = new cardSort();
		 int arr[] = new int [10];
	        for(int i=0; i<arr.length; i++) {
	        	arr[i] = (int) (Math.floor(Math.random()*14)+2);
	        }
	        
	        ob.sortCard(arr);
	        
	        System.out.println("Sorted array");
	        ob.printArray(arr);
	        long endTime = System.currentTimeMillis();
	        System.out.println("It took " + (endTime - startTime) + " milliseconds");

	}

}
