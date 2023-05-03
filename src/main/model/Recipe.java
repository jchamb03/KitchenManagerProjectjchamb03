package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.List;


//Recipe represents an individual recipe with name, steps and list of ingredients.
public class Recipe implements Writable {
    String nm;
    String steps;
    List<String> listOfIngredients;

    //EFFECTS: constructs a new recipe with specified fields.
    public Recipe(String nm, String steps, List<String> listOfIngredients) {
        this.nm = nm;
        this.steps = steps;
        this.listOfIngredients = listOfIngredients;
    }


    //toJSON() is modeled after WorkRoom example from edX
    @Override
    //EFFECTS: returns recipe as JSONObject.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray ingredients = new JSONArray();
        json.put("name", nm);
        json.put("steps", steps);
        for (String s : listOfIngredients) {
            ingredients.put(s);
        }
        json.put("ingredients", ingredients);
        return json;
    }
}
