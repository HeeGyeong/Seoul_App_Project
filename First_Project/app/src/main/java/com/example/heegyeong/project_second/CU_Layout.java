package com.example.heegyeong.project_second;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CU_Layout extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FIRST_VIEW_SELECT = 1; // 시작 화면 세팅
    private BackPressCloseHandler backPressCloseHandler;

    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Product> ProductList;

    protected Handler handler;
    private static final int imgMaxSize = 640;
    private int countSize = 20;

    public static String[] baseURL = new String[imgMaxSize];
    public static String[] product_name = new String[imgMaxSize];
    public static String[] event_value = new String[imgMaxSize];
    public static String[] product_price = new String[imgMaxSize];
    public static String[] product_category = new String[imgMaxSize];
    Bitmap bitmap;
    URL url;

    EditText pro_search;
    Spinner event;
    Spinner category;
    private RecyclerView searchView;
    ImageButton click;

    BitmapFactory.Options options = new BitmapFactory.Options();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    int DBindex = 0;
    public static String dummyString;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cu_layout);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#8a2be2"));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        click = (ImageButton)findViewById(R.id.button);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                pro_search = (EditText) findViewById(R.id.product_search);
                event = (Spinner) findViewById(R.id.event);
                category = (Spinner) findViewById(R.id.list);

                String srch = pro_search.getText().toString();
                imm.hideSoftInputFromWindow(pro_search.getWindowToken(), 0);
                pro_search.setText("");

                String spiEvent = event.getSelectedItem().toString();
                String spiCate = category.getSelectedItem().toString();

                int[] srch_index = new int[imgMaxSize];
                for (int i = 0; i < srch_index.length; i++) {
                    srch_index[i] = 1000;   // 0으로 초기화 하면 INDEX 0이 포함된 검색의 경우
                                // 해당 경우의 모든 값을 안받게 됨. 따라서, 전체 index를 넘는 임의의 값을 줘야 한다.
                }

                // 맨 처음상태.
                if (srch.length() <= 0 && spiEvent.contains("전체") && spiCate.contains("전체")) {
                    // 한번 검색을 했을 경우라면
                    if (countSize >= 640) {
                        countSize = 0;

                        // 맨 처음에 보내줄 리스트
                        mAdapter.delete();

                        mRecyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        // use a linear layout manager
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        // create an Object for Adapter
                        mAdapter = new DataAdapter(ProductList, searchView);
                        // set the adapter object to the Recyclerview
                        mRecyclerView.setAdapter(mAdapter);


                        Intent intent = getIntent();
                        // 맨 처음에 보여줄 리스트.
                        for (int i = 0; i <= 20; i++) {
                            final String img_url = intent.getStringExtra("cuImage"+i);
                            final String insert_name = intent.getStringExtra("cuName"+i);
                            final String insert_price = intent.getStringExtra("cuPrice"+i);
                            final String product_event = intent.getStringExtra("cuEvent" + i);
                            final String insert_category = intent.getStringExtra("cuCategory"+i);

                            Thread mThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        url = new URL(img_url);
                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setDoInput(true);
                                        conn.connect();

                                        options.inSampleSize = 16;
                                        InputStream is = conn.getInputStream();
                                        bitmap = BitmapFactory.decodeStream(is);
                                    } catch (IOException ex) {

                                    }
                                }
                            };
                            mThread.start();
                            try {

                                mThread.join();

                                ProductList.add(new Product(insert_name, insert_price, product_event, bitmap, url, insert_category));

                            } catch (InterruptedException e) {
                            }
                        }

                        // 추가 해주지 않으면, 새로 만든 adapter에서는 loadmore이 되지 않음.
                        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore() {
                                //add null , so the adapter will check view_type and show progress bar at bottom
                                ProductList.add(null);
                                // 사이즈를 넘어가면 더이상 추가하지 않는다.
                                if (countSize < imgMaxSize) {
                                    mAdapter.notifyItemInserted(ProductList.size() - 1);
                                }

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //   remove progress item
                                        ProductList.remove(ProductList.size() - 1);
                                        mAdapter.notifyItemRemoved(ProductList.size());
                                        //add items one by one
                                        int start = ProductList.size();
                                        int end = start + 10;
                                        // 검색 했을 경우,
                                        if (start < 20) {
                                            end = ProductList.size();
                                        }
                                        countSize += 10;
                                        for (int i = start; i <= end; i++) {
                                            ///////////////////////////////////////////////
                                            if (i < imgMaxSize) {
                                                final String img_url = baseURL[i];
                                                final String insert_name = product_name[i];
                                                final String insert_price = product_price[i];
                                                final String product_event = event_value[i];
                                                final String insert_category = product_category[i];
                                                Thread mThread = new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            url = new URL(img_url);
                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                            conn.setDoInput(true);
                                                            conn.connect();

                                                            options.inSampleSize = 16;
                                                            InputStream is = conn.getInputStream();
                                                            bitmap = BitmapFactory.decodeStream(is);
                                                        } catch (IOException ex) {

                                                        }
                                                    }
                                                };
                                                mThread.start();
                                                try {
                                                    mThread.join();
                                                    ProductList.add(new Product(insert_name, insert_price, product_event, bitmap, url,insert_category));
                                                    mAdapter.notifyItemInserted(ProductList.size());
                                                } catch (InterruptedException e) {
                                                }
                                            }
                                            //////////////////////////////////////////////////////////////
                                        }
                                        mAdapter.setLoaded();
                                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                    }
                                }, 2000);
                            }
                        });
                    } else { // 한번도 검색을 하지 않은 상태라면
                        Toast.makeText(getApplicationContext(),
                                "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //
                    countSize = 640;
                    int j = 0;
                    // 텍스트 + 카테고리 + 이벤트
                    if (srch.length() > 0 && !(spiEvent.contains("전체")) && !(spiCate.contains("전체"))) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (product_name[i].contains(srch) && product_category[i].contains(spiCate) && event_value[i].contains(spiEvent)) {
                                     srch_index[j++] = i;
                                }
                            }
                        }
                    }   // 텍스트만 입력 했을 때
                    else if (srch.length() > 0 && spiEvent.contains("전체") && spiCate.contains("전체")) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (product_name[i].contains(srch)) { // 검색 내용에 이름이 포함되어 있을 때
                                    srch_index[j++] = i;
                                }
                            }
                        }
                    }   // 텍스트 + 카테고리 입력
                    else if (srch.length() > 0 && !(spiCate.contains("전체")) && spiEvent.contains("전체")) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (product_name[i].contains(srch) && product_category[i].contains(spiCate)) {
                                    srch_index[j++] = i;
                                }
                            }
                        }
                    }   // 텍스트 + 이벤트 입력
                    else if (srch.length() > 0 && spiCate.contains("전체") && !(spiEvent.contains("전체"))) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (product_name[i].contains(srch) && event_value[i].contains(spiEvent)) {
                                    srch_index[j++] = i;
                                }
                            }
                        }
                    }
                    //////////////////////////// 텍스트 입력 안했을 경우
                     // 카테고리만 입력 했을 때
                    else if (srch.length() <= 0 && !(spiCate.contains("전체")) && spiEvent.contains("전체") ) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (product_category[i].contains(spiCate)) {
                                    srch_index[j++] = i;
                                }
                            }
                        }
                    }
                    // event만 설정 했을 때
                    else if (srch.length() <= 0 && spiCate.contains("전체") && !(spiEvent.contains("전체"))) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (event_value[i].contains(spiEvent)) {
                                    srch_index[j++] = i;
                                }
                            }
                        }
                    }   // category와 event 설정했을 때
                    else if (srch.length() <= 0 && !(spiCate.contains("전체")) && !(spiEvent.contains("전체"))) {
                        for (int i = 0; i < imgMaxSize; i++) {
                            if(product_name[i] != null){
                                if (product_category[i].contains(spiCate) && event_value[i].contains(spiEvent)) {
                                    srch_index[j++] = i;
                                }
                            }
                        }
                    }


                    mAdapter.delete();
                    searchView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    // use a linear layout manager
                    searchView.setLayoutManager(mLayoutManager);
                    // create an Object for Adapter
                    mAdapter = new DataAdapter(ProductList, searchView);

                    // 맨 처음에 보여줄 리스트.
                    for (int i = 0; i <= 20; i++) {
                        if(srch_index[0] == 1000){
                            Toast.makeText(getApplicationContext(),"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                            i = 21;
                        }else{
                            if(srch_index[i] != 1000){
                                final String img_url = baseURL[srch_index[i]];
                                final String insert_name = product_name[srch_index[i]];
                                final String insert_price = product_price[srch_index[i]];
                                final String product_event = event_value[srch_index[i]];
                                final String insert_category = product_category[srch_index[i]];

                                Thread mThread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            url = new URL(img_url);
                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                            conn.setDoInput(true);
                                            conn.connect();

                                            options.inSampleSize = 16;
                                            InputStream is = conn.getInputStream();
                                            bitmap = BitmapFactory.decodeStream(is);
                                        } catch (IOException ex) {

                                        }
                                    }
                                };
                                mThread.start();
                                try {

                                    mThread.join();

                                    ProductList.add(new Product(insert_name, insert_price, product_event, bitmap, url, insert_category));

                                } catch (InterruptedException e) {
                                }
                            } else{
                            }
                        }
                    }
                    //srch_index를 final로 사용하기 위하여 loadIndex 추가
                    final int[] loadIndex = new int[imgMaxSize];
                    int in =0;
                    for(int i=0;i<imgMaxSize;i++){
                        if(srch_index[i] != 1000){
                            loadIndex[in++] = srch_index[i];
                        }
                    }
                    for(int i=0; i<imgMaxSize;i++){
                        if(loadIndex[i] != 0){

                        }else {
                            loadIndex[i] = 0;
                        }
                    }

                    // 추가 해주지 않으면, 새로 만든 adapter에서는 loadmore이 되지 않음.
                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            //add null , so the adapter will check view_type and show progress bar at bottom
                            ProductList.add(null);
                            // 사이즈를 넘어가면 더이상 추가하지 않는다.
                            if (countSize < imgMaxSize) {
                                mAdapter.notifyItemInserted(ProductList.size() - 1);
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    ProductList.remove(ProductList.size() - 1);
                                    mAdapter.notifyItemRemoved(ProductList.size());
                                    //add items one by one
                                    int start = ProductList.size();
                                    int end = start + 10;
                                    // 검색 했을 경우,
                                    if (start < 20) {
                                        end = ProductList.size();
                                    }
                                    countSize += 10;
                                    for (int i = start; i <= end; i++) {
                                        ///////////////////////////////////////////////
                                        if (i < imgMaxSize) {
                                            if(loadIndex[i] != 0){
                                                final String img_url = baseURL[loadIndex[i]];
                                                final String insert_name = product_name[loadIndex[i]];
                                                final String insert_price = product_price[loadIndex[i]];
                                                final String product_event = event_value[loadIndex[i]];
                                                final String insert_category = product_category[loadIndex[i]];
                                                Thread mThread = new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            url = new URL(img_url);
                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                            conn.setDoInput(true);
                                                            conn.connect();

                                                            options.inSampleSize = 16;
                                                            InputStream is = conn.getInputStream();
                                                            bitmap = BitmapFactory.decodeStream(is);
                                                        } catch (IOException ex) {

                                                        }
                                                    }
                                                };
                                                mThread.start();
                                                try {
                                                    mThread.join();
                                                    if(loadIndex[i] != 0){
                                                        ProductList.add(new Product(insert_name, insert_price, product_event, bitmap, url,insert_category));
                                                        mAdapter.notifyItemInserted(ProductList.size());
                                                    }
                                                } catch (InterruptedException e) {
                                                }
                                            }
                                        }
                                        //////////////////////////////////////////////////////////////
                                    }
                                    mAdapter.setLoaded();
                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                }
                            }, 2000);
                        }
                    });
                    countSize = 640;
                    // set the adapter object to the Recyclerview
                    searchView.setAdapter(mAdapter);
                }
            }
        });
        // 여기까지 click 리스너
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        databaseReference.child("product").child("cu").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product proDuct = dataSnapshot.getValue(Product.class);  // proDuct를 가져오고
                if (proDuct != null) {
                    product_name[DBindex] = proDuct.getproName();
                    product_price[DBindex] = proDuct.getproPrice();
                    event_value[DBindex] = proDuct.getproEvent();
                    baseURL[DBindex] = proDuct.getproUrl();
                    product_category[DBindex] = proDuct.getproCategory();
                    DBindex++;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //     tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        ProductList = new ArrayList<Product>();
        handler = new Handler();

        searchView = (RecyclerView) findViewById(R.id.my_recycler_view);

        Intent intent = getIntent();
        // 맨 처음에 보여줄 리스트.
        for (int i = 0; i <= 20; i++) {
            final String img_url = intent.getStringExtra("cuImage"+i);
            final String insert_name = intent.getStringExtra("cuName"+i);
            final String insert_price = intent.getStringExtra("cuPrice"+i);
            final String product_event = intent.getStringExtra("cuEvent" + i);
            final String insert_category = intent.getStringExtra("cuCategory"+i);

            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        url = new URL(img_url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        options.inSampleSize = 16;
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException ex) {

                    }
                }
            };
            mThread.start();
            try {

                mThread.join();

                ProductList.add(new Product(insert_name, insert_price, product_event, bitmap, url,insert_category));

            } catch (InterruptedException e) {
            }
        }

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        // create an Object for Adapter
        mAdapter = new DataAdapter(ProductList, mRecyclerView);
        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        if (countSize < imgMaxSize) {
            // 맨 아래로 내려갔을 때, 새로운 리스트를 load 해주는 메서드.
            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    ProductList.add(null);
                    event = (Spinner) findViewById(R.id.event);
                    category = (Spinner) findViewById(R.id.list);
                    String spiEvent = event.getSelectedItem().toString();
                    if (!(spiEvent.contains("전체"))) {
                        countSize = 640;
                    }
                    // 사이즈를 넘어가면 더이상 추가하지 않는다.
                    if (countSize < imgMaxSize) {
                        mAdapter.notifyItemInserted(ProductList.size() - 1);
                    }


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            ProductList.remove(ProductList.size() - 1);
                            mAdapter.notifyItemRemoved(ProductList.size());
                            //add items one by one
                            int start = ProductList.size();
                            int end = start + 10;
                            // 검색 했을 경우,
                            if (start < 20) {
                                end = ProductList.size();
                            }
                            // start +1 하면 첫번째 로딩때 20번째 list 생략함.
                            for (int i = start; i <= end; i++) {
                                ///////////////////////////////////////////////
                                if (i < imgMaxSize && countSize < imgMaxSize) {
                                    final String img_url = baseURL[i];
                                    final String insert_name = product_name[i];
                                    final String insert_price = product_price[i];
                                    final String product_event = event_value[i];
                                    final String insert_category = product_category[i];

                                    Thread mThread = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                url = new URL(img_url);
                                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                conn.setDoInput(true);
                                                conn.connect();

                                                options.inSampleSize = 16;
                                                InputStream is = conn.getInputStream();
                                                bitmap = BitmapFactory.decodeStream(is);
                                            } catch (IOException ex) {

                                            }
                                        }
                                    };
                                    mThread.start();
                                    try {
                                        mThread.join();
                                        // if문 추가
                                        if(insert_name != null){
                                            ProductList.add(new Product(insert_name,insert_price, product_event, bitmap, url,insert_category));
                                        }
                                        //ProductList.add(new Product(insert_name,insert_price, product_event, bitmap, url,insert_category));
                                        mAdapter.notifyItemInserted(ProductList.size());
                                    } catch (InterruptedException e) {
                                    }
                                }
                                //////////////////////////////////////////////////////////////
                            }
                            countSize += 10;
                            mAdapter.setLoaded();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                        }
                    }, 2000);
                }
            });
        }

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Product list = ProductList.get(position);

                Intent intent = new Intent(CU_Layout.this, Product_Detail.class);
                intent.putExtra("image", list.getUrl().toString());
                // 값 넘길때는 url 값으로 넘기기.
                intent.putExtra("name", list.getText1());
                intent.putExtra("price", list.getText2());
                intent.putExtra("event", list.getText3());

                startActivity(intent);
            }

        }));

        ///////////////////////////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Spinner list_spinner = (Spinner) findViewById(R.id.list);
        ArrayAdapter<CharSequence> list_adapter = ArrayAdapter.createFromResource(
                this, R.array.list_arrayCU, android.R.layout.simple_spinner_item);
        list_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_spinner.setAdapter(list_adapter);

        list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner event_spinner = (Spinner) findViewById(R.id.event);
        ArrayAdapter<CharSequence> event_adapter = ArrayAdapter.createFromResource(
                this, R.array.event_arrayCU, android.R.layout.simple_spinner_item);

        event_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(event_adapter);
        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);

        //////////////////////////////////////////////////////////////////////////////////
        // RecyclerView 생성

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            final Dialog SearchDialog = new Dialog(this);
            SearchDialog.setContentView(R.layout.map_search);
            SearchDialog.setTitle("매장 찾기");
            SearchDialog.show();
        } else if (id == R.id.nav_setting) {
            final Dialog OptionDialog = new Dialog(this);
            OptionDialog.setContentView(R.layout.option_main);
            OptionDialog.setTitle("옵션 설정");
            OptionDialog.show();
        } else if (id == R.id.nav_other) {
            final Dialog InquireDialog = new Dialog(this);
            InquireDialog.setContentView(R.layout.inquire);
            InquireDialog.setTitle("문의하기");
            InquireDialog.show();
        } else if (id == R.id.nav_version) {
            Toast.makeText(this,
                    "Ver.2016.09.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_shared) {
            final Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.shared_link));
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.kakao.talk");
            try {
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
                // 카톡이 없다는 팝업을 뛰워주자.
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.home_button:
                intent = new Intent(CU_Layout.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.service_button:
                final Dialog SearchDialog = new Dialog(this);
                SearchDialog.setContentView(R.layout.cu_service);
                SearchDialog.show();
                break;
            case R.id.position_button:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.map.naver.com/search2/search.nhn?query=CU&sm=shistory#/map/1"));
                break;
            case R.id.top_button:
                // 가장 처음으로 스크롤
                mLayoutManager.scrollToPositionWithOffset(0, 0);
                break;
            case R.id.call_button:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01089149295"));
                break;
            case R.id.email_button:
                Intent intentEmail = new Intent(CU_Layout.this, Email_Layout.class);
                startActivity(intentEmail);
                break;
            case R.id.first_button:
                showDialog(FIRST_VIEW_SELECT);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    public void onClickService(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.cu_btn_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/store/list.do?category=store"));
                break;
            case R.id.cu_btn_etc:
                intent = new Intent(CU_Layout.this, CU_Service.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    public void onNeviButton(View view) {
        switch (view.getId()) {
            case R.id.nav_b_search:
                final Dialog SearchDialog = new Dialog(this);
                SearchDialog.setContentView(R.layout.map_search);
                SearchDialog.setTitle("매장 찾기");
                SearchDialog.show();
                break;
            case R.id.nav_b_service:
                final Dialog ServiceDialog = new Dialog(this);
                ServiceDialog.setContentView(R.layout.service);
                ServiceDialog.setTitle("서비스");
                ServiceDialog.show();
                break;
            case R.id.nav_b_setting:
                showDialog(FIRST_VIEW_SELECT);
                break;
            case R.id.nav_b_other:
                Intent intentEmail = new Intent(CU_Layout.this, Email_Layout.class);
                startActivity(intentEmail);
                break;
            case R.id.nav_b_version:
                Toast.makeText(this,
                        "Ver.2016.11.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_b_shared:
                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.shared_link));
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.kakao.talk");
                try {
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // 카톡이 없다는 팝업을 뛰워주자.
                }
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final CharSequence[] conven = {"Default", "CU", "GS 25", "7 - eleven", "Mini Stop"};
        final CharSequence[] name = {"Default", "CU", "GS 25", "7 - eleven", "Mini Stop"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("초기 화면을 선택하세요.");
        builder.setItems(conven, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                if (conven[item] == name[0]) {
                    intent.putExtra("value", "Default");
                    intent.putExtra("index", 0);
                    startActivity(intent);
                } else if (conven[item] == name[1]) {
                    intent.putExtra("value", "CU");
                    intent.putExtra("index", 1);
                    startActivity(intent);
                } else if (conven[item] == name[2]) {
                    intent.putExtra("value", "GS");
                    intent.putExtra("index", 2);
                    startActivity(intent);
                } else if (conven[item] == name[3]) {
                    intent.putExtra("value", "eleven");
                    intent.putExtra("index", 3);
                    startActivity(intent);
                } else if (conven[item] == name[4]) {
                    intent.putExtra("value", "mini");
                    intent.putExtra("index", 4);
                    startActivity(intent);
                }
            }
        });
        AlertDialog alert = builder.create();

        return alert;
    }

    public void onServiceClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.service_btn_cu:
                intent = new Intent(CU_Layout.this, CU_Service.class);
                break;
            case R.id.service_btn_gs:
                intent = new Intent(CU_Layout.this, GS_Service.class);
                break;
            case R.id.service_btn_eleven:
                intent = new Intent(CU_Layout.this, Eleven_Service.class);
                break;
            case R.id.service_btn_mini:
                intent = new Intent(CU_Layout.this, Mini_Service.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    public void onMapClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.cu_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cu.bgfretail.com/store/list.do?category=store"));
                break;
            case R.id.gs_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gs25.gsretail.com/gscvs/ko/store-services/locations?uiel=Mobile"));
                //  모바일로 수정. ?uiel=Mobile 추가됨
                break;
            case R.id.eleven_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.7-eleven.co.kr/store/storeSearch1.asp"));
                break;
            case R.id.mini_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ministop.co.kr//MiniStopHomePage/page/store/store.do"));
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    public void onClickList(View view) {
        switch (view.getId()) {

            case R.id.life_service_cu:
                final Dialog ServiceDialog = new Dialog(this);
                ServiceDialog.setContentView(R.layout.list);
                ServiceDialog.show();
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}