package shop;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CartTest {

    Cart testCart;
    RealItem testRealIem;
    VirtualItem testVirtualItem;
    private static final int WEIGTHORSIZE = 99;
    private static final double priceForSet = 10;
    private static final double totalPriceExpected = priceForSet + priceForSet * 0.2;

    @BeforeTest (groups = "Before")
    void setUp() {
        testCart = new Cart("Name Of Cart");
        testRealIem = new RealItem();
        testRealIem.setPrice(priceForSet);
        testVirtualItem = new VirtualItem();
        testVirtualItem.setPrice(priceForSet);
    }


    @Test (testName = "Add Real And Virtual Item and then get total Price",
            groups = {"Smoke", "Cart Get Total Price", "Cart Add Real Item", "Cart Add Virtual Item"},
            priority = 2)

    public void addRealVirtualItemAndGetTotalPrice() {

        testCart.addRealItem(testRealIem);
        testCart.addVirtualItem(testVirtualItem);

        assertEquals(testCart.getTotalPrice(), totalPriceExpected*2,
                "Something wrong with getting total price");
    }

}