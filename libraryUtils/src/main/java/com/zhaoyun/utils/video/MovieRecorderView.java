package com.zhaoyun.utils.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhaoyun.utils.R;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 视频播放控件
 */
public class MovieRecorderView extends LinearLayout implements OnErrorListener
{
    
    private static final String TAG = "MovieRecorderView";
    
    private Context context;
    
    private SurfaceView mSurfaceView;
    
    private SurfaceHolder mSurfaceHolder;
    
    private MediaRecorder mMediaRecorder;
    
    private Camera mCamera;
    
    private Timer mTimer;// 计时器
    
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口
    
    private int mWidth;// 视频分辨率宽度
    
    private int mHeight;// 视频分辨率高度
    
    private boolean isOpenCamera;// 是否一开始就打开摄像头
    
    private int mRecordMaxTime;// 一次拍摄最长时间
    
    private int mTimeCount;// 时间计数
    
    private File mVecordFile = null;// 文件
    
//    private VerticalSeekBar mZoomBar = null;
    
    private String videoDir;
    
  //正在录制
    private boolean isRecording = false;

    public void setRoundProgressBar(RoundProgressBar roundProgressBar) {
        this.roundProgressBar = roundProgressBar;
    }

    private RoundProgressBar roundProgressBar;
    
    //	private Camera.AutoFocusCallback mAutoFocusCallback;
    
    public MovieRecorderView(Context context)
    {
        this(context, null);
    }
    
    public MovieRecorderView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    @SuppressLint("NewApi")
    public MovieRecorderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        this.context = context;

        mWidth = CamParaUtil.getWindowWidth(context);
        mHeight =CamParaUtil.getWindowWidth(context);
        
        isOpenCamera = true;
        mRecordMaxTime = 10;
        
        LayoutInflater.from(context)
                .inflate(R.layout.video_movie_recorder_view, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mProgressBar.setMax(mRecordMaxTime);// 设置进度条最大量
        
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
//        mZoomBar = (VerticalSeekBar) findViewById(R.id.seekBar);
        
        // a.recycle();
        
    }
    public void setWidthAndHeight(int width,int height){
        this.mWidth = width;
        this.mHeight = height;
    }
    
    /*class CameraTimerTask extends TimerTask {

    	@Override
    	public void run() {
    		if (mCamera != null) {
    			mCamera.autoFocus(mAutoFocusCallback);
    		}
    	}
    }*/


    private class CustomCallBack implements Callback
    {
        
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            if (!isOpenCamera)
                return;
            try
            {
                initCamera();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                int height)
        {

//            System.out.println("surfaceChanged  mWidth == "+width + "----"+height);
//            mHeight = height;
//            mWidth = width;
//            if (!isOpenCamera)
//                return;
//            try
//            {
//                initCamera();
//            }
//            catch (IOException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
        
        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            if (!isOpenCamera)
                return;
            freeCameraResource();

        }
        
    }
    
    /**
     * 初始化摄像头
     * 
     * @throws IOException
     */
    private void initCamera() throws IOException
    {
        Log.d("MovieRecorderView", "initCamera |  begin initCamera ");
        if (mCamera != null)
        {
            freeCameraResource();
        }
        try
        {
            mCamera = Camera.open();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null)
            return;
        
        initSeekBar();
        setCameraParams();
        mCamera.setPreviewDisplay(mSurfaceHolder);
        mCamera.startPreview();
        mCamera.unlock();
        /*mAutoFocusCallback = new Camera.AutoFocusCallback() {
        	public void onAutoFocus(boolean success, Camera camera) {
        		if (success) {
        			mCamera.setOneShotPreviewCallback(null);
        			// Toast.makeText(TestPhotoActivity.this, "自动聚焦成功" ,
        			// Toast.LENGTH_SHORT).show();
        		}
        	}
        };
        mCamera.autoFocus(mAutoFocusCallback);*/
        
        Log.d("MovieRecorderView", "initCamera | end initCamera ");
    }
    
    /**
     * 初始化焦距控制条
     */
    private void initSeekBar()
    {
        int max = mCamera.getParameters().getMaxZoom();
        
        final Parameters params = mCamera.getParameters();
        if (max == 0)
        {
//            mZoomBar.setVisibility(View.GONE);
        }
        else
        {
//            mZoomBar.setMax(max);
//            mZoomBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
//            {
//                public void onStopTrackingTouch(SeekBar seekBar)
//                {
//
//                }
//
//                public void onStartTrackingTouch(SeekBar seekBar)
//                {
//                }
//
//                public void onProgressChanged(SeekBar seekBar, int progress,
//                        boolean fromUser)
//                {
//                    try
//                    {
//                        if (mCamera != null && params.isZoomSupported())
//                        {
//                            if (params.isSmoothZoomSupported())
//                            {
//                                mCamera.startSmoothZoom(progress);
//                            }
//                            else
//                            {
//                                Log.e("MovieRecorderView", "不支持智能变焦");
//                                params.setZoom(progress);
//                                mCamera.setParameters(params);
//                            }
//                        }
//                    }
//                    catch (Exception e)
//                    {
//                        mZoomBar.setVisibility(View.GONE);
//                    }
//                }
//            });
        }
    }
    
