/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.Semaphore;
import model.ProductBarcode;

/**
 *
 * @author Talonos
 */
public class PluginManager 
{
    private static PluginManager instance;
    private List<Plugin> plugins = new ArrayList<>();
    
    /**
     * Retrieves the Plugin Manager, so that you can perform operations with it.
     * 
     * Note that this class is a singleton because it holds, internally, a list of plugins.
     * As part of our design, we decided that if a class is a collection of methods with
     * no associated data, it should be static. Because this has associated data, it is
     * a singleton.
     * 
     * @return 
     */
    public static PluginManager inst()
    {
        if (instance == null)
        {
            instance = new PluginManager();
        }
        return instance;
    }
    
    private PluginManager()
    {
        File directory = new File("plugins");
        List<File> files = Arrays.asList(directory.listFiles());
        //The following code was retrieved from ExampleDepot.com, then heavily modified
        //and edited. The purpose of this assignment is to learn how to use
        //java reflection, amongst other things, and so I feel that learning from online
        //examples is appropriate. If you object to my use of online example code,
        //feel free to talk to me.
            
        //http://stackoverflow.com/questions/6219829/method-to-dynamically-load-java-class-files
        try 
        {
            // Convert File to a URL
            URL url = directory.toURI().toURL();
            //Apparently, the classloader requires an array of URLs. This puts
            //the URL into an array by itself.
            URL[] urls = new URL[]{url};

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);

            //Load in the plugins
            addPlugins(directory, cl);
        } 
        catch (MalformedURLException | ClassNotFoundException | 
                InstantiationException | IllegalAccessException e) 
        {
            System.err.println("I got a "+e.getClass().getName()+", and I have no "
                    + "idea what caused it. Here are the details:");
            e.printStackTrace();
        }
    }

    /**
     * Launches each plugin in a separate thread to try and find the description
     * associated with a given barcode. If one of the plugins responds more quickly
     * than another plugin, then all the other plugins will be interrupted and killed.
     * This method returns the description given by the fastest plugin, or a blank
     * string if the plugins all either fail to find a description or hang for more than
     * fifteen seconds.
     * @param barcode
     * @return 
     */
    public String getDescFromBarCode(ProductBarcode barcode) 
    {
        Semaphore foundAnAnswer = new Semaphore(0);
        Semaphore failed = new Semaphore(0);
        Set<Thread> setOfThreads = new HashSet<>();
        Thread checkForFails = new Thread(new CheckForFailsThread(plugins.size(), foundAnAnswer, failed));
        checkForFails.start();
        for (Plugin p : plugins)
        {
            System.out.println("Starting plugin "+p);
            p.initializePlugin(foundAnAnswer, failed, barcode);
            Thread t = new Thread(p);
            setOfThreads.add(t);
            t.start();
        }
        foundAnAnswer.acquireUninterruptibly();
        for(Thread t : setOfThreads)
        {
            t.stop(); //Deprecated, I know. One day, I will learn to terminate threads
                      //gracefully. Today is not that day.
        }
        for(Plugin p : plugins)
        {
            if (p.getDescriptionFromBarcode() != null)
            {
                return p.getDescriptionFromBarcode();
            }
        }
        return "";
    }
    
    public void addPlugins(File sDir, ClassLoader cl) throws ClassNotFoundException, 
            InstantiationException, IllegalAccessException
    {
        File[] faFiles = sDir.listFiles();
        for(File file: faFiles)
        {
            if(file.getName().matches(".*\\.class"))
            {
                // Load in the class. Note we only get stuff before the first "dot".
                System.out.println(file.getName());
                Class loadedClass = cl.loadClass("added."+
                                              (file.getName().split("\\."))[0]);
                if (!Arrays.asList(loadedClass.getInterfaces()).contains(Plugin.class))
                {
                    System.err.println("Error! Non-plugin class file in directory! Ignoring!");
                    continue;
                }
                //Add it to our list of plugins.
                System.out.println("Loading plugin "+loadedClass.getName());
                plugins.add((Plugin)(loadedClass.newInstance()));
            }
            if(file.isDirectory())
            {
                addPlugins(file, cl);
            }
        }
    }
    
}
