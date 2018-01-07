package com.example.heegyeong.accessibilityservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Heegyeong on 2017-02-16.
 */
public class AppLockSetting extends AppCompatActivity {

    private static final String TAG = AppLockSetting.class.getSimpleName();
    // 메뉴 KEY
    private final int MENU_DOWNLOAD = 0;
    private final int MENU_ALL = 1;
    private int MENU_MODE = MENU_DOWNLOAD;

    private PackageManager pm;

    private View mLoadingContainer;
    private ListView mListView = null;
    private IAAdapter mAdapter = null;

    String[] setLockApp = new String[30];
    String[] saveRoute = new String[30];
    boolean isSetting = false;
    int[] settingNumber = new int[30];

    String[] setName = new String[30];
    String[] saveName = new String[30];

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        mLoadingContainer = findViewById(R.id.loading_container);
        mListView = (ListView) findViewById(R.id.listView1);

        mAdapter = new IAAdapter(this);
        mListView.setAdapter(mAdapter);

        Intent intent = getIntent();
        for(int i = 1 ; i<30 ; i++){
            setLockApp[i] = intent.getStringExtra("setLockApp"+i);
            setName[i] = intent.getStringExtra("setLockAppName"+i);
      //      Log.d("IntentValue", setLockApp[i] + " : Cathing");
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                String app_name = ((TextView) view.findViewById(R.id.app_name)).getText().toString();
                String package_name = ((TextView) view.findViewById(R.id.app_package)).getText().toString();

                for (int i = 1; i < 30; i++) {
                    if(setLockApp[i] != null){  // null일 경우 이 if문이 없으면 오류 발생.
                        if (setLockApp[i].equals(package_name)) {
                            isSetting = true;
                            settingNumber[i] = i;  // 해제할 index 저장
                            Log.d("IntentValue",  i +" : saving index for delete : "+settingNumber[i] + ": id ? : "+id);
                            Toast.makeText(AppLockSetting.this, app_name + "의 잠금을 해제합니다.", Toast.LENGTH_SHORT).show();
                            saveRoute[i] = null;
                            setLockApp[i] = null;
                            saveName[i] = null;
                            setName[i] = null;
                            break;
                        }
                    }
                }



                if (!isSetting) { // isSetting이 false 이면. 즉, 세팅되어있지 않으면
                    for (int i = 1; i < 30; i++) {
                        if(setLockApp[i] != null){
                            if (setLockApp[i].equals("delete")) {    // 저장되어있지 않은 첫번째 index를 찾아서 저장하기 위함.
                                Toast.makeText(AppLockSetting.this, app_name + "의 잠금을 설정합니다.", Toast.LENGTH_SHORT).show();
                                Log.d("IntentValue", i + " : saving index before : " + settingNumber[i] + ": id ? : " + id);
                                settingNumber[i] = 0;  // 만약 index가 저장되어있다면 0으로 세팅하여 삭제하지 않도록 설정.
                                Log.d("IntentValue",  i +" : saving index after : "+settingNumber[i] + ": id ? : "+id);
                                saveRoute[i] = package_name;
                                saveName[i] = app_name;
                                setLockApp[i] = saveRoute[i];   // 더블 클릭했을 때 다시 잠금을 해제하기 위함.
                                setName[i] = saveName[i];
                                break;
                            }
                        } else if (setLockApp[i] == null ) {    // 저장되어있지 않은 첫번째 index를 찾아서 저장하기 위함.
                                Toast.makeText(AppLockSetting.this, app_name + "의 잠금을 설정합니다.", Toast.LENGTH_SHORT).show();
                                Log.d("IntentValue", i + " : saving index before : " + settingNumber[i] + ": id ? : " + id);
                                settingNumber[i] = 0;  // 만약 index가 저장되어있다면 0으로 세팅하여 삭제하지 않도록 설정.
                                Log.d("IntentValue",  i +" : saving index after : "+settingNumber[i] + ": id ? : "+id);
                                saveRoute[i] = package_name;
                                saveName[i] = app_name;
                                setLockApp[i] = saveRoute[i];   // 더블 클릭했을 때 다시 잠금을 해제하기 위함.
                                setName[i] = saveName[i];
                                break;
                        }
                    }
                }
                //setting 이 끝나면 isSetting 값을 false로 수정한다.
                isSetting = false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 작업 시작
        startTask();
    }

    /**
     * 작업 시작
     */
    private void startTask() {
        new AppTask().execute();
    }

    /**
     * 로딩뷰 표시 설정
     *
     * @param isView
     *            표시 유무
     */
    private void setLoadingView(boolean isView) {
        if (isView) {
            // 화면 로딩뷰 표시
            mLoadingContainer.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            // 화면 어플 리스트 표시
            mListView.setVisibility(View.VISIBLE);
            mLoadingContainer.setVisibility(View.GONE);
        }
    }

    /**
     * List Fast Holder
     *
     * @author nohhs
     */
    private class ViewHolder {
        // App Icon
        public ImageView mIcon;
        // App Name
        public TextView mName;
        // App Package Name
        public TextView mPacakge;
    }

    /**
     * List Adapter
     *
     * @author nohhs
     */
    private class IAAdapter extends BaseAdapter {
        private Context mContext = null;

        private List<ApplicationInfo> mAppList = null;
        private ArrayList<AppInfo> mListData = new ArrayList<AppInfo>();

        public IAAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public int getCount() {
            return mListData.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_layout, null);

