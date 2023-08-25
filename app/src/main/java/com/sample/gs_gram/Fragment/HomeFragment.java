package com.sample.gs_gram.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.sample.gs_gram.Activity.CSVActivity;
import com.sample.gs_gram.R;

public class HomeFragment extends Fragment {
    private Button csv_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        csv_button = view.findViewById(R.id.csv_button);

        csv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CSVActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
