package shop;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class VirtualItemTest {

    private static final double SIZE = 999;
    private static final double PRICE = 100;
    VirtualItem testVirtualItem;

    @BeforeTest(groups = "Before")
    public void setUp() {
        testVirtualItem = new VirtualItem();
        testVirtualItem.setSizeOnDisk(SIZE);
        testVirtualItem.setPrice(PRICE);
    }


    @Test(testName = "Virtual item to String", groups = {"Smoke", "Virtual_item_to_String"}, priority = 1)
    public void testVirtualItemToString() {
        String expectedResult = String.format("Class: %s; Name: %s; Price: %s; Size on disk: %s",
                testVirtualItem.getClass(), testVirtualItem.getName(),
                testVirtualItem.getPrice(), testVirtualItem.getSizeOnDisk());

        Assert.assertEquals(testVirtualItem.toString(), expectedResult, "Something went wrong with To_string \n");
    }
}