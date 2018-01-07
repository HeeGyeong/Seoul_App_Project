package com.example.heegyeong.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Heegyeong on 2016-11-25.
 */
public class MINI extends AppCompatActivity {

    private static final int imgMaxSize = 400;

    public static String[] baseURL = new String[imgMaxSize];
    public static String[] product_name = new String[imgMaxSize];
    public static String[] event_value = new String[imgMaxSize];
    public static String[] product_price = new String[imgMaxSize];
    int count = 0;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    ArrayList<String>[] arrayListFood = new ArrayList[4];
    ArrayList<String>[] arrayListDrink = new ArrayList[4];
    ArrayList<String>[] arrayListSnack = new ArrayList[4];
    ArrayList<String>[] arrayListDaily = new ArrayList[4];

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minilayout);

        btn = (Button) findViewById(R.id.buttonmini);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadTxtDrink();
                loadTxtFood();
                loadTxtSnack();
                loadTxtDaily();
            }
        }).start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                while (true) {
                    if (i >= arrayListFood[0].size()) {
                        break;
                    } else if (arrayListFood[0].get(i) != null) {
                        baseURL[i] = arrayListFood[0].get(i);
                        product_name[i] = arrayListFood[1].get(i);
                        product_price[i] = arrayListFood[2].get(i)+"원";
                        event_value[i] = arrayListFood[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : ");
                        if (product_name[i] != null) {
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "식품");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("mini").push().setValue(product);
                        }
                        i++;
                    }
                }
                i = 0;
                while (true) {
                    if (i >= arrayListSnack[0].size()) {
                        break;
                    } else if (arrayListSnack[0].get(i) != null) {
                        baseURL[i] =  arrayListSnack[0].get(i);
                        product_name[i] = arrayListSnack[1].get(i);
                        product_price[i] = arrayListSnack[2].get(i) +"원";
                        event_value[i] = arrayListSnack[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : ");
                        if (product_name[i] != null) {
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "과자");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("mini").push().setValue(product);
                        }
                        i++;
                    }
                }
                i = 0;
                while (true) {
                    if (i >= arrayListDrink[0].size()) {
                        break;
                    } else if (arrayListDrink[0].get(i) != null) {
                        //text4.setText(arraylist4.get(i));
                        //product_name[i] = text1.getText().toString();
                        baseURL[i] = arrayListDrink[0].get(i);
                        product_name[i] = arrayListDrink[1].get(i);
                        product_price[i] = arrayListDrink[2].get(i) +"원";
                        event_value[i] = arrayListDrink[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : ");
                        if (product_name[i] != null) {
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "음료");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("mini").push().setValue(product);
                        }
                        i++;
                    }
                }
                i = 0;
                while (true) {
                    if (i >= arrayListDaily[0].size()) {
                        break;
                    } else if (arrayListDaily[0].get(i) != null) {
                        baseURL[i] = arrayListDaily[0].get(i);
                        product_name[i] = arrayListDaily[1].get(i);
                        product_price[i] = arrayListDaily[2].get(i) +"원";
                        event_value[i] = arrayListDaily[3].get(i);
                        Log.d("  index[count] : ", count++ + "번 : ");
                        if (product_name[i] != null) {
                            Product product
                                    = new Product(product_name[i], product_price[i], event_value[i], baseURL[i], "생필");  // 유저 이름과 메세지로 chatData 만들기
                            databaseReference.child("product").child("mini").push().setValue(product);
                        }
                        i++;
                    }
                }
                Toast.makeText(getApplication(), "DB에 저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadTxtDrink() {
        InputStream inputData1 = getResources().openRawResource(R.raw.minidrinkurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.minidrinkname);
        InputStream inputData3 = getResources().openRawResource(R.raw.minidrinkprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.minidrinkevent);
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
        InputStream inputData1 = getResources().openRawResource(R.raw.minifoodurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.minifoodname);
        InputStream inputData3 = getResources().openRawResource(R.raw.minifoodprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.minidrinkevent);
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
    public void loadTxtSnack(){
        InputStream inputData1 = getResources().openRawResource(R.raw.minisnackurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.minisnackname);
        InputStream inputData3 = getResources().openRawResource(R.raw.minisnackprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.minisnackevent);
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
        InputStream inputData1 = getResources().openRawResource(R.raw.minidailyurl);
        InputStream inputData2 = getResources().openRawResource(R.raw.minidailyname);
        InputStream inputData3 = getResources().openRawResource(R.raw.minidailyprice);
        InputStream inputData4 = getResources().openRawResource(R.raw.minidailyevent);
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

