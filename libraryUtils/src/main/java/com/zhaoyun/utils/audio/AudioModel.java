package com.zhaoyun.utils.audio;

/**
 * description: 音频文件模型
 * autour: zhaoyun
 * date: 2017/5/24 15:06
 */

public class AudioModel {
    String filePath;
    long time;

    public AudioModel(String path, long t){
        this.filePath =  path;
        this.time = t;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePaht(String filePath) {
        this.filePath = filePath;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
