package UI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.KitchenControllerUI;

import java.util.ArrayList;
import java.util.List;


public class KitchenControllerUITest {
    private KitchenControllerUI controller;

    @BeforeEach
    public void runBefore() {
        controller = new KitchenControllerUI();
    }


    @Test
    public void testParamToString() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("Test 1");
        ingredients.add("Test 2");
        ingredients.add("Test 3");
        ingredients.add("Test 4");

        String test = controller.kitchenParameterToText(ingredients);

        Assertions.assertEquals(test, "Test 1\nTest 2\nTest 3\nTest 4\n");
    }
}
