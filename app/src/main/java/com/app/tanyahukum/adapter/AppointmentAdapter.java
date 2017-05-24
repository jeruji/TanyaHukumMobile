package com.app.tanyahukum.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tanyahukum.R;
import com.app.tanyahukum.model.Appointment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/9/17.
 */

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {
    private List<Appointment> listAppointment;
    private AppointmentAdapterCallback mCallback;
    @Inject
    public AppointmentAdapter() {
        listAppointment = new ArrayList<>();
    }
    public void setAppointment(List<Appointment> listQuestions) {
        this.listAppointment.addAll(listQuestions);
    }
    @Override
    public AppointmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_appointment,
                parent, false);
        return new AppointmentHolder(view);
    }

    @Override
    public void onBindViewHolder(AppointmentHolder holder, int position) {
        Appointment appointment = listAppointment.get(position);
        holder.setAppointment(appointment);
        holder.appointmentDateDate.setText(appointment.getDateAppointment());
        holder.titleAppointment.setText(appointment.getTitle());
        holder.statusAppointment.setText(appointment.getStatus());
    }
   private void simpleDateFormat(String date){

   }
    public void setCallback(AppointmentAdapterCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return listAppointment.size();
    }

    class AppointmentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.appointmentDate)
        TextView appointmentDateDate;
        @BindView(R.id.titleApointment)
        TextView titleAppointment;
        @BindView(R.id.statusAppointment)
        TextView statusAppointment;
        Appointment appointment;
        public AppointmentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setAppointment(Appointment appointment) {
            this.appointment = appointment;
        }
        @OnClick(R.id.row_layout)
        void onItemClicked(View view) {
            if (mCallback != null) mCallback.onAppointmentClicked(appointment);
        }
    }

    public static interface AppointmentAdapterCallback {
        public void onAppointmentClicked(Appointment appointment);
    }
}
