package com.app.tanyahukum.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.CheckBox;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerTermsAndConditionActivityComponent;
import com.app.tanyahukum.data.module.TermsAndConditionActivityModule;
import com.app.tanyahukum.presenter.TermsAndConditionPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsAndConditionActivity extends AppCompatActivity implements TermsAndConditionActivityInterface.View {

    @BindView(R.id.containerTermsAndCondition)
    WebView containerTermsAndCondition;

    @BindView(R.id.checkTermsAndCondition)
    CheckBox checkTermsAndCondition;

    @Inject
    TermsAndConditionPresenter termsAndConditionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        ButterKnife.bind(this);

        DaggerTermsAndConditionActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .termsAndConditionActivityModule(new TermsAndConditionActivityModule(this, this))
                .build().inject(this);

        setWebViewContent();
    }

    @Override
    @OnClick(R.id.btnContinue)
    public void continueToRegistration() {
        if(termsAndConditionPresenter.checkCheckedAgreeBox(checkTermsAndCondition)){
            Intent intent = getIntent();
            intent.putExtra("loginType",intent.getStringExtra("loginType"));
            intent.putExtra("userId",intent.getStringExtra("userId"));
            intent.putExtra("name",intent.getStringExtra("name"));
            intent.putExtra("email",intent.getStringExtra("email"));
            intent.setClassName(this, "com.app.tanyahukum.view.RegistrationActivity");
            startActivity(intent);
        }
        else{
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Anda harus centang Checkbox \"Setuju\" sebelum melanjutkan");
            dlgAlert.setTitle("Centang Checkbox");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int whichButton){
                            return;
                        }
                    });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
    }

    @Override
    public void setWebViewContent() {

        String termsAndConditionContent = "<body style=\"text-align:justify;color:gray;background-color:black;\">\n" +
                "  Lorem ipsum dolor sit amet, consectetur \n" +
                "  adipiscing elit. Nunc pellentesque, urna\n" +
                "  nec hendrerit pellentesque, risus massa\n" +
                " </body>";

        containerTermsAndCondition.loadData(termsAndConditionContent, "text/html", "UTF-8");

    }
}
