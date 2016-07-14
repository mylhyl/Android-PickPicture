package com.mylhyl.pickpicture;

import java.util.List;

/**
 * Created by hupei on 2016/7/14.
 */
public interface PickPictureCallback {
    void onStart();

    void onSuccess(List<PictureTotal> list);

    void onError();
}
