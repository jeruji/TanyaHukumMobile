package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Consultations;

import java.util.List;

/**
 * Created by emerio on 4/9/17.
 */

public interface ListConsultationInterface {
    interface View{
        void toConsultationForm();
        void showQuestions(List<Consultations> questions);
        void showQuestionsProgress(boolean show);
        void showEmptyMessage();
    }
    interface Presenter{
      void getQuestionsByUser(String userId);
    }
}
