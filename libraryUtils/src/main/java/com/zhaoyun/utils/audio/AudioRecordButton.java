package com.zhaoyun.utils.audio;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoyun.utils.R;

/**
 * description: 录音按钮
 * autour: zhaoyun
 * date: 2017/5/24 10:24
 */

public class AudioRecordButton extends Button implements
        ActivityCompat.OnRequestPermissionsResultCallback {
    static final int VOICE_REQUEST_CODE = 66;
    private ImageView mImageView;
    private TextView mTextView;
    private PopupWindowFactory mPop;
    private AudioRecoderUtils mAudioRecoderUtils;

    private String mouse_action_down_text = "松开保存";



    private String mouse_action_up_text = "按住说话";

    public AudioRecordButton(Context context) {
        super(context);
        init();
    }

    public AudioRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioRecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 设置音频文件保存路径
     */
    public void setSavePath(String path){
        mAudioRecoderUtils.setSavePath(path);
    }

    private void init(){
        //PopupWindow的布局文件
        final View view = View.inflate(getContext(), R.layout.audio_layout_microphone, null);

        mPop = new PopupWindowFactory(getContext(),view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

        mAudioRecoderUtils = new AudioRecoderUtils();

        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                if(!mAudioRecoderUtils.isCancel()) {
                    Toast.makeText(getContext(), "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                }

                mTextView.setText(TimeUtils.long2String(0));
            }
        });

        //6.0以上需要申请权限
        requestPermissions();
    }

    OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    mPop.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);

                    setText(mouse_action_down_text);
                    mAudioRecoderUtils.startRecord();


                    break;

                case MotionEvent.ACTION_UP:

                    mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                    if(mAudioRecoderUtils.isCancel()) {
                        Toast.makeText(getContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
                    }
                    mPop.dismiss();
                    setText(mouse_action_up_text);

                    break;
            }
            return true;
        }
    };

    /**
     * 判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启语音权限
        if ((ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            setOnTouchListener(onTouchListener);
        } else {
            //判断是否开启语音权限
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == VOICE_REQUEST_CODE) {
            if(grantResults.length>1) {
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    setOnTouchListener(onTouchListener);
                } else {
                    Toast.makeText(getContext(), "已拒绝权限！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public AudioRecoderUtils getAudioRecoderUtils() {
        return mAudioRecoderUtils;
    }
    public void setMouseDownText(String mouse_action_down_text) {
        this.mouse_action_down_text = mouse_action_down_text;
    }

    public void setMouseUpText(String mouse_action_up_text) {
        this.mouse_action_up_text = mouse_action_up_text;
    }
}
