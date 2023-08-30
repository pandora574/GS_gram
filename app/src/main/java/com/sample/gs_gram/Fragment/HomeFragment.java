package com.sample.gs_gram.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sample.gs_gram.Activity.CSVActivity;
import com.sample.gs_gram.Data.ScheduleCellData;
import com.sample.gs_gram.R;
import com.sample.gs_gram.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding mBinding;
    private String[][] cellStatus = new String[8][5];
    private boolean modifySchedule = false;
    private ScheduleCellData[][] scheduleCellDatas = new ScheduleCellData[8][5];
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private Integer[] colors = {Color.parseColor("#FAED7D"), Color.parseColor("#CEF279"), Color.parseColor("#D1B2FF"), Color.parseColor("#FFB2D9"), Color.parseColor("#FFA7A7")};
    private int colorIndex = 0;
    private String currentGrade = "1학년 1학기";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater);
        View view = mBinding.getRoot();

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setCLickListener(view);

        mBinding.initCellStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCellStatus(view, false, false);
            }
        });

        mBinding.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(view);
            }
        });

        mBinding.modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modifySchedule) {
                    uploadToFireStore();
                    modifySchedule = false;
                    mBinding.modifyBtn.setText("수정");
                    mBinding.addScheduleBtn.setVisibility(View.INVISIBLE);
                    mBinding.initCellStateBtn.setVisibility(View.INVISIBLE);
                    mBinding.gradeSpinner.setVisibility(View.VISIBLE);
                    initCellStatus(view, true, false);
                } else {
                    modifySchedule = true;
                    mBinding.modifyBtn.setText("저장");
                    mBinding.addScheduleBtn.setVisibility(View.VISIBLE);
                    mBinding.initCellStateBtn.setVisibility(View.VISIBLE);
                    mBinding.gradeSpinner.setVisibility(View.INVISIBLE);
                }
            }
        });

        mBinding.csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CSVActivity.class);
                startActivity(intent);
            }
        });

        String[] spinnerItems = {"1학년 1학기", "1학년 2학기", "2학년 1학기", "2학년 2학기", "3학년 1학기", "3학년 2학기", "4학년 1학기", "4학년 2학기", "5학년 1학기", "5학년 2학기"};
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerItems);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.gradeSpinner.setAdapter(gradeAdapter);

        mBinding.gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentGrade = spinnerItems[position];
                Log.d("test spinner", spinnerItems[position]);
                downloadFromFirestore(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentGrade = spinnerItems[0];
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadFromFirestore(getView());
    }

    View.OnClickListener onModifyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.timeRow0Col0:
                    setCell(0, 0, view);
                    break;
                case R.id.timeRow0Col1:
                    setCell(0, 1, view);
                    break;
                case R.id.timeRow0Col2:
                    setCell(0, 2, view);
                    break;
                case R.id.timeRow0Col3:
                    setCell(0, 3, view);
                    break;
                case R.id.timeRow0Col4:
                    setCell(0, 4, view);
                    break;

                case R.id.timeRow1Col0:
                    setCell(1, 0, view);
                    break;
                case R.id.timeRow1Col1:
                    setCell(1, 1, view);
                    break;
                case R.id.timeRow1Col2:
                    setCell(1, 2, view);
                    break;
                case R.id.timeRow1Col3:
                    setCell(1, 3, view);
                    break;
                case R.id.timeRow1Col4:
                    setCell(1, 4, view);
                    break;

                case R.id.timeRow2Col0:
                    setCell(2, 0, view);
                    break;
                case R.id.timeRow2Col1:
                    setCell(2, 1, view);
                    break;
                case R.id.timeRow2Col2:
                    setCell(2, 2, view);
                    break;
                case R.id.timeRow2Col3:
                    setCell(2, 3, view);
                    break;
                case R.id.timeRow2Col4:
                    setCell(2, 4, view);
                    break;

                case R.id.timeRow3Col0:
                    setCell(3, 0, view);
                    break;
                case R.id.timeRow3Col1:
                    setCell(3, 1, view);
                    break;
                case R.id.timeRow3Col2:
                    setCell(3, 2, view);
                    break;
                case R.id.timeRow3Col3:
                    setCell(3, 3, view);
                    break;
                case R.id.timeRow3Col4:
                    setCell(3, 4, view);
                    break;

                case R.id.timeRow4Col0:
                    setCell(4, 0, view);
                    break;
                case R.id.timeRow4Col1:
                    setCell(4, 1, view);
                    break;
                case R.id.timeRow4Col2:
                    setCell(4, 2, view);
                    break;
                case R.id.timeRow4Col3:
                    setCell(4, 3, view);
                    break;
                case R.id.timeRow4Col4:
                    setCell(4, 4, view);
                    break;

                case R.id.timeRow5Col0:
                    setCell(5, 0, view);
                    break;
                case R.id.timeRow5Col1:
                    setCell(5, 1, view);
                    break;
                case R.id.timeRow5Col2:
                    setCell(5, 2, view);
                    break;
                case R.id.timeRow5Col3:
                    setCell(5, 3, view);
                    break;
                case R.id.timeRow5Col4:
                    setCell(5, 4, view);
                    break;

                case R.id.timeRow6Col0:
                    setCell(6, 0, view);
                    break;
                case R.id.timeRow6Col1:
                    setCell(6, 1, view);
                    break;
                case R.id.timeRow6Col2:
                    setCell(6, 2, view);
                    break;
                case R.id.timeRow6Col3:
                    setCell(6, 3, view);
                    break;
                case R.id.timeRow6Col4:
                    setCell(6, 4, view);
                    break;

                case R.id.timeRow7Col0:
                    setCell(7, 0, view);
                    break;
                case R.id.timeRow7Col1:
                    setCell(7, 1, view);
                    break;
                case R.id.timeRow7Col2:
                    setCell(7, 2, view);
                    break;
                case R.id.timeRow7Col3:
                    setCell(7, 3, view);
                    break;
                case R.id.timeRow7Col4:
                    setCell(7, 4, view);
                    break;

            }
        }
    };

    private void setCLickListener(View view) {
        initCellStatus(view, false, false);
        for (int i = 0; i < cellStatus.length; i++) {
            for (int j = 0; j < cellStatus[i].length; j++) {
                String idString = String.format("timeRow%sCol%s", i, j);
                int resId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
                if (resId == 0) {
                    continue;
                }
                TextView tempTxt = view.findViewById(resId);
                tempTxt.setOnClickListener(onModifyClickListener);
            }
        }
    }

    private void initCellStatus(View view, boolean initisChecked, boolean all) {
        for (int i = 0; i < cellStatus.length; i++) {
            for (int j = 0; j < cellStatus[i].length; j++) {
                if (all) {
                    scheduleCellDatas = new ScheduleCellData[8][5];
                    if (cellStatus[i][j] != null) {
                        String idString = String.format("timeRow%sCol%s", i, j);
                        int resId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
                        TextView tempTxt = getActivity().findViewById(resId);
                        if (tempTxt != null) {
                            Log.d("test progressed", "init 실행됨(true)"+i+", "+j);
                            tempTxt.setBackgroundResource(R.drawable.schedule_cell);
                            tempTxt.setText("");
                        } else {
                            Log.d("test progressed null", "tempTxt is null(true)"+i+", "+j);
                        }
                    }
                } else {
                    if (cellStatus[i][j] != null) {
                        if (!cellStatus[i][j].equals("using")) {
                            String idString = String.format("timeRow%sCol%s", i, j);
                            int resId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
                            TextView tempTxt = getActivity().findViewById(resId);
                            if (tempTxt != null) {
                                Log.d("test progressed", "init 실행됨(false)");
                                tempTxt.setBackgroundResource(R.drawable.schedule_cell);
                                tempTxt.setText("");
                            } else {
                                Log.d("test progressed null", "tempTxt is null(false)");
                            }
                        }
                    }

                }
            }
        }
        initcellStatusChecked(initisChecked);
        changeCellStatus();
    }

    private void changeCellStatus() {
        ArrayList<String> testing = new ArrayList<>();
        for (int i = 0; i < cellStatus.length; i++) {
            for (int j = 0; j < cellStatus[i].length; j++) {
                testing.add(i+","+j);
                String idString = String.format("timeRow%sCol%s", i, j);
                int resId = getResources().getIdentifier(idString, "id", getActivity().getPackageName());
                TextView tempTxt = getActivity().findViewById(resId);
                if (tempTxt != null) {
                    if (cellStatus[i][j].equals("true")) {
                        tempTxt.setBackgroundColor(Color.RED);
                    } else if (cellStatus[i][j].equals("false")) {
                        tempTxt.setBackgroundResource(R.drawable.schedule_cell);
                        tempTxt.setText("");
                    } else if (cellStatus[i][j].equals("using")) {
                        ScheduleCellData data = scheduleCellDatas[i][j];
                        tempTxt.setBackgroundColor(data.getColor());
                        tempTxt.setText(data.getTitle());
                    }
                } else {
                    Log.d("test progressed null", "tempTxt is null(change)");
                }
            }
        }
        Log.d("test testing", String.valueOf(testing));
    }

    private void initcellStatusChecked(boolean isChecked) {
        if (isChecked) {
            for (int i = 0; i < cellStatus.length; i++) {
                for (int j = 0; j < cellStatus[i].length; j++) {
                    if (cellStatus[i][j] == null || cellStatus.equals("true")) {
                        cellStatus[i][j] = "false";
                    }
                }
            }
        } else {
            for (int i = 0; i < cellStatus.length; i++) {
                for (int j = 0; j < cellStatus[i].length; j++) {
                    cellStatus[i][j] = "false";
                }
            }
        }
    }

    private void showDialog(View view) {
        ArrayList<String> selectedCoordinate = new ArrayList<>();
        for (int i = 0; i < cellStatus.length; i++) {
            for (int j = 0; j < cellStatus[i].length; j++) {
                if (cellStatus[i][j].equals("true")) {
                    selectedCoordinate.add(i + "," + j);
                }
            }
        }
        View dialogview = LayoutInflater.from(getActivity()).inflate(R.layout.add_schedule_dialog, null);
        TextView titleTxt = dialogview.findViewById(R.id.titleTxt);
        TextView professorNameTxt = dialogview.findViewById(R.id.professorNameTxt);
        TextView locationTxt = dialogview.findViewById(R.id.locationTxt);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogview)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (String k : selectedCoordinate) {
                            String[] temp = k.split(",");
                            int row = Integer.parseInt(temp[0]);
                            int col = Integer.parseInt(temp[1]);
                            ScheduleCellData data = new ScheduleCellData(row, col, colors[colorIndex], titleTxt.getText().toString(), "time", locationTxt.getText().toString(), professorNameTxt.getText().toString());
                            scheduleCellDatas[row][col] = data;
                            cellStatus[row][col] = "using";
                            changeCellStatus();
                        }
                        if (colorIndex == colors.length) {
                            colorIndex = 0;
                        } else {
                            colorIndex++;
                        }
                    }
                })
                .create();
        dialog.show();
    }

    private void setCell(int row, int col, View view) {
        String StringID = String.format("timeRow%sCol%s", row, col);
        int resId = getResources().getIdentifier(StringID, "id", getActivity().getPackageName());
        TextView tempTextview = view.findViewById(resId);
        if (modifySchedule) {
            if (cellStatus[row][col].equals("true")) {
                tempTextview.setBackgroundResource(R.drawable.schedule_cell);
                cellStatus[row][col] = "false";
            } else if (cellStatus[row][col].equals("false")) {
                tempTextview.setBackgroundColor(Color.parseColor("#D3D3D3"));
                cellStatus[row][col] = "true";
            }
        } else {
            Toast.makeText(getActivity(), "row: " + row + ", col: " + col, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadToFireStore() {
        Map<String, ScheduleCellData> cellDatas = new HashMap<>();
        for (int i = 0; i < cellStatus.length; i++) {
            for (int j = 0; j < cellStatus[i].length; j++) {
                if (cellStatus[i][j].equals("using")) {
                    cellDatas.put(i + "-" + j, scheduleCellDatas[i][j]);
                    Log.d("test count", cellDatas.get(i + "-" + j).getTime());
                }
            }
        }
        mStore.collection("Account")
                .document(mAuth.getUid())
                .collection("schedule")
                .document(currentGrade)
                .set(cellDatas).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void downloadFromFirestore(View view) {
        initCellStatus(view, false, true);
        Log.d("test getData", currentGrade);
        mStore.collection("Account").document(mAuth.getUid())
                .collection("schedule").document(currentGrade)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Map<String, Object> data = documentSnapshot.getData();
                                for (String coordinate : data.keySet()) {
                                    Map<String, Object> oneData = (Map<String, Object>) data.get(coordinate);
                                    ScheduleCellData scheduleCellData = new ScheduleCellData(
                                            Integer.parseInt(oneData.get("row").toString()),
                                            Integer.parseInt(oneData.get("col").toString()),
                                            Integer.parseInt(oneData.get("color").toString()),
                                            oneData.get("title").toString(),
                                            oneData.get("time").toString(),
                                            oneData.get("location").toString(),
                                            oneData.get("professorName").toString());
                                    String[] splitedcoordinate = coordinate.split("-");
                                    Integer[] Coor = Stream.of(splitedcoordinate).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
                                    scheduleCellDatas[Coor[0]][Coor[1]] = scheduleCellData;
                                    cellStatus[Coor[0]][Coor[1]] = "using";
                                    changeCellStatus();
                                }
                            } else {
                                initCellStatus(view, false, false);
                                Log.d("test dataExist", "no");
                            }
                            Log.d("test data", Arrays.deepToString(scheduleCellDatas));
                        }
                    }
                });
    }
}
