package com.example.navdrawertest10;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class ZAdminDrawer_MainAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zadmin_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AdminAccountsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_accounts);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_accounts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminAccountsFragment()).commit();
                break;
            case R.id.nav_inventory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminInventoryFragment()).commit();
                break;
            case R.id.nav_sales:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminSalesFragment()).commit();
                break;
            case R.id.nav_records:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminRecordsFragment()).commit();
                break;
            case R.id.nav_admin_pass:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminChangePasswordFragment()).commit();
                break;
            case R.id.nav_admin_logout: //name in drawer menu
                SessionManagement sessionManagement = new SessionManagement(ZAdminDrawer_MainAct.this);
                sessionManagement.removeSession();
                Intent intent = new Intent(ZAdminDrawer_MainAct.this, ALogin.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}