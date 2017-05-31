/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package added;

import common.plugin.Plugin;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ProductBarcode;

/**
 *
 * @author Talonos
 */
public class SamplePlugin implements Plugin
{
    private ProductBarcode barcode;
    private Semaphore found;
    private Semaphore failedOut;
    
    private String result = null;

    @Override
    public String getDescriptionFromBarcode() 
    {
        //This method should only return the result it already found. All the 
        //stuff that involves outside wait time should be in the run method.
        return result;
    }

    //Because dynamically loaded classes can't use constructers, you need to pass in all
    //the initialization data here, in this method.
    @Override
    public void initializePlugin(Semaphore found, Semaphore failedOut, ProductBarcode barcode) 
    {
        //Note the importance of this line. When you "reset" the plugin, you need to
        //Erase its previous answer.
        result = null;
        //For trace debugging. You can omit this line, if you want. I left it in.
        System.out.println("[SamplePlugin]: Initialized with "+barcode.toString());
        //Note that each time the plugin is initialized, the semaphores passed in are different.
        //Because of that, keep these lines of replace them with something similar.
        this.found = found;
        this.failedOut = failedOut;
        this.barcode = barcode;
    }

    @Override
    public void run() 
    {
        //Basically, this is the function that gets the description from the barcode.
        //Edit it as you see fit, because it is website (or database) specific. I'll
        //Explain what I'm doing here, but you'll probably have to gut this and start
        //from scratch.
        
        //Make a URL
        URL url = null;
        try 
        {
            //Try to make it the place you want to go to.
            url = new URL("http://www.upcdatabase.com/item/"+barcode.toString());
        } 
        catch (MalformedURLException ex) 
        {   // If the URL is malformed, print to the error out and notify the 
            // manager of your failure.
            System.err.println("[SamplePlugin]: Malformed URL! Quitting this plugin.");
            //THIS IS IMPORTANT! After throwing an exception or returning abnormally in
            //any way, you must signal failedOut! Failure to do so will make it so the
            //only way the AddProductManager can proceed is for the "timeout" to happen,
            //which takes an awkwardly long amount of time!
            failedOut.release();
            return;
        }
        try 
        {
            //Open a connection and make an associated reader.
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (connection.getInputStream()));
            String response = in.readLine();
            //I know from the source code of the site that the line with the description
            //on it starts with this line:
            while(!response.startsWith("<tr><td>Description</td><td></td><td>"))
            {
                response = in.readLine();
                if(!in.ready())
                {
                    //If you get to the end of the html without finding that line,
                    //it means the description wasn't in the database.
                    System.err.println("[SamplePlugin]: No valid barcode! "
                            + "Quitting this plugin");
                    //Again, signal to failedOut before returning to notify him of your failure.
                    failedOut.release();
                    return;
                }
            }
            //This replaceAll command extracts the description from the HTML.
            result = response.replaceAll("<tr><td>Description</td><td></td><td>", "")
                             .replaceAll("</td></tr>","");
            //Print it. Again, trace debugging.
            System.out.println("[SamplePlugin]: Found result: "+result);
            //Once you set result to be non-null, signal "found". This will unblock the
            //add product manager, which will then go through and find your response and
            //add it in.
            found.release();
        } 
        catch (IOException ex) 
        {
            // If there is some sort of IO error, print to the error out and notify the 
            // manager of your failure.
            System.err.println("[SamplePlugin]: IO error making connection! "
                    + "Quitting this plugin.");
            ex.printStackTrace();
            //Again, notify of a failure before leaving to avoid having to wait for the timeout.
            failedOut.release();
        }
    }
}
