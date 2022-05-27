package com.example.petsplace.json;

public class Current {
    String last_updated_epoch;
    String last_updated;
    String temp_c;
    String temp_f;
    String is_day;
    Condition condition;

    public String getLast_updated_epoch() {
        return last_updated_epoch;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public String getTemp_c() {
        return temp_c;
    }

    public String getTemp_f() {
        return temp_f;
    }

    public String getIs_day() {
        return is_day;
    }

    public Condition getCondition() {
        return condition;
    }
}
