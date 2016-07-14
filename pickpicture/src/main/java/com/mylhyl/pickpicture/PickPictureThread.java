package com.mylhyl.pickpicture;

/**
 * Created by hupei on 2016/7/14.
 */
abstract class PickPictureThread extends Thread implements Runnable {
    public abstract void pickPictureThreadRun();
    @Override
    public void run() {
        pickPictureThreadRun();
    }
}
