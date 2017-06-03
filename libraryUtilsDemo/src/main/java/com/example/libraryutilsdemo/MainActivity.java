package com.example.libraryutilsdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhaoyun.utils.audio.AudioModel;
import com.zhaoyun.utils.audio.AudioPlayButton;
import com.zhaoyun.utils.audio.AudioRecordButton;
import com.zhaoyun.utils.audio.AudioWatcher;
import com.zhaoyun.utils.video.PickVideoActivity;
import com.zhaoyun.utils.video.RecordVideo;

public class MainActivity extends AppCompatActivity implements AudioWatcher {
    private RecordVideo recordVideo;
    private String mFilePath;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioRecordButton btn1 = (AudioRecordButton)findViewById(R.id.btn1);
        btn1.setMouseDownText("按下");
        btn1.setMouseUpText("抬起");

        AudioPlayButton play = (AudioPlayButton)findViewById(R.id.play);
        btn1.getAudioRecoderUtils().add(play);
        //加入观察者队列
        btn1.getAudioRecoderUtils().add(this);

        Button btnRecord = (Button) findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recordVideo = new RecordVideo(MainActivity.this);
                recordVideo.makeVideo(Environment.getExternalStorageDirectory()+"/a1",Environment.getExternalStorageDirectory()+"/a2");
            }
        });

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo.play(mFilePath);
            }
        });
    }

    @Override
    public void audioFileSaveSuccess(AudioModel audio) {
        Log.d("", "audioFileSaveSuccess: "+audio.getFilePath()+"时长"+audio.getTime());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 final Intent data) {
        switch (requestCode) {
            case RecordVideo.REQUEST_VIDEO:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        mFilePath = data.getStringExtra(PickVideoActivity.PICK_VIDEO_PATH);
                        String imagePath = data.getStringExtra(PickVideoActivity.PICK_IMAGE_PATH);

                        if (TextUtils.isEmpty(mFilePath)) {
                            Toast.makeText(MainActivity.this, "获取视频失败，有可能是麦克风功能被禁止，请手动设置后重试",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            imageView.setImageURI(Uri.parse("file://"+imagePath));
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "获取视频失败，有可能是麦克风功能被禁止，请手动设置后重试",
                                Toast.LENGTH_SHORT).show();
                    }

                }
        }
    }
}
