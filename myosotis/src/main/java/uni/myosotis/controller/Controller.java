package uni.myosotis.controller;

import uni.myosotis.gui.Language;
import uni.myosotis.gui.MainMenu;
import uni.myosotis.logic.*;
import uni.myosotis.objects.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The Controller of the application.
 */
public class Controller {

    /**
     * The IndexcardLogic of the application.
     */
    private final IndexcardLogic indexcardLogic;

    /**
     * The KeywordLogic of the application.
     */
    private final KeywordLogic keywordLogic;

    /**
     * The LinkLogic of the application.
     */
    private final LinkLogic linkLogic;

    /**
     * The CategoryLogic of the application.
     */
    private final CategoryLogic categoryLogic;

    /**
     * The IndexcardBoxLogic of the application.
     */
    private final IndexcardBoxLogic indexcardBoxLogic;

    /**
     * The LearnsystemLogic of the application.
     */
    private final LeitnerLearnSystemLogic leitnerLearnSystemLogic;

    /**
     * The main-menu of the application.
     */
    private MainMenu mainMenu;

    /**
     * The language of the application.
     */
    private Language language;

    /**
     * Creates a new Controller with the given logic.
     *
     * @param indexcardLogic The logic for the Indexcards.
     * @param keywordLogic The logic for the Keywords.
     * @param linkLogic The logic for the Links.
     * @param categoryLogic The logic for the Categories.
     * @param indexcardBoxLogic The logic for the IndexcardBoxes.
     * @param leitnerLearnSystemLogic The logic for the LearnSystems.
     */
    public Controller(final IndexcardLogic indexcardLogic, final KeywordLogic keywordLogic, final LinkLogic linkLogic, final CategoryLogic categoryLogic, final IndexcardBoxLogic indexcardBoxLogic, final LeitnerLearnSystemLogic leitnerLearnSystemLogic) {

        this.indexcardLogic = indexcardLogic;
        this.keywordLogic = keywordLogic;
        this.linkLogic = linkLogic;
        this.categoryLogic = categoryLogic;
        this.indexcardBoxLogic = indexcardBoxLogic;
        this.leitnerLearnSystemLogic = leitnerLearnSystemLogic;

    }

    /**
     * Starts the application and displays the MainMenu.
     */
    public void startApplication() {
        language = new Language("English");
        mainMenu = new MainMenu(this, language);
        mainMenu.setVisible(true);
    }

    /**
     * Sets the language of the application.
     *
     * @param lang The language.
     */
    public void setLanguage(String lang) {
        this.language = new Language(lang);
        mainMenu.setLanguage(language);
    }

    /* INDEXCARDS */

    /**
     * Displays an Indexcard.
     *
     * @param indexcard The Indexcard that should be displayed.
     */
    public void displayIndexcard(Indexcard indexcard) {
        mainMenu.displayIndexcard(indexcard);
    }

    /**
     * Displays the dialog to create a new Indexcard.
     */
    public void createIndexcard() {
        mainMenu.displayCreateIndexcard();
    }

