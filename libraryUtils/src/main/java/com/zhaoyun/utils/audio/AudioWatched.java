package com.zhaoyun.utils.audio;

/**
 * description: 录音被观察者
 * autour: zhaoyun
 * date: 2017/5/24 15:05
 */

public interface AudioWatched {
    //在其接口中定义一个用来增加观察者的方法
    public void add(AudioWatcher watcher);
    //再定义一个用来删除观察者权利的方法
    public void remove(AudioWatcher watcher);
    //通知观察者音频文件保存成功
    public void notifyAudioFileSaveSuccess(AudioModel audio);
}
