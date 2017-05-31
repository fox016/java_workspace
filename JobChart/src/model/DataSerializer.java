package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** This class is responsible for saving and retrieving 
 *  data via Java serialization
 * 
 * @author Nathan Fox
 *
 */
public class DataSerializer
{
	/** Saves all non-static data for the given house
	 * 
	 * @param filename	The full class path to save the data to
	 * @throws IOException Informs caller that write to file failed
	 */
	public static void save(String filepath) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(filepath + "JobChart");
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(LocationManager.instance());
		out.close();
		fos.close();
	}
	
	/** Loads all non-static data from the given file and
	 *  creates a House
	 * 
	 * @param filename The full class path to load the data from
	 * @throws IOException Informs caller that read from file failed
	 * @throws FileNotFoundException Informs caller that file wasn't found
	 * @throws ClassNotFoundException Informs caller that house could not be read in
	 */
	public static void load(String filepath) throws IOException, ClassNotFoundException
	{
		FileInputStream fis = new FileInputStream(filepath + "JobChart");
		ObjectInputStream in = new ObjectInputStream(fis);
		LocationManager.setInstance((LocationManager) in.readObject());
		in.close();
		fis.close();
		LocationManager.instance().restoreSingletons();
	}
}
