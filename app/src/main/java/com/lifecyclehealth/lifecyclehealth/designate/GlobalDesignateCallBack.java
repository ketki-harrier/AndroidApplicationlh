package com.lifecyclehealth.lifecyclehealth.designate;

import com.lifecyclehealth.lifecyclehealth.model.GlobalCheckProviderResponse;

public interface GlobalDesignateCallBack {

    void onSuccess(GlobalCheckProviderResponse response);

    void onError(int error);
}
