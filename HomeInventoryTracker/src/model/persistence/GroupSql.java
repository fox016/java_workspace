
package model.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import model.ProductGroup;

/** This class is responsible for executing individual sql statements concerning ProductGroups
	@author Chris Tensmeyer, Clint Rollins
	@version March 25 2013
*/

class GroupSql
{
	/** 
	 * Handles all SQL operations involved with creating a product group.
		@param pgdo The GroupDataObject representing the ProductGroup
	*/
	void createGroup(DatabaseGroupDataObject pgdo) {

		try {
			addGroup(pgdo);
			insParToChild(pgdo);		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		DatabaseFacade.instance().updateLastIds();
	}


	/**
	 * Handles all operations related to updating a product group in the SQL database.
	 * 
	 * @param pgdo
	 *            The GroupDataObject representing the ProductGroup
	 */
	void updateGroup(DatabaseGroupDataObject pgdo) {
		try {
			removeGroup(pgdo.groupId());
			addGroup(pgdo);
			remContToProduct(pgdo.groupId());
			
			Set<String> prodBarcodes = pgdo.pBarcodes();
			for(String barcode : prodBarcodes){
				insContToProduct(pgdo, barcode);			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Removes an ProductGroup from the data persistence model as well the
	 * parent to child relationship from the parentToChild table.
		@param pgdo The GroupDataObject representing the ProductGroup
	*/
	void deleteGroup(DatabaseGroupDataObject pgdo) {

		try {
			removeGroupRecurse(pgdo.groupId());		
			remParToChild(pgdo.parent(), pgdo.groupId());
			remContToProduct(pgdo.groupId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Private functions start here
	/**
	 * Handles SQL operation of inserting into the containerToProduct table
	 * @param pgdo Container being added
	 * @param barcode product being added
	 * @throws SQLException
	 */
	private void insContToProduct(DatabaseGroupDataObject pgdo, String barcode) {
		System.out.println("Adding " + barcode + " to group " + pgdo.name());
		try{
			PreparedStatement insContToProduct = DatabaseFacade.instance().connection()
					.prepareStatement(SqlStatement.insertContToProduct);
			insContToProduct.setString(1, pgdo.groupId() + "");
			insContToProduct.setString(2, barcode);
			System.out.println(SqlStatement.insertContToProduct + ", " + 
			pgdo.groupId() + ", " + barcode);
			insContToProduct.executeUpdate();
		}catch(Exception ex)
		{
			System.out.println("Insert cons to product failed (group): " + ex.getMessage());
		}
	}

	/**
	 * Handles SQL operation of removing from the containerToProduct table
	 * @param pgdo the container from which all product references will be removed
	 * @throws SQLException
	 */
	
	private void remParToChild(int parentId, int childId)
			throws SQLException {
		PreparedStatement remParToChild = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.deleteParToChild);
		remParToChild.setInt(1,parentId);
		remParToChild.setInt(2, childId);
		remParToChild.executeUpdate();
	}
	/**
	 * This method inserts a product into the ProductGroup table
	 * @param pgdo the product to insert
	 * @throws SQLException
	 */
	private void addGroup(DatabaseGroupDataObject pgdo) throws SQLException {
		PreparedStatement stmt = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.createProductGroup);
		stmt.setInt(1, pgdo.groupId());
		stmt.setString(2, pgdo.name());
		stmt.setDouble(3, pgdo.amount());
		stmt.setString(4, pgdo.amountUnit());
		stmt.executeUpdate();
	}

	/**
	 * This method deletes a product from the ProductGroup table
	 * @param pgdo
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
	
	private void removeGroup(int groupId) throws SQLException {
		PreparedStatement stmt = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.deleteProductGroup);
		stmt.setInt(1, groupId);
		stmt.executeUpdate();
	}
	
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
	 * Deals only with inserting into the ParentToChild table in the database.
	 * @param pgdo
	 * @throws SQLException
	 */
	private void insParToChild(DatabaseGroupDataObject pgdo)
			throws SQLException {
		PreparedStatement insertParToChild = DatabaseFacade.instance().connection()
				.prepareStatement(SqlStatement.insertParToChild);
		insertParToChild.setInt(1,pgdo.parent());
		insertParToChild.setInt(2, pgdo.groupId());
		insertParToChild.executeUpdate();
	}
	/**
	 * Deals only with removing products from a container in the containerToProduct table.
	 * @param pgdo
	 * @throws SQLException
	 */
	private void remContToProduct(int id)
			throws SQLException {
		PreparedStatement remContToProduct = DatabaseFacade.instance()
				.connection()
				.prepareStatement(SqlStatement.removeContToProduct);
		remContToProduct.setInt(1, id);
		remContToProduct.executeUpdate();
	}
}
