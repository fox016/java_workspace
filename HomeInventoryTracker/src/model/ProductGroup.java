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
public interface ProductGroup extends ProductContainer
{
	void setParent(ProductContainer parent);

    /**
     * Gets the amount of supply needed in a three month supply.
     * Note that because this returns a pointer to the size held in the Product
     * group internally, editing the returned size object edits the supply
     * in this product group. Beware setting two product groups to have the same
     * size object! Use the Size(Size) constructor to duplicate one if need be!
     * @return the type and amount necessary to fuel a 3 month supply.
     */
    Size getSupply();

    /**
     * Sets the 3 month supply of the product group.
     * @param supply the new Supply type.
     * @throws IllegalArgumentException if the type is count and the amount is non-integer.
     */
    void setSupply(Size supply) throws IllegalArgumentException;

    /**
     * Converts this ProductGroup into a String representation of this
     * ProductGroup, for use with debugging.
     * @return String representation of product group
     */
    String toString();

    public String getName();
	 void accept(Visitor visitor);
    
}
