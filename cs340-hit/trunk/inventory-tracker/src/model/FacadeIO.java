/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import model.persistence.PersistenceFacade;
import model.persistence.PersistenceFactory;

/**
 *
 * @author chris
 */
class FacadeIO {
	
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException 
	 */
	static void loadHouse()
			throws FileNotFoundException, IOException, ClassNotFoundException, SQLException
	{
		PersistenceFacade loadFacade = PersistenceFactory.getFacade();
		loadFacade.loadAll();
	}
	
	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	static void saveHouse() throws FileNotFoundException, IOException, ClassNotFoundException
	{
		PersistenceFacade saveFacade = PersistenceFactory.getFacade();
		saveFacade.saveAll();
	}

}

