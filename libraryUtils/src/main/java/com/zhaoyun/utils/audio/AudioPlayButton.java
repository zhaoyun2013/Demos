package com.zhaoyun.utils.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zhaoyun.utils.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * description: $todo$
 * autour: zhaoyun
 * date: 2017/5/24 14:32
 */

public class AudioPlayButton extends ImageView implements AudioWatcher {

    AudioModel audio;
    private MediaPlayer mediaPlayer;

    /**
     * 设置向左或者向右的动画
     * @param leftOrRight 1：向左，2：向右
     */
    public void setLeftOrRight(int leftOrRight) {
        this.leftOrRight = leftOrRight;
    }

    private int leftOrRight = 1;

    public AudioPlayButton(Context context) {
        super(context);
        init();
    }

    public AudioPlayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioPlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AudioPlayButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setBackgroundResource(R.drawable.audio_voice_playing_bg);
        setImageResource(R.drawable.audio_in_voice_playing_f3);
        setOnClickListener(onClickListener);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(audio != null) {
                if(mediaPlayer!=null && mediaPlayer.isPlaying()) {
                    return;
                }
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audio.getFilePath());
                    mediaPlayer.prepare();

                    mediaPlayer.start();

                    playAudioAnimation(AudioPlayButton.this,leftOrRight);
                } catch (IOException e) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void audioFileSaveSuccess(AudioModel audio) {
        this.audio = audio;
    }

    // 记录语音动画图片
    Timer mTimer = null;
    // 语音动画控制任务
    TimerTask mTimerTask = null;
    int index = 1;
    AudioAnimationHandler audioAnimationHandler = null;
    private void playAudioAnimation(final ImageView imageView, final int inOrOut) {
        // 定时器检查播放状态
        stopTimer();
        mTimer = new Timer();
        // 将要关闭的语音图片归位
        if (audioAnimationHandler != null) {
            Message msg = new Message();
            msg.what = 3;
            audioAnimationHandler.sendMessage(msg);
        }

        audioAnimationHandler = new AudioAnimationHandler(imageView, inOrOut);
        mTimerTask = new TimerTask() {
            public boolean hasPlayed = false;

            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    hasPlayed = true;
                    index = (index + 1) % 3;
                    Message msg = new Message();
                    msg.what = index;
                    audioAnimationHandler.sendMessage(msg);
                } else {
                    // 当播放完时
                    Message msg = new Message();
                    msg.what = 3;
                    audioAnimationHandler.sendMessage(msg);
                    // 播放完毕时需要关闭Timer等
                    if (hasPlayed) {
                        stopTimer();
                    }
                }
            }
        };
        // 调用频率为500毫秒一次
        mTimer.schedule(mTimerTask, 0, 500);
    }

    class AudioAnimationHandler extends Handler {
        ImageView imageView;
        // 判断是左对话框还是右对话框
        private boolean isLeft;

        public AudioAnimationHandler(ImageView imageView, int inOrOut) {
            this.imageView = imageView;
            this.isLeft = inOrOut == 1 ? true : false;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 根据msg.what来替换图片，达到动画效果
            switch (msg.what) {
                case 0:
                    imageView
                            .setImageResource(isLeft ? R.drawable.audio_in_voice_playing_f1
                                    : R.drawable.audio_out_voice_playing_f1);
                    break;
                case 1:
                    imageView
                            .setImageResource(isLeft ? R.drawable.audio_in_voice_playing_f2
                                    : R.drawable.audio_out_voice_playing_f2);
                    break;
                case 2:
                    imageView
                            .setImageResource(isLeft ? R.drawable.audio_in_voice_playing_f3
                                    : R.drawable.audio_out_voice_playing_f3);
                    break;
                default:
                    imageView
                            .setImageResource(isLeft ? R.drawable.audio_in_voice_playing_f3
                                    : R.drawable.audio_out_voice_playing_f3);
                    break;
            }
        }

    }
    /**
     * 停止
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }
}
