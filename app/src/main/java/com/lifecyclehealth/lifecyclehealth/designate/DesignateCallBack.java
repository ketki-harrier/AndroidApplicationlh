package com.lifecyclehealth.lifecyclehealth.designate;


import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;

/**
 * Created by vaibhavi on 01-11-2017.
 */

public interface DesignateCallBack {
    void onSuccess(CheckProviderResponse response);

    void onError(int error);
}