/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.plugin;

import java.util.concurrent.Semaphore;
import model.ProductBarcode;

/**
 *
 * @author Talonos
 */
public interface Plugin extends Runnable
{
    /**
     * Returns the description of the barcode.
     * If this plugin has a valid description from the barcode it was passed in 
     * with, then this will return the description. Otherwise, it will return null.
     * @return the description of the barcode.
     */
    public String getDescriptionFromBarcode();
    
    /**
     * Initializes a plugin by giving it a semaphore to signal upon completion, a
     * semaphore to signal if it fails, and a barcode to check for.
     * 
     * A valid plugin, when run, will search for a description for the given barcode.
     * if it finds a description, it will signal the found. If it conclusively
     * cannot find a description, it will signal failedOut. In either case, the result
     * of this plugins search can be retrieved by using the "getDescriptionFromBarcode"
     * method.
     * 
     * @param s the semaphore to signal upon finding a valid description
     * @param barcode the barcode to check for.
     */
    public void initializePlugin(Semaphore found, Semaphore failedOut, ProductBarcode barcode);
}
