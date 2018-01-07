package com.example.heegyeong.project_second;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Product_Detail extends AppCompatActivity {
    String image = null;
    String name =  null;
    String price = null;
    String event = null;

    Bitmap bitmap;
    int log_test;

    // 이부분 추가
    final Firebase mRoofRef = new Firebase("https://projectdatebase.firebaseio.com/product/detail");

    ListView mListview;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        final EditText opinion = (EditText)findViewById(R.id.product_opinion);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#2e8b57"));
        }


        ///
        Firebase.setAndroidContext(this);
        mListview = (ListView)findViewById(R.id.listview);
        final Firebase massagesRef = mRoofRef.child("messages");
        ///

        /// 폰 번호 가져오는데 사용
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        final String phoneNum = telephonyManager.getLine1Number();
        ///

        Intent intent = getIntent();
        // intent값 호출  S_~ 가 붙어있지 않는 값은 실행시에 넘겨주는 값.
        image = intent.getStringExtra("image");// 처음 시작시 각 string은 null 무조건 null이됨
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        event = intent.getStringExtra("event");

        TextView text_img = (TextView)findViewById(R.id.explain_img);
        TextView text_price = (TextView)findViewById(R.id.product_price);
        ImageView url_img = (ImageView)findViewById(R.id.swar_img);

        TextView text_event = (TextView)findViewById(R.id.product_event);


        text_img.setText(name);
        text_price.setText("  " + price);
        text_event.setText("  " + event);

        Thread mThread = new Thread() {
            @Override
            public void run(){
                try{
                    URL url = new URL(image);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    log_test = conn.getResponseCode();
        //            Log.d("로그타이틀2", log_test + " ");
                    conn.getResponseCode();
                    if(conn.getDoInput() == false){
                        conn.setDoInput(true);
                    }
                    conn.connect();
                    log_test = conn.getResponseCode();
           //         Log.d("로그타이틀3", log_test + " ");

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch(IOException ex){

                }
            }
        };

        mThread.start();

        try{

            mThread.join();

            url_img.setImageBitmap(bitmap);
        } catch(InterruptedException e){
        }
        ////////////////////////////////////////////////////////////////////////////////////

        final FirebaseListAdapter<String> adapter =
                new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, massagesRef.child(name)) {
                    @Override
                    protected void populateView(View view, String s, int i) {
                        TextView textview = (TextView)view.findViewById(android.R.id.text1);
                        textview.setText(s);

                    }
                };
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //       Toast.makeText(getApplicationContext(), mListview.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                String chck = mListview.getItemAtPosition(position).toString();
                if (chck.contains(phoneNum)) {
                    adapter.getRef(position).removeValue();
                }
            }
        });

        Button addButton = (Button)findViewById(R.id.btn_input_opinion) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if( opinion.getText().toString().length() >0){
                    massagesRef.child(name).push().setValue(phoneNum + " : " + opinion.getText().toString());
                    opinion.setText("");
                } else {
                    Toast.makeText(getApplicationContext(),"댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Action send를 사용해서 댓글 남기기 기능 추가.
        opinion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //   ChatData chatData = new ChatData(opinion.getText().toString(), rating.getRating());
                    if(opinion.getText().toString().length() >0){
                        massagesRef.child(name).push().setValue(phoneNum + " : " + opinion.getText().toString());
                        opinion.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(),"댓글을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }

                }
                return handled;
            }

        });
    }
}
