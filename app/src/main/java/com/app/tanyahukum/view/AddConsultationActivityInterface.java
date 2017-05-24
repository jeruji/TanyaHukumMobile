package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Consultations;

/**
 * Created by emerio on 4/9/17.
 */

public interface AddConsultationActivityInterface {
    interface View{
        void submit();
        void detailConsultationResult(boolean result);
        void toastShedule(String msg);
    }
    interface Presenter{
        void submitConsultation(Consultations consultations,String status);
        void answers(String questionId,String historyId,String clientId,String answers);
        void getConsultant(String usertype,String specialization);
    }
}
