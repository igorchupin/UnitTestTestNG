package parser;

import com.google.gson.Gson;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import shop.Cart;
import shop.RealItem;
import shop.VirtualItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static org.testng.Assert.*;

public class JsonParserTest {

    Cart testCart;
    JsonParser testParser;
    File newFile;
    Gson gson;
    int cartNameLength = 10; //Set the Cart name length
    String cartName;
    String pathToFolder = "src/main/resources/";
    String pathToFile;


    public String generateCardName () {
        String cartName = "";
        for (int i = 0; i <= cartNameLength  ; i++) {
            int rndChar = 97 + (new Random().nextInt(122 - 97));
            Character tempChar = (char) rndChar;
            cartName = cartName + tempChar;
        }
        return cartName;
    }


    public Cart addingItemsToCart (Cart cart) {

        RealItem testRealItem = new RealItem();
        testRealItem.setName("Test Real Item Name");
        testRealItem.setWeight(100);
        testRealItem.setPrice(10);

        VirtualItem testVirtualItem = new VirtualItem();
        testRealItem.setName("Test Virtual Item");
        testVirtualItem.setSizeOnDisk(200);
        testRealItem.setPrice(20);

        testCart.addRealItem(testRealItem);
        testCart.addVirtualItem(testVirtualItem);

        return cart;
    }

    @BeforeMethod (groups = "Before")
    void setUp() {
        cartName = new String(generateCardName());
        testCart = new Cart(cartName);
        testParser = new JsonParser();
        pathToFile = pathToFolder + cartName + ".json";
        newFile = new File(pathToFile);
        gson = new Gson();
    }

    @AfterMethod (groups = "After")
    void tearDown() throws IOException {
        Path fileToDeletePath = Paths.get(pathToFile);
        Files.deleteIfExists(fileToDeletePath);
    }


    @Test (testName = "File was created successfully", groups = {"JsonParser", "Write_to_file", "Smoke"},
            priority = 1)

    public void writeToFileSmoke() throws IOException {

        testParser.writeToFile(testCart);

        Assert.assertFalse(newFile.createNewFile(), "File was not created by *writeToFile*");
    }


    @Test (testName = "Write in already existing file", groups = {"JsonParser", "Write_to_file", "Critical Path"},
            priority = 2)

    public void writeToFileAlreadyexists() throws IOException {
        newFile.createNewFile();
        long sizeBefore = newFile.length();
        testParser.writeToFile(testCart);
        long sizeAfter = newFile.length();

        Assert.assertTrue(sizeBefore < sizeAfter, "File size not changed");
    }


    @Test (testName = "Creating file with null in the name", groups = {"JsonParser", "Write_to_file", "Extended"},
            priority = 3)

    public void writeToFileNullName() throws IOException {
        Path fileToDeletePath = Paths.get(pathToFolder + "null.json");
        Cart cartNull = new Cart(null);
        testParser.writeToFile(cartNull);

        Assert.assertTrue(Files.deleteIfExists(fileToDeletePath), "File was not created");
    }


    @Test (testName = "File contains correct data from Object", groups = {"JsonParser", "Write_to_file", "Smoke"},
            priority = 1)

    public void writeToFileContainsCorrectData() throws IOException {
        addingItemsToCart(testCart);
        testParser.writeToFile(testCart);
        Reader reader = new FileReader(pathToFile);
        Cart createdCart = gson.fromJson(reader, Cart.class);
        reader.close();

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(createdCart.getCartName(), testCart.getCartName(), "Incorrect Cart Name");
        softAssert.assertEquals(createdCart.getTotalPrice(), testCart.getTotalPrice(), "Incorrect total price");
        softAssert.assertAll();
    }


    //Disabled Test
    @Test (testName = "Exception because file is read only", groups = {"JsonParser", "Write_to_file", "Critical Path"},
            priority = 2,
            expectedExceptions = IOException.class)
    @Ignore

    public void writeToFileReadOnly() throws IOException {
     newFile.createNewFile();
     newFile.setReadOnly();
     System.out.println(newFile.createNewFile());
    }


    //Disabled Test another decision
    @Test (enabled = false,
            testName = "Exception because file is read only", groups = {"JsonParser", "Write_to_file", "Critical Path"},
            priority = 2,
            expectedExceptions = IOException.class)

    public void writeToFileReadOnly2() throws IOException {
        newFile.createNewFile();
        newFile.setReadOnly();
    }


    @Test (testName = "Positive test, reading data from file", groups = {"JsonParser", "Read_From_File", "Smoke"},
            priority = 1)

    public void readFromFileSmoke() throws IOException {

        addingItemsToCart(testCart);
        File testCartFile = new File(pathToFile);

        FileWriter writer = new FileWriter(testCartFile);
        writer.write(gson.toJson(testCart));
        writer.close();

        Reader reader = new FileReader(pathToFile);
        Cart expectedCart = gson.fromJson(reader, Cart.class);
        reader.close();

        Cart actualCart = testParser.readFromFile(testCartFile);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals (actualCart.getCartName(), expectedCart.getCartName(), "incorrect cart " +
                        "name value");
        softAssert.assertEquals (actualCart.getTotalPrice(), expectedCart.getTotalPrice(), "Incorrect total " +
                        "Price" );
    }


    @Test (testName = "Reading data from empty file", groups = {"JsonParser", "Read_From_File", "Extended"},
            priority = 3,
            expectedExceptions = NullPointerException.class)

    public void readFromEmptyFile() throws IOException {

        newFile.createNewFile();

        Cart newCart = testParser.readFromFile(newFile);
        newCart.getCartName();
    }


    @DataProvider(name = "Unexisting_Pathes")
    public static Object [][] unexistingPathes() {
        return new Object[][] {
                {"empty"},
                {"src\\main\\\\rsources\\\\cart.json"},
                {"src/main/resources/CartName"},
                {"/vCartName.json"},
                {"src/main/resources/andrew-cartn.json"},
                {"src/main/resources/CartNme"},
                {"C:\\Users\\cloud_qa1\\.android"},
        };
    }


    //Values form Data provider
    @Test (dataProvider = "Unexisting_Pathes",
           testName = "Parametrized Test: Reading data from unexciting files",
           groups = {"JsonParser", "Read_From_File", "Critical Path"},
           priority = 2,
           expectedExceptions = NoSuchFileException.class)

    void testWithValueSource(String filePathParametrized) {
        File newFile2 = new File(filePathParametrized);
        testParser.readFromFile(newFile2);
    }

}
