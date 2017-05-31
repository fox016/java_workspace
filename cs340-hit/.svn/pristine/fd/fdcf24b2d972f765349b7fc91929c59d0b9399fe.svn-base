/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import model.report.Visitor;

/**
 *
 * @author Talonos
 */
public interface StorageUnit extends ProductContainer, Comparable {

    /**
     * Tests the quality between two storageUnits.
     * @param other the other storage unit to compare to.
     * @return true if the two storage units are equals, false otherwise.
     */
    boolean equals(Object other);

    /**
     * Returns the name of the Storage Unit
     *
     * @throws IllegalStateException Storage Unit should have name upon construction
     * @return name of Storage Unit
     */
    String getName() throws IllegalStateException;


    /**
     * This method should be called from the House, which
     * checks to make sure that the new name is valid
     *
     * @Deprecated Not needed and unsafe. House should contain a list of
     * "ChangableProductContainer", which has a rename method. If you make the
     * rename method public, then anybody can call it! Also, by putting it in
     * storage unit, you cannot polymorphically inherit it from the base class
     * "ProductContainer," weakening the composite design.
     * @throws IllegalArgumentException if name is not valid
     * @param newName
     */
    void setName(String newName) throws IllegalArgumentException;

	 void accept(Visitor visitor);
    
}