    /**
     * 设置摄像头为横屏
     * 
     */
    private void setCameraParams()
    {
        if (mCamera != null)
        {

            Parameters mParams = mCamera.getParameters();
            mParams.set("orientation",Parameters.SCENE_MODE_PORTRAIT);//portrait
            
            Size bSize = getOptimalPreviewSize(mParams.getSupportedPreviewSizes(),mWidth,mHeight);
            Log.d("MovieRecorderView", "previewSize = " + bSize.width
                    + " | " + bSize.height);
//
//            mParams.setPictureSize(bSize.width, bSize.height);
//            mParams.setPreviewSize(bSize.width, bSize.height);
//            long time = new Date().getTime();
//            mParams.setGpsTimestamp(time);
////            Size pictureSize = findBestPictureSize(mParams);
////            p.setPictureSize(pictureSize.width, pictureSize.height);
//            Size previewSize = findBestPreviewSize(mParams);
//            mParams.setPreviewSize(previewSize.width,previewSize.height);

            mCamera.setDisplayOrientation(90);
            mCamera.setParameters(mParams);

            int supportPreviewWidth = bSize.width;
            int supportPreviewHeight = bSize.height;

            int srcWidth = getScreenWH().widthPixels;
            int srcHeight = getScreenWH().heightPixels;

            int width = Math.min(srcWidth, srcHeight);
            int height = width * supportPreviewWidth / supportPreviewHeight ;

//            mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

//            System.out.println("after setting == "+mCamera.getParameters().getPreviewSize().width + "*" +
//                    mCamera.getParameters().getPreviewSize().height);
        }
    }
    
    /**
     * 获取屏幕长宽比
     */
    public float getScreenRate()
    {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }
    
    /**
     * 获取屏幕宽度和高度，单位为px
     * 
     * @param context
     * @return
     */
    public Point getScreenMetrics(Context context)
    {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
        
    }
    
    /**
     * 释放摄像头资源
     * 
     */
    private void freeCameraResource()
    {
        if (mCamera != null)
        {
        	try {
				mCamera.setPreviewCallback(null);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mCamera.stopPreview();
	            mCamera.lock();
	            mCamera.release();
	            mCamera = null;
			}
        }
    }
    
    private void createRecordDir()
    {
        File sampleDir;
        
        if (isDirEmpty(videoDir))
        {
            sampleDir = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "im/video/");
        }
        else
        {
            sampleDir = new File(videoDir);
        }
        
        if (!sampleDir.exists())
        {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try
        {
            mVecordFile = File.createTempFile("recording", ".mp4", vecordDir);
            Log.d("MovieRecorderView", mVecordFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            Log.d("MovieRecorderView", e.getMessage());
        }
    }
    
    private boolean isDirEmpty(String s)
    {
        if (s == null)
            return true;
        if ("".equals(s.trim()))
            return true;
        return false;
    }

    public static final int LEFT=1,BOTTOM=2,RIGHT=3,TOP=4;
    private int position = 2;
    public void setPosition(int i){
        position = i;
    }

    /**
     * 初始化
     * 
     * @throws IOException
     */
    @SuppressLint("NewApi")
    private boolean initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null)
            mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOnErrorListener(this);
        if (position==BOTTOM) {
            mMediaRecorder.setOrientationHint(90);
        }
        else if (position==RIGHT) {
            mMediaRecorder.setOrientationHint(180);
        }
        else if (position==TOP) {
            mMediaRecorder.setOrientationHint(270);
        }
        else if (position==LEFT) {
            mMediaRecorder.setOrientationHint(0);
        }

