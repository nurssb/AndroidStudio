package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityMainBinding;

public class homeFragment extends Fragment {

    public homeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnDeepLink = view.findViewById(R.id.btn_deep_link);
        Button btnMusic = view.findViewById(R.id.btn_music);
        Button btnAirplane = view.findViewById(R.id.btn_airplane);
        Button btnCalendar = view.findViewById(R.id.btn_calendar);

        btnDeepLink.setOnClickListener(v -> replaceFragment(new DeepLinkFragment(),R.id.deep_link));
        btnMusic.setOnClickListener(v -> replaceFragment(new MusicFragment(),R.id.music));
        btnAirplane.setOnClickListener(v -> replaceFragment(new AirplaneFragment(),R.id.nav_airplane));
        btnCalendar.setOnClickListener(v -> replaceFragment(new CalendarFragment(),R.id.calendar));
    }

    private void replaceFragment(Fragment fragment, int menuItemId) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.updateBottomNavigation(menuItemId);
        }

    }

}

