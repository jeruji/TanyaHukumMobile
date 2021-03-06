package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;

import java.util.List;

/**
 * Created by emerio on 4/9/17.
 */

public interface QuestionsDetailActivityInterface {
    interface View{
        void toAnswersPage();
        void toHistoryPage();
        void toNextQuestionsPage();
        void checkConsultation(List<HistoryConsultations> questions);
        void makeAppointment();
        void showImage(String url);
        void showData(Consultations consultations);
        void openFileAttachment(String filepath);
        void showProgressDialog(boolean status);
        void infoDialog(String message);
    }
    interface Presenter{
       void getHistoryConsultation(String questionsId);
       void getConsultationDetailById(String consultationId);
       void downloadAttachment(String filename);
    }
}
