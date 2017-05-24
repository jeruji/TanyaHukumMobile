package com.app.tanyahukum.view;

import android.content.Intent;

/**
 * Created by emerio on 4/9/17.
 */

public interface DashboardActivityInterface {
    interface View{
        void showProfile();
        void toConsultationPage();
        void toFormConsultation();
        void toLoginPage();
        void toAppointmentListPage(String type);
        void toMyAccountPage();
        void showImage(String url);
        void checkUserTypeLogin();
    }
    interface Presenter{
        void showProfile();
        void updateFirebaseToken(String token);
        void signOutUser();
    }
}
