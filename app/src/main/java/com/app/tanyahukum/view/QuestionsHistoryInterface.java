package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;

import java.util.List;

/**
 * Created by emerio on 4/18/17.
 */

public interface QuestionsHistoryInterface {
    interface View{
        void showQuestions(List<HistoryConsultations> questions);
        void showQuestionsProgress(boolean show);
        void showEmptyMessage();
    }
    interface Presenter{
        void getHistoryQuestions(String questionsId);
    }

}
