/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Talonos
 */
public class ProductGroupImplTest {
    
    public ProductGroupImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception 
    {
        ProductManager.instance().addProduct(new Product(Calendar.getInstance(), 
            new ProductBarcode("12345"), "Bagels", new Size(1, SupplyType.COUNT), 
            2, 2));
        ProductManager.instance().addProduct(new Product(Calendar.getInstance(), 
            new ProductBarcode("54321"), "Bagels", new Size(1, SupplyType.COUNT), 
            2, 2));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test of setSupply method, of class ProductGroupImpl.
     */
    @Test
    public void testSetSupply() 
    {
        System.out.println("setSupply");
        ProductGroup instance = new ProductGroupImpl("Breadbox");
        instance.setSupply(new Size(1, SupplyType.COUNT));
        assertEquals(instance.getSupply(), new Size(1, SupplyType.COUNT));
    }
    
    /**
     * Test of getSupply method, of class ProductGroupImpl. It's, uh... kind of
     * like the one up above.
     */
    @Test
    public void testGetSupply() 
    {
        System.out.println("setSupply");
        ProductGroup instance = new ProductGroupImpl("Breadbox");
        instance.setSupply(new Size(1, SupplyType.COUNT));
        assertEquals(instance.getSupply(), new Size(1, SupplyType.COUNT));
    }

    /**
     * Test of toString method, of class ProductGroupImpl.
     */
    @Test
    public void testToString() 
    {
        System.out.println("toString");
        StorageUnit root = Facade.addStorageUnit("Breadbox Holder");
        ProductGroup instance = Facade.addProductGroup("Breadbox",
        		new Size(0, SupplyType.COUNT), root);
        Facade.addProductToContainer(
        		ProductManager.instance().getProductByBarcode(new ProductBarcode ("12345")),
        		instance);
        String expResult = "ProductGroup called Breadbox contains:\n  Bagels";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of beProductGroup method, of class ProductGroupImpl.
     */
    @Test
    public void testBeProductGroup() 
    {
        System.out.println("beProductGroup");
        ProductContainer instance = new ProductGroupImpl("Breadbox");
        assertEquals(instance, instance.beProductGroup());
    }

    /**
     * Test of beStorageUnit method, of class ProductGroupImpl.
     */
    @Test
    public void testBeStorageUnit() 
    {
        System.out.println("beStorageUnit");
        ProductContainer instance = new ProductGroupImpl("Breadbox");
        assertNull(instance.beStorageUnit());
    }

    /**
     * Test of getRoot method, of class ProductGroupImpl.
     */
    @Test
    public void testGetRoot() 
    {
        System.out.println("getRoot");
        StorageUnit root = new StorageUnitImpl("Breadbox");
        ProductGroup parent = root.addContainer("Bread Products");
        ProductGroup instance = parent.addContainer("Breads and Pastries");

        assertEquals(instance.getRoot(), root);
        assertEquals(parent.getRoot(), root);
    }

    /**
     * Test of compareTo method, of class ProductGroupImpl.
     */
    @Test
    public void testCompareTo() 
    {
        System.out.println("compareTo");
        ProductGroup a = new ProductGroupImpl("A breadbox");
        ProductGroup b = new ProductGroupImpl("Breadbox");
        assert(a.compareTo(b)<0);
        assert(b.compareTo(a)>0);
        assert(a.compareTo(a)==0);
        assert(b.compareTo(b)==0);
    }

    /**
     * Test of getProductContainer method, of class ProductGroupImpl.
     */
    @Test
    public void testGetProductContainer() 
    {
        System.out.println("getProductContainer");
        Product test = ProductManager.instance().getProductByBarcode(new ProductBarcode("12345"));
        StorageUnit root = new StorageUnitImpl("Breadbox");
        ProductContainer instance = root.addContainer("Bread Products");
        ProductContainer finalC = instance.addContainer("Under the secret panel");
        finalC.addProduct(new ProductBarcode("12345"));
        ProductContainer toCompare = instance.getProductContainer(test);
        assertEquals(toCompare, finalC);
        test = ProductManager.instance().getProductByBarcode(new ProductBarcode("54321"));
        toCompare = instance.getProductContainer(test);
        assertNull(toCompare);
    }

    /**
     * Test of getParent method, of class ProductGroupImpl.
     */
    @Test
    public void testGetParent() 
    {
        System.out.println("getParent");
        StorageUnit root = new StorageUnitImpl("Breadbox");
        ProductGroup parent = root.addContainer("Bread Products");
        ProductGroup instance = parent.addContainer("Breads and Pastries");

        assertEquals(instance.getParent(), parent);
        assertEquals(parent.getParent(), root);
    }
}
