package ch.nj.zhaoyun.babyinfo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2;
    private TextView tvName, tvBirthday, tvDistance,tvConstellation;
    private ImageView imageView;
    private Baby baby;
    boolean isRunThread = false;
    SharedPreferences sp;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            loadData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        tvName = (TextView) findViewById(R.id.tvName);
        tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvConstellation = (TextView)findViewById(R.id.tvConstellation);
        imageView = (ImageView)findViewById(R.id.imageView);
        sp = getSharedPreferences("babyinfo",MODE_PRIVATE);

        initData();
        Glide.with(MainActivity.this).load(baby.getPhoto()).error(R.drawable.baby20170419_30).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.edit_name);
        menu.add(R.string.edit_birthday);
        menu.add(R.string.select_photos);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getTitle().equals(getResources().getString(R.string.edit_name))){
            createEditNameDialog();
        }
        else if(item.getTitle().equals(getResources().getString(R.string.edit_birthday))){
            createEditBirthdayDialog();
        }
        else if(item.getTitle().equals(getResources().getString(R.string.select_photos))){
            createSelectPhotosDialog();
        }

        return true;
    }

    private void createSelectPhotosDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.select_photos)
                .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] items = getResources().getStringArray(R.array.select_dialog_items);
                        if(items[which].equals(getResources().getString(R.string.album))){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");//相片类型
                            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                        }
                        else if(items[which].equals(getResources().getString(R.string.camera))){
                            String state = Environment.getExternalStorageState();
                            if (state.equals(Environment.MEDIA_MOUNTED)) {
                                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.sd_error), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).create().show();
    }

    private void createEditBirthdayDialog(){
        int[] times = DateUtils.getYMDHMS(baby.getBirthday());
        final String[] date = new String[1];
        final TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String birthday = date[0]+" "+hourOfDay+":"+minute+":00";
                baby.setBirthday(birthday);
                sp.edit().putString("birthday",birthday).commit();
            }
        },times[3],times[4],true);
        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date[0] = year+"-"+(month+1)+"-"+dayOfMonth;
                if(!timePickerDialog.isShowing()){
                    timePickerDialog.show();
                }

            }
        }, times[0], times[1]-1, times[2]).show();
    }

    private void createEditNameDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
        final EditText username_edit = (EditText)textEntryView.findViewById(R.id.username_edit);
        username_edit.setText(baby.getName());
        username_edit.setSelection(baby.getName().length());//将光标移至文字末尾
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.edit_name)
                .setView(textEntryView)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = username_edit.getText().toString();
                        if(!TextUtils.isEmpty(name)){
                            baby.setName(name);
                            sp.edit().putString("name",name).commit();
                        }
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startThread();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunThread =  false;
    }

    private void startThread() {
        isRunThread =  true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (isRunThread) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                    handler.sendEmptyMessage(i);
                }
            }
        }).start();
    }

    private void initData() {
        String name = sp.getString("name","name");
        String birthday = sp.getString("birthday","2017-04-12 17:47:00");
        String photo = sp.getString("photo","");
        baby = new Baby(name,birthday,photo);
        loadData();
    }

    private void loadData() {
        tvName.setText(getResources().getString(R.string.text_title_name)+baby.getName());
        tvBirthday.setText(getResources().getString(R.string.text_title_birthday)+baby.getBirthday().substring(0,baby.getBirthday().length()-3));
        tvConstellation.setText(getResources().getString(R.string.text_title_constellation)+baby.getConstellation(MainActivity.this));

        String distance = getResources().getString(R.string.text_title_distance_times);
        distance = String.format(distance, getDistanceTimes(baby));
        tvDistance.setText(distance);

    }

    private String getDistanceTimes(Baby baby) {
        String birthday = baby.getBirthday();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDay = sdf.format(d);
        String distance =DateUtils.getDistanceTime(birthday, nowDay,MainActivity.this);
        return distance;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                Uri uri = data.getData();
                if(uri !=null) {
                    try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        saveImageToGallery(MainActivity.this,photo);
                        Glide.with(MainActivity.this).load(baby.getPhoto()).error(R.mipmap.ic_launcher).into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
                Uri uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data");
                        saveImageToGallery(MainActivity.this,photo);
                        Glide.with(MainActivity.this).load(baby.getPhoto()).error(R.mipmap.ic_launcher).into(imageView);
                    } else {
                        Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                  try {
                        Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        saveImageToGallery(MainActivity.this,photo);
                        Glide.with(MainActivity.this).load(baby.getPhoto()).error(R.mipmap.ic_launcher).into(imageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();//注意小米手机必须这样获得public绝对路径
        String fileName = "BabyInfo";
        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        fileName = System.currentTimeMillis()+".jpg";
        File currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            sp.edit().putString("photo",currentFile.getAbsolutePath()).commit();
            baby.setPhoto(currentFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }
}
