package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.Indexcard;
import javax.swing.*;
import java.awt.event.*;

public class DeleteIndexcard extends JDialog {

    /**
     * The controller.
     */
    private final Controller controller;
    private final Language language;
    private JPanel contentPane;
    private JComboBox<String> comboBoxName;
    private JButton deleteButton;
    private JLabel whichIndexcardLabel;

    /**
     * Create a new Dialog to delete an Indexcard.
     *
     * @param controller The controller.
     * @param language The language.
     */
    public DeleteIndexcard(Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setContentPane(contentPane);
        setModal(true);
        setTitle(language.getName("deleteIndexcardTitle"));
        deleteButton.setText(language.getName("delete"));
        getRootPane().setDefaultButton(deleteButton);
        // Set the language
        whichIndexcardLabel.setText(language.getName("whichIndexcard"));

        // Array of all Indexcard-names
        String[] indexcardsNames = controller.getAllIndexcards().stream().
                map(Indexcard::getName).toList().toArray(new String[0]);

        comboBoxName.setModel(new DefaultComboBoxModel<>(indexcardsNames));

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
     * Deletes an Indexcard. If no Indexcard is select, an error will be displayed.
     */
    private void onDelete() {
        String indexCardToDelete = (String) comboBoxName.getSelectedItem();
        if (indexCardToDelete != null) {
            if (controller.getIndexcardByName(indexCardToDelete).isPresent()) {
                controller.deleteIndexcard(controller.getIndexcardByName(indexCardToDelete).get().getId());
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,language.getName("noIndexcardSelectedError") ,language.getName("deletionNotPossibleError") , JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Closes the window.
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