        /*mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(AudioSource.MIC);// 音频源
        mMediaRecorder.setOutputFormat(OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setAudioEncoder(AudioEncoder.AAC);// 音频格式
        mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
        // mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
        mMediaRecorder.setVideoEncodingBitRate(5 * 1280 * 720);// 设置帧频率，然后就清晰了
        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(VideoEncoder.H264);// 视频录制格式VideoEncoder.MPEG_4_SP
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
        mMediaRecorder.prepare();*/
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        // Step 2: Set sources
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//before setOutputFormat()
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//before setOutputFormat()
        
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        
        //设置视频输出的格式和编码
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
//                        mMediaRecorder.setProfile(mProfile);
        mMediaRecorder.setVideoSize(640, 480);//after setVideoSource(),after setOutFormat()
        mMediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 2 * 1024 * 1024)
            mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        else
            mMediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
        mMediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);//after setVideoSource(),after setOutFormat()
        
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//after setOutputFormat()
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//after setOutputFormat()
        
        //mMediaRecorder.setVideoEncodingBitRate(800);
        // Step 4: Set output file
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
        // Step 5: Set the preview output
        
        try {  
            mMediaRecorder.prepare(); 
            mMediaRecorder.start();
        } catch (Exception e) {  
            releaseRecord();
            freeCameraResource();
            return false;  
        }
        return true;
    }
    
    public void setOpenCamera(boolean isOpenCamera) {
        //		this.isOpenCamera = isOpenCamera;
        //		if (isOpenCamera) {
        //			try {
        //				initCamera();
        //			} catch (IOException e) {
        //				// TODO Auto-generated catch block
        //				e.printStackTrace();
        //				Log.d("MovieRecorderView", "setOpenCamera initCamera error : "
        //						+ e.getMessage());
        //			}
        //		} else {
        //			stop();
        //		}
    }
    
    /**
     * 开始录制视频
     * 
     */
    public void record(final OnRecordFinishListener onRecordFinishListener) {
      //录制中
        if (isRecording)
            return;
        
        this.mOnRecordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// 如果未打开摄像头，则打开
                initCamera();
            if(!initRecord()) {
            	Toast.makeText(context, "初始化音频失败", Toast.LENGTH_SHORT).show();
            	return;
            }
            isRecording = true;
            mTimeCount = 0;// 时间计数器重新赋值
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int percent = (int )(((float)mTimeCount / (float)mRecordMaxTime) * 100);
                    roundProgressBar.setProgress(percent);// 设置进度条
                    if (mTimeCount > mRecordMaxTime)
                    {// 达到指定时间，停止拍摄
//                        stop();
//                        if (mOnRecordFinishListener != null)
//                            mOnRecordFinishListener.onRecordFinish(mVecordFile.getAbsolutePath(),mTimeCount);
                        endRec();
                    }
                    mTimeCount++;
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 停止拍摄
     */
    public void stop()
    {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }
    
    public void cancelRec()
    {
        if (!isRecording)
            return;
        isRecording = false;
        stopRecord();
        releaseRecord();
        freeCameraResource();
                isRecording = false;
        if (mVecordFile.exists())
            mVecordFile.delete();
        //        if (recorderListener != null)
        //            recorderListener.recordCancel();
        
        try
        {
            initCamera();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void stopRec()
    {
        if (isRecording)
        {
            isRecording = false;
        }
        stop();
        if (mOnRecordFinishListener != null)
            mOnRecordFinishListener.onRecordFinish(mVecordFile.getAbsolutePath(),mTimeCount);
    }
    
    public void endRec()
    {
        if (!isRecording)
            return;
        isRecording = false;
        stop();
        if (mOnRecordFinishListener != null)
            mOnRecordFinishListener.onRecordFinish(mVecordFile.getAbsolutePath(),mTimeCount);
    }
    
    /**
     * 停止录制
     */
    public void stopRecord()
    {
        roundProgressBar.setProgress(0);
//        mProgressBar.setProgress(0);
        if (mTimer != null)
            mTimer.cancel();
        if (mMediaRecorder != null)
        {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try
            {
                mMediaRecorder.stop();
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 释放资源
     */
    private void releaseRecord()
    {
        if (mMediaRecorder != null)
        {
            mMediaRecorder.setOnErrorListener(null);
            try
            {
                mMediaRecorder.release();
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }
    
    public int getTimeCount()
    {
        return mTimeCount;
    }
    
    /**
     * @return the mVecordFile
     */
    public File getmVecordFile()
    {
        return mVecordFile;
    }
    
    /**
     * 录制完成回调接口
     */
    public interface OnRecordFinishListener
    {
        public void onRecordFinish(String path, int recordeTime);
    }
    
    @Override
    public void onError(MediaRecorder mr, int what, int extra)
    {
        try
        {
            if (mr != null)
                mr.reset();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String getVideoDir()
    {
        return videoDir;
    }
    
    public void setVideoDir(String videoDir)
    {
        this.videoDir = videoDir;
    }
    
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;
 
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
 
        int targetHeight = h;
 
        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
 
        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    private void updateCameraParameters() {
        if (mCamera != null) {
            Parameters p = mCamera.getParameters();

            long time = new Date().getTime();
            p.setGpsTimestamp(time);

            Size pictureSize = findBestPictureSize(p);
            p.setPictureSize(pictureSize.width, pictureSize.height);

            // Set the preview frame aspect ratio according to the picture size.
//            Size size = p.getPictureSize();
//            PreviewFrameLayout frameLayout = (PreviewFrameLayout) findViewById(R.id.frame_layout);
//            frameLayout.setAspectRatio((double) size.width / size.height);

            Size previewSize = findBestPreviewSize(p);
            p.setPreviewSize(previewSize.width,previewSize.height);

            mCamera.setParameters(p);

            int supportPreviewWidth = previewSize.width;
            int supportPreviewHeight = previewSize.height;

            int srcWidth = getScreenWH().widthPixels;
            int srcHeight = getScreenWH().heightPixels;

            int width = Math.min(srcWidth, srcHeight);
            int height = width * supportPreviewWidth / supportPreviewHeight ;

            mSurfaceView.setLayoutParams(new FrameLayout.LayoutParams(height, width));//
        }
    }

    private Size findBestPictureSize(Parameters parameters) {
        int  diff = Integer.MIN_VALUE;
        String pictureSizeValueString = parameters.get("picture-size-values");

        // saw this on Xperia
        if (pictureSizeValueString == null) {
            pictureSizeValueString = parameters.get("picture-size-value");
        }

        if(pictureSizeValueString == null) {
            return  mCamera.new Size(getScreenWH().widthPixels,getScreenWH().heightPixels);
        }

        Log.d("tag", "pictureSizeValueString : " + pictureSizeValueString);
        int bestX = 0;
        int bestY = 0;


        for(String pictureSizeString : pictureSizeValueString.split(COMMA_PATTERN))
        {
            pictureSizeString = pictureSizeString.trim();

            int dimPosition = pictureSizeString.indexOf('x');
            if(dimPosition == -1){
                Log.e(TAG, "Bad pictureSizeString:"+pictureSizeString);
                continue;
            }

            int newX = 0;
            int newY = 0;

            try{
                newX = Integer.parseInt(pictureSizeString.substring(0, dimPosition));
                newY = Integer.parseInt(pictureSizeString.substring(dimPosition+1));
            }catch(NumberFormatException e){
                Log.e(TAG, "Bad pictureSizeString:"+pictureSizeString);
                continue;
            }

            Point screenResolution = new Point (getScreenWH().widthPixels,getScreenWH().heightPixels);

            int newDiff = Math.abs(newX - screenResolution.x)+Math.abs(newY- screenResolution.y);
            if(newDiff == diff)
            {
                bestX = newX;
                bestY = newY;
                break;
            } else if(newDiff > diff){
                if((3 * newX) == (4 * newY)) {
                    bestX = newX;
                    bestY = newY;
                    diff = newDiff;
                }
            }
        }

        if (bestX > 0 && bestY > 0) {
            return mCamera.new Size(bestX, bestY);
        }
        return null;
    }

    public final static String COMMA_PATTERN = ",";
    private Size findBestPreviewSize(Parameters parameters) {

        String previewSizeValueString = null;
        int diff = Integer.MAX_VALUE;
        previewSizeValueString = parameters.get("preview-size-values");

        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }

        if(previewSizeValueString == null) {  // 有些手机例如m9获取不到支持的预览大小   就直接返回屏幕大小
            return  mCamera.new Size(getScreenWH().widthPixels,getScreenWH().heightPixels);
        }
        Log.d("tag", "previewSizeValueString : " + previewSizeValueString);
        int bestX = 0;
        int bestY = 0;

        for(String prewsizeString : previewSizeValueString.split(COMMA_PATTERN))
        {
            prewsizeString = prewsizeString.trim();

            int dimPosition = prewsizeString.indexOf('x');
            if(dimPosition == -1){
                Log.e(TAG, "Bad prewsizeString:"+prewsizeString);
                continue;
            }

            int newX = 0;
            int newY = 0;

            try{
                newX = Integer.parseInt(prewsizeString.substring(0, dimPosition));
                newY = Integer.parseInt(prewsizeString.substring(dimPosition+1));
            }catch(NumberFormatException e){
                Log.e(TAG, "Bad prewsizeString:"+prewsizeString);
                continue;
            }

            Point screenResolution = new Point (getScreenWH().widthPixels,getScreenWH().heightPixels);

            int newDiff = Math.abs(newX - screenResolution.x)+Math.abs(newY- screenResolution.y);

            if(newDiff == diff)
            {
                bestX = newX;
                bestY = newY;
                break;
            } else if(newDiff < diff){
                if((3 * newX) == (4 * newY)) {
                    bestX = newX;
                    bestY = newY;
                    diff = newDiff;
                }
            }
        }
        if (bestX > 0 && bestY > 0) {
            return mCamera.new Size(bestX, bestY);
        }
        return null;
    }

    protected DisplayMetrics getScreenWH() {
        DisplayMetrics dMetrics = new DisplayMetrics();
        dMetrics = this.getResources().getDisplayMetrics();
        return dMetrics;
    }
}
