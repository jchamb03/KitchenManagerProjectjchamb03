package persistence;


// JsonReader is modeled from the provided example from CPSC210 edX page.

import model.Kitchen;
import model.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//Represents a reader to retrieve Kitchen from Json file.
public class JsonReader {
    private String source;

    //EFFECTS: constructs reader to read from source.
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads Kitchen from file and returns it;
    // throws IOException if an error occurs while reading data.
    public Kitchen read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseKitchen(jsonObject);
    }

    //EFFECTS: reads source file as string, returns it.
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //EFFECTS: parses workroom from JSON object and returns it.
    private Kitchen parseKitchen(JSONObject jsonObject) {
        String name = jsonObject.getString("owner");
        Kitchen kitchen = new Kitchen(name);
        addRecipes(kitchen, jsonObject);
        addIngredients(kitchen, jsonObject);
        addMissing(kitchen, jsonObject);
        return kitchen;
    }

    //MODIFIES: kitchen
    //EFFECTS: parses list of recipes from Json file, adds to kitchen.
    private void addRecipes(Kitchen kitchen, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("recipes");
        for (Object json : jsonArray) {
            JSONObject nextRecipe = (JSONObject) json;
            addRecipe(kitchen, nextRecipe);
        }
    }

    //MODIFIES: kitchen
    //EFFECTS: parses Ingredients from JSON object and adds them to workroom.
    private void addIngredients(Kitchen kitchen, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("ingredients");
        for (Object json : jsonArray) {
            String ingredient = json.toString();
            kitchen.addKitchenIngredient(ingredient);
        }
    }

    //MODIFIES: kitchen
    //EFFECTS: parses missingIngredients from JSON file, adds to kitchen.
    private void addMissing(Kitchen kitchen, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("missing");
        for (Object json : jsonArray) {
            String ingredient = json.toString();
            kitchen.addMissingIngredient(ingredient);
        }
    }


    //MODIFIES: kitchen
    //EFFECTS: parses individual recipe from json file, adds to kitchen.
    private void addRecipe(Kitchen kitchen, JSONObject nextRecipe) {
        String nm = nextRecipe.getString("name");
        String steps = nextRecipe.getString("steps");
        List<String> ingredients = new ArrayList<>();
        JSONArray jsonArray = nextRecipe.getJSONArray("ingredients");
        for (Object json : jsonArray) {
            String ingredient = json.toString();
            ingredients.add(ingredient);
        }
        Recipe r = new Recipe(nm, steps, ingredients);
        kitchen.addRecipe(r, true);
    }

}
