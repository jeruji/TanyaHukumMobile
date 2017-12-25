package com.app.tanyahukum.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAboutActivityComponent;
import com.app.tanyahukum.data.module.AboutActivityModule;
import com.app.tanyahukum.presenter.AboutPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity implements AboutActivityInterface.View {

    @Inject
    AboutPresenter aboutPresenter;

    @BindView(R.id.containerAbout)
    WebView containerAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        DaggerAboutActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .aboutActivityModule(new AboutActivityModule(this, this))
                .build().inject(this);

        setWebViewContent();
    }

    @Override
    @OnClick(R.id.btnBack)
    public void btnBackFunction(){
        aboutPresenter.closeAbout();
    }

    @Override
    public void setWebViewContent() {

        String aboutContent = "<body style=\"text-align:justify;color:#37474F;background-color:#FAFAFA;\">\n" +
                "  Lorem ipsum dolor sit amet, consectetur \n" +
                "  adipiscing elit. Nunc pellentesque, urna\n" +
                "  nec hendrerit pellentesque, risus massa\n" +
                " </body>";

        containerAbout.loadData(aboutContent, "text/html", "UTF-8");
    }
}
