package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KitchenTest {
    Kitchen testKitchen ;

    @BeforeEach
    public void runBefore() {
        testKitchen = new Kitchen("owner");
    }

    @Test
    public void testAddOneRecipe() {
        List<String> test = new ArrayList<>();
        Recipe a = new Recipe("Test", "1: Don't do anything", test);
        testKitchen.addRecipe(a, false);
        assertEquals(1, testKitchen.getListOfRecipe().size());
        assertEquals("1: Don't do anything", testKitchen.getSteps("Test"));
        assertEquals(0, testKitchen.getIngredients("Test").size());
    }

    @Test
    public void testAddMultipleRecipes() {
        List<String> test1 = new ArrayList<>();
        List<String> test2 = new ArrayList<>();
        Recipe a = new Recipe("Test1","1: Don't do anything",test1);
        Recipe b = new Recipe("Test2","2: Don't do anything",test2);
        testKitchen.addRecipe(a, false);
        testKitchen.addRecipe(b, false);
        assertEquals(2, testKitchen.getListOfRecipe().size());
        assertEquals("1: Don't do anything", testKitchen.getSteps("Test1"));
        assertEquals("2: Don't do anything", testKitchen.getSteps("Test2"));
        assertEquals(0, testKitchen.getIngredients("Test1").size());
        assertEquals(0, testKitchen.getIngredients("Test2").size());
    }

    @Test
    public void testGetRecipeIngredients() {
        List<String> test1 = new ArrayList<>();
        test1.add("Oregano");
        test1.add("Chicken");
        test1.add("Water");
        Recipe a = new Recipe("Test1","1: Don't do anything",test1);
        testKitchen.addRecipe(a, false);
        List<String> ing = testKitchen.getIngredients("Test1");
        assertEquals(3, ing.size());
        assertEquals("Oregano", ing.get(0));
    }

    @Test
    public void testGetRecipeIngredientsNoRecipe() {
        List<String> test = testKitchen.getIngredients("Chicken Parmesan");
        assertEquals(0,test.size());
    }

    @Test
    public void testRemoveRecipe() {
        List<String> test1 = new ArrayList<>();
        List<String> test2 = new ArrayList<>();
        Recipe a = new Recipe("Test1","1: Don't do anything",test1);
        Recipe b = new Recipe("Test2","2: Don't do anything",test2);
        testKitchen.addRecipe(a, false);
        testKitchen.addRecipe(b, false);
        testKitchen.removeRecipe("Test2");
        assertEquals(1, testKitchen.getListOfRecipe().size());
        assertEquals("", testKitchen.getSteps("Test2"));
    }

    @Test
    public void testGetRecipeNames() {
        List<String> test1 = new ArrayList<>();
        List<String> test2 = new ArrayList<>();
        Recipe a = new Recipe("Test1","1: Don't do anything",test1);
        Recipe b = new Recipe("Test2","2: Don't do anything",test2);
        testKitchen.addRecipe(a, false);
        testKitchen.addRecipe(b, false);
        List<String> recipeNames = testKitchen.getRecipeNames();
        assertEquals("Test1",recipeNames.get(0));
        assertEquals("Test2",recipeNames.get(1));
    }

    @Test
    public void testAddKitchenIngredientNoMissing() {
        assertTrue(testKitchen.addKitchenIngredient("Oregano"));
        assertEquals("Oregano", testKitchen.getStock().get(0));
        assertEquals(1,testKitchen.getStock().size());
        assertEquals(0, testKitchen.getMissing().size());
    }
    @Test
    public void testAddKitchenIngredientMissing() {
        assertTrue(testKitchen.addMissingIngredient("Oregano"));
        assertEquals(1, testKitchen.getMissing().size());
        assertEquals("Oregano", testKitchen.getMissing().get(0));
        assertTrue(testKitchen.addKitchenIngredient("Oregano"));
        assertEquals("Oregano", testKitchen.getStock().get(0));
        assertEquals(1, testKitchen.getStock().size());
        assertEquals(0, testKitchen.getMissing().size());
    }

    @Test
    public void testAddKitchenIngredientAlreadyContained() {
        testKitchen.addKitchenIngredient("Oregano");
        assertFalse(testKitchen.addKitchenIngredient("Oregano"));
    }

    @Test
    public void testMissingKitchenIngredient() {
        assertTrue(testKitchen.addMissingIngredient("Oregano"));
        assertTrue(testKitchen.addMissingIngredient("Pepper"));
        assertEquals(2, testKitchen.getMissing().size());
        testKitchen.addKitchenIngredient("Pepper");
        assertEquals("Pepper", testKitchen.getStock().get(0));
        assertEquals(1, testKitchen.getStock().size());
        assertEquals(1, testKitchen.getMissing().size());
        assertEquals("Oregano", testKitchen.getMissing().get(0));
    }

    @Test
    public void testAddMissingIngredientFalse() {
        testKitchen.addKitchenIngredient("Oregano");
        assertFalse(testKitchen.addMissingIngredient("Oregano"));
    }

    @Test
    public void testRemoveKitchenIngredient() {
        testKitchen.addKitchenIngredient("Oregano");
        testKitchen.removeKitchenIngredient("Oregano");
        assertEquals(0,testKitchen.getStock().size());
    }

    @Test
    public void testRemoveKitchenIngredientNotContained() {
        testKitchen.removeKitchenIngredient("Oregano");
        assertEquals(1,testKitchen.getMissing().size());
        assertEquals("Oregano", testKitchen.getMissing().get(0));

    }

    @Test
    public void testCheckStockTrue() {
        List<String> test = new ArrayList<>();
        test.add("Oregano");
        test.add("Basil");
        test.add("Garlic");
        Recipe a = new Recipe("Test", "1: Don't do anything", test);
        testKitchen.addRecipe(a, false);
        testKitchen.addKitchenIngredient("Oregano");
        testKitchen.addKitchenIngredient("Basil");
        testKitchen.addKitchenIngredient("Garlic");
        assertTrue(testKitchen.checkStock("Test"));
    }

    @Test
    public void testCheckStockFalse() {
        List<String> test = new ArrayList<>();
        test.add("Oregano");
        test.add("Basil");
        test.add("Garlic");
        Recipe a = new Recipe("Test", "1: Don't do anything", test);
        testKitchen.addRecipe(a, false);
        testKitchen.addKitchenIngredient("Oregano");
        testKitchen.addKitchenIngredient("Basil");
        assertFalse(testKitchen.checkStock("Test"));
    }



    @Test
    public void testGetOwner() {
        assertEquals("owner", testKitchen.getOwner());
    }

    @Test
    public void testSetOwner() {
        testKitchen.setOwner("Jeremy");
        assertEquals("Jeremy", testKitchen.getOwner());
    }


    @Test
    public void testAddMissingIngredientWithRemoval() {
        testKitchen.addKitchenIngredient("Oregano");
        testKitchen.addKitchenIngredient("Garlic");
        assertEquals(2, testKitchen.getStock().size());
        testKitchen.addMissingIngredient("Oregano");
        assertEquals("Oregano", testKitchen.getMissing().get(0));
        assertEquals(1,testKitchen.getStock().size());
    }
}