package com.mylhyl.pickpicture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hupei on 2016/7/14.
 */
class PickPicture {
    private Context mContext;
    private HashMap<String, List<String>> mGroupMap = new HashMap<>();
    private List<PictureTotal> mPictureItems = new ArrayList<>();
    private PickPictureThread mThread;
    private PickPictureHandler mHandler;
    private PickPictureCallback mCallback;

    public PickPicture(Context context, PickPictureCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        mThread = new PickPictureThread() {
            @Override
            public void pickPictureThreadRun() {
                readPicture();
            }
        };
        mHandler = new PickPictureHandler(mCallback);
    }

    void start() {
        mThread.start();
    }

    private void readPicture() {
        mGroupMap.clear();
        mPictureItems.clear();
        Uri pictureUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        //只查询jpeg和png的图片
        Cursor cursor = contentResolver.query(pictureUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null || cursor.getCount() == 0) {
            mHandler.sendEmptyMessage(PickPictureHandler.SCAN_ERROR);
        } else {
            while (cursor.moveToNext()) {
                //获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                try {
                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    //根据父路径名将图片放入到groupMap中
                    if (!mGroupMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<>();
                        chileList.add(path);
                        mGroupMap.put(parentName, chileList);
                    } else {
                        mGroupMap.get(parentName).add(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            //通知Handler扫描图片完成
            mPictureItems = subGroupOfPicture(mGroupMap);
            Message message = mHandler.obtainMessage();
            message.obj = mPictureItems;
            message.what = PickPictureHandler.SCAN_OK;
            message.sendToTarget();
        }
    }

    /**
     * 组装分组数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param groupMap
     * @return
     */
    private List<PictureTotal> subGroupOfPicture(HashMap<String, List<String>> groupMap) {
        List<PictureTotal> list = new ArrayList<>();
        if (groupMap.size() == 0) {
            return list;
        }
        Iterator<Map.Entry<String, List<String>>> it = groupMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            PictureTotal pictureTotal = new PictureTotal();
            String key = entry.getKey();
            List<String> value = entry.getValue();
            SortPictureList sortList = new SortPictureList();
            Collections.sort(value, sortList);//按修改时间排序
            pictureTotal.setFolderName(key);
            pictureTotal.setPictureCount(value.size());
            pictureTotal.setTopPicturePath(value.get(0));//获取该组的第一张图片
            list.add(pictureTotal);
        }
        return list;
    }

    List<String> getChildPathList(int position) {
        List<String> childList = new ArrayList<>();
        if (mPictureItems.size() == 0)
            return childList;
        PictureTotal pictureTotal = mPictureItems.get(position);
        childList = mGroupMap.get(pictureTotal.getFolderName());
        SortPictureList sortList = new SortPictureList();
        Collections.sort(childList, sortList);
        return childList;
    }
}
