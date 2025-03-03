package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }


        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new homeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("BottomNav", "Selected item: " + item.getItemId());
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                Log.d("BottomNav", "Home selected");
                replaceFragment(new homeFragment());
            } else if (itemId == R.id.deep_link) {
                Log.d("BottomNav", "Deep Link selected");
                replaceFragment(new DeepLinkFragment());
            } else if (itemId == R.id.music) {
                Log.d("BottomNav", "Music selected");
                replaceFragment(new MusicFragment());
            } else if (itemId == R.id.nav_airplane) {
                Log.d("BottomNav", "Airplane selected");
                replaceFragment(new AirplaneFragment());
            } else if (itemId == R.id.calendar) {
                Log.d("BottomNav", "Calendar selected");
                replaceFragment(new CalendarFragment());
            }

            return true;
        });

    }
    public void updateBottomNavigation(int menuItemId) {
        binding.bottomNavigationView.setSelectedItemId(menuItemId);
    }


    private void replaceFragment(Fragment fragment){
        Log.d("FragmentChange", "Replacing fragment with: " + fragment.getClass().getSimpleName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}