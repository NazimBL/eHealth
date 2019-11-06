package com.example.dell.hackit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 23/03/2017.
 */

public class User implements Serializable {


public String name;
public int age;
public String gender;

public List<String> medocs;

public String sos1;

    public String sos2;
    private float temperature;
    private float humidity;
    private float heartbeat;
    public String data;


public User(String n,String g,int a){

medocs=new ArrayList<String>();
    this.name=n;
    this.gender=g;
    this.age=a;
    this.data="DummyData:Temp=38/Hum=40";


}
    public User(){

    }
    public void addMedoc(String med){
        this.medocs.add(med);
    }

    public String getName() {
        return name;
    }

    public String getSos2() {
        return sos2;
    }

    public void setSos2(String sos2) {
        this.sos2 = sos2;
    }

    public String getSos1() {
        return sos1;
    }

    public void setSos1(String sos1) {
        this.sos1 = sos1;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private float getHumidity() {

        return humidity;
    }

    private void setHumidity(float humidity) {
        this.humidity = humidity;
    }

     private float getHeartbeat() {

        return heartbeat;
    }

    private void setHeartbeat(float heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private float getTemperature() {
        return temperature;
    }

    private void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
