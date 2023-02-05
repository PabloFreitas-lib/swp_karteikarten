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

public class CreateCategory extends JDialog {

    /**
     * The controller.
     */
    private final Controller controller;
    private final Language language;
    private JPanel contentPane;
    private JTextField nameTextField;
    private JButton createButton;
    private JButton cancelButton;
    private JTree categoryTree;
    private JList<String> parentList;
    private JList<String> indexcardList;
    private JLabel nameLabel;
    private JLabel hierarchyLabel;
    private JLabel parentLabel;
    private JLabel indexcardLabel;

    /**
     * Creates the Dialog to create a new Category.
     *
     * @param controller The controller.
     */
    public CreateCategory(Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setTitle(language.getName("createCategoryTitle"));
        setModal(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(createButton);

        updateCategoryTree();
        updateParentList();
        updateIndexcardList();
        // Set the language
        nameLabel.setText(language.getName("name"));
        hierarchyLabel.setText(language.getName("hierarchy"));
        parentLabel.setText(language.getName("parent"));
        indexcardLabel.setText(language.getName("indexcard"));
        createButton.setText(language.getName("create"));
        cancelButton.setText(language.getName("cancel"));

        // call onCreate() when the createButton is clicked.
        createButton.addActionListener(e -> onCreate());

        // call onCancel() when the cancelButton is clicked.
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
     *
     * @param node The node of the Tree on which the nodes get added.
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
        // List of possible Parents
        DefaultListModel<String> newParentList = new DefaultListModel<>();
        newParentList.addAll(controller.getAllCategories().stream().map(Category::getCategoryName).toList());
        parentList.setModel(newParentList);
    }

    /**
     * Updates the list of Indexcards.
     */
    private void updateIndexcardList() {
        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        defaultListModel.addAll(controller.getAllIndexcardNames());
        indexcardList.setModel(defaultListModel);
    }

    /**
     * Create a new Category, if the entered Text isn't empty, and close the window.
     */
    private void onCreate() {
        String name = nameTextField.getText();
        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, language.getName("categoryWithNoNameError"), language.getName("categoryWithNoName"), JOptionPane.INFORMATION_MESSAGE);
        } else if (controller.getAllCategories().stream().map(Category::getCategoryName).toList().contains(name)) {
            JOptionPane.showMessageDialog(this, language.getName("categoryAlreadyExistError"), language.getName("categoryAlreadyExist"), JOptionPane.INFORMATION_MESSAGE);
        } else {
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
            controller.createCategory(name, selectedParents, selectedIndexcards);
            dispose();
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
