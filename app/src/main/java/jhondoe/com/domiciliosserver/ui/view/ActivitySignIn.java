package jhondoe.com.domiciliosserver.ui.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rey.material.widget.CheckBox;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.data.preferences.SessionPrefs;

public class ActivitySignIn extends AppCompatActivity {

    // Referencias UI
    EditText edt_number, edt_password;
    Button btnNext;
    CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Init view
        prepararUI();

    }

    private void prepararUI() {
        edt_number = (EditText)findViewById(R.id.edt_number);
        edt_password = (EditText)findViewById(R.id.edt_number_02);

        btnNext = (Button)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        ckbRemember = (CheckBox)findViewById(R.id.ckbRemember) ;
    }

    private void signInUser() {
        final String localPhone = edt_number.getText().toString();
        final String localPassword = edt_password.getText().toString();

        ProgressDialog mDialog = new ProgressDialog(ActivitySignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        // Save user & password
        if (ckbRemember.isChecked()){
            // Guardar usuario en preferencias
            SessionPrefs.get(ActivitySignIn.this).saveUser(localPhone, localPassword);
        }

        Intent intentHome = new Intent(ActivitySignIn.this, Home.class);
        startActivity(intentHome);
        mDialog.dismiss();


    }
}
