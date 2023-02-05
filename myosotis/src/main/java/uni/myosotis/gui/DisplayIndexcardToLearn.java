package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.IndexcardBox;
import uni.myosotis.objects.LeitnerLearnSystem;
import uni.myosotis.objects.Link;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class DisplayIndexcardToLearn extends JDialog{
    private final Controller controller;
    private final LeitnerLearnSystem learnSystem;
    private final Language language;
    private Indexcard indexcard;

    private List<Indexcard> indexCardList2Learn;

    private JPanel contentPane;
    //private JButton backButton;
    private JButton nextButton;
    private JButton answeredButton;
    private JLabel nameLabel;
    private JProgressBar learnProgressBar;
    private JLabel percentageValue;
    private JButton wrongButton;
    private JButton correctButton;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JList<String> linkedIndexcardsList;
    private JLabel linkedListLabel;
    private JLabel sorryLabel;
    private JLabel boxNameLabel;
    private JLabel sortLabel;

    private final String selectedBox;
    private final String selectedSort;

    /**
     * This function is the basics to the logic from the LearnSystem and also the GUI from the LearnSystem.
     * @param controller The controller that is used to get the indexcards.
     * @param learnSystem The learnSystem that is used to get the indexcards.
     * @param indexcardBox The indexcardBox that is used to get the indexcards.
     * @param language The language that is used to set the language.
     * @param sort The selected sort.
     * @param box The selected Box.
     */
    public DisplayIndexcardToLearn(Controller controller, LeitnerLearnSystem learnSystem, IndexcardBox indexcardBox, Language language, String sort, String box) {
        this.learnSystem = learnSystem;
        this.controller = controller;
        this.language = language;
        this.selectedSort = sort;
        this.selectedBox = box;
        //learnSystem.setSortType(this.selectedSort);
        if (learnSystem.getSortType().isEmpty() && this.selectedBox.isEmpty()) {
            this.indexCardList2Learn = controller.getIndexcardsByIndexcardNameList(learnSystem.getNextIndexcardNames());
        }
        else {
            this.indexCardList2Learn = controller.getIndexcardsByIndexcardNameList(learnSystem.getIndexcardBox(selectedBox).getIndexcardNames());
            List<Indexcard> sortedIndexCardList = learnSort(indexCardList2Learn, sort);
            if(sortedIndexCardList != null){
                this.indexCardList2Learn = learnSort(this.indexCardList2Learn, sort);
            }

        }
        if (selectedSort == language.getName("random")) {
            Collections.shuffle(indexCardList2Learn);
        }
        // TODO sort the index card list following the selectedSort
        if (checkIndexCardList2Learn()) {
            JOptionPane.showMessageDialog(this, String.format(language.getName("boxEmpty"), selectedBox)
                    ,String.format(language.getName("boxEmptyMessage"),selectedBox), JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }
        this.indexcard = this.indexCardList2Learn.get(learnSystem.getProgress());
        this.learnProgressBar.setMinimum(0);
        this.learnProgressBar.setMaximum(this.indexCardList2Learn.size());
        setProgressDisplay();
        setLabels();
        hiddenButtons();
        setContentPane(contentPane);
        setTitle(language.getName("indexcard"));
        pack();
        setMinimumSize(getSize());
        setSize(800, 600);
        // Set Language
        //backButton.setText(language.getName("back"));
        nextButton.setText(language.getName("next"));
        correctButton.setText(language.getName("correct"));
        wrongButton.setText(language.getName("wrong"));
        answeredButton.setText(language.getName("answered"));
        linkedListLabel.setText(language.getName("linkedIndexcardsList"));

        controller.updateLearnsystem(learnSystem);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        //backButton.addActionListener(e -> onBack());
        nextButton.addActionListener(e -> onNext());
        answeredButton.addActionListener(e -> onAnswered(indexcard));

        correctButton.addActionListener(e -> onCorrect(indexcard));

        wrongButton.addActionListener(e -> onWrong(indexcard));

        linkedIndexcardsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (controller.getIndexcardByName(linkedIndexcardsList.getSelectedValue()).isPresent()) {
                        controller.displayIndexcard(controller.getIndexcardByName(linkedIndexcardsList.getSelectedValue()).get());
                    }
                }
            }
        });

        questionArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int offset = questionArea.viewToModel2D(e.getPoint());
                try {
                    int start = Utilities.getWordStart(questionArea, offset);
                    int end = Utilities.getWordEnd(questionArea, offset);
                    String word = questionArea.getText().substring(start, end);
                    onWordClicked(word);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        answerArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int offset = answerArea.viewToModel2D(e.getPoint());
                try {
                    int start = Utilities.getWordStart(answerArea, offset);
                    int end = Utilities.getWordEnd(answerArea, offset);
                    String word = answerArea.getText().substring(start, end);
                    onWordClicked(word);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Font font = new Font("Arial", Font.PLAIN, 20);
        answerArea.setFont(font);
        questionArea.setFont(font);
        sorryLabel.setVisible(false);
        highlightWords();
    }

    /**
     * Sets the labels.
     */
    private void setLabels() {
        this.nameLabel.setText(indexcard.getName());
        this.boxNameLabel.setText(selectedBox);
        this.sortLabel.setText(learnSystem.getSortType());
        this.questionArea.setText(indexcard.getQuestion());
        DefaultListModel<String> linkedListModel = new DefaultListModel<>();
        linkedListModel.addAll(indexcard.getLinks().stream().map(Link::getIndexcard).map(Indexcard::getName).toList());
        linkedIndexcardsList.setModel(linkedListModel);
    }

    private void onWordClicked(String word) {
        DefaultListModel<String> model = (DefaultListModel<String>) linkedIndexcardsList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            String item = model.getElementAt(i);
            if (controller.getIndexcardByName(item).isPresent()) {
                Indexcard linkedCard = controller.getIndexcardByName(item).get();
                for (Link link : indexcard.getLinks()) {
                    if (link.getTerm().equals(word) && link.getIndexcard().getId() == linkedCard.getId()) {
                        DisplayIndexcard displayIndexcard = new DisplayIndexcard(controller, linkedCard, language);
                        displayIndexcard.setSize(600, 400);
                        displayIndexcard.setMinimumSize(displayIndexcard.getSize());
                        displayIndexcard.setLocationRelativeTo(this);
                        displayIndexcard.setVisible(true);
                    }
                }
            }
        }
    }

    /**
     * This method is called when the user clicks the "Answered" button.
     * It shows the answer.
     */
    private void onAnswered(Indexcard indexcard) {
        answerArea.setText(indexcard.getAnswer());
        showButtons();
        setLabels();
        highlightWords();
    }

    /**
     * This method is called when the user clicks the "Next" button.
     * It shows the next indexcard.
     * If there is no next indexcard, it closes the window.
     */
    private void onNext() {
        hiddenButtons();
        if (learnSystem.getProgress() < this.indexCardList2Learn.size() - 1) {
            learnSystem.increaseProgress();
            indexcard = indexCardList2Learn.get(learnSystem.getProgress());
            questionArea.setText(indexcard.getQuestion());
            answerArea.setText("");
            setProgressDisplay();
        }
        else {
            // The user has learned all the indexcards in the box.
            learnSystem.setProgress(0);
            learnSystem.setStarted(false);
            JOptionPane.showMessageDialog(this, String.format(language.getName("boxEnded"), selectedBox)
                    ,String.format(language.getName("boxEndedMessage"),selectedBox), JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
        controller.updateLearnsystem(learnSystem);
        setLabels();
        sorryLabel.setVisible(false);
        this.answerArea.setText("");
        highlightWords();
    }

    /**
     * This method is called when the user clicks the "Back" button.
     * It shows the previous indexcard.
     */
    private void onBack() {
        hiddenButtons();
        if (learnSystem.getProgress() > 0) {
            learnSystem.decreaseProgress();
            indexcard = indexCardList2Learn.get(learnSystem.getProgress());
            questionArea.setText(indexcard.getQuestion());
            answerArea.setText("");
            setProgressDisplay();
        }

        controller.updateLearnsystem(learnSystem);
        setLabels();
        this.answerArea.setText("");
    }

    /**
     * Updates the progress and closes the window.
     */
    private void onCancel() {
        learnSystem.setStarted(true);
        controller.updateLearnsystem(learnSystem);
        dispose();
    }

    /**
     * Hide the "Correct"- and "Wrong"-Button.
     */
    private void hiddenButtons(){
        correctButton.setVisible(false);
        wrongButton.setVisible(false);
    }

    /**
     * Shows the "Correct"- and "Wrong"-Button.
     */
    private void showButtons(){
        correctButton.setVisible(true);
        wrongButton.setVisible(true);
    }

    /**
     * Gets executed if the user clicked the "Correct"-Button.
     *
     * @param indexcard The Indexcard that was answered correct.
     */
    private void onCorrect(Indexcard indexcard){
        learnSystem.correctAnswer(indexcard);
        controller.updateLearnsystem(learnSystem);
        onNext();
    }

    /**
     * Gets executed when the user clicked the "Wrong"-Button.
     *
     * @param indexcard The Indexcard that was answered wrong.
     */
    private void onWrong(Indexcard indexcard){
        learnSystem.wrongAnswer(indexcard);
        controller.updateLearnsystem(learnSystem);
        sorryLabel.setText(language.getName("sorryMessage"));
        sorryLabel.setVisible(true);

    }

    /**
     * This methode will get all the index card from the LearsSystem which are in the in a box.
     */
    private List<Indexcard> getIndexcardFromBox(LeitnerLearnSystem learnSystem, int boxNumber){
        return controller.getIndexcardsByIndexcardNameList(learnSystem.getIndexcardFromBox(boxNumber));
    }

    /**
     * This methode will set the progress bar to the right value.
     */
    public void setProgressDisplay(){
        this.learnProgressBar.setValue(learnSystem.getProgress());
        this.percentageValue.setText((learnSystem.getProgress()) * 100 / indexCardList2Learn.size() +"%");
    }

    /**
     * Checks if there exists Indexcards to learn.
     *
     * @return True, if there exists Indexcards to learn.
     */
    public boolean checkIndexCardList2Learn(){
        return indexCardList2Learn.isEmpty();
    }

    /**
     * Sort the Indexcards.
     *
     * @param indexcards The Indexcards that should be sorted.
     * @param method The method how the Indexcards should be sorted.
     * @return The sorted Indexcards.
     */
    public List<Indexcard> learnSort(List<Indexcard> indexcards ,String method){
        if (method == null){
            return null;
        }
        else if (method.equals(language.getName("alphabetical"))){
            Collections.sort(indexcards, Comparator.comparing(Indexcard::getName));
            return indexcards;
        } else if (method.equals(language.getName("random"))) {
            Collections.shuffle(indexcards);
            return indexcards;

        }
        return null;
    }
    /**
     * highlight all words in the questionArea and answerArea which are linked to another Indexcard.
     */
    private void highlightWords(){
        List<Link> links = indexcard.getLinks();
        Color babyBlue = new Color(173, 216, 230);
        for (Link link : links) {
            String word = link.getTerm();
            int index = 0;
            while (index >= 0) {
                index = questionArea.getText().indexOf(word, index);
                if (index >= 0) {
                    try {
                        questionArea.getHighlighter().addHighlight(index, index + word.length(), new DefaultHighlighter.DefaultHighlightPainter(babyBlue));
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    index += word.length();
                }
            }
            index = 0;
            while (index >= 0) {
                index = answerArea.getText().indexOf(word, index);
                if (index >= 0) {
                    try {
                        answerArea.getHighlighter().addHighlight(index, index + word.length(), new DefaultHighlighter.DefaultHighlightPainter(babyBlue));
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    index += word.length();
                }
            }
        }
    }

}