package com.example.htmltest.Screen;


import android.view.GestureDetector;
import android.view.MotionEvent;

/*这个类，实际上是实现了以上两个接口的一个类。使用的时候，可以继承这个类，选择你要的方法来实现相应的操作。
        也就是说，你可以直接用这个，不用上面的两个接口。*/
public class SimpleGuestureImp extends GestureDetector.SimpleOnGestureListener {

    public SimpleGuestureImp() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
        super.onShowPress(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // TODO Auto-generated method stub
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // TODO Auto-generated method stub
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // TODO Auto-generated method stub
        return super.onSingleTapConfirmed(e);
    }

}





/*手势识别——滑动的使用

        这里我们开始用例子来说明如何实现滑动效果，步骤如下：
        1、新建工程，在新的工程中有默认的MainActivity，这个类要实现接口OnTouchListener；
        2、定义接口GestureDetector mGestureDetector，并将接口实现传入;
        3、绑定view与ontouchlistener；
        3、截取OnTouchListener的event，将它传入gesturedetector中。
        如果我们要将OnDoubleTapListener的接口实现也放入，那么用mGestureDetector.setOnDoubleTapListener(new DoubleTabImp());绑定这个实现。*/



















