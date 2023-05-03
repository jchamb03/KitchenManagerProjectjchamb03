package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;


//Kitchen represents a kitchen, with an owner, list of recipes, list of ingredients in stock,
//and a list of ingredients that have been shown to be missing from the kitchen.
public class Kitchen implements Writable {
    private final List<Recipe> listOfRecipe;
    private final List<String> listOfIngredient;
    private final List<String> listOfMissing;
    private String owner;
    private final EventLog eventLog;

    public Kitchen(String owner) {
        this.owner = owner;
        this.listOfRecipe = new ArrayList<>();
        this.listOfIngredient = new ArrayList<>();
        this.listOfMissing = new ArrayList<>();
        eventLog = EventLog.getInstance();
    }

    //MODIFIES: this
    //EFFECTS: adds recipe to kitchen's listOfRecipe.
    public void addRecipe(Recipe r, boolean loading) {
        if (!loading) {
            this.listOfRecipe.add(r);
            eventLog.logEvent(new Event("Added Recipe to Kitchen"));
        } else {
            this.listOfRecipe.add(r);
        }
    }

    //MODIFIES: this
    //EFFECTS: removes recipe from kitchen's listOfRecipe
    public void removeRecipe(String nm)  {
        int index = 0;
        for (Recipe r: this.listOfRecipe) {
            if (r.nm.equals(nm)) {
                index = this.listOfRecipe.indexOf(r);
            }
        }
        this.listOfRecipe.remove(index);
    }


    //EFFECTS; returns the steps required for specified recipe, if not found return empty string
    public String getSteps(String nm) {
        for (Recipe r: this.listOfRecipe) {
            if (r.nm.equals(nm)) {
                eventLog.logEvent(new Event("Retrieved Steps for Recipe"));
                return r.steps;
            }
        }
        return "";
    }

    //EFFECTS: returns list of ingredients for specified recipe.
    public List<String> getIngredients(String nm) {
        for (Recipe r: this.listOfRecipe) {
            if (r.nm.equals(nm)) {
                eventLog.logEvent(new Event("Retrieved Ingredients for Recipe"));
                return r.listOfIngredients;
            }
        }
        return new ArrayList<>();
    }

    //EFFECTS: returns list of all recipe names logged
    public List<String> getRecipeNames() {
        List<String> i = new ArrayList<>();
        for (Recipe r: this.listOfRecipe) {
            i.add(r.nm);
        }
        return i;
    }

    //MODIFIES: this
    //EFFECTS: adds ingredient to kitchen stock, removes from missing ingredients if applicable and returns true
    //         returns false if kitchen stock already contains ingredient.
    public boolean addKitchenIngredient(String s) {
        if (!this.listOfIngredient.contains(s)) {
            this.listOfIngredient.add(s);
            if (this.listOfMissing.contains(s)) {
                this.listOfMissing.remove(this.listOfMissing.indexOf(s));
            }
            return true;
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: adds ingredient to list of missing ingredients and returns true,
    // returns false if already contained in kitchen.
    public boolean addMissingIngredient(String s) {
        if (!this.listOfIngredient.contains(s)) {
            this.listOfMissing.add(s);
            return true;
        } else {
            this.listOfIngredient.remove(s);
            this.listOfMissing.add(s);
            return false;
        }

    }

    //MODIFIES: this
    //EFFECTS: removes ingredient from kitchen, adds to missing ingredients,
    public void removeKitchenIngredient(String s) {
        if (this.listOfIngredient.contains(s)) {
            this.listOfIngredient.remove(this.listOfIngredient.indexOf(s));
            this.listOfMissing.add(s);
        } else {
            this.listOfMissing.add(s);
        }

    }

    //REQUIRES: recipe must be in listOfRecipe
    //EFFECTS: returns true if all ingredients required for specified recipe are available.
    public boolean checkStock(String nm) {
        List<String> recipeIngredients = new ArrayList<>();
        for (Recipe r: this.listOfRecipe) {
            if (r.nm.equals(nm)) {
                recipeIngredients = r.listOfIngredients;
            }
        }
        for (String s: recipeIngredients) {
            if (!this.listOfIngredient.contains(s)) {
                return false;
            }
        }
        return true;
    }


    public List<String> getStock() {
        return this.listOfIngredient;
    }

    public List<Recipe> getListOfRecipe() {
        return this.listOfRecipe;
    }

    public List<String> getMissing() {
        return this.listOfMissing;
    }

    public String getOwner() {
        return this.owner;
    }

    //Modifies: this
    //Effects: Sets Kitchen's owner field to string specified.
    public void setOwner(String s) {
        this.owner = s;
    }

    public void printLog() {
        for (Event e : this.eventLog) {
            System.out.println(e.getDate() + ": " + e.getDescription());
        }
    }


    //toJson(), ingredientsToJSon(), missingToJson() and recipesToJson()
    // are based on WorkRoom example from edX.
    @Override
    //EFFECTS: returns JSONObject of kitchen.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("owner", owner);
        json.put("ingredients", ingredientsToJson());
        json.put("missing", missingToJson());
        json.put("recipes", recipesToJson());
        return json;
    }

    //EFFECTS: returns JSONArray representation of Kitchen.listOfIngredient
    private JSONArray ingredientsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String s: listOfIngredient) {
            jsonArray.put(s);
        }

        return jsonArray;
    }

    //EFFECTS: returns JSONArray representation of Kitchen.listOfMissing
    private JSONArray missingToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String s: listOfMissing) {
            jsonArray.put(s);
        }

        return jsonArray;
    }

    //EFFECTS: returns JSONArray representation of Recipes in kitchen.listOfRecipe.
    private JSONArray recipesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Recipe r : listOfRecipe) {
            jsonArray.put(r.toJson());
        }

        return jsonArray;
    }

}
