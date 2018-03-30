package com.example.htmltest.Screen;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

// 双击
// 这个方法的实现，需要首先实现了OnGestureListener才能进行。
public class DoubleTabImp implements GestureDetector.OnDoubleTapListener {
    String tag="me";
    public DoubleTabImp() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent) {
        // TODO Auto-generated method stub
        Log.e(tag, "onSingleTapConfirmed");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent paramMotionEvent) {
        // TODO Auto-generated method stub
        Log.e(tag, "onDoubleTap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent paramMotionEvent) {
        // TODO Auto-generated method stub
        Log.e(tag, "onDoubleTapEvent");
        return false;
    }

}








/*我们用写log的形式，来看它们的执行顺序。
        [html] view plain copy
        12-04 15:00:34.434: E/me(25274): down
        12-04 15:00:34.524: E/me(25274): onSingleTapUp
        12-04 15:00:34.614: E/me(25274): onDoubleTap
        12-04 15:00:34.614: E/me(25274): onDoubleTapEvent
        12-04 15:00:34.624: E/me(25274): down
        12-04 15:00:34.684: E/me(25274): onDoubleTapEvent
        如果是单击，顺序如下：
        [html] view plain copy
        12-04 15:15:33.664: E/me(25274): down
        12-04 15:15:33.764: E/me(25274): onSingleTapUp
        12-04 15:15:33.964: E/me(25274): onSingleTapConfirmed*/












