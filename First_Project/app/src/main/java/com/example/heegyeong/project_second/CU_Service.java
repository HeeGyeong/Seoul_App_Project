package com.example.heegyeong.project_second;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class CU_Service extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu__service);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#8a2be2"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void onClickDetail(View view){
        Intent intent = null;

        switch(view.getId()){
            case R.id.service_detail_life :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/service/frugal_info.do;jsessionid=bfgJYV4DyRwk0rbG5ZLnwv2GpxszZxSzmpTNGMBhVv8gG9zhyhg2!428688026?category=service&depth3=9"));
                break;
            case R.id.service_detail_gift :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/service/gift_info.do?category=service&depth3=5"));
                break;
            case R.id.service_detail_post :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cvsnet.co.kr/postbox/m_home/index.jsp"));
                break;
        }
        if(intent != null)
            startActivity(intent);
    }

    public void onClickList(View view){
        switch(view.getId()) {

            case R.id.life_service_cu:
                final Dialog ServiceDialog = new Dialog(this);
                ServiceDialog.setContentView(R.layout.service);
                ServiceDialog.setTitle("서비스");
                ServiceDialog.show();
                break;
        }
    }
    public void onServiceClick(View view){
        Intent intent = null;

        switch(view.getId()) {
            case R.id.service_btn_cu:
                intent = new Intent(CU_Service.this, CU_Service.class);
                break;
            case R.id.service_btn_gs:
                intent = new Intent(CU_Service.this, GS_Service.class);
                break;
            case R.id.service_btn_eleven:
                intent = new Intent(CU_Service.this, Eleven_Service.class);
                break;
            case R.id.service_btn_mini:
                intent = new Intent(CU_Service.this, Mini_Service.class);
                break;
        }
        if(intent != null)
            startActivity(intent);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
/*          이 부분은 삭제해도 됨. 맨 마지막에 전체적으로 삭제 한 후에, 오류 확인하기.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cu_, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }*/
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            // return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return SectionsFragment1.newInstance(position + 1);
                case 1:
                    return SectionsFragment2.newInstance(position + 1);
                case 2:
                    return SectionsFragment3.newInstance(position + 1);
                case 3:
                    return SectionsFragment4.newInstance(position + 1);
                // tab 추가시 case 추가
                // SectionsFragment는 맨 아래에 추가되는 메소드 이름
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
            // tab 추가시 return 값도 증가
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "택배 서비스";
                case 1:
                    return "상품권/복권 판매";
                case 2:
                    return "공공요금 수납";
                case 3:
                    return "기타";
                // tab 추가시 case도 추가
                // return 되는 값은 해당 탭의 이름이 된다.
            }
            return null;
        }
    }
    // 이 밑으로는 각 Tab에 페이지를 할당하는 부분

    public static class SectionsFragment1 extends Fragment {

        public SectionsFragment1() {

        }

        // PlaceholderFragment.newInstance() 와 똑같이 추가
        static SectionsFragment1 newInstance(int SectionNumber){
            SectionsFragment1 fragment = new SectionsFragment1();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.cu_service_post, container, false);
          /*  TextView tv = (TextView) rootView.findViewById(R.id.section_label1);
            tv.setText("섹션1");*/
            return rootView;
        }
    }
    // layout 이름 수정
    // section_label1 부분은 해당 layout xml에 있는 textView의 id임.

    public static class SectionsFragment2 extends Fragment {

        public SectionsFragment2() {

        }

        // PlaceholderFragment.newInstance() 와 똑같이 추가
        static SectionsFragment2 newInstance(int SectionNumber){
            SectionsFragment2 fragment = new SectionsFragment2();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.cu_service_giftcard, container, false);

            return rootView;
        }
    }
    public static class SectionsFragment3 extends Fragment {

        public SectionsFragment3() {

        }

        // PlaceholderFragment.newInstance() 와 똑같이 추가
        static SectionsFragment3 newInstance(int SectionNumber){
            SectionsFragment3 fragment = new SectionsFragment3();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.cu_service_public, container, false);

            return rootView;
        }
    }
    public static class SectionsFragment4 extends Fragment {

        public SectionsFragment4() {

        }

        // PlaceholderFragment.newInstance() 와 똑같이 추가
        static SectionsFragment4 newInstance(int SectionNumber){
            SectionsFragment4 fragment = new SectionsFragment4();
            Bundle args = new Bundle();
            args.putInt("section_number", SectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.cu_service_etc, container, false);
            return rootView;
        }
    }

}
