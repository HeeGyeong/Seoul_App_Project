package com.example.heegyeong.culture_app;

/**
 * Created by Heegyeong on 2017-10-30.
 */
public class Data  {
    private String newAddress;
    private String oldAddress;
    private String coordX;
    private String coordY;
    private String contsName;
    private String guName;

    private int dataIndex;

    public Data(String oldAddress, String contsName, String coordX, String coordY){
        this.oldAddress = oldAddress;
        this.contsName = contsName;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Data(){

    }

    public String getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }

    public String getContsName() {
        return contsName;
    }

    public void setContsName(String contsName) {
        this.contsName = contsName;
    }

    public String getGuName() {
        return guName;
    }

    public void setGuName(String guName) {
        this.guName = guName;
    }

    public void setDataIndex(int dataIndex) { this.dataIndex = dataIndex; }

    public int getDataIndex() { return dataIndex; }

}