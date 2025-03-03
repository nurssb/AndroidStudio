package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

public class MusicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        Button btnPlay = view.findViewById(R.id.btnPlay);
        Button btnPause = view.findViewById(R.id.btnPause);
        Button btnStop = view.findViewById(R.id.btnStop);

        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MusicService.class);
            intent.putExtra("action", "play");
            getActivity().startService(intent);
        });

        btnPause.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MusicService.class);
            intent.putExtra("action", "pause");
            getActivity().startService(intent);
        });

        btnStop.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MusicService.class);
            getActivity().stopService(intent);
        });

        return view;
    }
}

