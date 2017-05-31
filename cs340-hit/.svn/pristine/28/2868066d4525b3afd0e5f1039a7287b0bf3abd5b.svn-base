/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Talonos
 */
public class StorageUnitImplTest {
    
    public StorageUnitImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of setName method, of class StorageUnitImpl.
     */
    @Test
    public void testSetName() 
    {
        System.out.println("setName");
        StorageUnit a = new StorageUnitImpl("Breadbox");
        a.setName("Not a");
        assertEquals("Not a",a.getName());
    }
    
    /**
     * Test of equals method, of class StorageUnitImpl.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        StorageUnit a = new StorageUnitImpl("Breadbox");
        StorageUnit b = new StorageUnitImpl("Breadbox");
        assert(!b.equals(a));
        assert(!a.equals(b));
        assert(a.equals(a));
        assert(b.equals(b));
    }

    /**
     * Test of getRoot method, of class StorageUnitImpl.
     */
    @Test
    public void testGetRoot() 
    {
        System.out.println("getRoot");
        StorageUnit a = new StorageUnitImpl("A breadbox");
        assertEquals(a, a.getRoot());
    }

    /**
     * Test of beProductGroup method, of class StorageUnitImpl.
     */
    @Test
    public void testBeProductGroup() {
        System.out.println("beProductGroup");
        StorageUnit a = new StorageUnitImpl("A breadbox");
        assertNull(a.beProductGroup());
    }

    /**
     * Test of beStorageUnit method, of class StorageUnitImpl.
     */
    @Test
    public void testBeStorageUnit() {
        System.out.println("beStorageUnit");
        ProductContainer a = new StorageUnitImpl("A breadbox");
        assertEquals(a.beStorageUnit(), a);
    }

    /**
     * Test of compareTo method, of class StorageUnitImpl.
     */
    @Test
    public void testCompareTo() 
    {
        System.out.println("compareTo");
        StorageUnit a = new StorageUnitImpl("A breadbox");
        StorageUnit b = new StorageUnitImpl("Breadbox");
        assert(a.compareTo(b)<0);
        assert(b.compareTo(a)>0);
        assert(a.compareTo(a)==0);
        assert(b.compareTo(b)==0);
    }

    /**
     * Test of getProductContainer method, of class StorageUnitImpl.
     */
    @Test
    public void testGetProductContainer() 
    {
        //Tested, for some bizzarre reason, in ProductContainerImplTest, even though
        //ProductContainerImpl doesn't actually implement this method.
    }

    /**
     * Test of getParent method, of class StorageUnitImpl.
     */
    @Test
    public void testGetParent() 
    {
        System.out.println("getParent");
        StorageUnit instance = new StorageUnitImpl("Breadbox");
        assertNull(instance.getParent());
    }
}
