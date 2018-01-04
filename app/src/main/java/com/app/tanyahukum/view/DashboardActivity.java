package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerDashboardActivityComponent;
import com.app.tanyahukum.data.module.DashboardActivityModule;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.presenter.DashboardPresenter;
import com.app.tanyahukum.services.DeleteTokenService;
import com.app.tanyahukum.services.MyFirebaseInstanceIDService;
import com.app.tanyahukum.util.RoundedCornersTransform;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/9/17.
 */

public class DashboardActivity extends AppCompatActivity implements DashboardActivityInterface.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbarDashboard;
    @BindView(R.id.nav_view)
    NavigationView navigationViewDashboard;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayoutDashboard;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.email)
    TextView tvEmail;
    @BindView(R.id.newAnswer)
    ImageView newAnswer;

    String userid;

    @Inject
    DashboardPresenter dashboardPresenter;
    String regId;
    String userId;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarDashboard);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        DaggerDashboardActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .dashboardActivityModule((new DashboardActivityModule(this,this)))
                .build().inject(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayoutDashboard, toolbarDashboard, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayoutDashboard.addDrawerListener(toggle);

        toggle.syncState();
        registerFirebaseId();

        navigationViewDashboard.setNavigationItemSelectedListener(this);

        if(App.getInstance().getPrefManager().getUser().getUsertype().equals("CONSULTANT")){
            navigationViewDashboard.getMenu().findItem(R.id.nav_add_consultation).setVisible(false);
        }else{
            navigationViewDashboard.getMenu().findItem(R.id.nav_add_consultation).setVisible(true);
        }

        regId = App.getInstance().getPrefManager().getFirebaseToken();
        userId=App.getInstance().getPrefManager().getUserId();
        dashboardPresenter.getImageProfile(userId);

        Intent i=getIntent();
        String loginType="";
        loginType=i.getStringExtra("loginType");

        if (loginType!=null)
        if(regId!=null) {
            dashboardPresenter.updateFirebaseToken(regId);
        }else{
            startService(new Intent(this, MyFirebaseInstanceIDService.class));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (key.equals("AnotherActivity") && value.equals("True")) {
                    Intent intent = new Intent(this, AcceptQuestionsActivity.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    finish();
                }
            }
        }
        showProfile();
        checkNewAnswer();
    }

    private void registerFirebaseId() {
        Intent intent = new Intent(this, MyFirebaseInstanceIDService.class);
        startService(intent);
    }

    @Override
    public void showProfile() {
         User user=App.getInstance().getPrefManager().getUser();
         tvName.setText(user.getName());
         tvEmail.setText(user.getEmail());
    }
    @Override
    public void toConsultationPage() {
        registerFirebaseId();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.ListConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void toFormConsultation() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("statusConsultation","NEW");
        intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void toLoginPage() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.LoginActivity");
        startActivity(intent);
    }

    @Override
    public void toAppointmentListPage(String type) {
        Intent intent = new Intent();
        intent.putExtra("type",type);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.ListAppointmentActivity");
        startActivity(intent);
    }

    @Override
    public void toMyAccountPage() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.MyAccountActivity");
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutDashboard.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutDashboard.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_consultation_list) {
            toConsultationPage();
        } else if (id == R.id.nav_add_consultation) {
            toFormConsultation();
        }
        else if (id == R.id.nav_appointment_list) {
            toAppointmentListPage("APPOINTMENT");
        }
        else if (id == R.id.nav_my_account) {
            toMyAccountPage();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, DeleteTokenService.class);
            startService(intent);
            dashboardPresenter.signOutUser();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick(R.id.aboutBtn)
    public void callAboutActivity(){
        Intent intent = new Intent();
        intent.setClassName(this, "com.app.tanyahukum.view.AboutActivity");
        startActivity(intent);
    }

    @OnClick(R.id.balanceBtn)
    public void callBalanceActivity(){
        Intent intent = new Intent();
        intent.setClassName(this, "com.app.tanyahukum.view.BalanceActivity");
        startActivity(intent);
    }

    @OnClick(R.id.questionBtn)
    public void callListConsultationActivity(){
        Intent intent = new Intent();
        intent.setClassName(this, "com.app.tanyahukum.view.ListConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void showImage(String url) {
        View headerView = navigationViewDashboard.getHeaderView(0);
        ImageView profilePic = (ImageView)headerView.findViewById(R.id.profilePic);
        TextView profileName = (TextView)headerView.findViewById(R.id.txtUserName);
        profileName.setText(tvName.getText());

        profilePic.setVisibility(View.GONE);
        profilePic.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(url)
                .transform(new RoundedCornersTransform())
                .into(profilePic);
    }

    @Override
    public void checkNewAnswer() {
        String userType=App.getInstance().getPrefManager().getUserType();

        if (userType.equals("CLIENT")){
            dashboardPresenter.queryNewAnswer(userId);
        }
    }

    @Override
    public void appearNewNotification() {
        newAnswer.setVisibility(View.VISIBLE);
    }
}
