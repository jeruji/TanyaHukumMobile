package com.app.tanyahukum.view;

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
        void checkNewAnswer();
        void appearNewNotification();
    }
    interface Presenter{
        void updateFirebaseToken(String token);
        void signOutUser();
        void queryNewAnswer(String userId);
    }
}
