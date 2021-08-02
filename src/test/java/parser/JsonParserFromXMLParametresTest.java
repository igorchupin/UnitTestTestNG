package parser;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import shop.Cart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonParserFromXMLParametresTest {

    Cart testCart;
    File newFile;
    JsonParser testParser;
    String pathToFile;

    @BeforeMethod
    void setUp() {
        testParser = new JsonParser();
    }

    @AfterMethod
    void tearDown() throws IOException {
        Path fileToDeletePath = Paths.get(pathToFile);
        Files.deleteIfExists(fileToDeletePath);
    }

    @Test(testName = "Write in already existing file", groups = {"JsonParser", "Write_to_file", "Extended", "Broken"})
    @Parameters({"cartName", "pathToFolder", "message"})
    public void testWithValueSource(String cartName, String pathToFolder, String message) throws IOException {
        testCart = new Cart(cartName);
        pathToFile = pathToFolder + cartName + ".json";
        newFile = new File(pathToFile);
        newFile.createNewFile();
        long modified = newFile.lastModified();
        long sizeBefore = newFile.length();
        testParser.writeToFile(testCart);
        long modified2 = newFile.lastModified();
        long sizeAfter = newFile.length();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(sizeBefore < sizeAfter, message);
        softAssert.assertFalse(modified > modified2, message);
        softAssert.assertAll();
    }
}
