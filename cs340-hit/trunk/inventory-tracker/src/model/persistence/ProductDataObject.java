
package model.persistence;

import model.Product;

/** The interface for the ProductDataObjects 
	@author Chris Tensmeyer
	@version March 25 2013
*/

public interface ProductDataObject extends DataObject
{
	Product getProduct();
}

