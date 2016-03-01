package original;
//bomb detection from detachment and subsequent tracking
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import javax.imageio.ImageIO;

class location{
	static int COL=0;
	static int ROW=0;
	static int COUNT=11; //no of frames in the video
	static String filename = "input/input"; // file path of frames - must end with \\input and name of frames must be of the form  
														   // input<number>.jpg
	static int img_num=1; //index to iterate over all images/frames
	static MyImage img_array[] = new MyImage[COUNT];  
	static                                         //initialization of image objects
	{
		for(int i=0;i<COUNT;i++)
		{
			img_array[i]= new MyImage();
		}	
	}
	
	static boolean safe(int M[][], int row, int col, int visited[][]) //checks if the next pixel to visit for DFS is not yet visited and inside the image
	{
	    return (row >= 0) && (row < ROW) &&     // row number is in range
	           (col >= 0) && (col < COL) &&     // column number is in range
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
	static int countIslands(int M[][])
	{
	    // Make a Boolean array to mark visited cells.
	    // Initially all cells are unvisited
	    
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

	public static void main(String args[]) throws IOException
	{
	  for(img_num=1;img_num<=COUNT;img_num++) //for all frames do the following
	  {
		//Create file for the source  
		File input = new File(filename + img_num + ".jpg");  //read the jpg file
		//Read the file to a BufferedImage  
		BufferedImage image = ImageIO.read(input);  
		
		//Create a file for the output  
		File output = new File(filename + "temp.bmp");  //convert it into bmp and store it in the same folder 
		//Write the image to the destination as a BMP  
		ImageIO.write(image, "bmp", output); 
		
		Image image1 = ImageIO.read(output);
		BufferedImage img = new BufferedImage(image1.getWidth(null), image1.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = img.createGraphics();
		g.drawImage(image1, 0, 0, null);
		g.dispose();
		
		int w = img.getWidth();ROW=w; //height of image 
		int h = img.getHeight();COL=h; //width of image

		int[][] array = new int[w][h];
		
		Raster raster = img.getData();
		for (int j = 0; j < w; j++) {
		    for (int k = 0; k < h; k++) {
		        array[j][k] = raster.getSample(j, k, 0); //convert the bitmap into an array
		    }
		}
		
		for (int j = 0; j < w; j++) {
		    for (int k = 0; k < h; k++) {
		    	if(array[j][k] > 180) //pixel values > 180 are assigned to 0
		    		array[j][k]=0;
		    	else
		    		array[j][k]=1; //pixel values < 180 are assigned to 1
		    }
		}
		
		int count = countIslands(array);
		img_array[img_num-1].count=count; // populate the count variable with number of objects in the image 
	}
	
	  for(int i=0;i<COUNT;i++)
	  {
		  for(int j=0;j<img_array[i].count;j++)
		  {
		   System.out.println("Image " + (i+1) + ": Object " + (j+1) + ": " +img_array[i].CENTRE[j].x + " " + img_array[i].CENTRE[j].y);
		  }
	  }
	   
	  boolean array_of_probable_objects[]=new boolean[100]; //sets the index of the object which might be a bomb
	  for(int i=0;i<100;i++)
	  {
		  array_of_probable_objects[i]=true;
	  }
	  
	  for(int i=0;i<COUNT-1;i++) 
	  {
		  if(img_array[i].count>1) 
		  {
			  for(int k= img_array[i].count; k < 100; k++)
				  array_of_probable_objects[k]=false;
			  
			  compare(img_array[i], img_array[i+1], array_of_probable_objects); //comparison of objects to find the bomb
		  }
	  }
	  
	  boolean bomb_found=false;
	  //detects which of the objects might be a bomb
	  for(int i=0;i<100;i++)
	  {  
		if(array_of_probable_objects[i]==true)
			{
			System.out.println("Object " + (i+1) + " is a bomb");
			bomb_found=true;
			break;
			}
	  }
	  if(!bomb_found)// no object moves like a bomb  -- note that objects are numbered from bottom to top of the frame
		  System.out.println("None of the objects is a bomb");
   }
	
	//function to determine the bomb among all moving objects
	static void compare(MyImage img_array2, MyImage img_array3, boolean[] arr)
	{
		int ignore =0;
		for(int i=0;i<img_array2.count;i++)
		{
			ignore=0;
			if(arr[i]==false)
				ignore=1;
			if(img_array2.CENTRE[i].x < img_array3.CENTRE[i].x) //if its not moving left then do not consider
			{
					ignore=1;
			}
			if(img_array2.CENTRE[i].y > img_array3.CENTRE[i].y)// if it is not moving down then do not consider
			{
					ignore=1;
			}
			if(ignore==1)
				arr[i]=false;
			else
				arr[i]=true;			
		}	
	}
}