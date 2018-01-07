package com.example.heegyeong.seoul_maptagging;

import android.util.Log;

import java.util.Vector;

/**
 * Created by user on 2017-09-28.
 */

public class ADD {
    public void ADD(Vector<Data> Data, Vector<DataDetail> DataDetail, Vector<DataAll> DataAll) {

        for (int n = 0; n < Data.size(); n++) {
            for (int i = 0; i < DataDetail.size(); i++) {
                if (Data.get(n).getCoordX().equals(DataDetail.get(i).getCoordX())) {
                    DataAll app = new DataAll();
                    app.setNewAddress(Data.get(n).getNewAddress());
                    app.setOldAddress(Data.get(n).getOldAddress());
                    app.setCoordX(Data.get(n).getCoordX());
                    app.setCoordY(Data.get(n).getCoordY());
                    app.setGuName(Data.get(n).getGuName());
                    app.setContsName(Data.get(n).getContsName());
                    app.setName_01(DataDetail.get(i).getName_01());
                    app.setName_02(DataDetail.get(i).getName_02());
                    app.setName_03(DataDetail.get(i).getName_03());
                    app.setName_04(DataDetail.get(i).getName_04());
                    app.setName_05(DataDetail.get(i).getName_05());
                    app.setName_06(DataDetail.get(i).getName_06());
                    app.setName_07(DataDetail.get(i).getName_07());
                    app.setName_05(DataDetail.get(i).getName_08());
                    app.setName_06(DataDetail.get(i).getName_09());
                    app.setName_07(DataDetail.get(i).getName_10());
                    app.setValue_01(DataDetail.get(i).getValue_01());
                    app.setValue_02(DataDetail.get(i).getValue_02());
                    app.setValue_03(DataDetail.get(i).getValue_03());
                    app.setValue_04(DataDetail.get(i).getValue_04());
                    app.setValue_05(DataDetail.get(i).getValue_05());
                    app.setValue_06(DataDetail.get(i).getValue_06());
                    app.setValue_07(DataDetail.get(i).getValue_07());
                    app.setValue_05(DataDetail.get(i).getValue_08());
                    app.setValue_06(DataDetail.get(i).getValue_09());
                    app.setValue_07(DataDetail.get(i).getValue_10());
                    app.setNumber(DataDetail.get(n).getNumber());
                    Log.d("COT_TEL_NO", "app.setNumber : " + DataDetail.get(n).getNumber());

                    DataAll.add(app);

                }
            }
        }


    }
}
