package com.mymatatu.model;

/**
 * Created by anonymous on 03-09-2017.
 */

public class CountryCityRSM {
    public String id;
    public String name;
    public String id_c;

    public CountryCityRSM(String id, String name, String id_c) {
        this.id = id;
        this.name = name;
        this.id_c = id_c;
    }

    public CountryCityRSM(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
