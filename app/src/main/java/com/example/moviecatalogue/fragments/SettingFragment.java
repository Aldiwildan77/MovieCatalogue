package com.example.moviecatalogue.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviecatalogue.R;
import com.example.moviecatalogue.services.NotificationReceiver;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {

    private SharedPreferences mPreferences;
    private Switch reminder;

    private static final String KEY_REMINDER = "reminder";
    private static final boolean DEFAULT_REMINDER = false;

    private final String ACTIVITY_TITLE = "Setting";

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final NotificationReceiver notificationReceiver = new NotificationReceiver();

        getActivity().setTitle(ACTIVITY_TITLE);

        reminder = Objects.requireNonNull(getActivity()).findViewById(R.id.sw_reminder);
        mPreferences = getActivity().getSharedPreferences(getActivity().getApplication().toString(), MODE_PRIVATE);
        reminder.setChecked(mPreferences.getBoolean(KEY_REMINDER, DEFAULT_REMINDER));

        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putBoolean(KEY_REMINDER, isChecked);
                preferencesEditor.apply();

                reminder.setChecked(isChecked);

                notificationReceiver.setRepeatingAlarmRelease(getActivity(), isChecked);
            }
        });
    }

    public static String getKeyReminder() {
        return KEY_REMINDER;
    }

    public static boolean isDefaultReminder() {
        return DEFAULT_REMINDER;
    }
}
