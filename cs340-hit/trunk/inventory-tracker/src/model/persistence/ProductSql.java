
package model.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/** This class is responsible for executing individual sql statements concerning Products
	@author Chris Tensmeyer, Clint Rollins
	@version March 25 2013
*/

class ProductSql
{
	/**
	 * An instance of the DatabaseFacade for convenience
	 */
	
	/** Gets an Product 
		@param id Specifies which Product to get
		@return Returns the ProductDataObject for the Product of id
	*/
	ProductDataObject getProduct(int id) { 
		throw new UnsupportedOperationException("Not Implemented"); 
		}

	/** Stores a newly created Product in the data persistence model.
		@param pdo The ProductDataObject representing the Product
	*/
	void createProduct(DatabaseProductDataObject pdo) {
		
		try {
			PreparedStatement stmt = DatabaseFacade.instance().connection()
					.prepareStatement(SqlStatement.createProduct);
			stmt.setString(1, pdo.barcode());
			stmt.setLong(2, pdo.creationDate());
			stmt.setString(3, pdo.description());
			stmt.setDouble(4, pdo.amount());
			stmt.setString(5, pdo.amountUnit());
			stmt.setInt(6, pdo.shelfLife());
			stmt.setInt(7, pdo.threeMonthSupply());
			stmt.execute();
		} catch (SQLException e) {
			// Do nothing for now. For some reason this sometimes gets called twice
		}
	}

	/** Updates an existing Product in the data persistence model.
		@param pdo The ProductDataObject representing the Product
	*/
	void updateProduct(DatabaseProductDataObject pdo) {
		deleteProduct(pdo);
		createProduct(pdo);
		}

	/**
	 * Removes an Product from the data persistence model.
	 * 
	 * Nate - Also needs to remove all items of the product and
	 * needs to clean out containerToProduct table
	 * 
	 * @param pdo
	 *            The ProductDataObject representing the Product
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	void deleteProduct(DatabaseProductDataObject pdo) {

		try {
			PreparedStatement stmt = DatabaseFacade.instance().connection()
					.prepareStatement(SqlStatement.deleteProduct);
			stmt.setString(1, pdo.barcode());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			System.out.println("Removing product " + pdo.barcode() + " from all containers");
			
			PreparedStatement stmt = DatabaseFacade.instance().connection().
					prepareStatement(SqlStatement.removeProductFromCont);
			stmt.setString(1, pdo.barcode());
			stmt.execute();
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}




















