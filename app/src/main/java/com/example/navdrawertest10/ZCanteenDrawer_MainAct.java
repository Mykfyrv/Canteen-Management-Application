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

public class ZCanteenDrawer_MainAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zcanteen_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(ZCanteenDrawer_MainAct.this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) { //display first fragment when app is open
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CanteenPostFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_post);
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu: //name in drawer menu - fragment container -> main act.xml
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CanteenMenuFragment()).commit();
                break;
            case R.id.nav_post: //name in drawer menu
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CanteenPostFragment()).commit();
                break;
            case R.id.nav_order: //name in drawer menu
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CanteenOrdersFragment()).commit();
                break;
            case R.id.nav_sales: //name in drawer menu
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CanteenSalesFragment()).commit();
                break;
            case R.id.nav_canteen_pass: //name in drawer menu
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CanteenChangePasswordFragment()).commit();
                break;
            case R.id.nav_canteen_logout: //name in drawer menu
                SessionManagement sessionManagement = new SessionManagement(ZCanteenDrawer_MainAct.this);
                sessionManagement.removeSession();
                Intent intent = new Intent(ZCanteenDrawer_MainAct.this, ALogin.class);
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


}// end of onCreate

