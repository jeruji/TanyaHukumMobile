package com.app.tanyahukum.presenter;

import android.content.Context;
import android.util.Log;

import com.app.tanyahukum.model.Appointment;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.view.ListAppointmentActivityInterface;
import com.app.tanyahukum.view.ListConsultationInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by emerio on 4/19/17.
 */

public class ListAppointmentPresenter implements ListAppointmentActivityInterface.Presenter {

    FirebaseDatabase firebase;
    ListAppointmentActivityInterface.View mView;
    FirebaseAuth firebaseAuth;
    DatabaseReference appointmentRef;
    Context context;

    @Inject
    public ListAppointmentPresenter(FirebaseDatabase firebase, ListAppointmentActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.mView = view;
        firebaseAuth = FirebaseAuth.getInstance();
        appointmentRef = this.firebase.getReference("appointment");
        this.context = context;

    }
    @Override
    public void getAppointmentByUser(String userId, final String type) {
        mView.showAppointmentProgress(true);
        Query userQuery;
        if(Config.USER_TYPE.equalsIgnoreCase("CLIENT")){
            userQuery = appointmentRef.orderByChild("clientId").equalTo(userId);
        }else{
            userQuery = appointmentRef.orderByChild("consultantId").equalTo(userId);
        }
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("data consult : ", dataSnapshot.toString());
                List<Appointment> appointmentList=new ArrayList<Appointment>();
                Appointment appointment=null;
                if (type.equalsIgnoreCase("HISTORY")) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        appointment = singleSnapshot.getValue(Appointment.class);
                        if (appointment.getStatus().equalsIgnoreCase("Done")){
                            appointmentList.add(appointment);
                        }
                    }
                }else{
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    appointment = singleSnapshot.getValue(Appointment.class);
                    if (!appointment.getStatus().equalsIgnoreCase("Done")){
                        appointmentList.add(appointment);
                    }
                }

                }
                if (appointmentList.size()>0) {
                    mView.showAppointmentProgress(false);
                    mView.showAppointment(appointmentList);
                }else {
                    mView.showAppointmentProgress(false);
                    mView.showEmptyMessage();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
