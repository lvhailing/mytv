package com.jbtm.parentschool.models;

/**
 * Created by lvhailing on 2018/12/15.
 */

public class MaterModel {
    public String ma_title;
    public String updated_time;
    public String ma_time_format;
    public long ma_time;
    public int trial;
    public int ma_id;
    public long course_id;

    public MaterModel(String ma_title, String ma_time_format) {
        this.ma_title = ma_title;
        this.ma_time_format = ma_time_format;
    }
}
