package original;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class HelperFunctions {

	
	static boolean safe(int M[][], int row, int col, int visited[][]) //checks if the next pixel to visit for DFS is not yet visited and inside the image
	{
	    return (row >= 0) && (row < M.length) &&     // row number is in range
	           (col >= 0) && (col < M[0].length) &&     // column number is in range
	           (M[row][col]==1 && visited[row][col]==0); // value is 1 and not yet visited -> black pixel
	}
	 
	// A utility function to do DFS for a 2D boolean matrix. 
	// It only considers the 8 neighbors as adjacent vertices
	static void DFS(int M[][], int row, int col, int visited[][], position bottom, position up, position left, position right)
	{
	    // These arrays are used to get row and column numbers of 8 neighbors 
	    // of a given cell
	     int rowNbr[] = {-1, -1, -1,  0, 0,  1, 1, 1};
	     int colNbr[] = {-1,  0,  1, -1, 1, -1, 0, 1};
	 
	    // Mark this cell as visited
	    visited[row][col] = 1;
	 
	    // Recur for all connected neighbors
	    for (int k = 0; k < 8; ++k)
	        {
	    	if (safe(M, row + rowNbr[k], col + colNbr[k], visited) )
	            {
	    		DFS(M, row + rowNbr[k], col + colNbr[k], visited, bottom ,up,left,right);
	    		 if(row<left.x)
	                	{left.x=row;left.y=col;} //find the leftmost position of the object  
	                if(row>right.x)
	                	{right.x=row;right.y=col;} //find the rightmost position of the object   
	                if(col>bottom.y)
	                	{bottom.y=col;bottom.x=row;} //find the bottom most position of the object  
	                if(col<up.y)
	                	{up.y=col;up.x=row;} //find the topmost position of the object  
	                
	            }
	    	}
	}
	 
	// The main function that returns count of islands in a given boolean
	// 2D matrix
	static int countIslands(int M[][],MyImage img_array[],int img_num)
	{
	    // Make a Boolean array to mark visited cells.
	    // Initially all cells are unvisited
	    int ROW=M.length;
	    int COL=M[0].length;
	    
		int visited[][]=new int[ROW][COL];
	    int count = 0;
	    
	    position bottom,up,left,right;
	    bottom=new position(0,0);
	    up=new position(0,ROW);
	    left=new position(COL,0);
	    right=new position(0,0);
	    
	    for (int i = 0; i < ROW; ++i)
	        for (int j = 0; j < COL; ++j)
	            if (M[i][j]==1 && visited[i][j]==0) // If a cell with value 1 is not
	            {                              // visited yet, then new island found
	                DFS(M, i, j, visited,bottom,up,left,right); // Visit all cells in this island.
	                //store the center of the object
	         	    img_array[img_num-1].CENTRE[count].x=((bottom.x + up.x + right.x + left.x)/4); //x-coordinate of center 
	         	    img_array[img_num-1].CENTRE[count].y=((bottom.y + up.y + right.y + left.y)/4); //y-coordinate of center
	         	    ++count; //increase the count for this object 
	            }
	   
	  return count; //return the number of objects in the current frame
	}

	public static void convertImageToMatrix(BufferedImage img, int[][] array,
			int w, int h) {
		Raster raster = img.getData();
		for (int j = 0; j < w; j++) {
		    for (int k = 0; k < h; k++) {
		        array[j][k] = raster.getSample(j, k, 0); //convert the bitmap into an array
		    }
		}
	}

	public static void threshold(int[][] array, int w, int h, int limit) {
			for (int j = 0; j < w; j++) {
				for (int k = 0; k < h; k++) {
					if(array[j][k] > limit) //pixel values > limit are assigned to 0
						array[j][k]=0;
					else
						array[j][k]=1; //pixel values < 180 are assigned to 1
				}
			}	
		}
}
