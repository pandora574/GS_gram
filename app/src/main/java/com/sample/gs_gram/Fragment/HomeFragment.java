package com.sample.gs_gram.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.sample.gs_gram.Activity.CSVActivity;
import com.sample.gs_gram.R;
import com.sample.gs_gram.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIstanceState){
        mBinding = FragmentHomeBinding.inflate(inflater);
        View view = mBinding.getRoot();

        mBinding.timeRow1Col1.setOnClickListener(onClickListener);
        mBinding.timeRow1Col2.setOnClickListener(onClickListener);
        mBinding.timeRow1Col3.setOnClickListener(onClickListener);
        mBinding.timeRow1Col4.setOnClickListener(onClickListener);
        mBinding.timeRow1Col5.setOnClickListener(onClickListener);

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
                case R.id.timeRow1Col1:
                    boolean isClicked = false;
                    if (isClicked) {
                        mBinding.timeRow1Col1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        isClicked = false;
                        Log.d("test data", "clicked");
                    } else {
                        mBinding.timeRow1Col1.setBackgroundColor(Color.parseColor("#FF0000"));
                        isClicked = true;
                        Log.d("test data", "!clicked");
                    }
                    break;
                case R.id.timeRow1Col2:
                    break;
            }
        }
    };



}
