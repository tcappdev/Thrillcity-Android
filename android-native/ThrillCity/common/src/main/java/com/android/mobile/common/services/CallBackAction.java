package com.android.mobile.common.services;


public interface CallBackAction<DomainType> {

    void onSuccess(DomainType responseObject);

    void onError(RetrofitException failure);
}
