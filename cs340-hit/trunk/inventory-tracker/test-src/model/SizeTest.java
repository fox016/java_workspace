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
public class SizeTest {
    
    public SizeTest() {
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
     * Test of getAmount method, of class Size.
     */
    @Test
    public void testGetAmount() 
    {
        System.out.println("getAmount");
        Size instance = new Size(1, SupplyType.COUNT);
        assertEquals(1.0, instance.getAmount(),0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetAmountException1() 
    {
        System.out.println("setAmountException1");
        Size instance = new Size(1, SupplyType.COUNT);
        instance.setAmount(-1);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testSetAmountException2() 
    {
        System.out.println("setAmountException2");
        Size instance = new Size(1, SupplyType.COUNT);
        instance.setAmount(1.2);
    }
    /**
     * Test of setAmount method, of class Size.
     */
    @Test
    public void testSetAmount() 
    {
        System.out.println("setAmount");
        Size instance = new Size(1, SupplyType.COUNT);
        instance.setAmount(2);
        assertEquals(2, instance.getAmount(),0.0);
    }

    /**
     * Test of getType method, of class Size.
     */
    @Test
    public void testGetType() 
    {
        System.out.println("getType");
        Size instance = new Size(1, SupplyType.COUNT);
        assertEquals(SupplyType.COUNT, instance.getType());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetTypeException() 
    {
        System.out.println("setAmountException2");
        Size instance = new Size(1.2, SupplyType.FLUID_OUNCES);
        instance.setType(SupplyType.COUNT);
    }
    /**
     * Test of setType method, of class Size.
     */
    @Test
    public void testSetType() 
    {
        System.out.println("setType");
        Size instance = new Size(1, SupplyType.COUNT);
        instance.setType(SupplyType.GALLONS);
        assertEquals(SupplyType.GALLONS, instance.getType());
    }
}
