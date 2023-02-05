package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.Category;
import uni.myosotis.objects.Indexcard;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EditCategory extends JDialog {

    private final Controller controller;
    private final Language language;
    private JPanel contentPane;
    private JComboBox<String> comboBoxName;
    private JTextField nameTextField;
    private JButton editButton;
    private JButton cancelButton;
    private JTree categoryTree;
    private JList<String> parentList;
    private JList<String> indexcardList;
    private JLabel categoryLabel;
    private JLabel nameLabel;
    private JLabel hierarchyLabel;
    private JLabel parentLabel;
    private JLabel indexcardLabel;

    /**
     * Creates a new EditCategory-Dialog.
     *
     * @param controller The Controller of the application.
     */
    public EditCategory(Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setModal(true);
        setTitle(language.getName("editCategoryTitle"));
        editButton.setText(language.getName("confirm"));
        cancelButton.setText(language.getName("cancel"));
        getRootPane().setDefaultButton(editButton);
        setContentPane(contentPane);

        // List of all Categories
        List<Category> categories = controller.getAllCategories();
        // Array of all Category-names
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getCategoryName();
        }

        //ComboBox with all Category-names
        comboBoxName.setModel(new DefaultComboBoxModel<>(categoryNames));
        comboBoxName.setSelectedItem(null);

        updateCategoryTree();
        updateParentList();
        updateIndexcardList();

        // Set language
        categoryLabel.setText(language.getName("category"));
        nameLabel.setText(language.getName("name"));
        hierarchyLabel.setText(language.getName("hierarchy"));
        parentLabel.setText(language.getName("parent"));
        indexcardLabel.setText(language.getName("indexcard"));

        // Update infos, if another Category is selected.
        comboBoxName.addActionListener(e -> {
            nameTextField.setText((String) comboBoxName.getSelectedItem());
            updateParentList();
            updateIndexcardList();
        });

        editButton.addActionListener(e -> onEdit());

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
     * Updates the tree of the current poly-hierarchy of the Category`s.
     */
    private void updateCategoryTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(language.getName("categoryTitle"));
        for (Category category : controller.getAllCategories()) {
            if (controller.getParentCategories(category).isEmpty()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(category.getCategoryName());
                addNodes(node);
                root.add(node);
            }
        }
        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(root);
        categoryTree.setModel(defaultTreeModel);
    }

    /**
     * Adds the nodes to the categoryTree.
     */
    private void addNodes(DefaultMutableTreeNode node) {
        if (controller.getCategoryByName(String.valueOf(node.getUserObject())).isPresent()) {
            Category category = controller.getCategoryByName(String.valueOf(node.getUserObject())).get();
            for (Category child : category.getChildren()) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child.getCategoryName());
                addNodes(childNode);
                node.add(childNode);
            }
        }
    }

    /**
     * Updates the lists of possible parents of the Category.
     */
    private void updateParentList() {
        // All children of the edited Category
        List<String> allOwnChildrenNames = new ArrayList<>();
        if (controller.getCategoryByName((String) comboBoxName.getSelectedItem()).isPresent()) {
            Category categoryToEdit = controller.getCategoryByName((String) comboBoxName.getSelectedItem()).get();
            allOwnChildrenNames.addAll(categoryToEdit.getAllChildren().stream().map(Category::getCategoryName).toList());
        }
        // Filter own children
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        defaultListModel.addAll(controller.getAllCategories().stream()
                .map(Category::getCategoryName)
                .filter(categoryName -> !allOwnChildrenNames.contains(categoryName) && !categoryName.equals(comboBoxName.getSelectedItem())).toList());
        parentList.setModel(defaultListModel);
        // Select parents of the Category before editing
        if (controller.getCategoryByName((String) comboBoxName.getSelectedItem()).isPresent()) {
            List<String> parentNames = controller.getParentCategories(controller.getCategoryByName((String) comboBoxName.getSelectedItem()).get()).stream().map(Category::getCategoryName).toList();
            int[] selectedParents = new int[parentNames.size()];
            for (int i = 0; i < selectedParents.length; i++) {
                selectedParents[i] = defaultListModel.indexOf(parentNames.get(i));
            }
            parentList.setSelectedIndices(selectedParents);
        }
    }

    /**
     * Updates the list of Indexcards.
     */
    private void updateIndexcardList() {
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        defaultListModel.addAll(controller.getAllIndexcardNames());
        indexcardList.setModel(defaultListModel);
        // Select Indexcards, that are in the Category before editing
        if (comboBoxName.getSelectedItem() != null && controller.getCategoryByName((String) comboBoxName.getSelectedItem()).isPresent()) {
            List<String> indexcardNames = controller.getCategoryByName((String) comboBoxName.getSelectedItem()).get().getIndexcards().stream().map(Indexcard::getName).toList();
            int[] selectedIndexcards = new int[indexcardNames.size()];
            for (int i = 0; i < selectedIndexcards.length; i++) {
                selectedIndexcards[i] = defaultListModel.indexOf(indexcardNames.get(i));
            }
            indexcardList.setSelectedIndices(selectedIndexcards);
        }
    }

    /**
     * Sets a Category to edit by setting the ComboBox to the Category-name.
     *
     * @param category The Category.
     */
    public void setCategory(Category category){
        comboBoxName.setSelectedItem(category.getCategoryName());
    }

    /**
     * When the Edit-Button is pressed, the Category is edited.
     */
    private void onEdit() {
        String newName = nameTextField.getText();
        if (controller.getAllIndexcardNames().stream().filter(n -> !n.equals(comboBoxName.getSelectedItem())).toList().contains(newName)) {
            JOptionPane.showMessageDialog(this, language.getName("categoryAlreadyExistError"), language.getName("categoryAlreadyExist"), JOptionPane.INFORMATION_MESSAGE);
        } else if (newName.isBlank()) {
            JOptionPane.showMessageDialog(this, language.getName("categoryWithNoNameError"), language.getName("categoryWithNoName"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            Category oldCategory;
            if (controller.getCategoryByName((String) comboBoxName.getSelectedItem()).isPresent()) {
                oldCategory = controller.getCategoryByName((String) comboBoxName.getSelectedItem()).get();
            } else {
                throw new IllegalStateException(language.getName("noCategoryToEditError"));
            }
            final Long oldId = oldCategory.getId();
            List<Category> selectedParents = new ArrayList<>();
            for (String s : parentList.getSelectedValuesList()) {
                if (controller.getCategoryByName(s).isPresent()) {
                    selectedParents.add(controller.getCategoryByName(s).get());
                }
            }
            List<Indexcard> selectedIndexcards = new ArrayList<>();
            for (String s : indexcardList.getSelectedValuesList()) {
                if (controller.getIndexcardByName(s).isPresent()) {
                    selectedIndexcards.add(controller.getIndexcardByName(s).get());
                }
            }
            controller.editCategory(newName, selectedParents, selectedIndexcards, oldId);
            dispose();
        }
    }

    /**
     * When the Cancel-Button is pressed, the Dialog is closed.
     */
    private void onCancel() {
        dispose();
    }
}
