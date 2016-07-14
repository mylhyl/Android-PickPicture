# Android-PickPicture
读取系统相册图片工具类，此库没有UI层，是考虑到每个项目的UI各有千秋，所以不打算加入UI部分，如有需要可[Issues](https://github.com/mylhyl/Android-PickPicture/issues)或加入QQ群：435173211 提出意见

#引用

```javascript
compile 'com.mylhyl:pickpicture:1.0.0'
```

[下载jar](preview/pickpicture-1.0.0.jar)

#效果图
<img src="preview/gif.gif" width="240px"/>

# 使用
1、先读取相册目录列表数据集合

```java
        pickPictureHelper = PickPictureHelper.readPicture(this, new PickPictureCallback() {
            @Override
            public void onStart() {
                //显示进度条
                mProgressDialog = ProgressDialog.show(PickPictureTotalActivity.this, null, "正在加载");
            }

            @Override
            public void onSuccess(List<PictureTotal> list) {
                mProgressDialog.dismiss();
                //读取成功，返回 list，直接丢入到 ListView 适配器中
                mListView.setAdapter(new PickPictureTotalAdapter(PickPictureTotalActivity.this, list));
            }

            @Override
            public void onError() {
                mProgressDialog.dismiss();
            }
        });
```
2、点击目录列表进入详细，也就是显示当前选择的目录所有的图片

```java
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                List<String> childList = pickPictureHelper.getChildPathList(position);
                PickPictureActivity.gotoActivity(PickPictureTotalActivity.this, (ArrayList<String>) childList);
            }
        });
```
