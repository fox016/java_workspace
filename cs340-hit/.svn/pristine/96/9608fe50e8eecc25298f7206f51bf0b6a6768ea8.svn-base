
package model.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.BarcodeGenerator;
import model.House;
import model.Item;

/**
 * This class is responsible for executing individual sql statements concerning
 * Items
 * 
 * @author Chris Tensmeyer, Clint Rollins
 * @version March 25 2013
 */

class ItemSql {
	public ItemSql() {

	}

	/**
	 * Gets an Item
	 * 
	 * @param id
	 *            Specifies which Item to get
	 * @return Returns the ItemDataObject for the Item of id
	 */
	ItemDataObject getItem(int id) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	/**
	 * Stores a newly created Item in the data persistence model.
	 * 
	 * @param ido
	 *            The ItemDataObject representing the Item
	 */
	void createItem(DatabaseItemDataObject ido) {
		try {
			PreparedStatement stmt = DatabaseFacade.instance().connection()
					.prepareStatement(SqlStatement.createItem);
			stmt.setString(1, ido.iBarcode());
			stmt.setBoolean(2, ido.live());
			stmt.setLong(3, ido.entryDate());
			stmt.setLong(4, ido.exitTime());
			stmt.setString(5, ido.pBarcode());
			stmt.setString(6, ido.unitId() + "");
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Be sure to use SqlStatement.java to hold the sql statements" */

		DatabaseFacade.instance().updateLastIds();

	}

	/**
	 * Updates an existing Item in the data persistence model.
	 * 
	 * @param ido
	 *            The ItemDataObject representing the Item
	 */
	void updateItem(DatabaseItemDataObject ido) {
		deleteItem(ido);
		createItem(ido);
	}

	/**
	 * Removes an Item from the data persistence model.
	 * 
	 * @param ido
	 *            The ItemDataObject representing the Item
	 */
	void deleteItem(DatabaseItemDataObject ido) {

		try {
			PreparedStatement stmt = DatabaseFacade.instance().connection()
					.prepareStatement(SqlStatement.deleteItem);
			stmt.setString(1, ido.iBarcode());
			stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
