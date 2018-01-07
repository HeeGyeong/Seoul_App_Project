package com.example.heegyeong.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Heegyeong on 2016-11-23.
 */
public class CU extends AppCompatActivity {

    private static final int imgMaxSize = 700;

    public static String[] baseURL = new String[imgMaxSize];
    public static String[] product_name = new String[imgMaxSize];
    public static String[] event_value = new String[imgMaxSize];
    public static String[] product_price = new String[imgMaxSize];
    int count = 0;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    ArrayList<String>[] arrayListFood = new ArrayList[4];
    ArrayList<String>[] arrayListDrink = new ArrayList[4];
    ArrayList<String>[] arrayListIce = new ArrayList[4];
    ArrayList<String>[] arrayListIns = new ArrayList[4];
    ArrayList<String>[] arrayListSimp = new ArrayList[4];
    ArrayList<String>[] arrayListSnack = new ArrayList[4];
    ArrayList<String>[] arrayListDaily = new ArrayList[4];

    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.culayout);

        btn1 = (Button) findViewById(R.id.buttoncu);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadTxtDrink();
                loadTxtFood();
                loadTxtIce();
                loadTxtIns();
                loadTxtSimp();
                loadTxtSnack();
                loadTxtDaily();
            }
        }).start();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                while (true) {
                    if (i >= arrayListDrink[0].size()) {
                        break;
                    } else if (arrayListDrink[0].get(i) != null) {
                        //text4.setText(arraylist4.get(i));
                        //product_name[i] = text1.getText().toString();
                        baseURL[i] = arrayListDrink[0].get(i);
                        product_name[i] = arrayListDrink[1].get(i);
                        product_price[i] = arrayListDrink[2].get(i);
                        event_value[i] = arrayListDrink[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "음료");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                i=0;
                while (true) {
                    if (i >= arrayListFood[0].size()) {
                        break;
                    } else if (arrayListFood[0].get(i) != null) {
                        baseURL[i] = arrayListFood[0].get(i);
                        product_name[i] = arrayListFood[1].get(i);
                        product_price[i] = arrayListFood[2].get(i);
                        event_value[i] = arrayListFood[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "식품");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                i=0;
                while (true) {
                    if (i >= arrayListIce[0].size()) {
                        break;
                    } else if (arrayListIce[0].get(i) != null) {
                        baseURL[i] = arrayListIce[0].get(i);
                        product_name[i] = arrayListIce[1].get(i);
                        product_price[i] = arrayListIce[2].get(i);
                        event_value[i] = arrayListIce[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "아이스");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                i=0;
                while (true) {
                    if (i >= arrayListIns[0].size()) {
                        break;
                    } else if (arrayListIns[0].get(i) != null) {
                        baseURL[i] = arrayListIns[0].get(i);
                        product_name[i] = arrayListIns[1].get(i);
                        product_price[i] = arrayListIns[2].get(i);
                        event_value[i] = arrayListIns[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "즉석");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                i=0;
                while (true) {
                    if (i >= arrayListSimp[0].size()) {
                        break;
                    } else if (arrayListSimp[0].get(i) != null) {
                        baseURL[i] = arrayListSimp[0].get(i);
                        product_name[i] = arrayListSimp[1].get(i);
                        product_price[i] = arrayListSimp[2].get(i);
                        event_value[i] = arrayListSimp[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "간편");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                i=0;
                while (true) {
                    if (i >= arrayListSnack[0].size()) {
                        break;
                    } else if (arrayListSnack[0].get(i) != null) {
                        baseURL[i] = arrayListSnack[0].get(i);
                        product_name[i] = arrayListSnack[1].get(i);
                        product_price[i] = arrayListSnack[2].get(i);
                        event_value[i] = arrayListSnack[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "과자");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                i=0;
                while (true) {
                    if (i >= arrayListDaily[0].size()) {
                        break;
                    } else if (arrayListDaily[0].get(i) != null) {
                        baseURL[i] = arrayListDaily[0].get(i);
                        product_name[i] = arrayListDaily[1].get(i);
                        product_price[i] = arrayListDaily[2].get(i);
                        event_value[i] = arrayListDaily[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : " );
                        if(product_name[i] != null){
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "생필");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("cu").push().setValue(product);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                        }
                        i++;
                    }
                }
                Toast.makeText(getApplication(),"DB에 저장되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadTxtDrink() {
        InputStream inputData1 = getResources().openRawResource(R.raw.cudrinkurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cudrinkname);
        InputStream inputData3 = getResources().openRawResource(R.raw.cudrinkprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cudrinkevent);
        for(int i=0;i<4;i++){
            arrayListDrink[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListDrink[0].add(string1);
                    arrayListDrink[1].add(string2);
                    arrayListDrink[2].add(string3);
                    arrayListDrink[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void loadTxtFood(){
        InputStream inputData1 = getResources().openRawResource(R.raw.cufoodurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cufoodname);
        InputStream inputData3 = getResources().openRawResource(R.raw.cufoodprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cudrinkevent);
        for(int i=0;i<4;i++){
            arrayListFood[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListFood[0].add(string1);
                    arrayListFood[1].add(string2);
                    arrayListFood[2].add(string3);
                    arrayListFood[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void loadTxtIce(){
        InputStream inputData1 = getResources().openRawResource(R.raw.cuiceurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cuicename);
        InputStream inputData3 = getResources().openRawResource(R.raw.cuiceprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cuiceevent);
        for(int i=0;i<4;i++){
            arrayListIce[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListIce[0].add(string1);
                    arrayListIce[1].add(string2);
                    arrayListIce[2].add(string3);
                    arrayListIce[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void loadTxtIns(){
        InputStream inputData1 = getResources().openRawResource(R.raw.cuinsurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cuinsname);
        InputStream inputData3 = getResources().openRawResource(R.raw.cuinsprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cuinsevent);
        for(int i=0;i<4;i++){
            arrayListIns[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListIns[0].add(string1);
                    arrayListIns[1].add(string2);
                    arrayListIns[2].add(string3);
                    arrayListIns[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void loadTxtSimp(){
        InputStream inputData1 = getResources().openRawResource(R.raw.cusimpurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cusimpname);
        InputStream inputData3 = getResources().openRawResource(R.raw.cusimpprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cusimpevent);
        for(int i=0;i<4;i++){
            arrayListSimp[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListSimp[0].add(string1);
                    arrayListSimp[1].add(string2);
                    arrayListSimp[2].add(string3);
                    arrayListSimp[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void loadTxtSnack(){
        InputStream inputData1 = getResources().openRawResource(R.raw.cusnackurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cusnackname);
        InputStream inputData3 = getResources().openRawResource(R.raw.cusnackprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cusnackevent);
        for(int i=0;i<4;i++){
            arrayListSnack[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListSnack[0].add(string1);
                    arrayListSnack[1].add(string2);
                    arrayListSnack[2].add(string3);
                    arrayListSnack[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void loadTxtDaily(){
        InputStream inputData1 = getResources().openRawResource(R.raw.cudailyurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.cudailyname);
        InputStream inputData3 = getResources().openRawResource(R.raw.cudailyprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.cudailyevent);
        for(int i=0;i<4;i++){
            arrayListDaily[i] = new ArrayList<String>();
        }

        try {
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputData1, "EUC_KR"));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputData2, "EUC_KR"));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputData3, "EUC_KR"));
            BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(inputData4, "EUC_KR"));
            while (true) {
                String string1 = bufferedReader1.readLine();
                String string2 = bufferedReader2.readLine();
                String string3 = bufferedReader3.readLine();
                String string4 = bufferedReader4.readLine();

                if (string1 != null) {
                    arrayListDaily[0].add(string1);
                    arrayListDaily[1].add(string2);
                    arrayListDaily[2].add(string3);
                    arrayListDaily[3].add(string4);
                } else {

                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}