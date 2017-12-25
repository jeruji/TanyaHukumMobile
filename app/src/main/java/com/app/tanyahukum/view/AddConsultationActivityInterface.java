package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Consultations;

import java.util.ArrayList;

/**
 * Created by emerio on 4/9/17.
 */

public interface AddConsultationActivityInterface {
    interface View{
        void submit();
        void detailConsultationResult(boolean result,String consultationId);
        void showProgressDialog(boolean status);
        void infoDialog(String message);
        void constructConsultationType(ArrayList<String> consultationTypeList);
    }
    interface Presenter{
        void submitConsultation(Consultations consultations,String status,ArrayList<String> path);
        void answers(String questionId,String historyId,String clientId,String answers,ArrayList<String> path);
        void attachmentFileUpload(ArrayList<String> path);
        void getConsultationType();
    }
}
