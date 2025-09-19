package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void onEditCity(int position, City city);
    }
    // placeholders so compile time issues aren't raised for not matching type operations
    private final static String ARG_POSITION = "position";
    private final static String ARG_CITY = "city";
    private final static String ARG_PROVINCE = "province";
    private AddCityDialogListener listener;

    public static AddCityFragment editInstance(int position, City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();

        args.putSerializable("city", city);
        args.putInt("pos", position);

        fragment.setArguments(args);

        return fragment;
    }

    public static AddCityFragment addInstance() {
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args;
        if (getArguments() != null) {
            args = getArguments();
        }
        else {
            args = new Bundle();
        }

        City city = (City) args.getSerializable("city");

//       boolean isEditMode = args.containsKey("pos");
        boolean isEditMode = city != null && args.containsKey("pos");

        if (isEditMode) {
            assert city != null;
            editCityName.setText(city.getName());
            editProvinceName.setText(city.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (isEditMode) {
                        int position = args.getInt("pos");
                        listener.onEditCity(position, new City(cityName, provinceName));
                    }
                    else{
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}