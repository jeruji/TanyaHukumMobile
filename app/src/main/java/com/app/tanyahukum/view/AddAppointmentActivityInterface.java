package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Appointment;
import com.app.tanyahukum.model.Consultations;

/**
 * Created by emerio on 4/19/17.
 */

public interface AddAppointmentActivityInterface {
    interface View{
        void submit();
        void detailAppointmentResult(boolean result);
        void approve();
        void reschedule();
        void propose();
        void done();
        void rate();
        void clear();
        void toAppointmentList(boolean result,String type);
        void showData(Appointment appointment);
        void sendReport();
        void rateAppointment();
    }
    interface Presenter{
        void submitAppointment(Appointment appointment, String status);
        void proposeAppointment(String appointmetId,String clientId,String consultantId,String date,String appointment);
        void approveAppointment(String appointmentId,String clientId,String consultantId);
        void doneAppointment(String appointmentId,String questionsId,String clientId, String consultantId);
        void rateConsultant(String appointmentId,String rate,String report);
        void reportAppointment(String appointmentId,String report);
        void getAppointmentById(String appointment);
    }
}