    /**
     * Delegates the exercise to create a new Indexcard to the IndexcardLogic.
     * Displays an error, if already an Indexcard with the same name exists.
     *
     * @param name The name of the Indexcard.
     * @param question The question of the Indexcard.
     * @param answer The answer of the Indexcard.
     * @param keywords A List of keywords linked to the Indexcard.
     * @param links A list of Links added to the Indexcard.
     */
    public void createIndexcard(String name, String question, String answer, List<String> keywords, List<String> links) {
        try {
            // Create Keywords
            final List<Keyword> keywordObjects = new ArrayList<>();
            for (String keyword : keywords) {
                if (keywordLogic.getKeywordByName(keyword).isEmpty()) {
                    keywordObjects.add(keywordLogic.createKeyword(keyword));
                } else {
                    keywordObjects.add(keywordLogic.getKeywordByName(keyword).get());
                }
            }

            // Create Links
            final List<Link> linkObjects = new ArrayList<>();
            for (String link : links) {
                String[] splittedLink = link.split(" => ", 2);
                String term = splittedLink[0];
                String indexcardName = splittedLink[1];
                if (getIndexcardByName(indexcardName).isPresent()) {
                    Indexcard indexcard = getIndexcardByName(indexcardName).get();
                    linkObjects.add(linkLogic.createLink(term, indexcard));
                }
            }

            indexcardLogic.createIndexcard(name, question, answer, keywordObjects, linkObjects);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format(language.getName("indexcardCreatedMessage"),name), language.getName("indexcardCreated"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu,
                    String.format(language.getName("indexcardAlreadyExistError"), name), language.getName("nameAlreadyAssignedError"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the dialog to edit an Indexcard.
     */
    public void editIndexcard() {
        mainMenu.displayEditIndexcard();
    }

    /**
     * Displays the dialog to edit a selected Indexcard.
     *
     * @param indexcard The selected Indexcard.
     */
    public void editIndexcard(Indexcard indexcard) {
        mainMenu.displayEditIndexcard(indexcard);
    }

    /**
     * Delegates the exercise to edit an Indexcard to the IndexcardLogic.
     * Displays an error, if there is no Indexcard with the given name.
     *
     * @param name The name of the Indexcard.
     * @param question The question of the Indexcard.
     * @param answer The answer of the Indexcard.
     * @param keywords Keywords for the Indexcard.
     * @param links The Links of the Indexcard.
     * @param id The id of the Indexcard.
     */
    public void editIndexcard(String name, String question, String answer, List<String> keywords, List<String> links, Long id) {
        try {
            // Old Keywords from this Indexcard
            final List<Keyword> oldKeywords = indexcardLogic.getIndexcardById(id).getKeywords();

            // Create new added Keywords
            final List<Keyword> keywordObjects = new ArrayList<>();
            for (String keyword : keywords) {
                if (keywordLogic.getKeywordByName(keyword).isEmpty()) {
                    keywordObjects.add(keywordLogic.createKeyword(keyword));
                } else {
                    keywordObjects.add(keywordLogic.getKeywordByName(keyword).get());
                }
            }

            // Old Links from this Indexcard
            final List<Link> oldLinks = indexcardLogic.getIndexcardById(id).getLinks();

            // Create new added Links
            final List<Link> newLinks = new ArrayList<>();
            for (String link : links) {
                String[] splittedLink = link.split(" => ", 2);
                String term = splittedLink[0];
                String indexcardName = splittedLink[1];
                // Create new Link, if it not exists yet
                if (!oldLinks.stream().map(Link::getTerm).toList().contains(term)) {
                    if (getIndexcardByName(indexcardName).isPresent()) {
                        Indexcard indexcard = getIndexcardByName(indexcardName).get();
                        newLinks.add(linkLogic.createLink(term, indexcard));
                    }
                } else {
                    // Keep old Link, if it exists yet
                    newLinks.add(oldLinks.stream().filter(l -> l.getTerm().equals(term)).toList().get(0));
                }
            }

            // Update the Indexcard
            indexcardLogic.updateIndexcard(name, question, answer, keywordObjects, newLinks, id);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format(language.getName("indexcardEditedMessage"), name), language.getName("indexcardCreated"),
                    JOptionPane.INFORMATION_MESSAGE);

            // Remove Keywords that are not used anymore
            for (Keyword keyword : oldKeywords) {
                if (indexcardLogic.getIndexcardsByKeyword(keyword.getName()).isEmpty()) {
                    keywordLogic.deleteKeyword(keyword.getName());
                }
            }

            // Remove removed Links
            for (Link link : oldLinks) {
                if (!newLinks.contains(link)) {
                    linkLogic.deleteLink(link);
                }
            }
        } catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu,
                    language.getName("noIndexcardWithNameError"), language.getName("noIndexcardError"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the dialog to delete an Indexcard.
     */
    public void deleteIndexcard() {
        mainMenu.displayDeleteIndexcard();
    }

    /**
     * Delegates the exercise to delete an Indexcard to the IndexcardLogic.
     * Delegates the exercise to delete the Indexcard from the Keyword to the KeywordLogic.
     * Displays an error, if there is no Indexcard with the given name.
     *
     * @param id The id of the Indexcard.
     */
    public void deleteIndexcard(Long id) {
        try {
            // Save old values of the Indexcard
            Indexcard indexcard = indexcardLogic.getIndexcardById(id);
            id = indexcard.getId();
            String deletedName = indexcard.getName();
            List<Keyword> keywords = indexcard.getKeywords();

            // Remove Links to this Indexcard from Indexcards.
            for (Indexcard card : getAllIndexcards()) {
                List<Link> removedLinks = card.getLinks().stream().filter(l -> !l.getIndexcard().getName().equals(indexcard.getName())).toList();
                indexcardLogic.updateIndexcard(card.getName(), card.getQuestion(), card.getAnswer(), card.getKeywords(), removedLinks, card.getId());
            }

            // Delete Links that are linked with this Indexcard
            for (Link link : linkLogic.getLinksByIndexcard(indexcard)) {
                linkLogic.deleteLink(link);
            }

            // Delete the Indexcard
            indexcardLogic.deleteIndexcard(id);

            // Indexcard that should be deleted needs to be removed from the list
            // of Indexcards this keyword is attached to.
            for (Keyword keyword : keywords) {
                if (indexcardLogic.getIndexcardsByKeyword(keyword.getName()).isEmpty()) {
                    keywordLogic.deleteKeyword(keyword.getName());
                }
            }

            // Delete Links from this Indexcard
            for (Link link : indexcard.getLinks()) {
                linkLogic.deleteLink(link);
            }

            JOptionPane.showMessageDialog(mainMenu,
                    String.format(language.getName("indexcardDeletedMessage"), deletedName), language.getName("indexcardDeleted"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu,
                    language.getName("indexcardDeletedError"), language.getName("indexcardDeletedError"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delegates the exercise to find all Indexcards to the IndexcardLogic.
     *
     * @return A list of all Indexcards.
     */
    public List<Indexcard> getAllIndexcards() {
        return indexcardLogic.getAllIndexcards();
    }

    /**
     * Delegates the exercise to find an Indexcard with the given name to the IndexcardLogic.
     *
     * @param indexcard The name of the Indexcard.
     * @return The Indexcard if it exists.
     */
    public Optional<Indexcard> getIndexcardByName(String indexcard) {
        return indexcardLogic.getIndexcardByName(indexcard);
    }

    /**
     * Delegates the exercise to get a list of Indexcards by a list of names.
     *
     * @param indexcardNames The list of indexcard names.
     * @return A list of the Indexcards with these names.
     */
    public List<Indexcard> getIndexcardsByIndexcardNameList(List<String> indexcardNames) {
        return indexcardLogic.getIndexcardsByIndexcardNameList(indexcardNames);
    }

    /**
     * Returns a list of the names of all Indexcards.
     *
     * @return A list of the names of all Indexcards.
     */
    public List<String> getAllIndexcardNames() {
        return getAllIndexcards().stream().map(Indexcard::getName).toList();
    }

    /**
     * Returns a list of the Names of the given Indexcards.
     *
     * @param indexcards The Indexcards, from them the names should be returned.
     * @return A list of the names of the given Indexcards.
     */
    public List<String> getNamesByIndexcards(List<Indexcard> indexcards) {
        return indexcards.stream().map(Indexcard::getName).toList();
    }

    /**
     * Delegates the exercise to find all Indexcards in this category to the CategoryLogic.
     *
     * @param category The Category.
     * @return A list of all Indexcards in this category.
     */
    public List<Indexcard> getIndexcardsByCategory(Category category) {
        return categoryLogic.getIndexcardsByCategory(category);
    }

    /**
     * Delegates the exercise to search for a text in all Indexcards.
     *
     * @param text The text.
     * @return A list of Indexcards that contain the text.
     */
    public List<Indexcard> searchIndexcard(String text) {
        return indexcardLogic.searchIndexcard(text);
    }

    /* INDEXCARDBOXES */

    /**
     * Displays the dialog to create a new IndexcardBox.
     */
    public void createIndexcardBox() {
        mainMenu.displayCreateIndexcardBox();
    }

    /**
     * Delegates the exercise to create a new IndexcardBox to the IndexcardBoxLogic.
     * Displays an error, if already an IndexcardBox with the same name exists.
     *
     * @param name The name of the IndexcardBox.
     * @param categoryList The Categories that should be added to this IndexcardBox.
     */
    public void createIndexcardBox(String name, List<Category> categoryList) {
        try {
            //logic
            indexcardBoxLogic.createIndexcardBox(name,categoryList);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format((language.getName("indexcardBoxCreatedMessage")),name), language.getName("indexcardBoxCreated"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu,
                    language.getName("indexcardAlreadyExistError"), language.getName("nameAlreadyAssignedError"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the dialog to edit an IndexcardBox.
     */
    public void editIndexcardBox() {
        mainMenu.displayEditIndexcardBox();
    }

    /**
     * Displays the dialog to edit a selected IndexcardBox.
     *
     * @param indexcardBoxName The name of the selected IndexcardBox.
     */
    public void editIndexcardBox(String indexcardBoxName) {
        mainMenu.displayEditIndexcardBox(indexcardBoxName);
    }

    /**
     * Delegates the exercise to update an existing IndexcardBox.
     *
     * @param indexcardBoxName The name of the IndexcardBox that should be updated.
     * @param categoryList The new Categories of the IndexcardBox.
     */
    public void editIndexcardBox(String indexcardBoxName, List<Category> categoryList) {
        indexcardBoxLogic.updateIndexcardBox(indexcardBoxName, categoryList);
    }

    /**
     * Displays the dialog to delete an IndexcardBox.
     */
    public void deleteIndexcardBox() {
        mainMenu.displayDeleteIndexcardBox();
    }

    /**
     * Delegates the exercise to delete an existing IndexcardBox to the IndexcardBoxLogic.
     * Displays an error, if no IndexcardBox with the same name exists.
     *
     * @param name The name of the IndexcardBox.
     */
    public void deleteIndexcardBox(String name){
        try {
            indexcardBoxLogic.deleteIndexcardBox(name);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format(language.getName("indexcardBoxDeletedMessage"), name),language.getName("indexcardBoxDeleted") ,
                    JOptionPane.INFORMATION_MESSAGE);
        }
        catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu,
                    language.getName("noIndexcardBoxWithNameError"), language.getName("noIndexcardBoxError"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delegates the exercise to return a list of all IndexcardBoxes.
     *
     * @return A list of all IndexcardBoxes.
     */
    public List<IndexcardBox> getAllIndexcardBoxes() {
        return indexcardBoxLogic.getAllIndexcardBoxes();
    }

    /**
     * Delegates the exercise to find an IndexcardBox with the given name to the IndexcardBoxLogic.
     *
     * @param indexcardBoxName The name of the Indexcard.
     * @return The IndexcardBox if it exists.
     */
    public Optional<IndexcardBox> getIndexcardBoxByName(String indexcardBoxName) {
        return Optional.ofNullable(indexcardBoxLogic.getIndexcardBoxByName(indexcardBoxName));
    }

    /**
     * Returns the names of all IndexcardBoxes.
     *
     * @return A list of the names of all IndexcardBoxes.
     */
    public List<String> getAllIndexcardBoxNames() {
        return getAllIndexcardBoxes().stream().map(IndexcardBox::getName).toList();
    }

    /**
     * Delegates the exercise to search in the IndexcardBoxes for a text.
     *
     * @param text The text.
     * @return A list of all IndexcardBoxes that contain the text.
     */
    public List<IndexcardBox> searchIndexcardBox(String text) {
        return indexcardBoxLogic.searchIndexcardBox(text);
    }

    /* KEYWORDS */

    /**
     * Delegates the exercise to find all Keywords to the KeywordLogic.
     *
     * @return A list of all Keywords.
     */
    public List<Keyword> getAllKeywords() {
        return keywordLogic.getAllKeywords();
    }

    /**
     * Returns a list of the names of all Keywords.
     *
     * @return A list of the names of all Keywords.
     */
    public List<String> getAllKeywordNames() {
        return getAllKeywords().stream().map(Keyword::getName).toList();
    }

    /* CATEGORIES */

    /**
     * Displays the Dialog to create a new Category.
     */
    public void createCategory() {
        mainMenu.displayCreateCategory();
    }

    /**
     * Delegates the exercise to create a new Category to the CategoryLogic.
     *
     * @param name The name of the Category.
     * @param parents The parents of the Category.
     * @param indexcards The Indexcards that should be in this Category.
     */
    public void createCategory(String name, List<Category> parents, List<Indexcard> indexcards) {
        try {
            categoryLogic.createCategory(name, indexcards, parents);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format((language.getName("categoryCreatedMessage")), name), language.getName("categoryCreated"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu, e.getMessage());
        }
    }

    /**
     * Displays the Dialog to edit an existing Category.
     */
    public  void editCategory(){
        mainMenu.displayEditCategory();
    }

    /**
     * Displays the dialog to edit a selected Category.
     *
     * @param category The selected Category.
     */
    public void editCategory(Category category) {
        mainMenu.displayEditCategory(category);
    }

    /**
     * Delegates the exercise to edit a Category to the CategoryLogic.
     * Displays an error, if there is no Category with the given name.
     *
     * @param newName The new name of the Category.
     * @param newParents The new parents of the Category.
     * @param newIndexcards The new Indexcards of the Category.
     * @param id The id of the Category
     */
    public void editCategory(String newName, List<Category> newParents, List<Indexcard> newIndexcards, Long id) {
        try {
            categoryLogic.updateCategory(newName, newParents, newIndexcards, id);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format((language.getName("categoryEditedMessage")), newName), language.getName("categoryEdited"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu, e.getMessage());
        }
    }

    /**
     * Displays the Dialog to delete an existing Category.
     */
    public void deleteCategory() {
        mainMenu.displayDeleteCategory();
    }

    /**
     * Delegates the exercise to delete an existing Category to the CategoryLogic.
     * *
     * @param category The Category that should be deleted.
     */
    public void deleteCategory(Category category){
        try {
            String deletedName = category.getCategoryName();
            categoryLogic.deleteCategory(category);
            JOptionPane.showMessageDialog(mainMenu,
                    String.format(language.getName("categoryDeletedMessage"), deletedName), language.getName("categoryDeleted"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        catch (final IllegalStateException e) {
            JOptionPane.showMessageDialog(mainMenu, e.getMessage());
        }
    }

    /**
     * Delegates the exercise to return a list of all parents of a Category.
     *
     * @param category The Category from them the parents should be returned.
     * @return A list of all parents of the Category.
     */
    public List<Category> getParentCategories(Category category) {
        return categoryLogic.getParentCategories(category);
    }

    /**
     * Delegates the exercise to return a list of all parents of a Category. Includes the parents of the parent-categories.
     *
     * @param category The Category from them the parents should be returned.
     * @return A list of all parents of the Category, including the parents of the parents
     */
    public List<Category> getAllParentCategories(Category category) {
        return categoryLogic.getAllParentCategories(category);
    }

    /**
     * Delegates the exercise to find all Categories to the CategoryLogic.
     *
     * @return A list of all Categories.
     */
    public List<Category> getAllCategories() {
        return categoryLogic.getAllCategories();
    }

    /**
     * Delegates the exercise to find a Category with the given name to the CategoryLogic.
     *
     * @param category The name of the Category.
     * @return The Category if it exists.
     */
    public Optional<Category> getCategoryByName(String category) {
        return categoryLogic.getCategoryByName(category);
    }

    /**
     * Delegates the exercise to find all Categories from a CategoryNameList.
     *
     * @param categoryNameList The names of the Categories.
     * @return A list of the Categories with these names.
     */
    public List<Category> getCategoriesByCategoryNameList(List<String> categoryNameList) {
        return categoryLogic.getCategoriesByCategoryNameList(categoryNameList);
    }

    /**
     * Returns a list of the names of all Categories.
     *
     * @return A list of the names of all Categories.
     */
    public List<String> getCategoryNames() {
        return getAllCategories().stream().map(Category::getCategoryName).toList();
    }

    /**
     * Returns a list of all CategoryNames from a CategoryList.
     *
     * @param categoryList The Categories from them the names get returned.
     * @return A list of the Category-names from the list of Categories.
     */
    public List<String> getCategoriesByNames(List<Category> categoryList) {
        return categoryList.stream().map(Category::getCategoryName).toList();
    }

    /**
     * Delegates the exercise to get all Categories that contain a specific Indexcard to the CategoryLogic.
     *
     * @param indexCard The Indexcard
     * @return A list of all Categories that contain that Indexcard.
     */
    public List<Category> getCategoriesByIndexcard(Indexcard indexCard) {
        return categoryLogic.getCategoriesByIndexcard(indexCard);
    }

    /**
     * Delegates the exercise to search for Category`s with text in the category repository.
     *
     * @param text The Text.
     * @return A list of Categories that contains the text.
     */
    public List<Category> searchCategory(String text) {
        return categoryLogic.searchCategory(text);
    }

    /* LEARNSYSTEMS */

    /**
     * Delegates the exercise to learn an IndexcardBox to the LearnsystemLogic.
     *
     * @param indexcardBox The IndexcardBox that should be learned.
     */
    public void learnLeitnerSystem(String name, IndexcardBox indexcardBox, int numberOfBoxes, String sort, String box) {
        if (!indexcardBox.getCategoryList().isEmpty()) {
            String learnSystemName = indexcardBox.getName() + name;
            List<String> indexCardNameList = indexcardBox.getIndexcardList();
            mainMenu.displayLearning(leitnerLearnSystemLogic.learnLeitnerSystem(learnSystemName, indexCardNameList, numberOfBoxes, sort), indexcardBox, sort, box);
        }
        else {
            JOptionPane.showMessageDialog(mainMenu,
                    language.getName("noCategoryInIndexcardboxError"), language.getName("noCategoryInIndexcardbox"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void learnRandomLearnSystem(String name, IndexcardBox indexcardBox, int numberOfBoxes) {
        if (!indexcardBox.getCategoryList().isEmpty()) {
            String learnSystemName = indexcardBox.getName() + name;
            List<String> indexCardNameList = indexcardBox.getIndexcardList();
            mainMenu.displayLearning(leitnerLearnSystemLogic.learnLeitnerSystem(learnSystemName, indexCardNameList, numberOfBoxes, ""), indexcardBox, language.getName("random"));
        }
        else {
            JOptionPane.showMessageDialog(mainMenu,
                    language.getName("noCategoryInIndexcardboxError"), language.getName("noCategoryInIndexcardbox"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delegates the exercise to update an existing Learnsystem.
     * @param learnsystem The Learnsystem that should be edited
     */
    public void updateLearnsystem(LeitnerLearnSystem learnsystem) {
        leitnerLearnSystemLogic.updateLearnsystem(learnsystem);
    }

    /**
     * Delegates the exercise to check if a Learnsystem with the given name exists.
     *
     * @param name The name of the Learnsystem.
     * @return True, if a Learnsystem with this name exists.
     */
    public boolean existsLearnsystem(String name) {
        return leitnerLearnSystemLogic.existsLeitnerLearnSystem(name);
    }

    /**
     * Delegates the exercise to get a Learnsystem by its name.
     *
     * @param name The name of the Learnsystem.
     * @return The Learnsystem with the name.
     */
    public LeitnerLearnSystem getLeitnerLearnSystemByName(String name) {
        return leitnerLearnSystemLogic.getLeitnerLearnSystemByName(name);
    }
}