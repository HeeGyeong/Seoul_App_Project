package com.example.heegyeong.seoul_maptagging;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity { //RuntimePermission

    private Spinner spinner;
    String[] local = {"지역선택",
            "강북구","강동구","강남구","강서구","관악구",
            "광진구","구로구","금천구","노원구","도봉구",
            "동대문구","동작구","마포구","서대문구","서초구",
            "성동구","성북구","송파구","양천구","영등포구",
            "용산구","은평구","종로구","중구","중랑구" };
    int i; //즐겨찾기 Intent인덱스


    private BackPressCloseHandler backPressCloseHandler;    // 뒤로가기 종료를 하기위한 핸들러 선언

    String userID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPrefereneceUtil sharedPrefereneceUtil = new SharedPrefereneceUtil(this);
        i = sharedPrefereneceUtil.getSharedPreference("loc",0);

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        // 폰 번호 가져오고, 저장된 id값이 있으면 토스트 메시지만, null이면 해당 액티비티로 이동.
        // SharedPreferenceUtil 에 2개 메소드 추가함. String 타입으로 저장하고 불러올 수 있게.
        userID = sharedPrefereneceUtil.getSharedPreference("userID",null);
        /// 폰 번호 가져오는데 사용

        Intent intent = getIntent();
        final String guestID = intent.getStringExtra("guestID");
        ///

        if(userID == null && guestID == null){
            Toast.makeText(getApplicationContext(),"방문을 환영합니다.", Toast.LENGTH_SHORT).show();
            Intent i1 = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i1);//sharedPrefereneceUtil.putSharedPreference("userID", checkId.getText().toString());
        }else if(guestID!=null&&guestID.equals("guestID")){ // userID.equals("guest");
            Toast.makeText(getApplicationContext(), "게스트로 로그인 하였습니다.", Toast.LENGTH_SHORT).show();
            // sharedPrefereneceUtil.putSharedPreference("userID", null);
        } else{
            Toast.makeText(getApplicationContext(),userID + "님 환영합니다.", Toast.LENGTH_SHORT).show();
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        Button bmarker = (Button)findViewById(R.id.bmarker);
        bmarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i != 0) {
                    Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                    intent.putExtra("local", local[i]);

                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "즐겨찾기를 설정해 주세요", Toast.LENGTH_SHORT).show();
            }
        });



        final ImageView set = (ImageView)findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guestID!=null&&guestID.equals("guestID")){
                    Toast.makeText(getApplicationContext(), "게스트ID는 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
                    startActivity(intent);
                }
            }
        });


        if(i==1) bmarker.setText("즐겨찾기-강북구");
        else if(i==2) bmarker.setText("즐겨찾기-강동구");
        else if(i==3) bmarker.setText("즐겨찾기-강남구");
        else if(i==4) bmarker.setText("즐겨찾기-강서구");
        else if(i==5) bmarker.setText("즐겨찾기-관악구");
        else if(i==6) bmarker.setText("즐겨찾기-광진구");
        else if(i==7) bmarker.setText("즐겨찾기-구로구");
        else if(i==8) bmarker.setText("즐겨찾기-금천구");
        else if(i==9) bmarker.setText("즐겨찾기-노원구");
        else if(i==10) bmarker.setText("즐겨찾기-도봉구");
        else if(i==11) bmarker.setText("즐겨찾기-동대문구");
        else if(i==12) bmarker.setText("즐겨찾기-동작구");
        else if(i==13) bmarker.setText("즐겨찾기-마포구");
        else if(i==14) bmarker.setText("즐겨찾기-서대문구");
        else if(i==15) bmarker.setText("즐겨찾기-서초구");
        else if(i==16) bmarker.setText("즐겨찾기-성동구");
        else if(i==17) bmarker.setText("즐겨찾기-성북구");
        else if(i==18) bmarker.setText("즐겨찾기-송파구");
        else if(i==19) bmarker.setText("즐겨찾기-양천구");
        else if(i==20) bmarker.setText("즐겨찾기-영등포구");
        else if(i==21) bmarker.setText("즐겨찾기-용산구");
        else if(i==22) bmarker.setText("즐겨찾기-은평구");
        else if(i==23) bmarker.setText("즐겨찾기-종로구");
        else if(i==24) bmarker.setText("즐겨찾기-중구");
        else if(i==25) bmarker.setText("즐겨찾기-중랑구");


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,local);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                switch (pos) {
                    case 1:
                        Intent i1 = new Intent(MainActivity.this, ConvenActivity.class);
                        i1.putExtra("local", "gangbuk");
                        startActivity(i1);
                        break;
                    case 2:
                        Intent i2 = new Intent(MainActivity.this, ConvenActivity.class);
                        i2.putExtra("local", "gangdong");
                        startActivity(i2);
                        break;
                    case 3:
                        Intent i3 = new Intent(MainActivity.this, ConvenActivity.class);
                        i3.putExtra("local", "gangnam");
                        startActivity(i3);
                        break;
                    case 4:
                        Intent i4 = new Intent(MainActivity.this, ConvenActivity.class);
                        i4.putExtra("local", "gangseo");
                        startActivity(i4);
                        break;
                    case 5:
                        Intent i5 = new Intent(MainActivity.this, ConvenActivity.class);
                        i5.putExtra("local", "gwanak");
                        startActivity(i5);
                        break;
                    case 6:
                        Intent i6 = new Intent(MainActivity.this, ConvenActivity.class);
                        i6.putExtra("local", "gwangjin");
                        startActivity(i6);
                        break;
                    case 7:
                        Intent i7 = new Intent(MainActivity.this, ConvenActivity.class);
                        i7.putExtra("local", "guro");
                        startActivity(i7);
                        break;
                    case 8:
                        Intent i8 = new Intent(MainActivity.this, ConvenActivity.class);
                        i8.putExtra("local", "geumcheon");
                        startActivity(i8);
                        break;
                    case 9:
                        Intent i9 = new Intent(MainActivity.this, ConvenActivity.class);
                        i9.putExtra("local", "nowon");
                        startActivity(i9);
                        break;
                    case 10:
                        Intent i10 = new Intent(MainActivity.this, ConvenActivity.class);
                        i10.putExtra("local", "dobong");
                        startActivity(i10);
                        break;
                    case 11:
                        Intent i11 = new Intent(MainActivity.this, ConvenActivity.class);
                        i11.putExtra("local", "dongdaemun");
                        startActivity(i11);
                        break;
                    case 12:
                        Intent i12 = new Intent(MainActivity.this, ConvenActivity.class);
                        i12.putExtra("local", "dongjak");
                        startActivity(i12);
                        break;
                    case 13:
                        Intent i13 = new Intent(MainActivity.this, ConvenActivity.class);
                        i13.putExtra("local", "mapo");
                        startActivity(i13);
                        break;
                    case 14:
                        Intent i14 = new Intent(MainActivity.this, ConvenActivity.class);
                        i14.putExtra("local", "seodaemun");
                        startActivity(i14);
                        break;
                    case 15:
                        Intent i15 = new Intent(MainActivity.this, ConvenActivity.class);
                        i15.putExtra("local", "seocho");
                        startActivity(i15);
                        break;
                    case 16:
                        Intent i16 = new Intent(MainActivity.this, ConvenActivity.class);
                        i16.putExtra("local", "seongdong");
                        startActivity(i16);
                        break;
                    case 17:
                        Intent i17 = new Intent(MainActivity.this, ConvenActivity.class);
                        i17.putExtra("local", "seongbuk");
                        startActivity(i17);
                        break;
                    case 18:
                        Intent i18 = new Intent(MainActivity.this, ConvenActivity.class);
                        i18.putExtra("local", "songpa");
                        MainActivity.this.startActivity(i18);
                        break;
                    case 19:
                        Intent i19 = new Intent(MainActivity.this, ConvenActivity.class);
                        i19.putExtra("local", "yangcheon");
                        MainActivity.this.startActivity(i19);
                        break;
                    case 20:
                        Intent i20 = new Intent(MainActivity.this, ConvenActivity.class);
                        i20.putExtra("local", "yeongdeungpo");
                        MainActivity.this.startActivity(i20);
                        break;
                    case 21:
                        Intent i21 = new Intent(MainActivity.this, ConvenActivity.class);
                        i21.putExtra("local", "youngsan");
                        startActivity(i21);
                        break;
                    case 22:
                        Intent i22 = new Intent(MainActivity.this, ConvenActivity.class);
                        i22.putExtra("local", "eunpyeong");
                        startActivity(i22);
                        break;
                    case 23:
                        Intent i23 = new Intent(MainActivity.this, ConvenActivity.class);
                        i23.putExtra("local", "jongno");
                        startActivity(i23);
                        break;
                    case 24:
                        Intent i24 = new Intent(MainActivity.this, ConvenActivity.class);
                        i24.putExtra("local", "jung");
                        startActivity(i24);
                        break;
                    case 25:
                        Intent i25 = new Intent(MainActivity.this, ConvenActivity.class);
                        i25.putExtra("local", "jungnang");
                        startActivity(i25);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new myPagerAdapter(this));


        //해상도
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        // onCreate 내부에 뒤로가기 종료를 하기위한 핸들러 객체 생성
        backPressCloseHandler = new BackPressCloseHandler(this);


    }

    /// 뒤로가기를 눌렀을 때 발생하는 Event method
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    /////////////////////View Pager
    class myPagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;

        public myPagerAdapter(Context context) {
            super();
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 25;
        }

        public Object instantiateItem(View pager, final int position) {
            View v = null;
            LinearLayout mLinear;

            switch (position) {
                case 0:
                    v = mInflater.inflate(R.layout.local_gangbuk, null);
                    ImageButton btngangbuk = (ImageButton) v.findViewById(R.id.gangbuk);
                    btngangbuk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "gangbuk");
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    v = mInflater.inflate(R.layout.local_gangdong, null);
                    ImageButton btngangdong = (ImageButton) v.findViewById(R.id.gangdong);
                    btngangdong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "gangdong");
                            startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    v = mInflater.inflate(R.layout.local_gangnam, null);
                    ImageButton btngangname = (ImageButton) v.findViewById(R.id.gangnam);
                    btngangname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "gangnam");
                            startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    v = mInflater.inflate(R.layout.local_gangseo, null);
                    ImageButton btngangseo = (ImageButton) v.findViewById(R.id.gangseo);
                    btngangseo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "gangseo");
                            startActivity(intent);
                        }
                    });
                    break;
                case 4:
                    v = mInflater.inflate(R.layout.local_gwanak, null);
                    ImageButton btngwanak = (ImageButton) v.findViewById(R.id.gwanak);
                    btngwanak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "gwanak");
                            startActivity(intent);
                        }
                    });
                    break;
                case 5:
                    v = mInflater.inflate(R.layout.local_gwangjin, null);
                    ImageButton btngwangjin = (ImageButton) v.findViewById(R.id.gwangjin);
                    btngwangjin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "gwangjin");
                            startActivity(intent);
                        }
                    });
                    break;
                case 6:
                    v = mInflater.inflate(R.layout.local_guro, null);
                    ImageButton btnguro = (ImageButton) v.findViewById(R.id.guro);
                    btnguro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "guro");
                            startActivity(intent);
                        }
                    });
                    break;
                case 7:
                    v = mInflater.inflate(R.layout.local_geumcheon, null);
                    ImageButton btngeumcheon = (ImageButton) v.findViewById(R.id.geumcheon);
                    btngeumcheon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "geumcheon");
                            startActivity(intent);
                        }
                    });
                    break;
                case 8:
                    v = mInflater.inflate(R.layout.local_nowon, null);
                    ImageButton btnnowon = (ImageButton) v.findViewById(R.id.nowon);
                    btnnowon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "nowon");
                            startActivity(intent);
                        }
                    });
                    break;
                case 9:
                    v = mInflater.inflate(R.layout.local_dobong, null);
                    ImageButton btndobong = (ImageButton) v.findViewById(R.id.dobong);
                    btndobong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "dobong");
                            startActivity(intent);
                        }
                    });
                    break;
                case 10:
                    v = mInflater.inflate(R.layout.local_dongdaemun, null);
                    ImageButton btndongdaemun = (ImageButton) v.findViewById(R.id.dongdaemun);
                    btndongdaemun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "dongdaemun");
                            startActivity(intent);
                        }
                    });
                    break;
                case 11:
                    v = mInflater.inflate(R.layout.local_dongjak, null);
                    ImageButton btndongjak = (ImageButton) v.findViewById(R.id.dongjak);
                    btndongjak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "dongjak");
                            startActivity(intent);
                        }
                    });
                    break;
                case 12:
                    v = mInflater.inflate(R.layout.local_mapo, null);
                    ImageButton btnmapo = (ImageButton) v.findViewById(R.id.mapo);
                    btnmapo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "mapo");
                            startActivity(intent);
                        }
                    });
                    break;
                case 13:
                    v = mInflater.inflate(R.layout.local_seodaemun, null);
                    ImageButton btnseodaemun = (ImageButton) v.findViewById(R.id.seodaemun);
                    btnseodaemun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "seodaemun");
                            startActivity(intent);
                        }
                    });
                    break;
                case 14:
                    v = mInflater.inflate(R.layout.local_seocho, null);
                    ImageButton btnseocho = (ImageButton) v.findViewById(R.id.seocho);
                    btnseocho.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "seocho");
                            startActivity(intent);
                        }
                    });
                    break;
                case 15:
                    v = mInflater.inflate(R.layout.local_seongdong, null);
                    ImageButton btnseongdong = (ImageButton) v.findViewById(R.id.seongdong);
                    btnseongdong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "seongdong");
                            startActivity(intent);
                        }
                    });
                    break;
                case 16:
                    v = mInflater.inflate(R.layout.local_seongbuk, null);
                    ImageButton btnseongbuk = (ImageButton) v.findViewById(R.id.seongbuk);
                    btnseongbuk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "seongbuk");
                            startActivity(intent);
                        }
                    });
                    break;
                case 17:
                    v = mInflater.inflate(R.layout.local_songpa, null);
                    ImageButton btnsongpa = (ImageButton) v.findViewById(R.id.songpa);
                    btnsongpa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "songpa");
                            startActivity(intent);
                        }
                    });
                    break;
                case 18:
                    v = mInflater.inflate(R.layout.local_yangcheon, null);
                    ImageButton btnyangcheon = (ImageButton) v.findViewById(R.id.yangcheon);
                    btnyangcheon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "yangcheon");
                            startActivity(intent);
                        }
                    });
                    break;
                case 19:
                    v = mInflater.inflate(R.layout.local_yeongdeungpo, null);
                    ImageButton btnyeongdeungpo = (ImageButton) v.findViewById(R.id.yeongdeungpo);
                    btnyeongdeungpo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "yeongdeungpo");
                            startActivity(intent);
                        }
                    });
                    break;
                case 20:
                    v = mInflater.inflate(R.layout.local_yongsan, null);
                    ImageButton btnyongsan = (ImageButton) v.findViewById(R.id.yongsan);
                    btnyongsan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "youngsan");
                            startActivity(intent);
                        }
                    });
                    break;
                case 21:
                    v = mInflater.inflate(R.layout.local_eunpyeong, null);
                    ImageButton btneunpyeong = (ImageButton) v.findViewById(R.id.eunpyeong);
                    btneunpyeong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "eunpyeong");
                            startActivity(intent);
                        }
                    });
                    break;
                case 22:
                    v = mInflater.inflate(R.layout.local_jongno, null);
                    ImageButton btnjongno = (ImageButton) v.findViewById(R.id.jongno);
                    btnjongno.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "jongno");
                            startActivity(intent);
                        }
                    });
                    break;
                case 23:
                    v = mInflater.inflate(R.layout.local_jung, null);
                    ImageButton btnjung = (ImageButton) v.findViewById(R.id.jung);
                    btnjung.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "jung");
                            startActivity(intent);
                        }
                    });
                    break;
                case 24:
                    v = mInflater.inflate(R.layout.local_jungnang, null);
                    ImageButton btnjungnang = (ImageButton) v.findViewById(R.id.jungnang);
                    btnjungnang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, ConvenActivity.class);
                            intent.putExtra("local", "jungnang");
                            startActivity(intent);
                        }
                    });
                    break;
            }

            ((ViewPager) pager).addView(v, null);
            return v;
        }

        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == obj;
        }
////////////////////////////////////////////////////////////////////////////////////////////////

    }


}
