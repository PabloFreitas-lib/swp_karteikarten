package uni.myosotis;

import uni.myosotis.controller.Controller;
import uni.myosotis.logic.*;

/**
 * The main class of the application
 */
public class App {
    public static void main(String[] args) {

        /* Start the application */
        final KeywordLogic keywordLogic = new KeywordLogic();
        final IndexcardLogic indexcardLogic = new IndexcardLogic();
        final CategoryLogic categoryLogic = new CategoryLogic();
        final IndexcardBoxLogic indexcardBoxLogic = new IndexcardBoxLogic();
        final LeitnerLearnSystemLogic leitnerLearnSystemLogic = new LeitnerLearnSystemLogic();
        final LinkLogic linkLogic = new LinkLogic();
        final Controller controller = new Controller(indexcardLogic, keywordLogic, linkLogic, categoryLogic, indexcardBoxLogic, leitnerLearnSystemLogic);
        controller.startApplication();
    }
}


