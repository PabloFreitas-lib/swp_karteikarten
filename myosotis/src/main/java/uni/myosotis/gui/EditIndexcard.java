package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.Keyword;

public class EditIndexcard extends JDialog {

    private final Controller controller;
    private final Language language;
    private JPanel contentPane;
    private JButton confirmButton, cancelButton;
    private JTextArea textAreaQuestion, textAreaAnswer;
    private JComboBox<String> comboBoxName;
    private JTextField textFieldName;
    private JTextField textFieldKeywords;
    private JTextField termField;
    private JButton addLinkButton;
    private JButton removeLinkButton;
    private JList<String> linkList;
    private JList<String> indexcardList;
    private JLabel indexcardLabel;
    private JLabel nameLabel;
    private JLabel questionLabel;
    private JLabel answerLabel;
    private JLabel keywordLabel;
    private JLabel linkLabel;
    private JLabel termLabel;
    private JLabel indexcardLabel2;
    private String oldName;

    /**
     * Creates a new EditIndexcard-Dialog.
     *
     * @param controller The Controller of the application.
     */
    public EditIndexcard(Controller controller, Language language){
        this.controller = controller;
        this.language = language;
        setModal(true);
        setTitle("Karteikarte bearbeiten");
        getRootPane().setDefaultButton(confirmButton);
        setContentPane(contentPane);
        // Set Language
        indexcardLabel.setText(language.getName("indexcard"));
        nameLabel.setText(language.getName("name"));
        questionLabel.setText(language.getName("question"));
        answerLabel.setText(language.getName("answer"));
        keywordLabel.setText(language.getName("keyword"));
        linkLabel.setText(language.getName("link"));
        termLabel.setText(language.getName("term"));
        indexcardLabel2.setText(language.getName("indexcard"));
        confirmButton.setText(language.getName("confirm"));
        cancelButton.setText(language.getName("cancel"));
        addLinkButton.setText(language.getName("addLink"));
        removeLinkButton.setText(language.getName("removeLink"));

        //list of all indexcards
        List<Indexcard> indexcards = controller.getAllIndexcards();
        //Array of all indexcard names
        String[] indexcardsNames = new String[indexcards.size()];
        for (int i = 0; i < indexcards.size(); i++) {
            indexcardsNames[i] = indexcards.get(i).getName();
        }
        //ComboBox with all indexcard names
        comboBoxName.setModel(new DefaultComboBoxModel<>(indexcardsNames));

        // List of existing Indexcards
        DefaultListModel<String> listModelForIndexcards = new DefaultListModel<>();
        listModelForIndexcards.addAll(controller.getAllIndexcardNames());
        indexcardList.setModel(listModelForIndexcards);

        //ActionListener for the ComboBox
        comboBoxName.addActionListener(e -> {
            Optional<Indexcard> indexcard = controller.getIndexcardByName((String) comboBoxName.getSelectedItem());
            if(indexcard.isPresent()){
                textFieldName.setText(indexcard.get().getName());
                textAreaQuestion.setText(indexcard.get().getQuestion());
                textAreaAnswer.setText(indexcard.get().getAnswer());
                StringBuilder keywords = new StringBuilder();
                for (Keyword keyword : indexcard.get().getKeywords()) {
                    keywords.append("#").append(keyword.getName()).append(" ");
                }
                textFieldKeywords.setText(keywords.toString());

                // List of all Links of the Indexcard.
                DefaultListModel<String> listModel = new DefaultListModel<>();
                listModel.addAll(indexcard.get().getLinks().stream().map(link -> link.getTerm() + " => " + link.getIndexcard().getName()).toList());
                linkList.setModel(listModel);

                // List of existing Indexcards without the selected.
                DefaultListModel<String> listModelOfIndexcards = new DefaultListModel<>();
                List<String> list = controller.getAllIndexcardNames().stream().filter(name -> !name.equals(comboBoxName.getSelectedItem())).toList();
                listModelOfIndexcards.addAll(list);
                indexcardList.setModel(listModelOfIndexcards);

                oldName = indexcard.get().getName();
            }
        });

        confirmButton.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

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
     * When the OK-Button is pressed, the Indexcard is edited.
     */
    private void onOK() {

        Indexcard oldIndexcard;
        if (controller.getAllIndexcardNames().stream().filter(name -> !name.equals(comboBoxName.getSelectedItem())).toList().contains(textFieldName.getText())) {
            JOptionPane.showMessageDialog(this, language.getName("indexcardAlreadyExistError"), language.getName("nameAlreadyAssignedError"), JOptionPane.INFORMATION_MESSAGE);
        } else {

            //Old Parameters
            if (controller.getIndexcardByName(oldName).isPresent()) {
                oldIndexcard = controller.getIndexcardByName(oldName).get();
            } else {
                throw new IllegalStateException(language.getName("indexcardToEditNonExistingError"));
            }

            final Long oldIndexcardId = oldIndexcard.getId();

            // New Parameters
            final String name = textFieldName.getText();
            final String question = textAreaQuestion.getText();
            final String answer = textAreaAnswer.getText();

            // Separate Keywords
            String[] keywordStrings = textFieldKeywords.getText()
                    .replaceAll(" ", "")
                    .split("#");

            List<String> keywords = new ArrayList<>(Arrays.asList(keywordStrings));
            keywords.remove(0);

            // Save added Links
            List<String> links = new ArrayList<>();
            for (int i = 0; i < linkList.getModel().getSize(); i++) {
                links.add(linkList.getModel().getElementAt(i));
            }

            if (!name.isBlank() && !question.isBlank() && !answer.isBlank()) {
                controller.editIndexcard(name, question, answer, keywords, links, oldIndexcardId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        language.getName("notAllFieldsFilledError"), language.getName("indexcardNotEditedError"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Sets an indexcard to edit by setting the ComboBox to the indexcard name.
     *
     * @param indexcard The Indexcard that should be edited.
     */
    public void setIndexcard(Indexcard indexcard){
        comboBoxName.setSelectedItem(indexcard.getName());
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
            // Save previous added Links without the deleted Links
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
     * When the Cancel-Button is pressed, the Dialog is closed.
     */
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
