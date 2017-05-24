package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Appointment;
import com.app.tanyahukum.model.Consultations;

import java.util.List;

/**
 * Created by emerio on 4/19/17.
 */

public interface ListAppointmentActivityInterface  {
    interface View{
        void showAppointment(List<Appointment> appointments);
        void showAppointmentProgress(boolean show);
        void showEmptyMessage();
    }
    interface Presenter{
        void getAppointmentByUser(String userId,String type);
    }
}
