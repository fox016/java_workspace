/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Talonos
 */
public class ProductContainerImplTest 
{
    
    public ProductContainerImplTest() 
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception 
    {
        ProductManager.instance().addProduct(new Product(Calendar.getInstance(), 
            new ProductBarcode("12345"), "Bagels", new Size(1, SupplyType.COUNT), 
            2, 2));
        ProductManager.instance().addProduct(new Product(Calendar.getInstance(), 
            new ProductBarcode("54321"), "Milk", new Size(1, SupplyType.COUNT), 
            2, 2));
    }

    @AfterClass
    public static void tearDownClass() throws Exception 
    {
    }
    
    @Before
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() 
    {
    }
    
        /**
     * Test of isValidName method, of class ProductContainerImpl.
     */
    @Test
    public void testIsValidName() 
    {
        System.out.println("isValidName");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        assertEquals(false, instance.isValidName(""));
        assertEquals(false, instance.isValidName(null));
        assertEquals(true, instance.isValidName("Bread Products"));
        instance.addContainer("Bread Products");
        assertEquals(false, instance.isValidName("Bread Products"));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddContainerException()
    {
        System.out.println("addContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addContainer("");
    }

    /**
     * Test of addContainer method, of class ProductContainerImpl.
     */
    @Test
    public void testAddContainer() 
    {
        System.out.println("addContainer");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        ProductGroup result = instance.addContainer("Bread Products");
        assertEquals("Bread Products", result.getName());
        assertEquals(result, instance.getProductContainer("Bread Products"));
        Set<String> returns = instance.getContainers();
        assertEquals (returns.size(), 1);
        for (String s : returns)
        {
            assertEquals("Bread Products", s);   
        }
    }

    
    @Test (expected = IllegalArgumentException.class)
    public void testRenameContainerException1()
    {
        System.out.println("addContainer - Exception 1");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addContainer("Bread Products");
        instance.renameContainer(null, "Grain Products");
    }
        
    @Test (expected = IllegalArgumentException.class)
    public void testRenameContainerException2()
    {
        System.out.println("addContainer - Exception 2");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addContainer("Bread Products");
        instance.addContainer("Grain Products");
        instance.renameContainer("Bread Products", "Grain Products");
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testRenameContainerException3()
    {
        System.out.println("addContainer - Exception 3");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.renameContainer("Bread Products", "Grain Products");
    }
   /**
     * Test of renameContainer method, of class ProductContainerImpl.
     */
    @Test
    public void testRenameContainer() 
    {
        System.out.println("renameContainer");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addContainer("Bread Products");
        instance.renameContainer("Bread Products", "Grain Products");
        Set<String> returns = instance.getContainers();
        assertEquals (returns.size(), 1);
        for (String s : returns)
        {
            assertEquals("Grain Products", s);   
        }
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testDeleteContainerException1()
    {
        System.out.println("deleteContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.deleteContainer("Dairy Products");
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testDeleteContainerException2()
    {
        System.out.println("deleteContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.deleteContainer("Bread Products");
    }


    /**
     * Test of deleteContainer method, of class ProductContainerImpl.
     */
    @Test
    public void testDeleteContainer() 
    {
        System.out.println("deleteContainer");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addContainer("Bread Products");
        instance.addContainer("Grain Products");
        instance.deleteContainer("Bread Products");
        Set<String> returns = instance.getContainers();
        assertEquals (returns.size(), 1);
        for (String s : returns)
        {
            assertEquals("Grain Products", s);   
        }
        instance.deleteContainer("Grain Products");
        returns = instance.getContainers();
        assertEquals (returns.size(), 0);
    }

    /**
     * Test of getProductContainer method, of class ProductContainerImpl.
     */
    @Test
    public void testGetProductContainer() 
    {
        System.out.println("getProductContainer");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        ProductContainer bread = instance.addContainer("Bread Products");
        ProductContainer grain = instance.addContainer("Grain Products");
        assertEquals (bread, instance.getProductContainer("Bread Products"));
        assertEquals (grain, instance.getProductContainer("Grain Products"));
        assertEquals (null, instance.getProductContainer("Dairy Products"));
    }

    /**
     * Test of getContainers method, of class ProductContainerImpl.
     */
    @Test
    public void testGetContainers() 
    {
        System.out.println("getContainers");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        ProductContainer bread = instance.addContainer("Bread Products");
        ProductContainer grain = instance.addContainer("Grain Products");
        Set<String> returns = instance.getContainers();
        assertEquals (returns.size(), 2);
        int x = 0;
        for (String s : returns)
        {
            if (x == 0)
            {
                assertEquals("Bread Products", s);   
                x++;
            }
            else
            {
                assertEquals("Grain Products", s);   
            }
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddProductException1() //Adding non-existant product.
    {
        System.out.println("deleteContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addProduct(new ProductBarcode("12321"));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddProductException2() //Adding a product already in the unit.
    {
        System.out.println("deleteContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.addProduct(new ProductBarcode("12345"));
        instance.addProduct(new ProductBarcode("12345"));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddProductException3() //Adding a product already elsewhere in the tree
    {
        System.out.println("deleteContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        ProductContainer sub = instance.addContainer("Left Corner");
        sub.addProduct(new ProductBarcode("12345"));
        instance.addProduct(new ProductBarcode("12345")); //Throws Exception.
    }
    /**
     * Test of addProduct method, of class ProductContainerImpl.
     */
    @Test
    public void testAddProduct() 
    {
        System.out.println("addProduct");
        ProductContainer instance = new StorageUnitImpl("Breadbox");
        instance.addProduct(new ProductBarcode("12345"));
        Set<ProductBarcode> products = instance.getProducts();
        assertEquals(products.size(),1);
        assert(products.contains(new ProductBarcode("12345")));
    }
    
    /**
     * Test of getProducts method, of class ProductContainerImpl.
     */
    @Test
    public void testGetProducts() 
    {
        System.out.println("getProducts");
        ProductContainer instance = new StorageUnitImpl("Breadbox");
        instance.addProduct(new ProductBarcode("12345"));
        Set<ProductBarcode> products = instance.getProducts();
        assertEquals(products.size(),1);
        assert(products.contains(new ProductBarcode("12345")));
        instance.addProduct(new ProductBarcode("54321"));
        products = instance.getProducts();
        assertEquals(products.size(),2);
        assert(products.contains(new ProductBarcode("12345")));
        assert(products.contains(new ProductBarcode("54321")));
        products.add(new ProductBarcode("12321"));
        //Make sure that adding to the returned set does not modify the container.
        products = instance.getProducts();
        assertEquals(products.size(),2);
        assert(products.contains(new ProductBarcode("12345")));
        assert(products.contains(new ProductBarcode("54321")));
    }

    @Test (expected = NoSuchElementException.class)
    public void testRemoveProductException1() //Product not in container
    {
        System.out.println("deleteContainer - Exception");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        instance.removeProduct(new ProductBarcode("12345"));
    }
    
    /**
     * Test of removeProduct method, of class ProductContainerImpl.
     */
    @Test
    public void testRemoveProduct() {
        System.out.println("removeProduct");
        ProductContainer instance = new StorageUnitImpl("Breadbox");
        instance.addProduct(new ProductBarcode("12345"));
        instance.addProduct(new ProductBarcode("54321"));
        Set<ProductBarcode> products = instance.getProducts();
        assertEquals(products.size(),2);
        assert(products.contains(new ProductBarcode("12345")));
        assert(products.contains(new ProductBarcode("54321")));
        instance.removeProduct(new ProductBarcode("12345"));
        products = instance.getProducts();
        assertEquals(products.size(),1);
        assert(products.contains(new ProductBarcode("54321")));
    }

    /**
     * Test of hasProduct method, of class ProductContainerImpl.
     */
    @Test
    public void testHasProduct() 
    {
        System.out.println("hasProduct");
        ProductContainer instance = new StorageUnitImpl("Breadbox");
        instance.addProduct(new ProductBarcode("12345"));
        ProductContainer sub = instance.addContainer("Left Corner");
        sub.addProduct(new ProductBarcode("54321"));
        assert(instance.hasProduct(new ProductBarcode("12345")));
        assert(instance.hasProduct(new ProductBarcode("54321")));
    }

    /**
     * Test of rename method, of class ProductContainerImpl.
     * 
     * Small intentionally, because there are few errors to test for. All error
     * checking will occur in parent functions.
     */
    @Test
    public void testRename() {
        System.out.println("rename");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        assertEquals("Breadbox", instance.getName());
        instance.rename("Not a breadbox");
        assertEquals("Not a breadbox", instance.getName());
    }

    /**
     * Test of getProductContainer method, of class ProductContainerImpl.
     */
    @Test
    public void testGetProductContainer_Product() 
    {
        System.out.println("getProductContainer");
        Product test = ProductManager.instance().getProductByBarcode(new ProductBarcode("12345"));
        assertNotNull(test); //Used to track down the error in Chris' code.
        ProductContainer instance = new StorageUnitImpl("Breadbox");
        ProductContainer next = instance.addContainer("Left Corner");
        ProductContainer finalC = next.addContainer("Under the secret panel");
        finalC.addProduct(new ProductBarcode("12345"));
        ProductContainer toCompare = instance.getProductContainer(test);
        assertEquals(toCompare, finalC);
        test = ProductManager.instance().getProductByBarcode(new ProductBarcode("54321"));
        toCompare = instance.getProductContainer(test);
        assertEquals(toCompare, null);
    }

    /**
     * Test of getName method, of class ProductContainerImpl.
     */
    @Test
    public void testGetName() 
    {
        System.out.println("getName");
        ProductContainerImpl instance = new StorageUnitImpl("Breadbox");
        assertEquals("Breadbox", instance.getName());
        instance.rename("Not a breadbox");
        assertEquals("Not a breadbox", instance.getName());
    }

}
