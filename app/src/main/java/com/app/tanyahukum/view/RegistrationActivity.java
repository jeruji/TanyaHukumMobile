package com.app.tanyahukum.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.adapter.ProvinceAdapter;
import com.app.tanyahukum.adapter.RegencyAdapter;
import com.app.tanyahukum.data.component.DaggerRegistrationActivityComponent;
import com.app.tanyahukum.data.module.RegistrationActivityModule;
import com.app.tanyahukum.model.Province;
import com.app.tanyahukum.model.Regency;
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
    Long prov_id;
    RecyclerView _rcylerViewDialog;
    TextView _titleDialog;
    ArrayList<Province> provinceArrayList;
    ArrayList<Regency> regencyArrayList;
    private ProvinceAdapter provinceAdapter;
    private RegencyAdapter regencyAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        ButterKnife.bind(this);
        DaggerRegistrationActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .registrationActivityModule(new RegistrationActivityModule(this, this))
                .build().inject(this);

        regencyArrayList =new ArrayList<>();
         Intent i=getIntent();
        loginType=i.getStringExtra("loginType");
        userId=i.getStringExtra("userId");
            if (loginType.equals("FACEBOOK") || loginType.equals("GMAIL")) {
                String nameStr = i.getStringExtra("name");
                String emailStr = i.getStringExtra("email");
                name.setText(nameStr);
                email.setText(emailStr);
            }
            province.setEnabled(false);
            province.setTextColor(Color.parseColor("#000000"));
        city.setEnabled(false);
        city.setTextColor(Color.parseColor("#000000"));
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
        String provinceStr=
               // "";
                province.getText().toString();
        String cityStr=
                "";
                city.getText().toString();
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
        //province.setSe("");
        //city.setText("");
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
            String userStr = userType.getSelectedItem().toString();
            if (userStr.equals("Client")) {
                user.setUsertype("CLIENT");
                user.setLawFirm("");
                user.setLawFirmAddress("");
                user.setLawFirmCity("");
                user.setLawFirmPhone("");
                List<String> specialization = Arrays.asList(getResources().getStringArray(R.array.specialization));
                selectedSpecialization.add(specialization.get(0));
                user.setSpecialization(selectedSpecialization);

            }
            else if (userStr.equals("Consultant")) {
                user.setUsertype("CONSULTANT");
                user.setLawFirm(lawFirm.getText().toString());
                user.setLawFirmAddress(lawFirmAddress.getText().toString());
                user.setLawFirmCity(lawFirmCity.getText().toString());
                user.setLawFirmPhone(lawFirmPhone.getText().toString());
                user.setSpecialization(selectedSpecialization);
                user.setTotalConsultation(0);
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
    public void showSpinnerProvince(List<Province> provList) {
        final Dialog dialog=new Dialog(new ContextThemeWrapper(this,R.style.AppCompatAlertDialogStyle));
        LayoutInflater factory=LayoutInflater.from(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view=factory.inflate(R.layout.list_dialog,null);
        _rcylerViewDialog= (RecyclerView) view.findViewById(R.id.rcylerViewDialog);
        _titleDialog= (TextView) view.findViewById(R.id.titleDialog);
        provinceArrayList =new ArrayList<>();
        provinceArrayList.addAll(provList);
        provinceAdapter=new ProvinceAdapter(this,provinceArrayList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        _titleDialog.setText("Select Province : ");
        _rcylerViewDialog.setLayoutManager(layoutManager);
        _rcylerViewDialog.setAdapter(provinceAdapter);
        _rcylerViewDialog.setItemAnimator(new DefaultItemAnimator());
        _rcylerViewDialog.addOnItemTouchListener(new ProvinceAdapter.RecyclerTouchListener(getApplicationContext(), _rcylerViewDialog, new ProvinceAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Province prov=provinceArrayList.get(position);
                province.setText(prov.getNama());
                 prov_id=prov.getId();
                dialog.dismiss();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

    }

    @Override
    public void showSpinnerRegency(List<Regency> regencyList, final String tipe) {
        final Dialog dialog=new Dialog(new ContextThemeWrapper(this,R.style.AppCompatAlertDialogStyle));
      LayoutInflater factory=LayoutInflater.from(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view=factory.inflate(R.layout.list_dialog,null);
        _rcylerViewDialog= (RecyclerView) view.findViewById(R.id.rcylerViewDialog);
        _titleDialog= (TextView) view.findViewById(R.id.titleDialog);
        regencyArrayList=new ArrayList<>();
        regencyArrayList.addAll(regencyList);
        regencyAdapter=new RegencyAdapter(this,regencyArrayList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        _titleDialog.setText("Select Regency : ");
        _rcylerViewDialog.setLayoutManager(layoutManager);
        _rcylerViewDialog.setAdapter(regencyAdapter);
        _rcylerViewDialog.setItemAnimator(new DefaultItemAnimator());
        _rcylerViewDialog.addOnItemTouchListener(new ProvinceAdapter.RecyclerTouchListener(getApplicationContext(), _rcylerViewDialog, new ProvinceAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Regency regency=regencyArrayList.get(position);
                if (tipe.equalsIgnoreCase("FIRM")){
                    lawFirmCity.setText(regency.getNama());
                }else {
                    city.setText(regency.getNama());
                }
                dialog.dismiss();
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
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



    @Override
    @OnClick(R.id.selectProvince)
    public void selectProvinsi(){
      registrationPresenter.getProvince();
    }

    @Override
    @OnClick(R.id.selectRegency)
    public void selectRegency(){
        registrationPresenter.getRegencies(prov_id,"");

    }

    @OnClick(R.id.selectCity)
    public void selectCityLawFirm(){
        registrationPresenter.getRegencies(prov_id,"FIRM");

    }
}
