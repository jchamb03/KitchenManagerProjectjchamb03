package persistence;

import model.Kitchen;
import model.Recipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class JsonWriterTest {

    @Test
    public void testWriteInvalidFile() {
        try {
            Kitchen kitchen = new Kitchen("Owner");
            JsonWriter writer = new JsonWriter("./data/\0illegal:filename.json");
            writer.open();
            Assertions.fail("IOException was expected");

        } catch (FileNotFoundException e) {
            //pass
        }
    }

    @Test
    public void testWriteEmptyKitchen() {
        try {
            Kitchen kitchen = new Kitchen("Owner");
            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.write(kitchen);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            kitchen = reader.read();
            Assertions.assertEquals("Owner", kitchen.getOwner());
            Assertions.assertEquals(0, kitchen.getStock().size());
            Assertions.assertEquals(0, kitchen.getMissing().size());
            Assertions.assertEquals(0, kitchen.getListOfRecipe().size());
        } catch (FileNotFoundException e) {
            Assertions.fail("Exception not expected");
        } catch (IOException e) {
            Assertions.fail("Exception not expected");
        }
    }

    @Test
    public void testWriteKitchen() {
        try {
            Kitchen kitchen = new Kitchen("Owner");
            List<String> ingredients = new ArrayList<>();
            ingredients.add("Chicken");
            ingredients.add("Breading");
            ingredients.add("Oil");
            Recipe r = new Recipe("Fried Chicken", "1: Cook the chicken", ingredients);
            kitchen.addRecipe(r, false);
            kitchen.addKitchenIngredient("Chicken");
            kitchen.addMissingIngredient("Breading");
            kitchen.addKitchenIngredient("Oil");
            JsonWriter writer = new JsonWriter("./data/testWriterContainsData.json");
            writer.open();
            writer.write(kitchen);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterContainsData.json");
            kitchen = reader.read();
            Assertions.assertEquals("Owner", kitchen.getOwner());
            Assertions.assertEquals(2, kitchen.getStock().size());
            Assertions.assertEquals(1, kitchen.getMissing().size());
            Assertions.assertEquals(1, kitchen.getListOfRecipe().size());
            Assertions.assertEquals(1, kitchen.getRecipeNames().size());
            Assertions.assertEquals("1: Cook the chicken", kitchen.getSteps("Fried Chicken"));
            Assertions.assertFalse(kitchen.checkStock("Fried Chicken"));
        } catch (FileNotFoundException e) {
            Assertions.fail("Exception not expected");
        } catch (IOException e) {
            Assertions.fail("Exception not expected");
        }
    }
}
