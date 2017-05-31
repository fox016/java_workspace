package model;

/**
 * This is simply a product-container that can be renamed.
 * @author Talonos
 */
public interface ChangableProductContainer extends ProductContainer
{
    /**
     * Renames this product container. Does not do error checking; for that, use
     * the parent component's "Rename Child" method.
     * @param newName the new name to give this product container.
     */
    public void rename(String newName);
    
}
