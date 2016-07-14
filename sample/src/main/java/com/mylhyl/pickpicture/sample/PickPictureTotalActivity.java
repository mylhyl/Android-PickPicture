package com.mylhyl.pickpicture.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mylhyl.pickpicture.PickPictureCallback;
import com.mylhyl.pickpicture.PickPictureHelper;
import com.mylhyl.pickpicture.PictureTotal;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择相册，手机所有的图片列表
 * Created by hupei on 2016/7/14.
 */
public class PickPictureTotalActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_SELECT_PICTURE = 102;
    public static final int REQUEST_CODE_SELECT_ALBUM = 104;
    public static final String EXTRA_PICTURE_PATH = "picture_path";

    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private PickPictureHelper pickPictureHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture_total);
        mListView = (ListView) findViewById(R.id.pick_picture_total_listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<String> childList = pickPictureHelper.getChildPathList(position);
                PickPictureActivity.gotoActivity(PickPictureTotalActivity.this, (ArrayList<String>) childList);
            }
        });
        getPicture();
    }

    @Override
    protected void onPause() {
        mProgressDialog.dismiss();
        super.onPause();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getPicture() {
        pickPictureHelper = PickPictureHelper.readPicture(this, new PickPictureCallback() {
            @Override
            public void onStart() {
                //显示进度条
                mProgressDialog = ProgressDialog.show(PickPictureTotalActivity.this, null, "正在加载");
            }

            @Override
            public void onSuccess(List<PictureTotal> list) {
                mProgressDialog.dismiss();
                mListView.setAdapter(new PickPictureTotalAdapter(PickPictureTotalActivity.this, list));
            }

            @Override
            public void onError() {
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == PickPictureTotalActivity.REQUEST_CODE_SELECT_ALBUM) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }
    }

    public static void gotoActivity(Activity activity) {
        Intent intent = new Intent(activity, PickPictureTotalActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
    }
}
