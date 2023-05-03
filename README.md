# Recipe/Ingredient Manager
### Easing the cooking process
<p> For my term project, I will make a tool to help <strong>manage recipes and ingredients required for recipes</strong>, 
with functionality to <strong>add a new recipe</strong>,
<strong>retrieve ingredients required for a specified recipe</strong>, 
and <strong>log
ingredients that may need to be purchased</strong>. Anyone who cooks or would like to cook could use the tool,
as well as anyone who would simply like to keep track of things in their kitchen. I am interested in making
this tool because I am going to be moving out of my parents' house to start my second year of university, 
and myself and my roommate will be in charge of feeding ourselves, so having a set of recipes will be very useful
in making that process a bit easier.
</p>



### User Stories:
- As a user, I want to be able to add a new recipe to a list of recipes
- As a user, I want to be able to view my list of recipes (names of recipes)
- As a user, I want to be able to retrieve the list of ingredients required for a recipe and the steps
- As a user, I want to be able to log the ingredients in my kitchen/remove
- As a user, I want to be able to store the state of my kitchen in its entirety (all stored recipes, stored ingredients, and missing ingredients).
- As a user, I want to be able to retrieve my stored kitchen at will, to continue using the information stored.

# Instructions for Grader

### Steps for interaction:

- Upon running the KitchenControllerUI Program, There will be a control panel, and 3 empty boxes
- These boxes, initially empty, represent Ingredients contained in/ missing from the kitchen, and recipe names.
- 
- (VVV First Required Event VVV)
- To add a recipe to Kitchen, which you can do multiple times, press the button "Add Recipe", and enter Information.
- 
- (VVV Second/Third Required Event VVV)
- To view either the steps or ingredients associated with a recipe, press one of the two following buttons:
- "View Recipe Steps"
- "View Recipe Ingredients"
- 
- Visual Component is an image shown on startup.
- To save the state of your kitchen to file, press the "Save Kitchen" button.
- Similarly, press the "Load Kitchen" button to load your kitchen from file.


### Phase 4: Task 2
- Tue Aug 09 13:57:44 PDT 2022: Added Recipe to Kitchen
- Tue Aug 09 13:57:53 PDT 2022: Retrieved Steps for Recipe
- Tue Aug 09 14:00:32 PDT 2022: Retrieved Ingredients for Recipe

### Phase 4: Task 3
- Create new class called "IngredientManager" to manipulate Kitchen's stock/missing stock,
instead of all methods and fields being contained in Kitchen.
- InformationPrinter and StepsPrinter could potentially be condensed into one class.
