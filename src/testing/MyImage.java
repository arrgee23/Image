package testing;
//class to store a frame/image
class MyImage{
	position CENTRE[] = new position[100]; // provision to store up to 100 object centers
	int count; // number of objects in the frame
	MyImage()
	{
		for(int i=0;i<100;i++)
		{
			CENTRE[i]= new position(0,0);
		}
		count=0;
	}
}