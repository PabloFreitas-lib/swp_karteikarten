package uni.myosotis.gui;

import uni.myosotis.controller.Controller;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateIndexcard extends JDialog {

    /**
     * The controller.
     */
    private final Controller controller;
    private final Language language;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextArea textAreaQuestion;
    private JTextArea textAreaAnswer;
    private JTextField textFieldKeywords;
    private JList<String> linkList;
    private JTextField termField;
    private JList<String> indexcardList;
    private JButton addLinkButton;
    private JButton removeLinkButton;
    private JLabel questionLabel;
    private JLabel answerLabel;
    private JLabel keywordLabel;
    private JLabel linkLabel;
    private JLabel termLabel;
    private JLabel indexcardLabel;
    private JLabel nameLabel;

    /**
     * Create a new Dialog to create an Indexcard.
     *
     * @param controller The controller.
     * @param language The selected language.
     */
    public CreateIndexcard(Controller controller, Language language) {
        this.controller = controller;
        this.language = language;
        setTitle(language.getName("createIndexcardTitle"));
        setModal(true);
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        // Set Model
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(controller.getAllIndexcardNames());
        // For Language
        indexcardList.setModel(listModel);
        buttonOK.setText(language.getName("confirm"));
        buttonCancel.setText(language.getName("cancel"));
        nameLabel.setText(language.getName("name"));
        questionLabel.setText(language.getName("question"));
        answerLabel.setText(language.getName("answer"));
        keywordLabel.setText(language.getName("keyword"));
        linkLabel.setText(language.getName("link"));
        termLabel.setText(language.getName("term"));
        indexcardLabel.setText(language.getName("indexcard"));
        addLinkButton.setText(language.getName("addLink"));
        removeLinkButton.setText(language.getName("removeLink"));
        // ActionListeners
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        addLinkButton.addActionListener(e -> onAddLink());
        removeLinkButton.addActionListener(e -> onRemoveLink());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Add a term to the termField, if [text] ist entered, with the text as the term
        textAreaAnswer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE ) {
                    String text = textAreaAnswer.getText();
                    if (text.contains("[")) {
                        String term = text.substring(text.lastIndexOf("[") + 1);
                        if (term.contains("]")) {
                            term = term.substring(0, term.indexOf("]"));
                            if (!term.isBlank()&& !term.equals(textAreaAnswer.getText())) {
                                termField.setText(term);
                                onAddLink();
                                textAreaAnswer.setText(text.replace("[" + term + "]", term));
                            }
                        }
                    }
                }
            }
        });
        textAreaQuestion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE ) {
                    String text = textAreaQuestion.getText();
                    if (text.contains("[")) {
                        String term = text.substring(text.lastIndexOf("[") + 1);
                        if (term.contains("]")) {
                            term = term.substring(0, term.indexOf("]"));
                            if (!term.isBlank() && !term.equals(textAreaQuestion.getText())) {
                                termField.setText(term);
                                onAddLink();
                                textAreaQuestion.setText(text.replace("[" + term + "]", term));
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Create a new Indexcard, if the entered Text isn't empty, and close the window.
     */
    private void onOK() {
        final String name = textFieldName.getText();
        final String question = textAreaQuestion.getText();
        final String answer = textAreaAnswer.getText();

        // Separate Keywords
        String[] keywordStrings = textFieldKeywords.getText()
                .replaceAll(" ", "")
                .split("#");
        List<String> keywords = new ArrayList<>(Arrays.asList(keywordStrings));
        keywords.remove(0);

        // Separate Links
        List<String> links = new ArrayList<>();
        // Save added Links
        for (int i = 0; i < linkList.getModel().getSize(); i++) {
            links.add(linkList.getModel().getElementAt(i));
        }

        if (!name.isBlank() && !question.isBlank() && !answer.isBlank()) {
            controller.createIndexcard(name, question, answer, keywords, links);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, language.getName("notAllFieldsFilledError"), language.getName("indexcardNotCreated"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Add a new Link.
     */
    private void onAddLink() {
        if (termField.getText().contains(" => ")) {
            JOptionPane.showMessageDialog(this, language.getName("noValidTerm"), language.getName("noValidTerm"), JOptionPane.INFORMATION_MESSAGE);
        } else if (!termField.getText().isBlank() && indexcardList.getSelectedValue() != null) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            // Save previous added Links
            for (int i = 0; i < linkList.getModel().getSize(); i++) {
                listModel.addElement(linkList.getModel().getElementAt(i));
            }
            // Add new Link
            listModel.addElement(termField.getText() + " => " + indexcardList.getSelectedValue());
            linkList.setModel(listModel);
            // Clear selection
            termField.setText("");
            indexcardList.clearSelection();
        }
    }

    /**
     * Removes a Link.
     */
    private void onRemoveLink() {
        if (linkList.getSelectedValue() != null) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            // Save previous added Links without the deleted Link
            for (int i = 0; i < linkList.getModel().getSize(); i++) {
                int finalI = i;
                if (Arrays.stream(linkList.getSelectedIndices()).noneMatch(e -> e == finalI)) {
                    listModel.addElement(linkList.getModel().getElementAt(i));
                }
            }
            linkList.setModel(listModel);
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
