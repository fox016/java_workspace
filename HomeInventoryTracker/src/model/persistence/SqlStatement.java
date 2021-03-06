
package model.persistence;

/** This class is responsible for defining our sql strings
	@author Chris Tensmeyer
	@version March 25 2013
*/

class SqlStatement
{
	static final String createItem = "INSERT INTO Item(itemBarcode, alive, entryDate" +
									", exitTime, productBarcode, unitID) VALUES(?,?,?,?,?,?)";
	static final String updateIds = "INSERT INTO LastID" +
			"(itemBarcode, prodContainerId, expiredReport) VALUES (?,?,?)";
	static final String clearIds = "DELETE FROM LastID";
	//static final String readItem = "SELECT ... FROM item";
	//static final String updateItem = "UPDATE item SET ... ";
	static final String deleteItem = "DELETE FROM Item WHERE itemBarcode=?";

	static final String createProduct = "INSERT INTO Product(productBarcode, creationDate,"
										+ " description, amount, measurement, " +
										"shelfLife, threeMonthSupply)"
										+ "VALUES(?,?,?,?,?,?,?)";
	//static final String readProduct = "SELECT ... FROM product";
	//static final String updateProduct = "UPDATE product SET ... ";
	static final String deleteProduct = "DELETE FROM Product WHERE productBarcode=?";

	static final String createProductGroup = "INSERT INTO ProductGroup(groupID, name, " +
			"amount, measurement)"
											+ "VALUES(?,?,?,?)";
	//static final String readProductGroup = "SELECT ... FROM productgroup";
	//static final String updateProductGroup = "UPDATE productgroup SET ... ";
	static final String deleteProductGroup = "DELETE FROM ProductGroup WHERE groupID=?";

	static final String createUnit = "INSERT INTO storageunit(unitID, name) VALUES(?,?)";
	//static final String readUnit = "SELECT ... FROM unit";
	//static final String updateUnit = "UPDATE unit SET ... ";
	static final String deleteUnit = "DELETE FROM StorageUnit WHERE unitID=?";
			

	static final String readAllItems = "SELECT * FROM item";
	static final String readAllProducts = "SELECT * FROM product";
	static final String readAllProductGroups = "SELECT * FROM productgroup";
	static final String readAllUnits = "SELECT * FROM unit";

	//... ad nasium for join tables and such
	static final String deleteParToChild = "DELETE FROM containerToChild " +
			"WHERE parentID=? OR childID=?";
	static final String insertParToChild = "INSERT INTO containerToChild(parentID, " +
			"childID) VALUES(?,?)";
	static final String insertContToProduct = "INSERT INTO containerToProduct(containerID, " +
			"productBarcode) VALUES(?,?)";
	static final String removeContToProduct = "DELETE FROM containerToProduct " +
			"WHERE containerID=?";
	static final String removeProductFromCont = "DELETE FROM containerToProduct " +
			"WHERE productBarcode=?";
	
}

