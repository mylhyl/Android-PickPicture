package com.mylhyl.pickpicture;

/**
 * Created by hupei on 2016/7/14.
 */
public class PictureTotal {
    private String topPicturePath;//文件夹的第一张图片路径
    private String folderName;//文件夹名
    private int pictureCount;//文件夹中的图片数

    public String getTopPicturePath() {
        return topPicturePath;
    }

    public void setTopPicturePath(String topPicturePath) {
        this.topPicturePath = topPicturePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }

}
