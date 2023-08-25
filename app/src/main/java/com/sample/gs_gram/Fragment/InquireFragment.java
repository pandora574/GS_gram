package com.sample.gs_gram.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sample.gs_gram.Adapter.InquireAdapter;
import com.sample.gs_gram.Data.SubjectData;
import com.sample.gs_gram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InquireFragment extends Fragment {
    private TextView yearText, termText, inquire_button;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private InquireAdapter mAdapter;
    private RecyclerView inquireRecyclerView;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        View view = inflater.inflate(R.layout.fragment_inquire,container,false);

        yearText = view.findViewById(R.id.yearText);
        termText = view.findViewById(R.id.termText);
        inquire_button =  view.findViewById(R.id.inquire_button);
        inquireRecyclerView = view.findViewById(R.id.inquireRecyclerView);

        inquireRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),1));
        inquireRecyclerView.setHasFixedSize(true);
        inquireRecyclerView.setLayoutManager(layoutManager);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        yearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] yearOptions = {"2023년","2022년","2021년","2020년","2019년","2018년","2017년"};
                showSelectionDialog("이수 년도를 선택해주세요.", yearOptions, yearText);
            }
        });

        termText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] termOptions = {"1학년 1학기","1학년 2학기","2학년 1학기","2학년 2학기","3학년 1학기","3학년 2학기","4학년 1학기","4학년 2학기","5학년 1학기","5학년 2학기","1학년 여름계절학기","1학년 겨울계절학기","2학년 여름계절학기","2학년 겨울계절학기","3학년 여름계절학기","3학년 겨울계절학기","4학년 여름계절학기","4학년 겨울계절학기"};
                showSelectionDialog("과목을 이수한 학기를 선택해주세요.", termOptions,termText);
            }
        });

        inquire_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InquireData();
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
    public void InquireData(){
        String strYear = yearText.getText().toString();
        String strTerm = termText.getText().toString();
        String period = strYear + " " +strTerm;

        if (strYear.length() == 0) {
            Toast.makeText(getContext(), "이수 년도를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else if (strTerm.length() == 0) {
            Toast.makeText(getContext(), "조회할 학기를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            CollectionReference collectionReference = mStore.collection("UserDataList");
            DocumentReference documentReference = collectionReference.document(mAuth.getUid());

            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Map<String, Object> userData = documentSnapshot.getData();

                        // userData에서 각 item을 추출하여 mDatas에 추가
                        ArrayList<SubjectData> mDatas = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : userData.entrySet()) {
                            String code = entry.getKey();
                            Map<String, Object> itemData = (Map<String, Object>) entry.getValue();

                            String itemperiod = String.valueOf(itemData.get("period"));
                            Log.d("asdqwe",itemperiod + "그리고"+period);
                            if (itemperiod.equals(period)) {
                                SubjectData data = new SubjectData();
                                data.setCode(code);
                                data.setSubject(String.valueOf(itemData.get("subject")));
                                data.setDivition(String.valueOf(itemData.get("divition")));
                                data.setTerm(String.valueOf(itemData.get("term")));
                                data.setCredit(String.valueOf(itemData.get("credit")));
                                data.setCode(String.valueOf(itemData.get("code")));
                                data.setField(String.valueOf(itemData.get("field")));

                                mDatas.add(data);
                            }
                        }

                        // 여기서부터는 어댑터 설정과 화면 업데이트 코드가 들어갑니다.
                        mAdapter = new InquireAdapter(mDatas);
                        inquireRecyclerView.setLayoutManager(layoutManager);
                        inquireRecyclerView.setAdapter(mAdapter);
                    }
                }
            });
        }
    }
}