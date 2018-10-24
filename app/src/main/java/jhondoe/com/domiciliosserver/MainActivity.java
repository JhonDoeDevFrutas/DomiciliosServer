package jhondoe.com.domiciliosserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import jhondoe.com.domiciliosserver.data.preferences.SessionPrefs;
import jhondoe.com.domiciliosserver.ui.view.ActivitySignIn;
import jhondoe.com.domiciliosserver.ui.view.Home;

public class MainActivity extends AppCompatActivity {

    // Referencias UI
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.mytransition);

        final Intent intentLogin  = new Intent(this, ActivitySignIn.class);
        final Intent intentHome   = new Intent(this, Home.class);

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Redirección al Login
                    if (!SessionPrefs.get(MainActivity.this).isLoggedIn()){
                        startActivity(intentLogin);
                    } else {// Redirección al Menu
                        startActivity(intentHome);
                    }

                    finish();
                    return;
                }
            }
        };
        timer.start();

        // Init view
        prepararUI();
    }

    private void prepararUI() {

        btnContinue = (Button)findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage();
            }
        });

    }

    private void startLoginPage() {
        Intent intentSignIn = new Intent(MainActivity.this, ActivitySignIn.class);
        startActivity(intentSignIn);
    }
}
