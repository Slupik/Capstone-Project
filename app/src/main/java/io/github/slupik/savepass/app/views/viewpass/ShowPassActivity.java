package io.github.slupik.savepass.app.views.viewpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.slupik.savepass.R;
import io.github.slupik.savepass.app.MyApplication;
import io.github.slupik.savepass.app.views.addpass.AddPassActivity;
import io.github.slupik.savepass.data.password.room.EntityPassword;

public class ShowPassActivity extends AppCompatActivity {
    public static final String ARG_DATA = "data";
    private static final int REQUEST_EDIT_PASSWORD_ENTITY = 0;

    @BindView(R.id.app_bar)
    Toolbar appBar;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.edit_pass)
    ImageButton btnEditPass;
    @BindView(R.id.login_info)
    LinearLayout loginContainer;
    @BindView(R.id.login_field)
    TextInputEditText loginField;
    @BindView(R.id.copy_login)
    ImageButton btnCopyLogin;
    @BindView(R.id.password_field)
    TextInputEditText passField;
    @BindView(R.id.copy_password)
    ImageButton btnCopyPass;
    @BindView(R.id.short_desc_info)
    LinearLayout shortDescContainer;
    @BindView(R.id.short_desc)
    TextView shortDesc;
    @BindView(R.id.note_info)
    LinearLayout noteContainer;
    @BindView(R.id.note)
    TextView note;

    private EntityPassword mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pass);
        ButterKnife.bind(this);

        setupButtons();
        setupAppBar();
        loadFromBundle();
    }

    private void setupAppBar() {
        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEditPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAct = new Intent(getApplicationContext(), AddPassActivity.class);
                editAct.putExtra(AddPassActivity.ARG_DATA, new Gson().toJson(mEntity));
                startActivityForResult(editAct, REQUEST_EDIT_PASSWORD_ENTITY);
            }
        });
    }

    private void setupButtons() {
        btnCopyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(getString(R.string.login), loginField.getText().toString());
            }
        });
        btnCopyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(getString(R.string.pass), passField.getText().toString());
            }
        });
    }

    private void copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplication(), getString(R.string.copied_successfully), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), getString(R.string.error_while_copying), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromBundle(){
        String data = getIntent().getStringExtra(ARG_DATA);
        mEntity = new Gson().fromJson(data, EntityPassword.class);
        loadData(mEntity);
    }

    public void loadData(EntityPassword entity) {
        setTitle(entity.getPasswordName());
        setLogin(entity.getLogin());
        try {
            setPassword(entity.getDecryptedPassword(getMyApplication().getAppPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setShortDesc(entity.getShortDesc());
        setNote(entity.getNotes());
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    private void setLogin(String login){
        setFieldWithContainer(login, loginField, loginContainer);
    }

    private void setPassword(String password){
        passField.setText(password);
    }

    private void setShortDesc(String desc){
        setFieldWithContainer(desc, shortDesc, shortDescContainer);
    }

    private void setNote(String note){
        setFieldWithContainer(note, this.note, noteContainer);
    }

    private void setFieldWithContainer(String text, TextView field, LinearLayout container) {
        if(TextUtils.isEmpty(text)) {
            container.setVisibility(View.GONE);
        } else {
            field.setText(text);
            container.setVisibility(View.VISIBLE);
        }
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

    private MyApplication getMyApplication() {
        return (MyApplication) getApplication();
    }
}
