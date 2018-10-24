package jhondoe.com.domiciliosserver.ui.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import jhondoe.com.domiciliosserver.R;
import jhondoe.com.domiciliosserver.common.Common;
import jhondoe.com.domiciliosserver.data.preferences.SessionPrefs;
import jhondoe.com.domiciliosserver.service.ListenOrder;
import jhondoe.com.domiciliosserver.utilies.Uweb;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // UI references.
    TextView txtUser, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        // Checks if the device has any active internet connection.
        if (!Uweb.isNetworkConnected(getBaseContext())){
            onShowNetWorkError(getString(R.string.error_network));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.getHeaderView(0);
        prepararUI(navView);// Preparar elementos UI

        // Call Service
        Intent service = new Intent(getBaseContext(), ListenOrder.class);
        startService(service);

        // Call Notification
        if (Common.NOTIFICATION == "request"){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_order_status));
        }

        /*
        if (getIntent() == null){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_order_status));
        } else {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_order_status));
        }
        */
        onBringData(); // Traer Datos
    }

    void prepararUI(View view){
        txtUser = (TextView)view.findViewById(R.id.textUser);
        txtPhone = (TextView)view.findViewById(R.id.textPhone);
    }

    private void onBringData() {
        String phone = SessionPrefs.get(getBaseContext()).getPhone();
        // Set Name for user
        txtPhone.setText(phone);
    }


    private void onShowNetWorkError(String error) {
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_signOff){
            SessionPrefs.get(getBaseContext()).logOut();

            onClose();
        }

        return super.onOptionsItemSelected(item);
    }

    void onClose(){
        Intent intentLogin = new Intent(this, ActivitySignIn.class);
        startActivity(intentLogin);
        finish();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getFragmentManager();

        if (id == R.id.nav_store) {
            // Handle the camera action
            manager.beginTransaction().replace(R.id.content_frame, new FragmentStore()).commit();
        } /*else if (id == R.id.nav_cart) {

        } */else if (id == R.id.nav_order_status) {
            manager.beginTransaction().replace(R.id.content_frame, new FragmentOrderStatus()).commit();

        } else if (id == R.id.nav_budget) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_provider) {
            manager.beginTransaction().replace(R.id.content_frame, new FragmentProvider()).commit();
        } else if (id == R.id.nav_log_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
