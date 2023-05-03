package ui;

import model.Kitchen;
import model.Recipe;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//Runs the program to control and use a Kitchen.
public class KitchenApp {
    public static final String JSON_STORE = "./data/kitchen.json";
    private Kitchen kitchen;
    private Scanner input;
    private final JsonReader jsonReader;
    private final JsonWriter jsonWriter;

    //Runs the KitchenApp
    public KitchenApp() {
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        runKitchen();
    }

    //runKitchen(), printOptions(), and computeCommand are somewhat based on the TellerApp Class
    //from the TellerApp example program on EdX. No code was hard copied, but design is essentially the same
    //though not in exact execution.


    //Modifies: this
    //Effects: processes user actions
    public void runKitchen() {
        boolean cont = true;
        String command;

        init();
        System.out.println("\nWelcome to the Kitchen Manager!");
        printOptions();
        while (cont) {
            System.out.println("Next action:");
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                cont = false;
            } else {
                computeCommand(command);
            }
        }
        System.out.println("\nThank you!");
    }

    //Effects: prints out the options possible for the program.
    private void printOptions() {
        System.out.println("Select from the following options");
        System.out.println("name - Choose Owner Name");
        System.out.println("get-name - Get Owner Name");
        System.out.println("add - Add Recipe to Kitchen");
        System.out.println("remove - Remove Recipe from Kitchen");
        System.out.println("get-ingredients - Get Ingredients required for a recipe");
        System.out.println("get-steps - Get Steps for a recipe");
        System.out.println("get-recipes - Get names for all recipes stored");
        System.out.println("get-stock - Get Ingredients in Kitchen");
        System.out.println("add-stock - Add Ingredient to Kitchen");
        System.out.println("get-missing - Get ingredients missing from Kitchen");
        System.out.println("add-missing - Add missing ingredient to list");
        System.out.println("check-stock - Check stock required for recipe");
        System.out.println("load - Load Previously saved Kitchen");
        System.out.println("save - Save current Kitchen State");
        System.out.println("quit - quit the program");
    }

    //Effects: Takes input of command from the user, and calls the specified command if applicable.
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void computeCommand(String command) {
        switch (command) {
            case "name":
                changeName();
                break;
            case "get-name":
                getOwner();
                break;
            case "add":
                addRecipe();
                break;
            case "remove":
                removeRecipe();
                break;
            case "get-ingredients":
                getRecipeIngredients();
                break;
            case "get-steps":
                getRecipeSteps();
                break;
            case "get-recipes":
                getRecipes();
                break;
            case "get-stock":
                getStock();
                break;
            case "add-stock":
                addStock();
                break;
            case "get-missing":
                getMissing();
                break;
            case "add-missing":
                addMissing();
                break;
            case "check-stock":
                checkStock();
                break;
            case "load":
                loadKitchen();
                break;
            case "save":
                saveKitchen();
                break;
        }
    }

    //saveKitchen() modeled from saveWorkRoom() from WorkRoomApp in JSON example.
    //EFFECTS: saves Kitchen to file.
    private void saveKitchen() {
        try {
            jsonWriter.open();
            jsonWriter.write(kitchen);
            jsonWriter.close();
            System.out.println("Saved Kitchen of " + kitchen.getOwner() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to: " + JSON_STORE);
        }
    }


    //loadKitchen modeled from loadWorkRoom() from WorkRoomApp in JSON example.
    //MODIFIES: this
    //EFFECTS: loads Kitchen from file
    private void loadKitchen() {
        try {
            kitchen = jsonReader.read();
            System.out.println("Loaded Kitchen for " + kitchen.getOwner() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }

    }

    //EFFECTS: Prints the owner of the Kitchen.
    private void getOwner() {
        System.out.println(kitchen.getOwner());
    }

    //Modifies: this
    //Effects: changes the owner field in the initialized Kitchen.
    private void changeName() {
        String name;

        System.out.println("\nChoose Name:");
        name = input.next();
        kitchen.setOwner(name);
    }


    //Modifies: this
    //Effects: constructs a Recipe from user input, and adds said recipe to the Kitchen's ListOfRecipe.
    private void addRecipe() {
        String name;
        String steps;
        boolean doneIngredients = false;
        List<String> ingredients = new ArrayList<>();
        String ingredient;
        System.out.println("Enter Recipe Name:");
        name = input.next();
        System.out.println("Enter Steps for Recipe, press enter when done.");
        steps = input.next();
        while (!doneIngredients) {
            System.out.println("Enter Ingredient, or 'done' to stop");
            ingredient = input.next();
            if (ingredient.equals("done")) {
                doneIngredients = true;
                Recipe r = new Recipe(name,steps,ingredients);
                kitchen.addRecipe(r, false);
            } else {
                ingredients.add(ingredient);
            }
        }

    }

    //Modifies: this
    //Effects: removes specified Recipe from Kitchen's ListOfRecipe
    private void removeRecipe() {
        String recipeName;
        System.out.println("Select recipe to remove:");
        recipeName = input.next();
        kitchen.removeRecipe(recipeName);
    }

    //Effects: prints all ingredients required for a recipe to the console.
    private void getRecipeIngredients() {
        String name;
        System.out.println("Which Recipe?");
        name = input.next();
        List<String> ingredients = kitchen.getIngredients(name);
        for (String s: ingredients) {
            System.out.println(s);
        }
    }

    //Effects: Prints out the steps required for a specified recipe
    private void getRecipeSteps() {
        String name;
        System.out.println("Which Recipe?");
        name = input.next();
        String steps = kitchen.getSteps(name);
        System.out.println(steps);
    }

    //Effects: prints out all recipe names to the console.
    private void getRecipes() {
        List<String> names = kitchen.getRecipeNames();
        for (String s: names) {
            System.out.println(s);
        }
    }

    //Effects: prints out all ingredients contained in the Kitchen to the console.
    private void getStock() {
        List<String> ingredientList = kitchen.getStock();
        for (String s: ingredientList) {
            System.out.println(s);
        }
    }


    //Modifies: this
    //Effects: adds an arbitrary amount of ingredients to the Kitchen's ListOfIngredient based on user input.
    private void addStock() {
        boolean cont = true;
        String name;
        while (cont) {
            System.out.println("Next Item or 'done' to end:");
            name = input.next();
            if (name.equals("done")) {
                cont = false;
            } else {
                kitchen.addKitchenIngredient(name);
            }
        }
    }

    //Effects: prints out the list of missing ingredients to the console.
    private void getMissing() {
        List<String> ingredientList = kitchen.getMissing();
        for (String s: ingredientList) {
            System.out.println(s);
        }
    }

    //Modifies: this
    //Effects: adds an arbitrary amount of ingredients to the ListOfMissing for the Kitchen.
    private void addMissing() {
        boolean cont = true;
        String name;
        while (cont) {
            System.out.println("Next Item or 'done' to end:");
            name = input.next();
            if (name.equals("done")) {
                cont = false;
            } else {
                kitchen.addMissingIngredient(name);
            }
        }
    }

    //Effects: checks if Kitchen has stock of all ingredients required for a specified recipe.
    private void checkStock() {
        String name;
        System.out.println("Which recipe do you want to check?");
        name = input.next();
        boolean result = kitchen.checkStock(name);
        if (result) {
            System.out.println("You have the stock for that recipe.");
        } else {
            System.out.println("You do not have the stock for that recipe.");
        }
    }

    //Modifies: this
    //Effects: Initializes a Kitchen for the user.
    private void init() {
        kitchen = new Kitchen("User");
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }
}
