package com.sample.gs_gram.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sample.gs_gram.Activity.CartActivity;
import com.sample.gs_gram.Adapter.SaveAdapter;
import com.sample.gs_gram.Data.SubjectData;
import com.sample.gs_gram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaveFragment extends Fragment {
    private TextView gradeText,termText, divitionText, view_button;
    private Button cart_button;
    private RecyclerView subjectRecyclerView;
    private SaveAdapter mAdapter;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private String UserMajor;
    private ArrayList<SubjectData> selectedItems = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        View view = inflater.inflate(R.layout.fragment_save,container,false);

        divitionText = view.findViewById(R.id.divitionText);
        termText = view.findViewById(R.id.termText);
        gradeText = view.findViewById(R.id.gradeText);
        view_button = view.findViewById(R.id.view_button);
        cart_button = view.findViewById(R.id.cart_button);
        subjectRecyclerView = view.findViewById(R.id.subjectRecyclerView);

        subjectRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(layoutManager);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mStore.collection("Account").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        UserMajor = documentSnapshot.getString("major");
                    }
                }
            }
        });

        divitionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] divitionOptions = {"교양","교선","교필","전선","전필"};
                showSelectionDialog("이수 구분을 선택해주세요.", divitionOptions, divitionText);
            }
        });

        gradeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] gradeOptions = {"1학년","2학년","3학년","4학년"};
                showSelectionDialog("학년을 선택해주세요", gradeOptions, gradeText);
            }
        });

        termText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] termOptions = {"1학기","2학기"};
                showSelectionDialog("학기를 선택해주세요.", termOptions, termText);
            }
        });

        //장바구니 이동
        cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendItem();
            }
        });

        //과목 조회
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewSubjectData();
            }
        });


        return view;
    }

    private void showSelectionDialog(String title, String[] options, final TextView targetTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedOption = options[which];
                        targetTextView.setText(selectedOption);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void ViewSubjectData(){
        String strDivition = divitionText.getText().toString();
        String strGrade = gradeText.getText().toString();
        String strTerm = termText.getText().toString();

        if (strDivition.length() == 0) {
            Toast.makeText(getContext(), "이수 구분을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else if(strGrade.length() == 0){
            Toast.makeText(getContext(), "교과목 학년을 선택해주세요", Toast.LENGTH_SHORT).show();
        }else if (strTerm.length() == 0) {
            Toast.makeText(getContext(), "교과목 학기를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            if (strDivition.equals("교양")) {
                fetchSubjectData("전학과/" + strDivition + "/전학기");
            } else {
                fetchSubjectData(UserMajor + "/" + strDivition + "/" + strTerm);
            }

        }
    }
    private void fetchSubjectData(String collectionPath) {
        ArrayList<SubjectData> mDatas = new ArrayList<>();
        String strDivition = divitionText.getText().toString();
        String strGrade = gradeText.getText().toString();
        mStore.collection(collectionPath).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Map<String, Object> shot = documentSnapshot.getData();
                        String documentGrade = String.valueOf(shot.get("grade")); // Get the grade from the document
                        if (strGrade.equals(documentGrade)) { // Compare with strGrade
                            SubjectData data = new SubjectData();
                            data.setDivition(String.valueOf(shot.get("divition")));
                            data.setSubject(String.valueOf(shot.get("subject")));
                            data.setGrade(documentGrade); // Set the grade
                            data.setTerm(String.valueOf(shot.get("term")));
                            data.setCredit(String.valueOf(shot.get("credit")));
                            data.setCode(String.valueOf(shot.get("code")));
                            data.setField(String.valueOf(shot.get("field")));

                            boolean isCodeAlreadySelected = false;
                            for (SubjectData selectedData : selectedItems) {
                                if (selectedData.getCode().equals(data.getCode())) {
                                    isCodeAlreadySelected = true;
                                    break;
                                }
                            }

                            if (!isCodeAlreadySelected) {
                                mDatas.add(data);
                            }
                        } else if (strDivition.equals("교양")) {
                            SubjectData data = new SubjectData();
                            data.setDivition(String.valueOf(shot.get("divition")));
                            data.setSubject(String.valueOf(shot.get("subject")));
                            data.setGrade(String.valueOf(shot.get("grade")));
                            data.setTerm(String.valueOf(shot.get("term")));
                            data.setCredit(String.valueOf(shot.get("credit")));
                            data.setCode(String.valueOf(shot.get("code")));
                            data.setField(String.valueOf(shot.get("field")));

                            boolean isCodeAlreadySelected = false;
                            for (SubjectData selectedData : selectedItems) {
                                if (selectedData.getCode().equals(data.getCode())) {
                                    isCodeAlreadySelected = true;
                                    break;
                                }
                            }

                            if (!isCodeAlreadySelected) {
                                mDatas.add(data);
                            }
                        }
                    }
                    mAdapter = new SaveAdapter(mDatas);
                    subjectRecyclerView.setLayoutManager(layoutManager);
                    subjectRecyclerView.setAdapter(mAdapter);
                    if (mDatas.isEmpty()) {
                        Toast.makeText(getContext(), "다른 학년, 학과를 골라주세요.", Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.setOnItemClickListener(new SaveAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(SubjectData clickedData) {
                            selectedItems.add(clickedData);
                        }
                    });
                }
            }
        });
    }
    private void SendItem(){
        Intent intent = new Intent(getActivity(), CartActivity.class);
        intent.putExtra("selectedItems", selectedItems);
        startActivity(intent);
    }
}