package com.example.navdrawertest10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class ZCustomerDrawer_MainAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zcustomer_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ZCustomerDrawer_MainAct.this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) { //display first fragment when app is open
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CustomerMenuFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_customer_menu);
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_customer_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CustomerMenuFragment()).commit();
                break;
            case R.id.nav_customer_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CustomerCartFragment()).commit();
                break;
            case R.id.nav_customer_my_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CustomerMyOrderFragment()).commit();
                break;
            case R.id.nav_customer_notification:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CustomerNotificationFragment()).commit();
                break;
            case R.id.nav_user_pass:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CustomerChangePasswordFragment()).commit();
                break;
            case R.id.nav_user_logout: //name in drawer menu
                SessionManagement sessionManagement = new SessionManagement(ZCustomerDrawer_MainAct.this);
                sessionManagement.removeSession();
                Intent intent = new Intent(ZCustomerDrawer_MainAct.this, ALogin.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        } // end of if statement
    } //end of on backPressed

}