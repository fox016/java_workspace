
package model.persistence;

import model.ProductGroup;

/** The interface for the GroupDataObjects 
	@author Chris Tensmeyer
	@version March 25 2013
*/

public interface GroupDataObject extends DataObject
{
	ProductGroup getGroup();
}

