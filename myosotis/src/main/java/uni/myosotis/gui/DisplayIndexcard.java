package uni.myosotis.gui;

import uni.myosotis.controller.Controller;
import uni.myosotis.objects.Indexcard;
import uni.myosotis.objects.Link;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import java.util.List;
import java.util.Objects;

public class DisplayIndexcard extends JFrame {

    /**
     * The controller.
     */
    private final Controller controller;

    private final Indexcard indexcard;
    private JPanel contentPane;

    private final Language language;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JList<String> linkedIndexcardsList;
    private JLabel linkedListLabel;

    /**
     * Creates a new Window to display an Indexcard.
     *
     * @param controller The Controller.
     * @param indexcard The Indexcard that should be displayed.
     * @param language The selected language.
     */
    public DisplayIndexcard(Controller controller, Indexcard indexcard, Language language) {
        this.controller = controller;
        this.indexcard = indexcard;
        this.language = language;
        setTitle(indexcard.getName());
        setContentPane(contentPane);
        linkedListLabel.setText(language.getName("linkedIndexcardsList"));
        Font font = new Font("Arial", Font.PLAIN, 20);
        questionArea.setFont(font);
        answerArea.setFont(font);
        this.questionArea.setText(indexcard.getQuestion());

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
        this.answerArea.setText(indexcard.getAnswer());

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
        // Set Model
        DefaultListModel<String> linkedListModel = new DefaultListModel<>();
        linkedListModel.addAll(indexcard.getLinks().stream().map(Link::getIndexcard).map(Indexcard::getName).toList());
        linkedIndexcardsList.setModel(linkedListModel);

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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        highlightWords();
    }

    private void onWordClicked(String word) {
        DefaultListModel<String> model = (DefaultListModel<String>) linkedIndexcardsList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            String item = model.getElementAt(i);
            if (controller.getIndexcardByName(item).isPresent()) {
                Indexcard linkedCard = controller.getIndexcardByName(item).get();
                for (Link link : indexcard.getLinks()) {
                    if (link.getTerm().equals(word) && Objects.equals(link.getIndexcard().getId(), linkedCard.getId())) {
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
     * highlight all words which are linked to another Indexcard.
     */
private void highlightWords(){
    List<Link> links = indexcard.getLinks();
    Color babyBlue = new Color(173, 216, 230);
    for (Link link : links) {
        String term = link.getTerm();
        int index = 0;
        while (index >= 0) {
            index = questionArea.getText().indexOf(term, index);
            if (index >= 0) {
                try {
                    questionArea.getHighlighter().addHighlight(index, index + term.length(), new DefaultHighlighter.DefaultHighlightPainter(babyBlue));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                index += term.length();
            }
        }
        index = 0;
        while (index >= 0) {
            index = answerArea.getText().indexOf(term, index);
            if (index >= 0) {
                try {
                    answerArea.getHighlighter().addHighlight(index, index + term.length(), new DefaultHighlighter.DefaultHighlightPainter(babyBlue));
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                index += term.length();
            }
        }
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