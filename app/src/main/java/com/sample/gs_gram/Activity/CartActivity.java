package com.sample.gs_gram.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.sample.gs_gram.Adapter.CartAdapter;
import com.sample.gs_gram.Adapter.SaveAdapter;
import com.sample.gs_gram.Data.SubjectData;
import com.sample.gs_gram.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView termText, yearText,set_button;
    private RecyclerView saveRecyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private CartAdapter mAdapter;
    private ArrayList<SubjectData> selectedItems = new ArrayList<>();
    private ArrayList<SubjectData> mDatas = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_cart);

        termText = findViewById(R.id.termText);
        yearText = findViewById(R.id.yearText);
        set_button = findViewById(R.id.set_button);
        saveRecyclerView = findViewById(R.id.saveRecyclerView);

        saveRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        saveRecyclerView.setHasFixedSize(true);
        saveRecyclerView.setLayoutManager(layoutManager);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("선택 내역");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                ;
            }
        });

        yearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] yearOptions = {"2023년","2022년","2021년","2020년","2019년","2018년","2017년"};
                showSelectionDialog("수강 년도를 선택해주세요.", yearOptions, yearText);
            }
        });

        termText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] termOptions = {"1학년 1학기","1학년 2학기","2학년 1학기","2학년 2학기","3학년 1학기","3학년 2학기","4학년 1학기","4학년 2학기","5학년 1학기","5학년 2학기","1학년 여름계절학기","1학년 겨울계절학기","2학년 여름계절학기","2학년 겨울계절학기","3학년 여름계절학기","3학년 겨울계절학기","4학년 여름계절학기","4학년 겨울계절학기"};
                showSelectionDialog("수강 학년, 학기를 선택해주세요.", termOptions, termText);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        selectedItems = (ArrayList<SubjectData>) getIntent().getSerializableExtra("selectedItems");

        for (SubjectData subjectData : selectedItems) {

            SubjectData data = new SubjectData();
            data.setSubject(subjectData.getSubject());
            data.setDivition(subjectData.getDivition());
            data.setTerm(subjectData.getTerm());
            data.setCredit(subjectData.getCredit());
            data.setCode(subjectData.getCode());
            data.setField(subjectData.getField());

            mDatas.add(data);
        }
        mAdapter = new CartAdapter(mDatas);
        saveRecyclerView.setLayoutManager(layoutManager);
        saveRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SubjectData clickedData) {
                mDatas.remove(clickedData);
                mAdapter.notifyDataSetChanged();
            }
        });

    }
    //AlertDialog 생성 메소드
    private void showSelectionDialog(String title, String[] options, final TextView targetTextView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
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
    private void loadData(){
        String strYear = yearText.getText().toString();
        String strTerm = termText.getText().toString();

        if (strYear.length() == 0) {
            Toast.makeText(CartActivity.this, "수강 년도를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if (strTerm.length() == 0) {
            Toast.makeText(CartActivity.this, "이수 학년, 학기를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else {
            CollectionReference collectionReference = mStore.collection("UserDataList");
            DocumentReference documentReference = collectionReference.document(mAuth.getUid());

            Map<String, Object> dataArray = new HashMap<>();

            for (SubjectData subjectData : mDatas) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("subject", subjectData.getSubject());
                itemData.put("divition", subjectData.getDivition());
                itemData.put("term", subjectData.getTerm());
                itemData.put("credit", subjectData.getCredit());
                itemData.put("code", subjectData.getCode());
                itemData.put("field", subjectData.getField());
                itemData.put("period", strYear + " " + strTerm);

                dataArray.put(subjectData.getCode(), itemData);
            }

            documentReference.set(dataArray, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });

            }
        }

}
