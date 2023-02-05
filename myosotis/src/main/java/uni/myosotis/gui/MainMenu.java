package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends JFrame {
    private JTabbedPane tabbedPane;
    private Language language;
    private JPanel categoryPane;
    private JPanel indexcardPane;
    private JPanel glossarPane;
    private JPanel settingsPanel;
    private JPanel indexCardBoxPane;

    private final transient Controller controller;

    /**
     * Creates a new Window of the MainMenu.
     *
     * @param controller The controller.
     */
    public MainMenu(final Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setTitle("Myosotis");
        //createExampleIndexcards(); //TODO: Remove this line FIXME
        pack();
        setMinimumSize(getSize());
        setSize(800, 600);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        addAllTabs();
    }
    public void addAllTabs(){
        tabbedPane = new JTabbedPane();
        setContentPane(tabbedPane);
        //Add all the Tabs
        Glossar glossar = new Glossar(controller, language);
        glossarPane = glossar.getGlossarPane();

        IndexcardTab indexcardTab = new IndexcardTab(controller, language);
        indexcardPane = indexcardTab.getIndexcardPane();

        IndexcardBoxTab indexcardBoxTab = new IndexcardBoxTab(controller,language);
        indexCardBoxPane = indexcardBoxTab.getIndexcardPane();

        CategoryTab categoryTab = new CategoryTab(controller, language);
        categoryPane = categoryTab.getCategoryPane();

        SettingsTab settingsTab = new SettingsTab(controller, language);
        settingsPanel = settingsTab.getSettingsPane();

        tabbedPane.addTab(language.getName("glossarTitle"), glossarPane);
        tabbedPane.addTab(language.getName("categoryTitle"), categoryPane);
        tabbedPane.addTab(language.getName("indexcardTitle"), indexcardPane);
        tabbedPane.addTab(language.getName("indexcardBoxTitle"),indexCardBoxPane);
        tabbedPane.addTab(language.getName("settingsTitle"), settingsPanel);

        tabbedPane.addChangeListener(e -> {
            // Get the index of the selected tab
            int selectedTab = tabbedPane.getSelectedIndex();

            // Call your function here, passing in the selected tab index as a parameter
            if (selectedTab == 0) {
                glossar.setCategoryComboBox();
                glossar.setKeywordComboBox();
                glossar.setGlossar();
            }

        });
    }

    /**
     * Close the Window, and end the application.
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /**
     * Displays an Indexcard.
     *
     * @param indexcard The Indexcard that should be displayed.
     */
    public void displayIndexcard(Indexcard indexcard) {
        DisplayIndexcard displayIndexcard = new DisplayIndexcard(controller, indexcard, language);
        displayIndexcard.setSize(600, 400);
        displayIndexcard.setMinimumSize(displayIndexcard.getSize());
        displayIndexcard.setLocationRelativeTo(this);
        displayIndexcard.setVisible(true);
    }

    /**
     * Displays the Dialog to create a new Indexcard.
     */
    public void displayCreateIndexcard() {
        final CreateIndexcard createIndexcard = new CreateIndexcard(controller, language);
        createIndexcard.pack();
        createIndexcard.setMinimumSize(createIndexcard.getSize());
        createIndexcard.setLocationRelativeTo(this);
        createIndexcard.setVisible(true);
    }

    /**
     * Displays the Dialog to create a new Indexcard.
     */
    public void displayCreateIndexcardBox() {
        final CreateIndexcardBox createIndexcardBox = new CreateIndexcardBox(controller, language);
        createIndexcardBox.pack();
        createIndexcardBox.setMinimumSize(createIndexcardBox.getSize());
        createIndexcardBox.setSize(400, 300);
        createIndexcardBox.setLocationRelativeTo(this);
        createIndexcardBox.setVisible(true);
    }

    /**
     * Displays the Dialog to edit a new Indexcard.
     */
    public void displayEditIndexcardBox() {
        final EditIndexcardBox editIndexcardBox = new EditIndexcardBox(controller, language);
        editIndexcardBox.pack();
        editIndexcardBox.setMinimumSize(editIndexcardBox.getSize());
        editIndexcardBox.setSize(400, 300);
        editIndexcardBox.setLocationRelativeTo(this);
        editIndexcardBox.setVisible(true);
    }

    public void displayEditIndexcardBox(String indexcardBoxName) {
        final EditIndexcardBox editIndexcardBox = new EditIndexcardBox(controller, language);
        editIndexcardBox.pack();
        editIndexcardBox.setMinimumSize(editIndexcardBox.getSize());
        editIndexcardBox.setSize(400, 300);
        editIndexcardBox.setLocationRelativeTo(this);
        editIndexcardBox.setIndexcardBox(indexcardBoxName);
        editIndexcardBox.setVisible(true);
    }

    /**
     * Displays the Dialog to edit an existing Indexcard.
     */
    public void displayEditIndexcard() {
        final EditIndexcard editIndexcard = new EditIndexcard(controller, language);
        editIndexcard.pack();
        editIndexcard.setMinimumSize(editIndexcard.getSize());
        editIndexcard.setSize(400, 300);
        editIndexcard.setLocationRelativeTo(this);
        editIndexcard.setVisible(true);
    }

    /**
     * Displays the Dialog to edit a specific Indexcard.
     *
     * @param indexcard The Indexcard which is preset to be edited
     */
    public void displayEditIndexcard(Indexcard indexcard) {
        final EditIndexcard editIndexcard = new EditIndexcard(controller, language);
        editIndexcard.pack();
        editIndexcard.setMinimumSize(editIndexcard.getSize());
        editIndexcard.setLocationRelativeTo(this);
        editIndexcard.setIndexcard(indexcard);
        editIndexcard.setVisible(true);
    }

    /**
     * Displays the Dialog to delete an Indexcard.
     */
    public void displayDeleteIndexcard() {
        final DeleteIndexcard deleteIndexcard = new DeleteIndexcard(controller, language);
        deleteIndexcard.pack();
        deleteIndexcard.setMinimumSize(deleteIndexcard.getSize());
        deleteIndexcard.setLocationRelativeTo(this);
        deleteIndexcard.setVisible(true);
    }

    /**
     * Displays the Dialog to delete an Indexcard.
     */
    public void displayDeleteIndexcardBox() {
        final DeleteIndexcardBox deleteIndexcardBox = new DeleteIndexcardBox(controller, language);
        deleteIndexcardBox.pack();
        deleteIndexcardBox.setMinimumSize(deleteIndexcardBox.getSize());
        deleteIndexcardBox.setLocationRelativeTo(this);
        deleteIndexcardBox.setVisible(true);
    }

    /**
     * Displays the Dialog to create a new Indexcard.
     */
    public void displayCreateCategory() {
        final CreateCategory createCategory = new CreateCategory(controller, language);
        createCategory.setSize(400, 600);
        createCategory.setMinimumSize(createCategory.getSize());
        createCategory.setLocationRelativeTo(this);
        createCategory.setVisible(true);
    }

    /**
     * Displays the Dialog to edit an existing Indexcard.
     */
    public void displayEditCategory() {
        final EditCategory editCategory = new EditCategory(controller, language);
        editCategory.setSize(400, 600);
        editCategory.setMinimumSize(editCategory.getSize());
        editCategory.setLocationRelativeTo(this);
        editCategory.setVisible(true);
    }

    /**
     * Displays the Dialog to edit a specific Category.
     *
     * @param category The Category which is preset to be edited
     */
    public void displayEditCategory(Category category) {
        final EditCategory editCategory = new EditCategory(controller, language);
        editCategory.setSize(400, 600);
        editCategory.setMinimumSize(editCategory.getSize());
        editCategory.setLocationRelativeTo(this);
        editCategory.setCategory(category);
        editCategory.setVisible(true);
    }

    /**
     * Displays the Dialog to delete an Indexcard.
     */
    public void displayDeleteCategory() {
        final DeleteCategory deleteCategory = new DeleteCategory(controller, language);
        deleteCategory.pack();
        deleteCategory.setMinimumSize(deleteCategory.getSize());
        deleteCategory.setLocationRelativeTo(this);
        deleteCategory.setVisible(true);
    }

    /**
     * Displays the Menu for learning.
     */
    public void displayLearning(LeitnerLearnSystem learnSystem, IndexcardBox indexCardBox, String sort, String box) {
        final DisplayIndexcardToLearn displayIndexcardToLearn = new DisplayIndexcardToLearn(controller, learnSystem, indexCardBox, language, sort, box);
        displayIndexcardToLearn.pack();
        displayIndexcardToLearn.setMinimumSize(displayIndexcardToLearn.getSize());
        displayIndexcardToLearn.setLocationRelativeTo(this);
        if (!displayIndexcardToLearn.checkIndexCardList2Learn()) {
            displayIndexcardToLearn.setVisible(true);
        } else {
            displayIndexcardToLearn.dispose();
        }
    }

    /**
     * Displays the Menu for learning.
     */
    public void displayLearning(LeitnerLearnSystem learnSystem, IndexcardBox indexCardBox, String selectedSort) {
        final DisplayIndexcardToLearn displayIndexcardToLearn = new DisplayIndexcardToLearn(controller, learnSystem, indexCardBox, language, selectedSort, "");
        displayIndexcardToLearn.pack();
        displayIndexcardToLearn.setMinimumSize(displayIndexcardToLearn.getSize());
        displayIndexcardToLearn.setLocationRelativeTo(this);
        displayIndexcardToLearn.setVisible(true);
    }


    /**
     * Create basic tests for Indexcards, Categories, Keywords and IndexcardBoxes.
     */
    public void createExampleIndexcards(){
        // Test createIndexcard
        controller.createIndexcard("ITK1", "Was ist ein Computer?", "Ein Computer ist ein Gerät, das Daten verarbeitet.", List.of(new String[]{"#IT"}), new ArrayList<>());
        // Test editIndexcard
        controller.editIndexcard("ITK1", "Was ist ein Computer?", "Ein Computer ist ein Gerät, das Daten verarbeitet. ps:Version2", List.of(new String[]{"#IT", "#Biologie"}), new ArrayList<>(), controller.getIndexcardByName("ITK1").get().getId());
        // Test deleteIndexcard
        controller.deleteIndexcard(controller.getIndexcardByName("ITK1").get().getId());

        controller.createIndexcard("ITK2", "Was ist ein Programm?", "Ein Programm ist eine Anweisung für einen Computer.", List.of(new String[]{"#IT"}), new ArrayList<>());
        controller.createIndexcard("ITK3", "Was ist ein Programmierer?", "Ein Programmierer ist eine Person, die Programme schreibt.", List.of(new String[]{"#IT", "#Biologie"}), new ArrayList<>());

        controller.createIndexcard("BOTK1", "Was ist ein Baum?", "Ein Baum ist ein Lebewesen.", List.of(new String[]{"#Biologie"}), new ArrayList<>());
        controller.createIndexcard("BOTK2", "Was ist ein Blume?", "Eine Blume ist ein Lebewesen.", List.of(new String[]{"#Biologie"}), new ArrayList<>());

        controller.createIndexcard("BIOK1", "Was ist ein Organismus?", "Ein Organismus ist ein Lebewesen.", List.of(new String[]{"#Biologie"}), new ArrayList<>());
        controller.createIndexcard("BIOK2", "Was ist ein Zelle?", "Eine Zelle ist der kleinste Teil eines Organismus.", List.of(new String[]{"#Biologie"}), new ArrayList<>());
        controller.createIndexcard("BIOK1", "Was ist ein Organismus?", "Ein Organismus ist ein Lebewesen.", List.of(new String[]{"#Biologie"}), new ArrayList<>());
        controller.createIndexcard("BIOK2", "Was ist ein Zelle?", "Eine Zelle ist der kleinste Teil eines Organismus.", List.of(new String[]{"#Biologie"}), new ArrayList<>());
        // Test createCategory
        controller.createCategory("IT", new ArrayList<>(), controller.getAllIndexcards());
        // Test editCategory without editing parent
        controller.editCategory(controller.getCategoryByName("IT").get().getCategoryName(), controller.getCategoryByName("IT").get().getChildren(), controller.getCategoryByName("IT").get().getIndexcards(), controller.getCategoryByName("IT").get().getId());
        // Test deleteCategory
        controller.deleteCategory(controller.getCategoryByName("IT").get());

        controller.createCategory("Biologie", new ArrayList<>(), controller.getAllIndexcards());

        // Test createIndexcardBox
        controller.createIndexcardBox("ITBox",controller.getCategoriesByCategoryNameList(List.of(new String[]{"Biologie"})));
        // Test editIndexcardBox
        controller.editIndexcardBox("ITBox", controller.getCategoriesByCategoryNameList(List.of(new String[]{"IT","Biologie"})));
        // Test deleteIndexcardBox
        controller.deleteIndexcardBox("ITBox");

        controller.createIndexcardBox("BiologieBox",controller.getCategoriesByCategoryNameList(List.of(new String[]{"Biologie"})));

        // Parent Category test
        controller.createCategory("Botanic", List.of(controller.getCategoryByName("Biologie").get()), new ArrayList<>());
    }

    /**
     * Sets the language of the application.
     *
     * @param language The language.
     */
    public void setLanguage(Language language) {
        this.language = language;
        this.setTitle(language.getName("title"));
        addAllTabs();
        this.pack();
    }
}
