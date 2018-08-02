package io.github.slupik.savepass.app.views.viewpass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.app.views.addpass.AddPassActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class ShowPassActivity extends AppCompatActivity implements ShowPassFragment.OnFragmentInteractionListener {
    public static final String ARG_DATA = "data";
    public static final int REQUEST_EDIT_PASSWORD_ENTITY = 0;

    private EntityPassword mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pass);
        loadFromBundle();
    }

    private void loadFromBundle(){
        String data = getIntent().getStringExtra(ARG_DATA);
        mEntity = new Gson().fromJson(data, EntityPassword.class);
        loadData(mEntity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_EDIT_PASSWORD_ENTITY == requestCode) {
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
    }

    private void loadData(EntityPassword mEntity) {
        ShowPassFragment fragment = getShowingFragment();
        fragment.loadData(getMyApplication(), mEntity);
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
