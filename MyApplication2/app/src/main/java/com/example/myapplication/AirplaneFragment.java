package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AirplaneFragment extends Fragment {

    private TextView statusTextView;
    private Button toggleButton;
    private BroadcastReceiver airplaneReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_airplane, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statusTextView = view.findViewById(R.id.airplane_status_text);
        toggleButton = view.findViewById(R.id.toggle_airplane_button);

        updateAirplaneStatus();

        toggleButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Airplane Mode can only be changed manually!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            startActivity(intent);
        });

        airplaneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {
                    boolean isEnabled = intent.getBooleanExtra("state", false);
                    updateUI(isEnabled);
                }
            }
        };

        // ресивер
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        requireActivity().registerReceiver(airplaneReceiver, filter);
    }

    // UI
    private void updateUI(boolean isAirplaneModeOn) {
        if (isAirplaneModeOn) {
            statusTextView.setText("Airplane Mode is ON");
            toggleButton.setBackgroundColor(Color.GREEN);
        } else {
            statusTextView.setText("Airplane Mode is OFF");
            toggleButton.setBackgroundColor(Color.RED);
        }
    }

    // Проверка текущего состояния "В самолёте"
    private void updateAirplaneStatus() {
        boolean isAirplaneModeOn = Settings.Global.getInt(
                requireContext().getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        updateUI(isAirplaneModeOn);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAirplaneStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requireActivity().unregisterReceiver(airplaneReceiver);
    }
}




