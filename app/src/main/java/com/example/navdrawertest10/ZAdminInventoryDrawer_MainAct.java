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

public class ZAdminInventoryDrawer_MainAct extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zadmininventory_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_invent_layout);
        NavigationView navigationView = findViewById(R.id.nav_invent_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_invent_container,
                    new AdmininventoryInvetoryFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_edit_inventory);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_edit_inventory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_invent_container,
                        new AdmininventoryInvetoryFragment()).commit();
                break;
            case R.id.nav_inventory_pass:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_invent_container,
                        new AdminInventoryChangePasswordFragment()).commit();
                break;
            case R.id.nav_inventory_logout: //name in drawer menu
                SessionManagement sessionManagement = new SessionManagement(ZAdminInventoryDrawer_MainAct.this);
                sessionManagement.removeSession();
                Intent intent = new Intent(ZAdminInventoryDrawer_MainAct.this, ALogin.class);
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
        } else {
            super.onBackPressed();
        }
    }
}