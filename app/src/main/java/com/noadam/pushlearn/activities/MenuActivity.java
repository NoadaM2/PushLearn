package com.noadam.pushlearn.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.fragments.ComPacksFragment;
import com.noadam.pushlearn.fragments.MyPacksFragment;
import com.noadam.pushlearn.fragments.NowLearningFragment;
import com.noadam.pushlearn.fragments.SettingsFragment;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //loading the default fragment
        loadFragment(new NowLearningFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_now_learning:
                fragment = new NowLearningFragment();
                break;

            case R.id.navigation_myPacks:
                fragment = new MyPacksFragment();
                break;

            case R.id.navigation_communityPacks:
                fragment = new ComPacksFragment();
                break;

            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
