package com.sample.gs_gram.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sample.gs_gram.R;

import java.util.ArrayList;
import java.util.List;

public class SimulationFragment extends Fragment {
    private TextView total_credit,yearText,termText;
    private Button after_button;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        View view = inflater.inflate(R.layout.fragment_simulation,container,false);

        yearText = view.findViewById(R.id.yearText);
        termText = view.findViewById(R.id.termText);
        total_credit = view.findViewById(R.id.total_credit);
        after_button = view.findViewById(R.id.after_button);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        yearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] yearOptions = {"2023년","2022년","2021년","2020년","2019년","2018년","2017년"};
                showSelectionDialog("년도를 선택해주세요.", yearOptions, yearText);
            }
        });

        termText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] termOptions = {"1학기","2학기"};
                showSelectionDialog("학기를 선택해주세요.", termOptions,termText);
            }
        });

        DocumentReference documentReference = mStore.collection("UserDataList").document(mAuth.getUid());
        after_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String period = yearText.getText().toString() + " " +termText.getText().toString();

                Log.d("asdqwe","click");

            }
        });

        return view;
    }

    private void showSelectionDialog(String title, String[] options, final TextView targetTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedItem = options[which];
                        targetTextView.setText(selectedItem);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
