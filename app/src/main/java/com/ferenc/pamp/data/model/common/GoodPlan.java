package com.ferenc.pamp.data.model.common;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */

public class GoodPlan {
    private String mName;
    private String mProductName;
    private String mOrderStatus;
    private String PlanStatus;
    private boolean isReused;

    public GoodPlan(String mName, String mProductName, String mOrderStatus, String planStatus, boolean isReused) {
        this.mName = mName;
        this.mProductName = mProductName;
        this.mOrderStatus = mOrderStatus;
        PlanStatus = planStatus;
        this.isReused = isReused;
    }

    public GoodPlan() {
    }

    public String getmName() {
        return mName;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmOrderStatus() {
        return mOrderStatus;
    }

    public String getPlanStatus() {
        return PlanStatus;
    }

    public boolean isReused() {
        return isReused;
    }
}
