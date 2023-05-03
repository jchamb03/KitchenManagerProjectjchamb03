package ui;

import model.Kitchen;
import model.Recipe;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//KitchenControllerUI is based on AlarmControllerUI from AlarmSystem example on EdX
//All private classes for button actions are also based on AlarmControllerUI from EdX

//KitchenControllerUI creates a gui to control a kitchen object and its associated fields.
public class KitchenControllerUI extends JFrame {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 800;
    private Kitchen kitchen;
    private final JDesktopPane desktop;
    private final JInternalFrame controlPanel;
    private final InformationPrinter ingredients;
    private final InformationPrinter missingIngredients;
    private final InformationPrinter recipeNames;

    private final JsonReader jsonReader;
    private final JsonWriter jsonWriter;
    public static final String JSON_STORE = "./data/kitchen.json";
    public static final String IMAGE_STORE = "./data/kitchen.jpg";

    //EFFECTS: constructs a UI to control a Kitchen object
    public KitchenControllerUI() {
        kitchen = new Kitchen("User");
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);
        desktop = new JDesktopPane();
        ingredients = new InformationPrinter(desktop, "Ingredients", false);
        missingIngredients = new InformationPrinter(desktop, "Missing Ingredients", false);
        recipeNames = new InformationPrinter(desktop, "Recipe Names", false);
        controlPanel = new JInternalFrame("Control Panel",
                false,
                false,
                false, false);
        controlPanel.setLayout(new BorderLayout());
        showImage();
        addButtonPanel();
        initializeUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                kitchen.printLog();
            }
        });


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }


    //MODIFIES: this
    //EFFECTS: displays an image before GUI startup.
    private void showImage() {

        BufferedImage kitchenImage = null;
        try {
            kitchenImage = ImageIO.read(new File(IMAGE_STORE));
        } catch (IOException e) {
            System.out.println("Failed to retrieve Image");
        }
        assert kitchenImage != null;
        ImageIcon kitchenIcon = new ImageIcon(kitchenImage);
        JFrame imageFrame = new JFrame();
        imageFrame.setSize(612,408);

        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(kitchenIcon);
        imageFrame.add(imageLabel);

        imageFrame.setVisible(true);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //do nothing
        }
        imageFrame.setVisible(false);
        imageFrame.dispose();
    }

    //MODIFIES: this
    //EFFECTS: creates a button panel with abstract actions to control the instantiated kitchen object.
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 5));
        buttonPanel.add(new JButton(new AddRecipeAction()));
        buttonPanel.add(new JButton(new AddIngredientAction()));
        buttonPanel.add(new JButton(new AddMissingAction()));
        buttonPanel.add(new JButton(new CheckStockAction()));
        buttonPanel.add(new JButton(new SaveKitchenAction()));
        buttonPanel.add(new JButton(new LoadKitchenAction()));
        buttonPanel.add(new JButton(new ChangeOwnerAction()));
        buttonPanel.add(new JButton(new RemoveRecipeAction()));
        buttonPanel.add(new JButton(new ViewStepsAction()));
        buttonPanel.add(new JButton(new ViewIngredientsAction()));
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    //MODIFIES: this
    //EFFECTS: initializes UI with title, control panel, and information displays.
    private void initializeUI() {
        MouseListener listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        };
        desktop.addMouseListener(listener);

        setContentPane(desktop);
        setTitle("Kitchen/Recipe Manager");
        setSize(WIDTH, HEIGHT);

        missingIngredients.setVisible(true);
        missingIngredients.setLocation(175,80);
        desktop.add(missingIngredients);

        ingredients.setVisible(true);
        ingredients.setLocation(0,80);
        desktop.add(ingredients);

        recipeNames.setVisible(true);
        recipeNames.setLocation(350,80);
        desktop.add(recipeNames);

        controlPanel.pack();
        controlPanel.setVisible(true);
        controlPanel.setLocation(0,0);
        desktop.add(controlPanel);
    }

    //EFFECTS: returns a string representing a List<String> for a parameter of kitchen
    //(ListOfMissing, ListOfIngredient, ListOfRecipe).
    public String kitchenParameterToText(List<String> s) {
        StringBuilder result = new StringBuilder();
        for (String string : s) {
            result.append(string).append("\n");
        }
        return result.toString();
    }



    //An abstract action class to add ingredients to kitchen.ListOfIngredient
    private class AddIngredientAction extends AbstractAction {

        //EFFECTS: constructs action with specified title.
        AddIngredientAction() {
            super("Add Ingredient");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: takes user input and adds arbitrary amount of ingredients to kitchen.ListOfIngredient
        public void actionPerformed(ActionEvent e) {
            boolean cont = true;
            while (cont) {
                String nextIngredient = JOptionPane.showInputDialog(null,
                        "Next Ingredient, or 'done' to stop",
                        "Enter Ingredients",
                        JOptionPane.QUESTION_MESSAGE);
                if (nextIngredient.equals("done")) {
                    cont = false;
                } else {
                    kitchen.addKitchenIngredient(nextIngredient);
                }
            }
            ingredients.printText(kitchenParameterToText(kitchen.getStock()));
            ingredients.revalidate();
            ingredients.repaint();

            missingIngredients.printText(kitchenParameterToText(kitchen.getMissing()));
            missingIngredients.revalidate();
            missingIngredients.repaint();
        }
    }

    //Class to represent action of adding ingredient to ListOfMissing
    private class AddMissingAction extends AbstractAction {

        //EFFECTS: constructs action with specified title.
        AddMissingAction() {
            super("Add Missing Ingredient");
        }


        @Override
        //MODIFIES: this
        //EFFECTS: takes user input, adds missing ingredient to kitchen, and repaints GUI.
        public void actionPerformed(ActionEvent e) {
            boolean cont = true;
            while (cont) {
                String nextIngredient = JOptionPane.showInputDialog(null,
                        "Next Ingredient, or 'done' to stop",
                        "Enter Missing Ingredients",
                        JOptionPane.QUESTION_MESSAGE);
                if (nextIngredient.equals("done")) {
                    cont = false;
                } else {
                    if (!kitchen.getMissing().contains(nextIngredient)) {
                        kitchen.addMissingIngredient(nextIngredient);
                    }
                }
            }
            missingIngredients.printText(kitchenParameterToText(kitchen.getMissing()));
            missingIngredients.revalidate();
            missingIngredients.repaint();

            ingredients.printText(kitchenParameterToText(kitchen.getStock()));
            ingredients.revalidate();
            ingredients.repaint();
        }
    }

    //Class to represent action of adding a recipe to kitchen.
    private class AddRecipeAction extends AbstractAction {

        //EFFECTS: constructs an action with the specified title.
        AddRecipeAction() {
            super("Add Recipe");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: takes user input, adds recipe to kitchen, and repaints GUI.
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null, "Name?", "Enter Recipe Name",
                    JOptionPane.QUESTION_MESSAGE);
            String steps = JOptionPane.showInputDialog(null, "Steps:", "Enter Steps:",
                    JOptionPane.QUESTION_MESSAGE);
            List<String> ingredients = new ArrayList<>();
            boolean cont = true;
            while (cont) {
                String nextIngredient = JOptionPane.showInputDialog(null,
                        "Next Ingredient, or 'done' to stop", "Enter Ingredients",
                        JOptionPane.QUESTION_MESSAGE);
                if (nextIngredient.equals("done")) {
                    cont = false;
                } else {
                    if (!ingredients.contains(nextIngredient)) {
                        ingredients.add(nextIngredient);
                    }
                }
            }
            Recipe r = new Recipe(name, steps, ingredients);
            kitchen.addRecipe(r, false);
            recipeNames.printText(kitchenParameterToText(kitchen.getRecipeNames()));
            recipeNames.revalidate();
            recipeNames.repaint();
        }
    }

    //Class to represent action of checking stock for a specified recipe, and returning the result.
    private class CheckStockAction extends AbstractAction {

        //EFFECTS: constructs an action with the specified title.
        CheckStockAction() {
            super("Check Stock");
        }

        @Override
        //EFFECTS: takes user input for name of recipe, returns true if kitchen has stock for that recipe,
        //         false otherwise.
        public void actionPerformed(ActionEvent e) {
            String recipe = JOptionPane.showInputDialog(null,
                    "Recipe Name?",
                    "Select Recipe",
                    JOptionPane.QUESTION_MESSAGE);
            boolean result = kitchen.checkStock(recipe);
            if (result) {
                JOptionPane.showMessageDialog(null,
                        "You have the stock for that recipe!",
                        "Check Result",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "You do not have the stock for that recipe!",
                        "Check Result",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    //Class to represent the action of saving kitchen's state.
    private class SaveKitchenAction extends AbstractAction {

        //EFFECTS: constructs an action with the specified title.
        SaveKitchenAction() {
            super("Save Kitchen");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: saves current state of kitchen to file.
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(kitchen);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null,
                        "Successfully saved Kitchen!",
                        "Save result",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(null,
                        "Could not save Kitchen!",
                        "Save result",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //Class to represent action of loading kitchen state from file.
    private class LoadKitchenAction extends AbstractAction {

        //EFFECTS: constructs action with specified title.
        LoadKitchenAction() {
            super("Load Kitchen");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: reads kitchen state from file, changes state of kitchen, and repaints GUI with proper information.
        public void actionPerformed(ActionEvent e) {
            try {
                kitchen = jsonReader.read();
                JOptionPane.showMessageDialog(null,
                        "Successfully loaded Kitchen!",
                        "Load result",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(null,
                        "Could not load Kitchen!",
                        "Load result",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            missingIngredients.printText(kitchenParameterToText(kitchen.getMissing()));
            missingIngredients.revalidate();
            missingIngredients.repaint();

            ingredients.printText(kitchenParameterToText(kitchen.getStock()));
            ingredients.revalidate();
            ingredients.repaint();

            recipeNames.printText(kitchenParameterToText(kitchen.getRecipeNames()));
            recipeNames.revalidate();
            recipeNames.repaint();
            repaint();
        }
    }

    //Class to represent action of changing owner name.
    private class ChangeOwnerAction extends AbstractAction {

        //EFFECTS: constructs action with specified title.
        ChangeOwnerAction() {
            super("Change Owner");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: takes user input, changes owner name of kitchen.
        public void actionPerformed(ActionEvent e) {
            String owner = JOptionPane.showInputDialog(null,
                    "Name?",
                    "Change Owner's Name",
                    JOptionPane.QUESTION_MESSAGE);
            kitchen.setOwner(owner);
        }
    }

    //Class to represent action to remove a recipe from the kitchen.
    private class RemoveRecipeAction extends AbstractAction {

        //EFFECTS: constructs an action with the specified title.
        RemoveRecipeAction() {
            super("Remove Recipe");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: takes user input, and removes the specified recipe from the kitchen.
        public void actionPerformed(ActionEvent e) {
            String recipe = JOptionPane.showInputDialog(null,
                    "Recipe Name?",
                    "Select Recipe",
                    JOptionPane.QUESTION_MESSAGE);
            kitchen.removeRecipe(recipe);
            recipeNames.printText(kitchenParameterToText(kitchen.getRecipeNames()));
            recipeNames.revalidate();
            recipeNames.repaint();
        }
    }

    //Class to represent action of viewing steps for specified recipe.
    private class ViewStepsAction extends AbstractAction {

        //EFFECTS: constructs a new action with specified title
        ViewStepsAction() {
            super("View Recipe Steps");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: takes user input, and produces a new JTextArea with steps of recipe specified by user.
        public void actionPerformed(ActionEvent e) {
            String recipe = JOptionPane.showInputDialog(null,
                    "Recipe Name?",
                    "Select Recipe",
                    JOptionPane.QUESTION_MESSAGE);
            String steps = kitchen.getSteps(recipe);
            if (!Objects.equals(steps, "")) {
                StepsPrinter recipeSteps = new StepsPrinter(desktop, "Steps for " + recipe, true);
                recipeSteps.printText(steps);
                recipeSteps.setVisible(true);
                recipeSteps.setLocation(525, 80);
                desktop.add(recipeSteps);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Recipe does not exist!",
                        "Retrieval Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    //Class to represent the action to view ingredients associated with a recipe.
    private class ViewIngredientsAction extends AbstractAction {

        //EFFECTS: constructs a new action with the title specified.
        ViewIngredientsAction() {
            super("View Recipe Ingredients");
        }

        @Override
        //MODIFIES: this
        //EFFECTS: takes user input, and produces a JTextArea with ingredients of recipe specified by user.
        public void actionPerformed(ActionEvent e) {
            String recipe = JOptionPane.showInputDialog(null,
                    "Recipe Name?",
                    "Select Recipe",
                    JOptionPane.QUESTION_MESSAGE);
            List<String> ingredients = kitchen.getIngredients(recipe);
            if (ingredients.size() != 0) {
                InformationPrinter recipeIngredients = new InformationPrinter(desktop, "Recipe Ingredients", true);
                recipeIngredients.printText(
                        "Ingredients for " + recipe + ": \n" + kitchenParameterToText(ingredients));
                recipeIngredients.setVisible(true);
                recipeIngredients.setLocation(700, 80);
                desktop.add(recipeIngredients);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Recipe does not exist!",
                        "Retrieval Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
