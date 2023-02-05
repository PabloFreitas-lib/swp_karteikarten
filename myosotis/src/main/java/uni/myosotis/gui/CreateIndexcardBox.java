package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.Category;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CreateIndexcardBox extends JDialog {

    /**
     * The controller.
     */
    private final Controller controller;
    private final Language language;
    private JLabel nameLabel;
    private JTextField indexcardBoxName;
    private JScrollPane categoryScrollPane;
    private JButton confirmButton;
    private JButton cancelButton;
    private JPanel contentPane;
    private JLabel categoryLabel;


    private JList<String> indexcardsNamesList;

    private JList<String> categoryNamesList;

    private JList<String> categoryChildNamesList;


    /**
     * Create a new Dialog to create an IndexcardBox.
     *
     * @param controller The controller.
     * @param language The language.
     */
    public CreateIndexcardBox(Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setTitle(language.getName("createIndexcardBoxTitle"));
        setModal(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(confirmButton);
        setCategoryScrollPane();
        nameLabel.setText(language.getName("name"));
        categoryLabel.setText(language.getName("category"));
        confirmButton.setText(language.getName("confirm"));
        cancelButton.setText(language.getName("cancel"));

        confirmButton.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Sets the CategoryScrollPane with a list of all Categories.
     */
    public void setCategoryScrollPane(){
        // Array of all Categories
        String[] categoriesNames = controller.getAllCategories().stream().
                map(Category::getCategoryName).toList().toArray(new String[0]);
        categoryNamesList = new JList<>(categoriesNames);
        categoryNamesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Parents options
        categoryScrollPane.setViewportView(categoryNamesList);
    }

    /**
     * Create a new Category, if the entered Text isn't empty, and close the window.
     */
    private void onOK() {
        final String name = indexcardBoxName.getText();
        final List<String> selectedCategoriesNames = categoryNamesList.getSelectedValuesList();
        final List<Category> selectedCategories = new ArrayList<>();
        for (String selectedCategoriesName : selectedCategoriesNames) {
            if (controller.getCategoryByName(selectedCategoriesName).isPresent()) {
                selectedCategories.add(controller.getCategoryByName(selectedCategoriesName).get());
            }
        }
        if (!name.isBlank() && !selectedCategoriesNames.isEmpty()) {
            controller.createIndexcardBox(name, selectedCategories);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, language.getName("notAllFieldsFilledError"), language.getName("indexcardboxNotCreated"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Close the Window.
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
