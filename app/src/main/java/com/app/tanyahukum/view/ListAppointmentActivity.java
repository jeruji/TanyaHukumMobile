package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.adapter.AppointmentAdapter;
import com.app.tanyahukum.adapter.QuestionsAdapter;
import com.app.tanyahukum.data.component.DaggerListAppointmentActivityComponent;
import com.app.tanyahukum.data.component.DaggerListConsultationActivityComponent;
import com.app.tanyahukum.data.module.ListAppointmnetActivityModule;
import com.app.tanyahukum.data.module.ListConsultationActivityModule;
import com.app.tanyahukum.model.Appointment;
import com.app.tanyahukum.presenter.ListAppointmentPresenter;
import com.app.tanyahukum.presenter.ListConsultationPresenter;
import com.app.tanyahukum.util.Config;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by emerio on 4/19/17.
 */

public class ListAppointmentActivity extends AppCompatActivity implements ListAppointmentActivityInterface.View,AppointmentAdapter.AppointmentAdapterCallback {

    @Inject
    ListAppointmentPresenter listAppointmentPresenter;
    @Inject
    AppointmentAdapter appointmentAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbarList;
    @BindView(R.id.recycler_view_appointment)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_no_appointment)
    TextView mTextNoAppointment;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    String userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_appointnment_layout);
        ButterKnife.bind(this);
        DaggerListAppointmentActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .listAppointmnetActivityModule(new ListAppointmnetActivityModule(this, this))
                .build().inject(this);
        setSupportActionBar(toolbarList);
        getSupportActionBar().setTitle("List Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        userid=App.getInstance().getPrefManager().getUserId();
        listAppointmentPresenter.getAppointmentByUser(userid);
        appointmentAdapter.setCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(appointmentAdapter);
    }
    @Override
    public void showAppointment(List<Appointment> appointments) {
        mTextNoAppointment.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        appointmentAdapter.setAppointment(appointments);
        appointmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAppointmentProgress(boolean show) {
        if (show && appointmentAdapter.getItemCount() == 0) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyMessage() {
        mTextNoAppointment.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onAppointmentClicked(Appointment appointment) {
        if (Config.USER_TYPE.equals("CONSULTANT")) {
            Intent intent = new Intent();
            intent.putExtra("clientId", appointment.getClientId());
            intent.putExtra("date", appointment.getDateAppointment());
            intent.putExtra("time",appointment.getTimeAppointment());
            intent.putExtra("detail", appointment.getDetailAppointment());
            intent.putExtra("consultantId", appointment.getConsultantId());
            intent.putExtra("questionsId", appointment.getQuestionsId());
            intent.putExtra("title", appointment.getTitle());
            intent.putExtra("topic", appointment.getTopic());
            intent.putExtra("status", appointment.getStatus());
            intent.putExtra("id",appointment.getAppointmentId());
            intent.putExtra("clientName",appointment.getClientName());
            intent.putExtra("consultantName",appointment.getConsultantName());
            intent.putExtra("id",appointment.getAppointmentId());
            intent.putExtra("bookingcode",appointment.getBookingCode());
            intent.putExtra("rate",appointment.getRating());
            intent.putExtra("report",appointment.getReport());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.AddAppointmentActivity");
            startActivity(intent);
        }else{
            Intent intent = new Intent();
            intent.putExtra("clientId", appointment.getClientId());
            intent.putExtra("questionsId", appointment.getQuestionsId());
            intent.putExtra("consultantId", appointment.getConsultantId());
            intent.putExtra("clientName",appointment.getClientName());
            intent.putExtra("consultantName",appointment.getConsultantName());
            intent.putExtra("consultantPhone",appointment.getConsultantPhone());
            intent.putExtra("consultantAddress",appointment.getConsultantAddress());
            intent.putExtra("date", appointment.getDateAppointment());
            intent.putExtra("time",appointment.getTimeAppointment());
            intent.putExtra("detail", appointment.getDetailAppointment());
            intent.putExtra("title", appointment.getTitle());
            intent.putExtra("topic", appointment.getTopic());
            intent.putExtra("status", appointment.getStatus());
            intent.putExtra("id",appointment.getAppointmentId());
            intent.putExtra("bookingcode",appointment.getBookingCode());
            intent.putExtra("rate",appointment.getRating());
            intent.putExtra("report",appointment.getReport());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.AddAppointmentActivity");
            startActivity(intent);
        }

    }
}