                holder.mIcon = (ImageView) convertView
                        .findViewById(R.id.app_icon);
                holder.mName = (TextView) convertView
                        .findViewById(R.id.app_name);
                holder.mPacakge = (TextView) convertView
                        .findViewById(R.id.app_package);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppInfo data = mListData.get(position);

            if (data.mIcon != null) {
                holder.mIcon.setImageDrawable(data.mIcon);
            }

            holder.mName.setText(data.mAppName);
            holder.mPacakge.setText(data.mAppPackge);

            return convertView;
        }

        /**
         * 어플리케이션 리스트 작성
         */
        public void rebuild() {
            if (mAppList == null) {

                Log.d(TAG, "Is Empty Application List");
                // 패키지 매니저 취득
                pm = AppLockSetting.this.getPackageManager();

                // 설치된 어플리케이션 취득
                mAppList = pm
                        .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
                                | PackageManager.GET_DISABLED_COMPONENTS);
            }

            AppInfo.AppFilter filter;
            switch (MENU_MODE) {
                case MENU_DOWNLOAD:
                    filter = AppInfo.THIRD_PARTY_FILTER;
                    break;
                default:
                    filter = null;
                    break;
            }

            if (filter != null) {
                filter.init();
            }

            // 기존 데이터 초기화
            mListData.clear();

            AppInfo addInfo = null;
            ApplicationInfo info = null;
            for (ApplicationInfo app : mAppList) {
                info = app;

                if (filter == null || filter.filterApp(info)) {
                    // 필터된 데이터

                    addInfo = new AppInfo();
                    // App Icon
                    addInfo.mIcon = app.loadIcon(pm);
                    // App Name
                    addInfo.mAppName = app.loadLabel(pm).toString();
                    // App Package Name
                    addInfo.mAppPackge = app.packageName;
                    mListData.add(addInfo);
                }
            }

            // 알파벳 이름으로 소트(한글, 영어)
            Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
        }

        public void filters(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            AppInfo.AppFilter filter;
            switch (MENU_MODE) {
                case MENU_DOWNLOAD:
                    filter = AppInfo.THIRD_PARTY_FILTER;
                    break;
                default:
                    filter = null;
                    break;
            }
            AppInfo addInfo = null;
            ApplicationInfo info = null;

            mListData.clear();

            if (charText.length() == 0) {
                for (ApplicationInfo app : mAppList) {
                    info = app;

                    if (filter == null || filter.filterApp(info)) {
                        // 필터된 데이터

                        addInfo = new AppInfo();
                        // App Icon
                        addInfo.mIcon = app.loadIcon(pm);
                        // App Name
                        addInfo.mAppName = app.loadLabel(pm).toString();
                        // App Package Name
                        addInfo.mAppPackge = app.packageName;
                        mListData.add(addInfo);
                    }
                }
                // 알파벳 이름으로 소트(한글, 영어)
                Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);

            } else {
                for (ApplicationInfo app : mAppList) {
                    info = app;

                    if (filter == null || filter.filterApp(info)) {
                        // 필터된 데이터

                        addInfo = new AppInfo();
                        // App Icon
                        addInfo.mIcon = app.loadIcon(pm);
                        // App Name
                        addInfo.mAppName = app.loadLabel(pm).toString();

                        String tmp = app.loadLabel(pm).toString();

                        // App Package Name
                        addInfo.mAppPackge = app.packageName;
                        if(charText.length() != 0 && tmp.toLowerCase(Locale.getDefault()).contains(charText)){
                            mListData.add(addInfo);
                        }

                    }
                }
                // 알파벳 이름으로 소트(한글, 영어)
                Collections.sort(mListData, AppInfo.ALPHA_COMPARATOR);
            }

            notifyDataSetChanged();
        }

    }

    /**
     * 작업 태스크
     * @author nohhs
     */
    private class AppTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // 로딩뷰 시작
            setLoadingView(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // 어플리스트 작업시작
            mAdapter.rebuild();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // 어댑터 갱신
            mAdapter.notifyDataSetChanged();

            // 로딩뷰 정지
            setLoadingView(false);
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_DOWNLOAD, 1, R.string.menu_download);
        menu.add(0, MENU_ALL, 2, R.string.menu_all);
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                mAdapter.filters(searchQuery.toString().trim());
                mListView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (MENU_MODE == MENU_DOWNLOAD) {
            menu.findItem(MENU_DOWNLOAD).setVisible(false);
            menu.findItem(MENU_ALL).setVisible(true);
        } else {
            menu.findItem(MENU_DOWNLOAD).setVisible(true);
            menu.findItem(MENU_ALL).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == MENU_DOWNLOAD) {
            MENU_MODE = MENU_DOWNLOAD;
        } else {
            MENU_MODE = MENU_ALL;
        }

        startTask();

        return true;
    }

    // 뒤로가기 누를 시에 잠금이 설정된다.
    @Override
    public void onBackPressed() {

        Toast.makeText(AppLockSetting.this, "잠금 설정이 저장되었습니다.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AppLockSetting.this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("setLockApp" + 0, "Throwing");
        for(int i =1 ; i<30 ; i++){
            if(settingNumber[i] == 0){
                intent.putExtra("setLockApp" + i, setLockApp[i]);
                intent.putExtra("setLockAppName" + i, setName[i]);
            }

            else if(settingNumber[i] != 0){
                intent.putExtra("setLockApp" + i, "delete");
                intent.putExtra("setLockAppName" + i, "delete");
            }

            Log.d("IntentValue", setLockApp[i] + " : to Main Throwing : " +settingNumber[i]);
        }
        startActivity(intent);
    }
}

