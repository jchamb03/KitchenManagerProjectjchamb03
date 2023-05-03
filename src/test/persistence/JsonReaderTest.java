package persistence;

import model.Kitchen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;


//test JsonReader Class.
//JsonReaderTest uses information from provided
public class JsonReaderTest {
    @Test
    public void testReaderNoFile() {
        JsonReader reader = new JsonReader("./data/fake.json");
        try {
            Kitchen kitchen = reader.read();
            Assertions.fail("IOException expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    public void testReaderNoRecipe() {
        JsonReader reader = new JsonReader("./data/testKitchenNoRecipe.json");
        try {
            Kitchen kitchen = reader.read();
            Assertions.assertEquals(3, kitchen.getStock().size());
            Assertions.assertEquals("Garlic", kitchen.getStock().get(2));
            Assertions.assertEquals(2, kitchen.getMissing().size());
            Assertions.assertEquals("Oil", kitchen.getMissing().get(1));
            Assertions.assertEquals("Jeremy", kitchen.getOwner());
        } catch (IOException e) {
            Assertions.fail("IOException not expected");
        }
    }

    @Test
    public void testReaderAllAspects() {
        JsonReader reader = new JsonReader("./data/testKitchenAllAspects.json");
        try {
            Kitchen kitchen = reader.read();
            Assertions.assertEquals(3, kitchen.getStock().size());
            Assertions.assertEquals("Garlic", kitchen.getStock().get(2));
            Assertions.assertEquals(2, kitchen.getMissing().size());
            Assertions.assertEquals("Oil", kitchen.getMissing().get(1));
            Assertions.assertEquals("Jeremy", kitchen.getOwner());
            Assertions.assertEquals(2, kitchen.getRecipeNames().size());
            Assertions.assertEquals("1: Do nothing. 2: End.", kitchen.getSteps("Chicken Parmesan"));
            Assertions.assertEquals(2, kitchen.getIngredients("Chicken Parmesan").size());
        } catch (IOException e) {
            Assertions.fail("IOException not expected");
        }
    }
}
