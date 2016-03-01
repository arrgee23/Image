package testing;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import javax.imageio.ImageIO;

class imgread{
	static int COL=0;
	static int ROW=0;
	static int COUNT=11;
	static String filename = "input/input";
	static boolean safe(int M[][], int row, int col, int visited[][])
	{
	    return (row >= 0) && (row < ROW) &&     // row number is in range
	           (col >= 0) && (col < COL) &&     // column number is in range
	           (M[row][col]==1 && visited[row][col]==0); // value is 1 and not yet visited
	}
	 
	// A utility function to do DFS for a 2D boolean matrix. It only considers
	// the 8 neighbors as adjacent vertices
	static void DFS(int M[][], int row, int col, int visited[][])
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
	            DFS(M, row + rowNbr[k], col + colNbr[k], visited);
	    	}
	}
	 
	// The main function that returns count of islands in a given boolean
	// 2D matrix
	static int countIslands(int M[][])
	{
	    // Make a Boolean array to mark visited cells.
	    // Initially all cells are unvisited
	    
		int visited[][]=new int[ROW][COL];
	    int count = 0;
	    for (int i = 0; i < ROW; ++i)
	        for (int j = 0; j < COL; ++j)
	            if (M[i][j]==1 && visited[i][j]==0) // If a cell with value 1 is not
	            {                              // visited yet, then new island found
	                DFS(M, i, j, visited);     // Visit all cells in this island.
	                ++count;                   // and increment island count
	            }
	  return count;
	}
	public static void main(String args[]) throws IOException
	{
	  int img_num=1;
	  int loc_x[]= new int[COUNT];
	  int loc_y[]= new int[COUNT];
	  
	  for(img_num=1;img_num<=COUNT;img_num++)
	  {
		//Create file for the source  
		File input = new File(filename + img_num + ".jpg");  
		//Read the file to a BufferedImage  
		BufferedImage image = ImageIO.read(input);  
		
		//Create a file for the output  
		File output = new File(filename + "temp.bmp");  
		//Write the image to the destination as a BMP  
		ImageIO.write(image, "bmp", output); 
		
		Image image1 = ImageIO.read(output);
		BufferedImage img = new BufferedImage(image1.getWidth(null), image1.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = img.createGraphics();
		g.drawImage(image1, 0, 0, null);
		g.dispose();
		
		int w = img.getWidth();ROW=w;
		int h = img.getHeight();COL=h;

		int[][] array = new int[w][h];
		
		Raster raster = img.getData();
		for (int j = 0; j < w; j++) {
		    for (int k = 0; k < h; k++) {
		        array[j][k] = raster.getSample(j, k, 0);
		    }
		}
		
		for (int j = 0; j < w; j++) {
		    for (int k = 0; k < h; k++) {
		    	if(array[j][k] > 180)
		    		array[j][k]=0;
		    	else
		    		array[j][k]=1;
		    }
		}
		
		/////////////////////////////////////////////////////////
		
		int count = countIslands(array);
		System.out.println("Number of Islands: " + count);
		//str.close();
		
		/*int k=0,j=0;
		//System.out.println(w+ "  "+ h);
		if(count>1)
		{
			outer:
			for(j=COL-1;j>=0;j--)
				 {
		            for(k=ROW-1;k>=0;k--)
				      {
		                if( array[k][j] == 1 )
		                  {
		                	break outer;	
		                  }
		             }
		       }
			//System.out.println("Col(x):" + k + "   Row(y):" + j );
			loc_x[img_num-1]=k;
			loc_y[img_num-1]=j;
			
		}*/
	}
	  /*int min=0,ignore=0;
	  for(int i=1;i<=COUNT;i++)
	  {
	  System.out.println(loc_x[i-1] + " " + loc_y[i-1]);
	  }
	  for(int i=1;i<=COUNT;i++)
	  {
	  if(loc_x[i-1]>0)
		  {min=i-1;break;}
	  }
	  System.out.println(min);
	  
	  for(int i=min+2;i<=COUNT;i++)
	  {
		  if(loc_x[i-2]<loc_x[i-1])
		  {
			  ignore=1;System.out.println(i-2 + " -> anomaly in movement in x_axis");
			  break;
		  }
	  }
	  
	  for(int i=min+2;i<=COUNT;i++)
	  {
		  if(loc_y[i-2]>loc_y[i-1])
		  {
			  ignore=1;System.out.println(i-2 + " -> anomaly in movement in y_axis");
			  break;
		  }
	  }
	  
	  if(ignore==1)
		  System.out.println("Detected object is not a bomb");
	  else
		  System.out.println("Detected object is a bomb");
	  
	  */
	  
   }
}
