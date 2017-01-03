package com.topnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.topnews.LoginActivity;
import com.topnews.R;
import com.topnews.helper.Constants;
import com.topnews.helper.Helper;
import com.topnews.helper.UserInfoManager;


public class SettingFragment extends Fragment {

    private View v;

    private ImageView imgAvatar;
    private Button btn;


    public static SettingFragment newInstance(){
        SettingFragment settingFragment = new SettingFragment();
        return settingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity()).inflate(R.layout.setting_fragment, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        setListener();
    }

    private void initView(){
        imgAvatar = (ImageView) v.findViewById(R.id.imgAvatar);
        Helper.loadImageNewNoCache(getActivity(), Constants.IMAGE_BASE_URL + UserInfoManager.getInstance(getActivity()).getAvatar(),imgAvatar);

        btn = (Button) v.findViewById(R.id.btn);

        if(UserInfoManager.getInstance(getActivity()).getToken() == null){
            btn.setText("登录");
        }else{
            btn.setText("退出");
        }
    }

    private void setListener(){
//        imgAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(),SignUpActivity.class));
//            }
//        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserInfoManager.getInstance(getActivity()).getToken() == null){
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else{
                    UserInfoManager.getInstance(getActivity()).logout();
                    getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                }
            }
        });
    }


}
