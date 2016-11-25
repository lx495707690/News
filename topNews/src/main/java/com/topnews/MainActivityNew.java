package com.topnews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.topnews.fragment.HomeFragment;
import com.topnews.fragment.MessageFragment;
import com.topnews.fragment.SettingFragment;


public class MainActivityNew extends FragmentActivity implements BottomNavigationBar.OnTabSelectedListener {

    public static final String NOWFRAGMENT = "nowFragment";
    public static final String HOMEFRAGMENT = "HomeFragment";
    public static final String MESSAGEFRAGMENT = "MessageFragment";
    public static final String SETTINGFRAGMENT = "SettingFragment";

    private Fragment mContent;
    private BottomNavigationBar bottomNavigationBar;

    private HomeFragment homeFragment;
    private MessageFragment messageFragment;
    private SettingFragment settingFragment;
    private FragmentManager fragmentManager;

    int lastSelectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_new);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomLayout);
        bottomNavigationBar.setTabSelectedListener(this);

        refresh();

        initFragment(savedInstanceState);
    }

    private void refresh() {

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        bottomNavigationBar.setBarBackgroundColor("#F5F5F5");
        bottomNavigationBar.setInActiveColor(R.color.gray);
        bottomNavigationBar.setActiveColor(R.color.blue_normal);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_drawer_appstore_normal, getString(R.string.news_home)))
                .addItem(new BottomNavigationItem(R.drawable.ic_drawer_feedback_normal, getString(R.string.news_message)))
                .addItem(new BottomNavigationItem(R.drawable.ic_drawer_setting_normal, getString(R.string.news_setting)))
                .setFirstSelectedPosition(lastSelectedPosition > 2 ? 2 : lastSelectedPosition)
                .initialise();
    }

    private void initFragment(Bundle save) {
        fragmentManager = getSupportFragmentManager();
        if(save == null) {
            homeFragment = HomeFragment.newInstance();
            messageFragment = MessageFragment.newInstance();
            settingFragment = SettingFragment.newInstance();
            mContent = homeFragment;
            fragmentManager.beginTransaction()
                    .add(R.id.layoutFragment, mContent,HOMEFRAGMENT)
                    .commit();
        }else {

            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HOMEFRAGMENT);
            messageFragment = (MessageFragment) fragmentManager.findFragmentByTag(MESSAGEFRAGMENT);
            settingFragment = (SettingFragment) fragmentManager.findFragmentByTag(SETTINGFRAGMENT);

            mContent = fragmentManager.findFragmentByTag(save.getString(NOWFRAGMENT));
            fragmentManager.beginTransaction()
                    .show(homeFragment)
                    .commit();
        }
    }

    /**
     * 切换fragment
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to, String tag){
        if(mContent != to){
            mContent = to;
            //添加动画
            FragmentTransaction transaction = fragmentManager.beginTransaction()
                    ;
            if(to.isAdded()){
                transaction.hide(from).show(to);
            }else {
                transaction.hide(from).add(R.id.layoutFragment, to, tag);
            }
            transaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NOWFRAGMENT, mContent.getTag());
    }

    @Override
    public void onTabSelected(int position) {

        lastSelectedPosition = position;

        switch (position){
            case 0:
                switchContent(mContent,homeFragment,HOMEFRAGMENT);
                break;
            case 1:
                switchContent(mContent,messageFragment,MESSAGEFRAGMENT);
                break;
            case 2:
                switchContent(mContent,settingFragment,SETTINGFRAGMENT);
                break;
            default:
                switchContent(mContent,homeFragment,HOMEFRAGMENT);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}