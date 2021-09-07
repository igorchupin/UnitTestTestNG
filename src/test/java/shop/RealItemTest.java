package shop;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RealItemTest {


    private static final double WEIGTH = 100;
    private static final double PRICE = 10;
    RealItem testRealItem;

    @BeforeTest(groups = "Before")
    void setUp() {
        testRealItem = new RealItem();
        testRealItem.setName("TestRealItem");
        testRealItem.setPrice(PRICE);
        testRealItem.setWeight(WEIGTH);
    }


    @Test(testName = "Real item to String", groups = {"Smoke", "Virtual_item_to_String"}, priority = 2)
    void testRealItemToString() {

        String expectedResult = String.format("Class: %s; Name: %s; Price: %s; Weight: %s",
                testRealItem.getClass(), testRealItem.getName(), testRealItem.getPrice(),
                testRealItem.getWeight());

        Assert.assertEquals(testRealItem.toString(), expectedResult, "Something went wrong with To_string");
    }

}
