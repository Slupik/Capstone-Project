package io.github.slupik.savepass.app.views.viewpass;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.app.views.addpass.AddPassActivity;
import io.github.slupik.savepass.app.views.main.MainActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class ShowPassActivity extends AppCompatActivity implements ShowPassFragment.OnFragmentInteractionListener {
    public static final String ARG_DATA = "data";
    public static final int REQUEST_EDIT_PASSWORD_ENTITY = 0;
    public static final int REQUEST_LOGGED_IN = 1;

    private EntityPassword mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600 && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            startInsteadPassList();
        }

        setContentView(R.layout.activity_show_pass);
        loadFromBundle();
    }

    private void startInsteadPassList() {
        // Code below creates new activity in history stack - it's not necessary because
        // MainActivity is already instanced
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(MainActivity.ARG_DATA, getIntent().getStringExtra(ARG_DATA));
//        startActivity(intent);

        finish();
    }

    private void loadFromBundle(){
        if(TextUtils.isEmpty(getMyApplication().getAppPassword())) {
            showLoginActivity();
        } else {
            String data = getIntent().getStringExtra(ARG_DATA);
            mEntity = new Gson().fromJson(data, EntityPassword.class);
            loadData(mEntity);
        }
    }

    private void showLoginActivity() {
        Intent logIn = new Intent(getApplicationContext(), MainActivity.class);
        logIn.putExtra(MainActivity.ARG_ACTION_TYPE, MainActivity.ARG_VALUE_ACTION_LOG_IN);
        startActivityForResult(logIn, REQUEST_LOGGED_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_PASSWORD_ENTITY) {
            if (resultCode == RESULT_OK && data!=null) {
                if(data.getIntExtra(AddPassActivity.ARG_RESULT_STATUS, -1) == AddPassActivity.RESULT_STATUS_DELETE) {
                    finish();
                }
                if(data.getIntExtra(AddPassActivity.ARG_RESULT_STATUS, -1) == AddPassActivity.RESULT_STATUS_ADD) {
                    String entData = data.getStringExtra(AddPassActivity.ARG_DATA);
                    loadData(new Gson().fromJson(entData, EntityPassword.class));
                }
            }
        }
        if(requestCode==REQUEST_LOGGED_IN) {
            if (resultCode == RESULT_OK) {
                loadFromBundle();
            }
        }
    }

    private void loadData(EntityPassword mEntity) {
        ShowPassFragment fragment = getShowingFragment();
        fragment.loadData(mEntity);
    }

    private ShowPassFragment getShowingFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (ShowPassFragment)fragmentManager.findFragmentById(R.id.pass_info_fragment);
    }

    private MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }

    @Override
    public void startEditActivity(EntityPassword entity) {
        Intent editAct = new Intent(getApplicationContext(), AddPassActivity.class);
        editAct.putExtra(AddPassActivity.ARG_DATA, new Gson().toJson(entity));
        startActivityForResult(editAct, REQUEST_EDIT_PASSWORD_ENTITY);
    }
}
