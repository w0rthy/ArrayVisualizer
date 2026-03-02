public class EBs {

	public static void main(String[] args){
		
		int temp=0;
		Boolean sorted = true;
		int swaps=0;
		int comparisons=0;
	    int[] myArray = new int[6];
	      
	      System.out.println("Original Array:");
	        for(int i = 0; i <  myArray.length; i++) 
	        {
	        	myArray[i] = (int)(Math.random() * 10);
	            System.out.print(myArray[i] + "  ");
	        }
	     
	     
	           
	      for (int i = 0; i < myArray.length-1; i++)
	      {
	    	
	         for (int j=0; j<myArray.length-i-1; j++)
	         {
	        	
	        	comparisons++;
	            if (myArray[j] < myArray[j+1])
	            {
	            temp = myArray[j];
	            myArray[j] = myArray[j+1];
	            myArray[j+1] = temp;
	            sorted=false;
	            swaps++;
	            }
	         }  
	         if(sorted)
	        	 break;
	        System.out.print("\n"+myArray[i]); 
	      }
	           
	      System.out.println("\nResults in Numerical Order: "); 
	     for (int i = 0; i < myArray.length; i++)
	      {
	         System.out.print(+myArray[i] +" ");
	      }  
	    
	     System.out.println("\nSwaps occured: "); 
	     for (int i = 0; i < myArray.length; i++)
	      {
	         System.out.print(swaps);
	      }  
	   
	     System.out.println("\nComparisons occured: "); 
	     for (int i = 0; i < myArray.length; i++)
	      {
	         System.out.print(comparisons);
	      }  
	     
	     for (int i = myArray.length; i > 0; i--)
	      {
	         System.out.println(i);
	      }  
	}

}
