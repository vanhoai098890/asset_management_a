package com.example.app_common.ui.drawflower;

import android.annotation.SuppressLint;
import android.view.View;

/**
 * Created by mxn on 2016/12/9.
 * ViewHelper
 */

final class ViewHelper {

    private ViewHelper() {
    }

    @SuppressLint("NewApi")
    static int getLeft(View v) {
        return (int) (v.getLeft() + v.getTranslationX());
    }

    @SuppressLint("NewApi")
    static int getTop(View v) {
        return (int) (v.getTop() + v.getTranslationY());
    }

    @SuppressLint("NewApi")
    static int getRight(View v) {
        return (int) (v.getRight() + v.getTranslationX());
    }

    @SuppressLint("NewApi")
    static int getLayoutDirection(View v) {
        return v.getLayoutDirection();

    }
}