package com.zhaoyun.utils.video;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaoyun.utils.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PickVideoActivity extends Activity {

	public static final String PICK_VIDEO_PATH = "pick_video_path";
    public static final String PICK_IMAGE_PATH = "pick_image_path";

	
	private String fileDir;
    /**
     * 视频截图路径
     */
    private String imagePath;

    public String getImageFullName() {
        return imageFullName;
    }

    /**
     * 视频截图文件全路径
     */
    private String imageFullName;
	
	private MovieRecorderView movieRecorderView;
	private RoundProgressBar tvRec;
    private TextView cancel;
	private RelativeLayout endview;
	private ImageView imgArrow;

	private LinearLayout lyUpCancel;
	
	private String filePath;
	private int startY;
	
	private boolean isCanceled = false;
    private ScreenSwitchUtils instance;
	
	private MovieRecorderView.OnRecordFinishListener stopRecListener = new MovieRecorderView.OnRecordFinishListener()
	{

		@Override
		public void onRecordFinish(String path, int recordeTime) {
			filePath = path;
			if(recordeTime < 2)
			{
			    movieRecorderView.cancelRec();
                Toast.makeText(PickVideoActivity.this, "时间过短，请重新拍摄",Toast.LENGTH_SHORT).show();
			}
			else {
			    closeVideo(isCanceled);

            }
			
		}
		
	};

    @Override
    protected void onStart() {
        super.onStart();
        instance.start(this);
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.video_activity_pick_video);
		
		movieRecorderView = (MovieRecorderView) findViewById(R.id.movierecordview);
        instance = ScreenSwitchUtils.init(this.getApplicationContext());

        ViewTreeObserver vto = movieRecorderView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                movieRecorderView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = movieRecorderView.getHeight();//605 806
                int width = movieRecorderView.getWidth();//720
                movieRecorderView.setWidthAndHeight(width,height);
            }
        });
        cancel = (TextView)findViewById(R.id.tvCancel);
        tvRec = (RoundProgressBar) findViewById(R.id.tvRec);
        movieRecorderView.setRoundProgressBar(tvRec);
        lyUpCancel = (LinearLayout)findViewById(R.id.lyUpCancel);
        imgArrow = (ImageView)findViewById(R.id.imgArrow);
        endview= (RelativeLayout) findViewById(R.id.endview);

        //录像文件保存路径
        fileDir = getIntent().getStringExtra(PICK_VIDEO_PATH);
        //录像截图文件保存路径
        imagePath = getIntent().getStringExtra(PICK_IMAGE_PATH);
        if(TextUtils.isEmpty(fileDir)){
            File workspace = Environment.getExternalStorageDirectory();
            File voiceWorkspace = new File(workspace, "video/");
            if (!voiceWorkspace.exists()) {
                voiceWorkspace.mkdirs();
            }
            fileDir = voiceWorkspace.toString() + "/";
        }else{
            File file = new File(fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        if(TextUtils.isEmpty(imagePath)) {
            File workspace = Environment.getExternalStorageDirectory();
            File voiceWorkspace = new File(workspace, "video/");
            if (!voiceWorkspace.exists()) {
                voiceWorkspace.mkdirs();
            }
            imagePath = voiceWorkspace.getAbsolutePath();
        }else{
            File file = new File(imagePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        movieRecorderView.setVideoDir(fileDir);
        
        tvRec.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startY = (int) event.getRawY();
                        movieRecorderView.setPosition(instance.getPosition());
                        movieRecorderView.record(stopRecListener);
                        lyUpCancel.setVisibility(View.VISIBLE);
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        if ((startY - (int) event.getRawY()) > 90) {
                            cancel.setText("松开取消");
                            imgArrow.setVisibility(View.INVISIBLE);
                        } else {
                            imgArrow.setVisibility(View.VISIBLE);
                            cancel.setText("上移取消");
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if ((startY - (int) event.getRawY()) > 90) {
                            isCanceled = true;
                            lyUpCancel.setVisibility(View.GONE);
                            cancel.setText("上移取消");
                            movieRecorderView.cancelRec();
                        } else {
                            isCanceled = false;
                            movieRecorderView.stopRec();
                        }
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }
        });
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		movieRecorderView.setOpenCamera(true);
	}
	
	
	
	@Override
	protected void onStop() {
        instance.stop();
		movieRecorderView.setOpenCamera(false);
		super.onStop();
	}

	private void closeVideo(boolean isCancel)
	{
        getVideoThumbnail();
        Intent intent = new Intent();
        intent.putExtra(PICK_VIDEO_PATH, filePath);
        intent.putExtra(PICK_IMAGE_PATH, imageFullName);
        setResult(RESULT_OK,intent);
		finish();
	}

    private void getVideoThumbnail() {
        Bitmap bmp = VideodemoUtil.getVideoThumbnail(filePath, 96, 96, MediaStore.Images.Thumbnails.MICRO_KIND);
        if (bmp != null) {
            imageFullName = VideodemoUtil.saveBitmapFile(bmp,createPhotoByTime());
        }
    }

    private File createPhotoByTime() {
        String sfileName = new SimpleDateFormat("yyyyMMddHHmmss",
                Locale.getDefault()).format(Calendar.getInstance().getTime())
                + ".jpeg";

        return new File(imagePath, sfileName);
    }
}
