package com.app.tanyahukum.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.adapter.ProvinceAdapter;
import com.app.tanyahukum.adapter.RegencyAdapter;
import com.app.tanyahukum.data.component.DaggerEditAccountActivityComponent;
import com.app.tanyahukum.data.component.DaggerMyAccountActivityComponent;
import com.app.tanyahukum.data.module.EditAccountActivityModule;
import com.app.tanyahukum.data.module.MyAccountActivityModule;
import com.app.tanyahukum.model.Province;
import com.app.tanyahukum.model.Regency;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.presenter.EditAccountPresenter;
import com.app.tanyahukum.presenter.MyAccountPresenter;
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
 * Created by emerio on 4/20/17.
 */

public class EditAccountActivity extends AppCompatActivity implements EditAccountInterface.View ,MultiSpinnerListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar_;
    @BindView(R.id.edTextName)
    EditText _name;
    @BindView(R.id.edTextEmail)
    EditText _email;
    @BindView(R.id.edTextBornDate)
    EditText _borndate;
    @BindView(R.id.edTextPhone)
    EditText _phone;
    @BindView(R.id.edTextProvince)
    EditText _province;
    @BindView(R.id.edTextCity)
    EditText _city;
    @BindView(R.id.edTextAddress)
    EditText _address;
    @BindView(R.id.spinnerUserType)
    Spinner userType;
    @BindView(R.id.spinnerGender)
    Spinner gender;
    @BindView(R.id.SpinnerSpecialization)
    MultiSpinner specializationSpinner;
    ArrayList<String> selectedSpecialization;

    @BindView(R.id.edTextLawFirm)
    EditText _lawFirm;
    @BindView(R.id.edTextLawFirmCity)
    EditText _lawFirmCity;
    @BindView(R.id.edTextLawFirmAddress)
    EditText _lawFirmAddress;
    @BindView(R.id.edTextLawFirmPhone)
    EditText _lawFirmPhone;
    @BindView(R.id.edTextLawFirmProvince)
    TextView _lawFirmProvince;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.tableRowSpecialization)
    TableRow tableRowSpecialization;
    @BindView(R.id.tableRowLawFirm)
    TableRow tableRowLawFirm;
    @BindView(R.id.tableRowCity)
    TableRow tableRowCity;
    @BindView(R.id.tableRowProvince)
    TableRow tableRowProvince;
    @BindView(R.id.tableRowAddress)
    TableRow tableRowAddress;
    @BindView(R.id.tableRowPhone)
    TableRow tableRowPhone;
    @Inject
    EditAccountPresenter editAccountPresenter;
    RecyclerView _rcylerViewDialog;
    TextView _titleDialog;
    ArrayList<Province> provinceArrayList;
    ArrayList<Regency> regencyArrayList;
    private ProvinceAdapter provinceAdapter;
    private RegencyAdapter regencyAdapter;
    Long prov_id;
    String firebaseToken,userId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("Edit Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerEditAccountActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .editAccountActivityModule((new EditAccountActivityModule(this,this)))
                .build().inject(this);
        userId=App.getInstance().getPrefManager().getUser().getId();
        editAccountPresenter.getUserById(userId);
        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String user=userType.getSelectedItem().toString();
                if (user.equals("Client")){
                    tableRowSpecialization.setVisibility(View.GONE);
                    tableRowLawFirm.setVisibility(View.GONE);
                    tableRowProvince.setVisibility(View.GONE);
                    tableRowCity.setVisibility(View.GONE);
                    tableRowAddress.setVisibility(View.GONE);
                    tableRowPhone.setVisibility(View.GONE);

                }else if(user.equals("Consultant")){
                    tableRowSpecialization.setVisibility(View.VISIBLE);
                    tableRowLawFirm.setVisibility(View.VISIBLE);
                    tableRowProvince.setVisibility(View.VISIBLE);
                    tableRowCity.setVisibility(View.VISIBLE);
                    tableRowAddress.setVisibility(View.VISIBLE);
                    tableRowPhone.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> specialization = Arrays.asList(getResources().getStringArray(R.array.specialization));
        specializationSpinner.setItems(specialization, "Select Specialization", this);
        selectedSpecialization = new ArrayList<>();
    }
    @Override
    public void showAccount(User user) {
            _name.setText(user.getName());
            _email.setText(user.getEmail());
            _borndate.setText(user.getBornDate());

            _phone.setText(user.getPhone());
            _province.setText(user.getProvince());
            _city.setText(user.getCity());
            _address.setText(user.getAddress());

            _name.setText(user.getName());
            _email.setText(user.getEmail());
            _borndate.setText(user.getBornDate());

            _phone.setText(user.getPhone());
            _province.setText(user.getProvince());
            _city.setText(user.getCity());
            _address.setText(user.getAddress());
        if (user.getGender().equalsIgnoreCase("FEMALE"))
            gender.setSelection(1);
        if (user.getUsertype().equalsIgnoreCase("CONSULTANT")){
            userType.setSelection(1);
            _lawFirm.setText(user.getLawFirm());
            _lawFirmProvince.setText(user.getLawFirmProvince());
            _lawFirmCity.setText(user.getLawFirmCity());
            _lawFirmAddress.setText(user.getLawFirmAddress());
            _lawFirmPhone.setText(user.getLawFirmPhone());
        }

        firebaseToken=user.getFirebaseToken();
            userId=user.getId();
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    @OnClick(R.id.btnSave)
    public void update() {
        String usertype=App.getInstance().getPrefManager().getUserType();

        User users=new User();
        users.setId(userId);
        users.setName(_name.getText().toString());
        users.setEmail(_email.getText().toString());
        users.setBornDate(_borndate.getText().toString());
        users.setPhone(_phone.getText().toString());
        users.setProvince(_province.getText().toString());
        users.setCity(_city.getText().toString());
        users.setAddress(_address.getText().toString());
        users.setUsertype(userType.getSelectedItem().toString());
        String genderStr = gender.getSelectedItem().toString();
        if (genderStr.equals("Male"))
            users.setGender("MALE");
        else if (genderStr.equals("Female"))
            users.setGender("FEMALE");
        String userStr = userType.getSelectedItem().toString();
        if (userStr.equals("Client")) {
            users.setUsertype("CLIENT");

        }
        else if (userStr.equals("Consultant")) {
            users.setUsertype("CONSULTANT");


        }
        users.setLawFirm(_lawFirm.getText().toString());
        users.setLawFirmProvince(_lawFirmProvince.getText().toString());
        users.setLawFirmCity(_lawFirmCity.getText().toString());
        users.setLawFirmAddress(_lawFirmAddress.getText().toString());
        users.setLawFirmPhone(_lawFirmPhone.getText().toString());
        users.setFirebaseToken(firebaseToken);
        App.getInstance().getPrefManager().storeUser(users);
        editAccountPresenter.saveUpdatedAccount(userId,users);
    }

    @Override
    public void toMyAccountPage(boolean result) {
        if (result){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.MyAccountActivity");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Edit Account Failed",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void showSpinnerProvince(List<Province> provList,final  String tipe) {
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
                _province.setText(prov.getNama());
                prov_id=prov.getId();
                if (tipe.equalsIgnoreCase("FIRM")){
                    _lawFirmProvince.setText(prov.getNama());
                    _lawFirmCity.setText("");
                }else {
                    _city.setText("");
                    _province.setText(prov.getNama());
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
                    _lawFirmCity.setText(regency.getNama());
                }else {
                    _city.setText(regency.getNama());
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
    @OnClick(R.id.btnCancel)
    public void cancel() {
        super.onBackPressed();
    }

    @Override
    @OnClick(R.id.selectProvince)
    public void selectProvinsi(){
        editAccountPresenter.getProvince("");
    }

    @OnClick(R.id.selectProvinceLawFirm)
    public void selectProvinsiLawFirm() {
        editAccountPresenter.getProvince("FIRM");
    }
        @Override
    @OnClick(R.id.selectRegency)
    public void selectRegency(){
        editAccountPresenter.getRegencies(prov_id,"");

    }
    @OnClick(R.id.selectCity)
    public void selectCityLawFirm(){
        editAccountPresenter.getRegencies(prov_id,"FIRM");

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
