package com.jbtm.parentschool.models;

/**
 * Created by lvhailing on 2018/12/15.
 */

public class KaModel {
    public String name;
    public String price;
    public String original_price;
    public Integer package_id;
    public int discount;    //是否特价优惠

    public KaModel(String name, String price, String original_price, int discount) {
        this.name = name;
        this.price = price;
        this.original_price = original_price;
        this.discount = discount;
    }
}
