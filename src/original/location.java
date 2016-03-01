package original;
//bomb detection from detachment and subsequent tracking
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import javax.imageio.ImageIO;

class location{
	
	static int COUNT=5; //no of frames in the video
	static String filename = "input1/input"; // file path of frames - must end with \\input and name of frames must be of the form  
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
	
	

	public static void main(String args[]) throws IOException
	{
	  for(img_num=1;img_num<=COUNT;img_num++) //for all frames do the following
	  {
		//Create file for the source  
		File input = new File(filename + img_num + ".jpg");  //read the jpg file
		//Read the file to a BufferedImage  
		BufferedImage image = ImageIO.read(input);  
		
		/*
		
		//Create a file for the output  
		File output = new File(filename + "temp.bmp");  //convert it into bmp and store it in the same folder 
		//Write the image to the destination as a BMP  
		ImageIO.write(image, "bmp", output); 
		*/
		
		
		Image image1 = ImageIO.read(input);
		BufferedImage img = new BufferedImage(image1.getWidth(null), image1.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = img.createGraphics();
		g.drawImage(image1, 0, 0, null);
		g.dispose();
		
		int w = img.getWidth(); //height of image 
		int h = img.getHeight(); //width of image

		int[][] array = new int[w][h]; 
		
		HelperFunctions.convertImageToMatrix(img,array,w,h);
		HelperFunctions.threshold(array,w,h,180);
		
		int count = HelperFunctions.countIslands(array,img_array,img_num);
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