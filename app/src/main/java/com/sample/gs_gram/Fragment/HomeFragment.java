package com.sample.gs_gram.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sample.gs_gram.Activity.CSVActivity;
import com.sample.gs_gram.R;
import com.sample.gs_gram.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding mBinding;
    private boolean[][] isChecked = new boolean[10][5];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        mBinding = FragmentHomeBinding.inflate(inflater);
        View view = mBinding.getRoot();

        mBinding.timeRow0Col0.setOnClickListener(onClickListener);
        mBinding.timeRow0Col1.setOnClickListener(onClickListener);
        mBinding.timeRow0Col2.setOnClickListener(onClickListener);
        mBinding.timeRow0Col3.setOnClickListener(onClickListener);
        mBinding.timeRow0Col4.setOnClickListener(onClickListener);

        mBinding.initCellStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = new boolean[10][5];
                mBinding.timeRow0Col0.setBackgroundResource(R.drawable.schedule_cell);
                mBinding.timeRow0Col1.setBackgroundResource(R.drawable.schedule_cell);
                mBinding.timeRow0Col2.setBackgroundResource(R.drawable.schedule_cell);
                mBinding.timeRow0Col3.setBackgroundResource(R.drawable.schedule_cell);
                mBinding.timeRow0Col4.setBackgroundResource(R.drawable.schedule_cell);
            }
        });

        mBinding.csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CSVActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.timeRow0Col0:
                    if (isChecked[0][0]) {
                        mBinding.timeRow0Col0.setBackgroundResource(R.drawable.schedule_cell);
                        isChecked[0][0] = false;
                        Log.d("test data", Arrays.deepToString(isChecked));
                    } else {
                        mBinding.timeRow0Col0.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        isChecked[0][0] = true;
                        Log.d("test data", Arrays.deepToString(isChecked));
                    }
                    break;
                case R.id.timeRow0Col1:
                    if (isChecked[0][1]) {
                        mBinding.timeRow0Col1.setBackgroundResource(R.drawable.schedule_cell);
                        isChecked[0][1] = false;
                        Log.d("test data", Arrays.deepToString(isChecked));
                    } else {
                        mBinding.timeRow0Col1.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        isChecked[0][1] = true;
                        Log.d("test data", Arrays.deepToString(isChecked));
                    }
                    break;

                case R.id.timeRow0Col2:
                    if (isChecked[0][2]) {
                        mBinding.timeRow0Col2.setBackgroundResource(R.drawable.schedule_cell);
                        isChecked[0][2] = false;
                    } else {
                        mBinding.timeRow0Col2.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        isChecked[0][2] = true;
                    }
                    break;

                case R.id.timeRow0Col3:
                    if (isChecked[0][3]) {
                        mBinding.timeRow0Col3.setBackgroundResource(R.drawable.schedule_cell);
                        isChecked[0][3] = false;
                    } else {
                        mBinding.timeRow0Col3.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        isChecked[0][3] = true;
                    }
                    break;

                case R.id.timeRow0Col4:
                    if (isChecked[0][4]) {
                        mBinding.timeRow0Col4.setBackgroundResource(R.drawable.schedule_cell);
                        isChecked[0][4] = false;
                    } else {
                        mBinding.timeRow0Col4.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        isChecked[0][4] = true;
                    }
                    break;
            }
        }
    };

}
