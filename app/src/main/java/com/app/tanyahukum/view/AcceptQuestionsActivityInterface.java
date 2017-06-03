package com.app.tanyahukum.view;

/**
 * Created by emerio on 4/12/17.
 */

public interface AcceptQuestionsActivityInterface {

    interface View{
        void accept();
        void toDashboard();
        void infoDialog();
    }
    interface Presenter{
        void acceptQuestions();
        void updateQuestions(String questionsId);
    }
}
