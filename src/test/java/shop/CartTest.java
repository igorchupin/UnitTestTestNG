package shop;

import nl.altindag.console.ConsoleCaptor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static org.testng.Assert.assertEquals;

public class CartTest {

    private static final int WEIGHT_OR_SIZE = 99;
    private static final double priceForSet = 10;
    private static final double totalPriceExpected = priceForSet + priceForSet * 0.2;
    Cart testCart;
    Cart testCart2;
    RealItem testRealIem;
    VirtualItem testVirtualItem;
    ConsoleCaptor consoleCaptor;

    @BeforeMethod(groups = "Before")
    void setUp() {
        testCart = new Cart("Name Of Cart");
        testRealIem = new RealItem();
        testRealIem.setPrice(priceForSet);
        testVirtualItem = new VirtualItem();
        testVirtualItem.setPrice(priceForSet);
        testVirtualItem.setName("Virt Item Name");
    }

    @Test(testName = "Add Real And Virtual Item and then get total Price",
            groups = {"Smoke", "Cart Get Total Price", "Cart Add Real Item", "Cart Add Virtual Item"})
    public void addRealVirtualItemAndGetTotalPrice() {
        testCart.addRealItem(testRealIem);
        testCart.addVirtualItem(testVirtualItem);

        assertEquals(testCart.getTotalPrice(), totalPriceExpected * 2,
                "Something wrong with getting total price");
    }

    @Test(testName = "Delete virtual item", groups = {"Smoke", "Delete virtual item", "Cart Add Virtual Item"})
    public void deleteVirtualItem() {
        testCart.addVirtualItem(testVirtualItem);
        double totalPriceBefore = testCart.getTotalPrice();
        testCart.deleteVirtualItem(testVirtualItem);
        consoleCaptor = new ConsoleCaptor();
        testCart.showItems();
        String resultAfter = consoleCaptor.getStandardOutput().toString();
        consoleCaptor.close();
        double totalPriceAfter = testCart.getTotalPrice();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(resultAfter.contains(testVirtualItem.getName()), "Name of the deleted Virtual " +
                "item is shown");
        softAssert.assertTrue(totalPriceBefore < totalPriceAfter, "Total price after deletion is higher or equal");
        softAssert.assertAll();
    }
}