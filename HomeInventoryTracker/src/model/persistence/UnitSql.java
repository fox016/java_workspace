
package model.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

/** This class is responsible for executing individual sql statements concerning Units
	@author Chris Tensmeyer, Clint Rollins
	@version March 25 2013
*/

class UnitSql
{
	/** Gets an Unit 
		@param id Specifies which Storage Unit to get
		@return Returns the UnitDataObject for the Unit of id
	*/
	UnitDataObject getUnit(int id) {
		throw new UnsupportedOperationException("Not Implemented");
		}

	/** Stores a newly created Unit in the data persistence model.
		@param udo The UnitDataObject representing the Unit
	*/
	void createUnit(DatabaseUnitDataObject udo) {		
		try {
			addUnit(udo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		DatabaseFacade.instance().updateLastIds();
	}
	/**
	 * Handles all SQL operations involving updating a unit in the database.
	 * 
	 * @param udo
	 *            The UnitDataObject representing the Unit
	 */
	void updateUnit(DatabaseUnitDataObject udo) {
		try {
			System.out.println("update Unit called");
			removeUnit(udo);
			remContToProduct(udo.unitId());
			addUnit(udo);
			Set<String> prodBarcodes = udo.pBarcodes();
			for(String barcode : prodBarcodes){
				insContToProduct(udo, barcode);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	/** Removes an Unit from the data persistence model and removes the parentToChild entry
	 * associated with the unit removed.
		@param udo The UnitDataObject representing the Unit
	*/
	void deleteUnit(DatabaseUnitDataObject udo) {
		
		try {
			System.out.println("delete unit called");
			removeUnit(udo);
			Set<Integer> children = getChildren(udo.unitId());
			for(int child : children){
				removeGroupRecurse(child);
			}
			remParToChild(udo.unitId(), udo.unitId());
			remContToProduct(udo.unitId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Private functions start here
	
	/**
	 * this will give us a set of ID's that are children of parentId
	 * @param parentId the parentId
	 * @return
	 * @throws SQLException
	 */
	private Set<Integer> getChildren(int parentId) throws SQLException {
		String childrenSQL = "SELECT childID FROM containerToChild WHERE parentID=?";
		PreparedStatement stmt = DatabaseFacade.instance().connection()
				.prepareStatement(childrenSQL);
		stmt.setInt(1, parentId);
		ResultSet rs = stmt.executeQuery();
		Set<Integer> children = new TreeSet<Integer>();
		while(rs.next()){
			children.add(rs.getInt(1));
		}	
		return children;
	}
	
	/**
	 * This method deletes a product from the ProductGroup table
	 * @throws SQLException
	 */
	private void removeGroupRecurse(int groupId) throws SQLException {
		PreparedStatement stmt = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.deleteProductGroup);
		stmt.setInt(1, groupId);
		stmt.executeUpdate();
		
		Set<Integer> children = getChildren(groupId);
		for(int child : children){
			removeGroupRecurse(child);
		}	
		remParToChild(groupId,groupId);
		remContToProduct(groupId);
	}

	private void remParToChild(int parId, int childId) throws SQLException {
		PreparedStatement remParToChild = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.deleteParToChild);
		remParToChild.setInt(1,parId);
		remParToChild.setInt(2, childId);
		remParToChild.executeUpdate();
	}

	private void removeUnit(DatabaseUnitDataObject udo) throws SQLException {
		PreparedStatement stmt = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.deleteUnit);
		stmt.setInt(1, udo.unitId());
		stmt.executeUpdate();
	}
	
	/**
	 * Handles SQL operation of adding a unit into the Unit table
	 * @param udo the unit to be added
	 * @throws SQLException
	 */
	private void addUnit(DatabaseUnitDataObject udo) throws SQLException {
		PreparedStatement stmt = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.createUnit);
		stmt.setInt(1, udo.unitId());
		stmt.setString(2, udo.name());
		stmt.executeUpdate();
	}
	/**
	 * Deals only with removing products from a container in the containerToProduct table.
	 * @param pgdo
	 * @throws SQLException
	 */
	private void remContToProduct(int id)
			throws SQLException {
		
		//System.out.println("Removing " + udo.name() + " from Cont to Product table");
		
		PreparedStatement remContToProduct = DatabaseFacade.instance()
				.connection()
				.prepareStatement(SqlStatement.removeContToProduct);
		remContToProduct.setInt(1, id);
		remContToProduct.executeUpdate();
	}
	
	/**
	 * Handles inserting into the containerToProduct method in the database.
	 * @param udo The unit we need to update
	 * @throws SQLException
	 */
	private void insContToProduct(DatabaseUnitDataObject udo, String prodBarcode) {
		System.out.println("Adding " + prodBarcode + " to unit " + udo.name());
		try
		{
		PreparedStatement insContToProduct = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.insertContToProduct);
		insContToProduct.setString(1,udo.unitId() + "");
		insContToProduct.setString(2, prodBarcode);
		System.out.println(SqlStatement.insertContToProduct + ", " + 
				udo.unitId() + ", "+ prodBarcode);
		insContToProduct.executeUpdate();
		}catch(Exception ex)
		{
			System.out.println("Insert cont to product failed (unit): " + ex.getMessage());
		}
	}
		
}

