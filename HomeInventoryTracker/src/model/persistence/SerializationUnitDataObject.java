
package model.persistence;

import model.StorageUnit;

/** The class representing a Serialized Storage Unit
	@author Chris Tensmeyer
	@version March 25 2013
*/

class SerializationUnitDataObject implements UnitDataObject
{
	private StorageUnit unit;

	/** Creates a SerializationUnitDataObject representing a StorageUnit 
		@param unit The Storage Unit to be represented
	*/
	public SerializationUnitDataObject(StorageUnit unit)
	{
		this.unit = unit;
	}

	public StorageUnit getUnit()
	{
		return unit;
	}

}

