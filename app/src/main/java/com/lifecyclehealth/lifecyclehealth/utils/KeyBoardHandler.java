package com.lifecyclehealth.lifecyclehealth.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by satyam on 07/10/2016.
 */
public class KeyBoardHandler {
    private AppCompatActivity appCompatActivity;
    private View mRootView;

    public KeyBoardHandler(AppCompatActivity appCompatActivity, View mRootView) {
        this.appCompatActivity = appCompatActivity;
        this.mRootView = mRootView;
    }

    private boolean isKeyboardVisible() {
        Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        mRootView.getWindowVisibleDisplayFrame(r);

        int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
        boolean flag = heightDiff > 100;
        Log.e("KEYBOARD___", flag + " ");
        return heightDiff > 100; // if more than 100 pixels, its probably a keyboard...
    }

    public void showKeyboard() {
        if (isKeyboardVisible())
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (appCompatActivity.getCurrentFocus() == null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            View view = appCompatActivity.getCurrentFocus();
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public void hideKeyboard() {
        if (!isKeyboardVisible())
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (appCompatActivity.getCurrentFocus() == null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        } else {
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            // inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void hideKeyboard(boolean flag) {
        InputMethodManager imm = (InputMethodManager) appCompatActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//Hide:
        if (flag) {
            if (!isKeyboardVisible())
                return;
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
//Show
        else {
            if (isKeyboardVisible())
                return;
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
}
