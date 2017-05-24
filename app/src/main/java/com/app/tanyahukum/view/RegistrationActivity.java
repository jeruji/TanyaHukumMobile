package com.app.tanyahukum.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerRegistrationActivityComponent;
import com.app.tanyahukum.data.module.RegistrationActivityModule;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.presenter.RegistrationPresenter;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.util.MultiSpinner;
import com.app.tanyahukum.util.MultiSpinnerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/8/17.
 */

public class RegistrationActivity extends AppCompatActivity implements RegistrationActivityInterface.View,MultiSpinnerListener{
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.titleProgress)
    TextView titleProgressBar;
    @BindView(R.id.edTextName)
    EditText name;
    @BindView(R.id.spinnerGender)
    Spinner gender;
    @BindView(R.id.edTextBornDate)
    EditText bornDate;
    @BindView(R.id.edTextEmail)
    EditText email;
    @BindView(R.id.edTextPassword)
    EditText password;
    @BindView(R.id.edTextProvince)
    EditText province;
    @BindView(R.id.edTextCity)
    EditText city;
    @BindView(R.id.edTextAddress)
    EditText address;
    @BindView(R.id.edTextPhone)
    EditText phone;
    @BindView(R.id.spinnerUserType)
    Spinner userType;
    @BindView(R.id.SpinnerSpecialization)
    MultiSpinner specializationSpinner;
    @BindView(R.id.formConsultant)
    RelativeLayout layoutFormConsultant;
    @BindView(R.id.edTextLawFirm)
    EditText lawFirm;
    @BindView(R.id.edTextLawFirmCity)
    EditText lawFirmCity;
    @BindView(R.id.edTextLawFirmAddress)
    EditText lawFirmAddress;
    @BindView(R.id.edTextLawFirmPhone)
    EditText lawFirmPhone;
    @BindView(R.id.visibility)
    ImageView _showPassword;
    @BindView(R.id.invisibility)
    ImageView _hidePassword;
    @Inject
    RegistrationPresenter registrationPresenter;
    ArrayList<String> selectedSpecialization;
    String loginType="";
    String userId="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        ButterKnife.bind(this);
        DaggerRegistrationActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .registrationActivityModule(new RegistrationActivityModule(this, this))
                .build().inject(this);

        Intent i=getIntent();
        loginType=i.getStringExtra("loginType");
        userId=i.getStringExtra("userId");
            if (loginType.equals("FACEBOOK") || loginType.equals("GMAIL")) {
                String nameStr = i.getStringExtra("name");
                String emailStr = i.getStringExtra("email");
                name.setText(nameStr);
                email.setText(emailStr);
            }

        List<String> specialization = Arrays.asList(getResources().getStringArray(R.array.specialization));
        specializationSpinner.setItems(specialization, "Select Specialization", this);
        selectedSpecialization = new ArrayList<>();
        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String user=userType.getSelectedItem().toString();
                if (user.equals("Client")){
                    layoutFormConsultant.setVisibility(View.GONE);
                }else if(user.equals("Consultant")){
                    layoutFormConsultant.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.imgBornDate)
    public void showCalendar() {
        calendarDialog(99).show();
    }
    private Dialog calendarDialog(int id) {
        if (id == 99) {
            int[] dateArr = registrationPresenter.getDate();
            return new DatePickerDialog(this, dateListener, dateArr[0], dateArr[1], dateArr[2]);
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

    private void showDate(int year, int month, int day) {
        bornDate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }
    @Override
    @OnClick(R.id.btnSignUp)
    public void submit() {
        String nameStr=name.getText().toString();
        String genderStr = gender.getSelectedItem().toString();
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        String bornDateStr= bornDate.getText().toString();
        String provinceStr=province.getText().toString();
        String cityStr=city.getText().toString();
        String addressStr=address.getText().toString();
        String phoneStr=phone.getText().toString();
        String userTypeStr=userType.getSelectedItem().toString();
        int statusValidation=registrationPresenter.checkValidation( nameStr,  genderStr,  bornDateStr,  emailStr,  passwordStr,  provinceStr,  cityStr,  addressStr,  phoneStr,  userTypeStr);
        if(statusValidation==1) {
            name.setError("Name is invalid, minimal 3 characters");
        }
        else
        if(statusValidation==3) {
            TextView errorText = (TextView)gender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please choose your gender");
        }
        else
        if(statusValidation==4) {
            email.setError("Invalid email address");
        }
        else
        if(statusValidation==5) {
            password.setError("Password length must be 8 or more");
        }
        else
        if(statusValidation==6) {
            phone.setError("phone number is not empty");
        }
        else
        registrationPresenter.submitRegistration(loginType,userId,nameStr,genderStr,bornDateStr,emailStr,passwordStr, provinceStr, cityStr,addressStr,phoneStr,userTypeStr);
    }

    @Override
    @OnClick(R.id.btnClear)
    public void clear() {
        name.setText("");
        email.setText("");
        address.setText("");
        password.setText("");
        bornDate.setText("");
        province.setText("");
        city.setText("");
        lawFirm.setText("");
        lawFirmCity.setText("");
        lawFirmAddress.setText("");
        lawFirmPhone.setText("");
        phone.setText("");
    }


    @Override
    public void registrationResult(String result, boolean status) {
        if(status) {
            User user = new User();
            user.setBornDate(bornDate.getText().toString());
            user.setEmail(email.getText().toString());
            user.setName(name.getText().toString());
            user.setProvince(province.getText().toString());
            user.setCity(city.getText().toString());
            user.setPassword(password.getText().toString());
            user.setAddress(address.getText().toString());
            user.setPhone(phone.getText().toString());
            user.setFirebaseToken("");

            String genderStr = gender.getSelectedItem().toString();
            if (genderStr.equals("Male"))
                user.setGender("MALE");
            else if (genderStr.equals("Female"))
                user.setGender("FEMALE");
            else
                user.setGender("TRANSGENDER");
            String userStr = userType.getSelectedItem().toString();
            if (userStr.equals("Client")) {
                user.setUsertype("CLIENT");
            }
            else if (userStr.equals("Consultant")) {
                user.setUsertype("CONSULTANT");
                user.setLawFirm(lawFirm.getText().toString());
                user.setLawFirmAddress(lawFirmAddress.getText().toString());
                user.setLawFirmCity(lawFirmCity.getText().toString());
                user.setLawFirmPhone(lawFirmPhone.getText().toString());
                user.setSpecialization(selectedSpecialization);
            }

            if (loginType.equals("FACEBOOK")||loginType.equals("GMAIL")){
                user.setId(userId);
                App.getInstance().getPrefManager().storeUser(user);
                registrationPresenter.submitUserDetail(user);
            }else {
                user.setId(result);
                registrationPresenter.submitUserDetail(user);
            }

        }
        else
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();

    }

    @Override
    public void detailRegistrationResult(boolean status) {
        if(status){
            if (loginType.equals("FACEBOOK")||loginType.equals("GMAIL")){
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClassName(this, "com.app.tanyahukum.view.DashboardActivity");
                startActivity(intent);
            }else {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClassName(this, "com.app.tanyahukum.view.LoginActivity");
                startActivity(intent);
            }
        }
        else
            Toast.makeText(this, "false", Toast.LENGTH_LONG).show();


    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
        titleProgressBar.setVisibility(View.VISIBLE);
        titleProgressBar.setText("registering account..");

    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        titleProgressBar.setVisibility(View.GONE);

    }

    @Override
    @OnClick(R.id.visibility)
    public void showPassword() {
        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        _showPassword.setVisibility(View.GONE);
        _hidePassword.setVisibility(View.VISIBLE);
    }

    @Override
    @OnClick(R.id.invisibility)
    public void hidePassword() {
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        _showPassword.setVisibility(View.VISIBLE);
        _hidePassword.setVisibility(View.GONE);

    }

    @Override
    @OnClick(R.id.btnLoginPage)
    public void toLoginPage() {
        Intent intent = new Intent();
        intent.setClassName(this, "com.app.tanyahukum.view.LoginActivity");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        List<String> specialization = Arrays.asList(getResources().getStringArray(R.array.specialization));

        for(int x=0; x<selected.length;x++)
        {
            if(selected[x])
                selectedSpecialization.add(specialization.get(x));
        }
    }
}
