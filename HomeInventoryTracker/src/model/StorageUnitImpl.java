package model;

import model.report.Visitor;

/** This class represents a StorageUnit, which contains
 *  Items and Product Groups
 *
 * @author Talonos
 */
public class StorageUnitImpl extends ProductContainerImpl implements StorageUnit
{
	private static final long serialVersionUID = -2049857044314078747L;
	
        /**
         * Constructs a new StorageUnitImpl with the given name. Does no error
         * checking! Error checking is done in the parent object, before adding
         * this storageUnitImple to the set.
         * @param name 
         */
	public StorageUnitImpl(String name)
	{
            assert(true);
		super.rename(name);
	}
	
    @Override
    public void setName(String newName) throws IllegalArgumentException
    {
    	if(!(House.instance().isValidName(newName)))
    	{
    	    throw new IllegalArgumentException("The name " + newName + 
                    " is not valid");
    	}
    	
    	rename(newName);
    }

    @Override
    public StorageUnit getRoot()
    {
        assert(true);
        return this; 
    }

    @Override
    public ProductGroup beProductGroup() 
    {
        assert(true);
        return null;
    }

    @Override
    public StorageUnit beStorageUnit() 
    {
        assert(true);
        return this;
    }

    @Override
    public int compareTo(Object o) 
    {
        assert(true);
        if (!(o instanceof StorageUnit))
        {
            return -1;
        }
        return this.getName().compareTo(((StorageUnit)o).getName());
    }
    
     /**
      * Gets the productContainer the target product is located in.
      * @param p the Product to search for.
      * @return the productContainer this product is located in.
      */
    @Override
     public ProductContainer getProductContainer(Product product)
     {
        assert(true);
    	if (products.contains(product.getBarcode()))
        {
            return this;
        }
        else
        {
            for (ProductContainer tContainer : groups)
            {
                ProductContainer location = tContainer.getProductContainer(product);
                if (location != null)
                {
                    return location;
                }
            }
        }
        //For a storage unit, if the product is not in the storage system, then
        //If you *add* the product to the storage system, it should be added
        //at the root node. Therefore, if, after recusing the tree, you
        //cannot find the product, you should return yourself.
        
        //Note this is different than the behaviour for productContainer, which
        //returns null, intentionally so.
        
        //Don't change this again. EDIT: Changed again, at the request of every 
        //person in the room. We have now all decided that returning null is,
        //in fact, the proper behaviour. Now let us never talk of it again.
        return null;
     }


    
    /**
      * Returns the parent of this product container, which is null because this
      * container is actually a StorageUnit (Whose parent is, therefore, the house).
      * @return null
      */
    @Override
    public ProductContainer getParent() 
    {
        assert(true);
        return null;
    }

	/** For the Visitor Pattern
		@param visitor The visitor
	*/
	 public void accept(Visitor visitor)
	 {
		visitor.visit(this);
		for(ProductContainer child: groups)
			child.accept(visitor);
	 }
}
