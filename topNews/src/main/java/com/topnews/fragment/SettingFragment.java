package com.topnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topnews.R;


public class SettingFragment extends Fragment {

    private View v;

    public static SettingFragment newInstance(){
        SettingFragment settingFragment = new SettingFragment();
        return settingFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity()).inflate(R.layout.setting_fragment, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}
