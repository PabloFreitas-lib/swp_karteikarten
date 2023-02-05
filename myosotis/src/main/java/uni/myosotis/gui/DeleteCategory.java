package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.Category;

import javax.swing.*;
import java.awt.event.*;

public class DeleteCategory extends JDialog{
    /**
     * The controller.
     */

    private final Controller controller;
    private final Language language;
    private JPanel contentPane;
    private JComboBox<String> categoryNamesComboBox;
    private JButton deleteButton;
    private JLabel whichCategoryLabel;

    /**
     * Create a new Dialog to delete a Category.
     *
     * @param controller Controller.
     * @param language The selected language.
     */
    public DeleteCategory(Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setContentPane(contentPane);
        setModal(true);
        setTitle(language.getName("deleteCategoryTitle"));
        getRootPane().setDefaultButton(deleteButton);
        // Set the language
        whichCategoryLabel.setText(language.getName("whichCategory"));
        deleteButton.setText(language.getName("delete"));
        // Array of all Categories
        String[] categoriesNames = controller.getAllCategories().stream().
                map(Category::getCategoryName).toList().toArray(new String[0]);

        categoryNamesComboBox.setModel(new DefaultComboBoxModel<>(categoriesNames));

        deleteButton.addActionListener(e -> onDelete());

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
     * Deletes a Category. If no Category is selected, an error will be displayed.
     */
    private void onDelete() {
        String categoryToDelete = (String) categoryNamesComboBox.getSelectedItem();
        if (categoryToDelete != null) {
            if (controller.getCategoryByName(categoryToDelete).isPresent()) {
                controller.deleteCategory(controller.getCategoryByName(categoryToDelete).get());
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, language.getName("noCategorySelectedError"), language.getName("noCategorySelectedError"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Closes the window
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
