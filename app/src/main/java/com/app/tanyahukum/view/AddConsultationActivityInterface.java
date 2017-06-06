package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Consultations;

/**
 * Created by emerio on 4/9/17.
 */

public interface AddConsultationActivityInterface {
    interface View{
        void submit();
        void detailConsultationResult(boolean result,String consultationId);
        void toastShedule(String msg);
        void showProgressDialog(boolean status);
        void infoDialog(String message);
    }
    interface Presenter{
        void submitConsultation(Consultations consultations,String status,String path);
        void answers(String questionId,String historyId,String clientId,String answers,String path);
        void getConsultant(String usertype,String specialization);
        void attachmentFileUpload(String path);
    }
}
