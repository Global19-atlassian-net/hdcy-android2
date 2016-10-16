package com.hdcy.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.fragment.first.FirstFragment;
import com.hdcy.app.fragment.fourth.FourthFragment;
import com.hdcy.app.fragment.second.SecondFragment;
import com.hdcy.app.fragment.third.ThirdFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by WeiYanGeorge on 2016-10-07.
 */

public class MainFragment extends BaseFragment {

    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];



    //声明相关变量
    private Toolbar toolbar;
    private TextView toolBarTv1;
    private TextView toolBarTv2;
    private TextView toolBarTv3;
    private TextView toolBarTv4;

    private ViewPager mainViewPager;
    private MainFragmentAdapter mainFragmentAdapter;
    private ArrayList<SupportFragment> fragmentArrayList = new ArrayList<>();



    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container,false);

        if(savedInstanceState == null){
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInstance();
            mFragments[THIRD] = ThirdFragment.newInstance();
            mFragments[FOURTH]= FourthFragment.newInstance();
/*            loadMultipleRootFragment(R.id.mainPager,FIRST,mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);*/
        }else {
            mFragments[FIRST] = findChildFragment(FirstFragment.class);
            mFragments[SECOND] = findChildFragment(SecondFragment.class);
            mFragments[THIRD] = findChildFragment(ThirdFragment.class);
            mFragments[FOURTH] = findChildFragment(FourthFragment.class);
        }
        initView(view);
        return view;
    }

    private void initView(View view){
        EventBus.getDefault().register(this);
        toolbar = (Toolbar) view.findViewById(R.id.tl_custom);
        toolBarTv1 = (TextView) view.findViewById(R.id.toolbar_btn1);
        toolBarTv2 = (TextView) view.findViewById(R.id.toolbar_btn2);
        toolBarTv3 = (TextView) view.findViewById(R.id.toolbar_btn3);
        toolBarTv4 = (TextView) view.findViewById(R.id.toolbar_btn4);
        toolBarTv1.setOnClickListener(onClickListener);
        toolBarTv2.setOnClickListener(onClickListener);
        toolBarTv3.setOnClickListener(onClickListener);
        toolBarTv4.setOnClickListener(onClickListener);
        toolBarTv1.setTextColor(getResources().getColor(R.color.colorPrimary));

        mainViewPager = (ViewPager) view.findViewById(R.id.mainPager);
        mainFragmentAdapter = new MainFragmentAdapter(getActivity().getSupportFragmentManager(),mFragments);

        mainViewPager.setAdapter(mainFragmentAdapter);
        mainViewPager.setCurrentItem(0);
        mainViewPager.setOnPageChangeListener(new MyOnPageChangeListen());

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.toolbar_btn1:
                    updateToolBarUi(0);
                    mainViewPager.setCurrentItem(0);
                    break;
                case R.id.toolbar_btn2:
                    updateToolBarUi(1);
                    mainViewPager.setCurrentItem(1);
                    break;
                case R.id.toolbar_btn3:
                    updateToolBarUi(2);
                    mainViewPager.setCurrentItem(2);
                    break;
                case R.id.toolbar_btn4:
                    updateToolBarUi(3);
                    mainViewPager.setCurrentItem(3);
                    break;
                default:
                    break;
            }
        }
    };

    private class MyOnPageChangeListen implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            updateToolBarUi(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void updateToolBarUi(int index){
        switch (index){
            case 0:
                toolBarTv1.setTextColor(getResources().getColor(R.color.colorPrimary));
                toolBarTv2.setTextColor(getResources().getColor(R.color.white));
                toolBarTv3.setTextColor(getResources().getColor(R.color.white));
                toolBarTv4.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                toolBarTv1.setTextColor(getResources().getColor(R.color.white));
                toolBarTv2.setTextColor(getResources().getColor(R.color.colorPrimary));
                toolBarTv3.setTextColor(getResources().getColor(R.color.white));
                toolBarTv4.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                toolBarTv1.setTextColor(getResources().getColor(R.color.white));
                toolBarTv2.setTextColor(getResources().getColor(R.color.white));
                toolBarTv3.setTextColor(getResources().getColor(R.color.colorPrimary));
                toolBarTv4.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                toolBarTv1.setTextColor(getResources().getColor(R.color.white));
                toolBarTv2.setTextColor(getResources().getColor(R.color.white));
                toolBarTv3.setTextColor(getResources().getColor(R.color.white));
                toolBarTv4.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private class MainFragmentAdapter extends FragmentPagerAdapter{

        SupportFragment[] sp;


        public MainFragmentAdapter(FragmentManager fm,SupportFragment[] data){
            super(fm);
            this.sp = data;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FirstFragment.newInstance();
                case 1:
                    return SecondFragment.newInstance();
                case 2:
                    return ThirdFragment.newInstance();
                case 3:
                    return FourthFragment.newInstance();
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}