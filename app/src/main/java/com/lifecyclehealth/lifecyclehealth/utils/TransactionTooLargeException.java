package com.lifecyclehealth.lifecyclehealth.utils;


import android.os.RemoteException;

public class TransactionTooLargeException extends RemoteException {

    public TransactionTooLargeException() {
        super();
    }

    public TransactionTooLargeException(String msg) {
        super(msg);
    }
}
