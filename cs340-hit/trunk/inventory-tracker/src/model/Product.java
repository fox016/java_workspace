package model;

import model.report.Visitor;

import java.io.Serializable;
import java.util.Calendar;

/**
 * The class implements the Product interface
 * 
 * @author Clint Rollins
 */

public class Product implements Serializable, Comparable<Product> {

	private static final long serialVersionUID = -6744337854867882901L;

	/** The date on which this product was created */
	private Calendar creationDate;

	/** The manufacturer's barcode of this product */
	private ProductBarcode barcode;

	/** A description of this product */
	private String description;

	/** The size of the product. */
	private Size size;

	/** The Product's shelf life, measured in months. */
	private int shelfLife;

	/**
	 * The number of this product required for a 3-month supply. A value of zero
	 * means "unspecified."
	 */
	private int threeMonthSupply;

	/** Product containers that contain this product. */
	// private Set<ProductContainer> productContainers;

	/**
	 * Creates a product. Individual parameters are specified under each
	 * variable declaration.
	 * 
	 */

	public Product(Calendar creationDate, ProductBarcode barcode,
			String description, Size size, int shelfLife, int threeMonthSupply) {
		this.creationDate = creationDate;
		this.barcode = barcode;
		this.description = description;
		this.size = size;
		this.shelfLife = shelfLife;
		this.threeMonthSupply = threeMonthSupply;
	};

	/**
	 * Updates the Product
	 * 
	 * @param newProduct
	 *            The Product containing the updated values that are to replace
	 *            the old Product's values
	 */
	public void updateProduct(Product newProduct) {
		assert(newProduct != null);
		if(!newProduct.isValidProduct())
		{
			throw new IllegalStateException("Cannot update to " +
					newProduct.getDescription());
		}
		this.setCreationDate(newProduct.getCreationDate());
		this.setDescription(newProduct.getDescription());
		this.setShelfLife(newProduct.getShelfLife());
		this.setSize(newProduct.getSize());
		this.setThreeMonthSupply(newProduct.getThreeMonthSupply());

	};
	
	/**
	 * Checks to see if the oldProduct can be updated with the properties of the
	 * new product.
	 * 
	 * @param oldProduct
	 *            the old product
	 * @param newProduct
	 *            the new product containing new properties that are to replace
	 *            those of the old product
	 * @return boolean telling us whether a product can be updated.
	 */
public boolean isValidProduct() {
		
		if(this.getSize().getAmount() < 0)
			return false;
		if(this.getSize().getType() == SupplyType.COUNT)
		{
			if(this.getSize().getAmount() != 1)
				return false;
		}
		if(this.getDescription() == null || this.getDescription().isEmpty())
			return false;
		if(this.getBarcode() == null)
			return false;
		if(this.getShelfLife() < 0)
			return false;
		return true;
	}

	/**
	 * Compares two products
	 * 
	 * @param other
	 *            Product to compare with.
	 * @return 0 if equal, non-zero integer otherwise
	 */
	public int compareTo(Product other) {
		return this.barcode.compareTo(other.barcode);
	}
	
	public boolean equals(Product other) {
		if(other == null)
			return false;
		return this.barcode.compareTo(other.barcode) == 0; 
	}

	public String toString() {
		return barcode.toString();
	}

	/**
	 * A hash code for a hash map of products
	 */
	@Override
	public int hashCode() {
		return barcode.hashCode();
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public ProductBarcode getBarcode() {
		return barcode;
	}

	public void setBarcode(ProductBarcode barcode)
			throws IllegalArgumentException {
		assert (barcode != null);
		if (barcode.compareTo(new ProductBarcode("")) == 0)
			throw new IllegalArgumentException("Barcode can't be empty");
		this.barcode = barcode;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Changes product's description
	 * 
	 * @param description
	 * @throws IllegalArgumentException
	 */
	public void setDescription(String description)
			throws IllegalArgumentException {
		if (description == null || description.isEmpty())
			throw new IllegalArgumentException("Description cannot be empty");

		this.description = description;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		assert (size.getAmount() > 0);
		if (size.getType() == SupplyType.COUNT) {
			assert (Math.floor(size.getAmount()) - size.getAmount() == 0);
		}
		this.size = size;
	}

	public int getShelfLife() {
		return shelfLife;
	}

	/**
	 * Changes shelf life
	 * 
	 * @param shelfLife
	 * @throws IllegalArgumentException
	 */
	public void setShelfLife(int shelfLife) throws IllegalArgumentException {
		if (shelfLife < 0)
			throw new IllegalArgumentException(
					"Shelf life must be non-negative");

		this.shelfLife = shelfLife;
	}

	public int getThreeMonthSupply() throws IllegalArgumentException {
		if (threeMonthSupply < 0)
			throw new IllegalArgumentException(
					"Shelf life must be non-negative");
		return threeMonthSupply;
	}

	/**
	 * Changes 3-month supply
	 * 
	 * @param threeMonthSupply
	 * @throws IllegalArgumentException
	 */
	public void setThreeMonthSupply(int threeMonthSupply)
			throws IllegalArgumentException {
		if (threeMonthSupply < 0)
			throw new IllegalArgumentException(
					"Three month supply must be non-negative");

		this.threeMonthSupply = threeMonthSupply;
	}

	/** For the Visitor Pattern
		@param visitor The visitor
	*/
	 public void accept(Visitor visitor)
	 {
	 	visitor.visit(this);
	 }
}

