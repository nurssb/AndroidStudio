package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private CalendarView calendarView;
    private Button btnAddEvent;
    private int selectedYear, selectedMonth, selectedDay;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // UI элементы
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recycler_view);
        btnAddEvent = view.findViewById(R.id.btn_add_event);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // адаптер
        eventAdapter = new EventAdapter(new ArrayList<>());
        recyclerView.setAdapter(eventAdapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_CALENDAR}, PERMISSION_REQUEST_CODE);
        } else {
            loadCalendarEvents();
        }

        // Обработчик выбора даты
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month;
            selectedDay = dayOfMonth;
            String selectedDate = formatDate(year, month, dayOfMonth);
            Log.d("CalendarFragment", "Выбрана дата: " + selectedDate);
            loadEventsForDate(selectedDate);
        });

        btnAddEvent.setOnClickListener(v -> addEventToCalendar());

        return view;
    }

    private void loadEventsForDate(String date) {
        List<String> events = getEventsForDate(date);
        Log.d("CalendarFragment", "События для даты " + date + ": " + events);
        eventAdapter.updateData(events);
    }

    // Получение событий из календаря устройства
    private List<String> getEventsForDate(String date) {
        List<String> eventList = new ArrayList<>();

        long startTime = getStartOfDayMillis(date);
        long endTime = startTime + 86400000; // +1 день в миллисекундах

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String[] projection = {CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};
        String selection = CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " < ?";
        String[] selectionArgs = {String.valueOf(startTime), String.valueOf(endTime)};
        String sortOrder = CalendarContract.Events.DTSTART + " ASC";

        Cursor cursor = requireContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String eventTitle = cursor.getString(0);
                eventList.add(eventTitle);
            }
            cursor.close();
        }

        return eventList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents();
        }
    }

    // Загрузка всех событий из календаря устройства
    private void loadCalendarEvents() {
        List<String> eventList = new ArrayList<>();

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String[] projection = {CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};

        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, CalendarContract.Events.DTSTART + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String eventTitle = cursor.getString(0);
                long eventDate = cursor.getLong(1);
                eventList.add(eventTitle + " - " + new Date(eventDate));
            }
            cursor.close();
        }

        eventAdapter.updateData(eventList);
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }

    private long getStartOfDayMillis(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(dateString).getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void addEventToCalendar() {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Новое событие");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Место");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Описание события");
        intent.putExtra(CalendarContract.Events.ALL_DAY, true);

        long startMillis = getStartOfDayMillis(formatDate(selectedYear, selectedMonth, selectedDay));
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);

        try {
            startActivity(intent);

            new Handler().postDelayed(() -> loadEventsForDate(formatDate(selectedYear, selectedMonth, selectedDay)), 2000);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Не удалось открыть календарь", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}






