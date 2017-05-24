package com.app.tanyahukum.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAcceptQuestionsActivityComponent;
import com.app.tanyahukum.data.component.DaggerAddAppointmentActivityComponent;
import com.app.tanyahukum.data.module.AcceptQuestionsActivityModule;
import com.app.tanyahukum.data.module.AddAppointmentActivityModule;
import com.app.tanyahukum.model.Appointment;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.presenter.AddAppointmentPresenter;
import com.app.tanyahukum.presenter.QuestionDetailPresenter;
import com.app.tanyahukum.util.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/19/17.
 */

public class AddAppointmentActivity extends AppCompatActivity implements AddAppointmentActivityInterface.View{
    @BindView(R.id.edTextDateAppointment)
    EditText dateAppointment;
    @BindView(R.id.edTextBookingCode)
    EditText bookingCode;
    @BindView(R.id.edTextDetail)
    EditText detailAppointment;
    @BindView(R.id.edTextTitle)
    EditText titleAppointment;
    @BindView(R.id.spinnerTopic)
    Spinner mSpinnerTopic;
    @BindView(R.id.layoutButtonApprove)
    LinearLayout _layoutButtonApprove;
    @BindView(R.id.layoutButton)
    LinearLayout _layoutButtonMakeAppointment;
    @BindView(R.id.layoutReschedule)
    RelativeLayout _layoutReschedule;
    @BindView(R.id.layoutRescheduleTime)
    RelativeLayout _layoutRescheduleTIme;
    @BindView(R.id.layoutStatusAppointment)
    RelativeLayout _layoutStatusAppointment;
    @BindView(R.id.layoutButtonPropose)
    LinearLayout _layoutPropose;
    @BindView(R.id.edTextDateNewAppointment)
    EditText dateNewAppointment;
    @BindView(R.id.edTextStatusAppointment)
    EditText _statusAppointment;
    @BindView(R.id.edTextUser)
    EditText _user;
    @BindView(R.id.edTextPhoneUser)
    EditText _phoneUser;
    @BindView(R.id.edTextAddressUser)
    EditText _AddressUser;
    @BindView(R.id.labelUser)
    TextView labelUser;
    @BindView(R.id.labelPhoneUser)
    TextView labelPhoneUser;
    @BindView(R.id.labelAddressUser)
    TextView labelAddressUser;
    @BindView(R.id.tvDone)
    TextView labelDone;
    @BindView(R.id.layoutUser)
    RelativeLayout layoutUser;
    @BindView(R.id.layoutPhoneUser)
    RelativeLayout layoutPhoneUser;
    @BindView(R.id.layoutAddressUser)
    RelativeLayout layoutAddressUser;
    @BindView(R.id.layoutButtonDone)
    LinearLayout _layoutButtonDone;
    @BindView(R.id.calenderAppointmnetNew)
    ImageView calenderNew;
    @BindView(R.id.timeAppointmnetNew)
    ImageView selectTimeNew;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edTextTimeAppointment)
    EditText timeAppointment;
    @BindView(R.id.edTextTImeNewAppointment)
    EditText timeAppointmentNew;
    @BindView(R.id.timeAppointment)
    ImageView selectTimeAppointment;
    @Inject
    AddAppointmentPresenter addAppointmentPresenter;
    Appointment appointments;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.add_appointment_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerAddAppointmentActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .addAppointmentActivityModule((new AddAppointmentActivityModule(this,this)))
                .build().inject(this);
        Intent intent=getIntent();
         appointments=new Appointment();
        String status=intent.getStringExtra("status");
        appointments.setAppointmentId(intent.getStringExtra("id"));
        appointments.setClientId(intent.getStringExtra("clientId"));
        appointments.setConsultantId(intent.getStringExtra("consultantId"));
        appointments.setQuestionsId(intent.getStringExtra("questionsId"));
        if (Config.USER_TYPE.equals("CONSULTANT")){
            _layoutButtonApprove.setVisibility(View.VISIBLE);
            _layoutButtonMakeAppointment.setVisibility(View.GONE);
            _layoutStatusAppointment.setVisibility(View.VISIBLE);
            layoutUser.setVisibility(View.VISIBLE);
            labelUser.setText("Client");
            _user.setText(intent.getStringExtra("clientName"));
            _user.setEnabled(false);
            _user.setTextColor(Color.parseColor("#000000"));
            mSpinnerTopic.setEnabled(false);
            mSpinnerTopic.setPrompt(intent.getStringExtra("topic"));
            dateAppointment.setText(intent.getStringExtra("date"));
            dateAppointment.setEnabled(false);
            dateAppointment.setTextColor(Color.parseColor("#000000"));
            timeAppointment.setText(intent.getStringExtra("time"));
            timeAppointment.setEnabled(false);
            timeAppointment.setTextColor(Color.parseColor("#000000"));
            detailAppointment.setText(intent.getStringExtra("detail"));
            detailAppointment.setEnabled(false);
            detailAppointment.setTextColor(Color.parseColor("#000000"));
            titleAppointment.setText(intent.getStringExtra("title"));
            titleAppointment.setEnabled(false);
            titleAppointment.setTextColor(Color.parseColor("#000000"));
            bookingCode.setText(intent.getStringExtra("bookingcode"));
            bookingCode.setEnabled(false);
            bookingCode.setTextColor(Color.parseColor("#000000"));
            _statusAppointment.setText(intent.getStringExtra("status"));
            _statusAppointment.setEnabled(false);
            _statusAppointment.setTextColor(Color.parseColor("#000000"));
            if (status.equals("Accepted")){
                _layoutButtonApprove.setVisibility(View.GONE);
                _layoutButtonDone.setVisibility(View.VISIBLE);
                labelDone.setVisibility(View.VISIBLE);
            }
            else if(status.equals("RESCHEDULE")){
                _layoutButtonApprove.setVisibility(View.GONE);
            }
            else if(status.equals("Done")){
                _layoutButtonApprove.setVisibility(View.GONE);
            }
        }else if(Config.USER_TYPE.equals("CLIENT")&&status.equalsIgnoreCase("Accepted")){
            _layoutStatusAppointment.setVisibility(View.VISIBLE);
            _layoutButtonApprove.setVisibility(View.GONE);
            _layoutButtonMakeAppointment.setVisibility(View.GONE);
            layoutUser.setVisibility(View.VISIBLE);
            labelUser.setText("Consultant");
            layoutAddressUser.setVisibility(View.VISIBLE);
            labelAddressUser.setText("Address");
            layoutPhoneUser.setVisibility(View.VISIBLE);
            labelPhoneUser.setText("Phone");
            _user.setText(intent.getStringExtra("consultantName"));
            _user.setEnabled(false);
            _user.setTextColor(Color.parseColor("#000000"));
            _AddressUser.setText(intent.getStringExtra("consultantAddress"));
            _AddressUser.setEnabled(false);
            _AddressUser.setTextColor(Color.parseColor("#000000"));
            _phoneUser.setText(intent.getStringExtra("consultantPhone"));
            _phoneUser.setEnabled(false);
            _phoneUser.setTextColor(Color.parseColor("#000000"));
            dateAppointment.setText(intent.getStringExtra("date"));
            dateAppointment.setEnabled(false);
            dateAppointment.setTextColor(Color.parseColor("#000000"));
            timeAppointment.setText(intent.getStringExtra("time"));
            timeAppointment.setEnabled(false);
            timeAppointment.setTextColor(Color.parseColor("#000000"));
            detailAppointment.setText(intent.getStringExtra("detail"));
            detailAppointment.setEnabled(false);
            detailAppointment.setTextColor(Color.parseColor("#000000"));
            titleAppointment.setText(intent.getStringExtra("title"));
            titleAppointment.setEnabled(false);
            titleAppointment.setTextColor(Color.parseColor("#000000"));
            bookingCode.setText(intent.getStringExtra("bookingcode"));
            bookingCode.setEnabled(false);
            bookingCode.setTextColor(Color.parseColor("#000000"));
            _statusAppointment.setText(intent.getStringExtra("status"));
            _statusAppointment.setEnabled(false);
            _statusAppointment.setTextColor(Color.parseColor("#000000"));
        }else if(Config.USER_TYPE.equals("CLIENT")&&status.equalsIgnoreCase("Pending")){
            _layoutStatusAppointment.setVisibility(View.VISIBLE);
            _layoutButtonApprove.setVisibility(View.GONE);
            _layoutButtonMakeAppointment.setVisibility(View.GONE);
            dateAppointment.setText(intent.getStringExtra("date"));
            dateAppointment.setEnabled(false);
            dateAppointment.setTextColor(Color.parseColor("#000000"));
            timeAppointment.setText(intent.getStringExtra("time"));
            timeAppointment.setEnabled(false);
            timeAppointment.setTextColor(Color.parseColor("#000000"));
            detailAppointment.setText(intent.getStringExtra("detail"));
            detailAppointment.setEnabled(false);
            detailAppointment.setTextColor(Color.parseColor("#000000"));
            titleAppointment.setText(intent.getStringExtra("title"));
            titleAppointment.setEnabled(false);
            titleAppointment.setTextColor(Color.parseColor("#000000"));
            bookingCode.setText(intent.getStringExtra("bookingcode"));
            bookingCode.setEnabled(false);
            bookingCode.setTextColor(Color.parseColor("#000000"));
            _statusAppointment.setText(intent.getStringExtra("status"));
            _statusAppointment.setEnabled(false);
            _statusAppointment.setTextColor(Color.parseColor("#000000"));
        }else if(Config.USER_TYPE.equals("CLIENT")&&status.equalsIgnoreCase("Reschedule")){
            _layoutStatusAppointment.setVisibility(View.VISIBLE);
            _layoutButtonApprove.setVisibility(View.VISIBLE);
            _layoutButtonMakeAppointment.setVisibility(View.GONE);
            layoutUser.setVisibility(View.VISIBLE);
            labelUser.setText("Consultant");
            _user.setText(intent.getStringExtra("consultantName"));
            _user.setEnabled(false);
            _user.setTextColor(Color.parseColor("#000000"));
            dateAppointment.setText(intent.getStringExtra("date"));
            dateAppointment.setEnabled(false);
            dateAppointment.setTextColor(Color.parseColor("#000000"));
            timeAppointment.setText(intent.getStringExtra("time"));
            timeAppointment.setEnabled(false);
            timeAppointment.setTextColor(Color.parseColor("#000000"));
            detailAppointment.setText(intent.getStringExtra("detail"));
            detailAppointment.setEnabled(false);
            detailAppointment.setTextColor(Color.parseColor("#000000"));
            titleAppointment.setText(intent.getStringExtra("title"));
            titleAppointment.setEnabled(false);
            titleAppointment.setTextColor(Color.parseColor("#000000"));
            bookingCode.setText(intent.getStringExtra("bookingcode"));
            bookingCode.setEnabled(false);
            bookingCode.setTextColor(Color.parseColor("#000000"));
            _statusAppointment.setText(intent.getStringExtra("status"));
            _statusAppointment.setEnabled(false);
            _statusAppointment.setTextColor(Color.parseColor("#000000"));
        }
        else if(Config.USER_TYPE.equals("CLIENT")&&status.equalsIgnoreCase("Done")){
            _layoutStatusAppointment.setVisibility(View.VISIBLE);
            _layoutButtonApprove.setVisibility(View.GONE);
            _layoutButtonMakeAppointment.setVisibility(View.GONE);
            layoutUser.setVisibility(View.VISIBLE);
            labelUser.setText("Consultant");
            _user.setText(intent.getStringExtra("consultantName"));
            _user.setEnabled(false);
            _user.setTextColor(Color.parseColor("#000000"));
            dateAppointment.setText(intent.getStringExtra("date"));
            dateAppointment.setEnabled(false);
            dateAppointment.setTextColor(Color.parseColor("#000000"));
            timeAppointment.setText(intent.getStringExtra("time"));
            timeAppointment.setEnabled(false);
            timeAppointment.setTextColor(Color.parseColor("#000000"));
            detailAppointment.setText(intent.getStringExtra("detail"));
            detailAppointment.setEnabled(false);
            detailAppointment.setTextColor(Color.parseColor("#000000"));
            titleAppointment.setText(intent.getStringExtra("title"));
            titleAppointment.setEnabled(false);
            titleAppointment.setTextColor(Color.parseColor("#000000"));
            bookingCode.setText(intent.getStringExtra("bookingcode"));
            bookingCode.setEnabled(false);
            bookingCode.setTextColor(Color.parseColor("#000000"));
            _statusAppointment.setText(intent.getStringExtra("status"));
            _statusAppointment.setEnabled(false);
            _statusAppointment.setTextColor(Color.parseColor("#000000"));
        }else{
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            bookingCode.setText("APP"+ts);
            bookingCode.setEnabled(false);
            bookingCode.setTextColor(Color.parseColor("#000000"));
        }

        selectTimeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        selectTimeNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeNew();
            }
        });
    }
    @OnClick(R.id.calenderAppointmnet)
    public void showCalendar() {
        calendarDialog(99).show();
    }
    @OnClick(R.id.calenderAppointmnetNew)
    public void showCalendarNew() {
        calendarDialog(88).show();
    }
    private Dialog calendarDialog(int id) {
        if (id == 99) {
            int[] dateArr = addAppointmentPresenter.getDate();
            return new DatePickerDialog(this, dateListener, dateArr[0], dateArr[1], dateArr[2]);
        }else if(id==88){
            int[] dateArr = addAppointmentPresenter.getDate();
            return new DatePickerDialog(this, dateListenerNew, dateArr[0], dateArr[1], dateArr[2]);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener dateListenerNew = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDateNew(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateAppointment.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }
    private void showDateNew(int year, int month, int day) {
        dateNewAppointment.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

     void setTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeAppointment.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    void setTimeNew(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeAppointmentNew.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    @Override
    @OnClick(R.id.btnMakeAppointment)
    public void submit() {
        Intent intent=getIntent();
        Appointment appointment=new Appointment();
        appointment.setClientId(intent.getStringExtra("clientId"));
        appointment.setClientName(App.getInstance().getPrefManager().getUser().getName());
        appointment.setClientPhone(App.getInstance().getPrefManager().getUser().getPhone());
        appointment.setConsultantName("");
        appointment.setConsultantAddress("");
        appointment.setConsultantPhone("");
        appointment.setConsultantId(intent.getStringExtra("consultantId"));
        appointment.setQuestionsId(intent.getStringExtra("consultationId"));
        appointment.setDateAppointment(dateAppointment.getText().toString());
        appointment.setDetailAppointment(detailAppointment.getText().toString());
        appointment.setTopic(mSpinnerTopic.getSelectedItem().toString());
        appointment.setTitle(titleAppointment.getText().toString());
        appointment.setBookingCode(bookingCode.getText().toString());
        appointment.setTimeAppointment(timeAppointment.getText().toString());
        appointment.setStatus("PENDING");
        addAppointmentPresenter.submitAppointment(appointment,"PENDING");
    }

    @Override
    public void detailAppointmentResult(boolean result) {
        if (result){
            Toast.makeText(getApplicationContext(),"appointment created",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.DashboardActivity");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"failed create consultations",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    @OnClick(R.id.btnApproveAppointment)
    public void approve() {
      addAppointmentPresenter.approveAppointment(appointments.getAppointmentId(),appointments.getClientId(),appointments.getConsultantId());
    }

    @Override
    @OnClick(R.id.btnRescheduleAppointment)
    public void reschedule() {
        _layoutReschedule.setVisibility(View.VISIBLE);
        _layoutRescheduleTIme.setVisibility(View.VISIBLE);
        _layoutPropose.setVisibility(View.VISIBLE);
        calenderNew.setVisibility(View.VISIBLE);
        selectTimeNew.setVisibility(View.VISIBLE);

    }

    @Override
    @OnClick(R.id.btnPropose)
    public void propose() {
        addAppointmentPresenter.proposeAppointment(appointments.getAppointmentId(),appointments.getClientId(),appointments.getConsultantId(),dateNewAppointment.getText().toString());

    }

    @Override
    @OnClick(R.id.btnDone)
    public void done() {
        addAppointmentPresenter.doneAppointment(appointments.getAppointmentId(),appointments.getQuestionsId(),appointments.getClientId(),appointments.getConsultantId());
    }

    @Override
    public void rate() {

    }

    @Override
    @OnClick(R.id.btnClear)
    public void clear() {
        dateAppointment.setText("");
        timeAppointment.setText("");
        detailAppointment.setText("");
        titleAppointment.setText("");
    }

    @Override
    public void toAppointmentList(boolean result,String type) {
        if (result) {
            Intent intent = new Intent();
            intent.putExtra("type",type);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.ListAppointmentActivity");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"update failed",Toast.LENGTH_LONG).show();
        }
    }

}
