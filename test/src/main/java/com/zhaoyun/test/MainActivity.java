package com.zhaoyun.test;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.zhaoyun.test.widget.GooView;
import com.zhaoyun.test.widget.PianoView;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toast =  Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
        setContentView(new PianoView(this));

        MyTask task1 = new MyTask();
        task1.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,1);
        MyTask task2 = new MyTask();
        task2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,1);

    }

    public class MyTask extends AsyncTask<Integer,Integer,Integer>{

        private int id = 0;
        @Override
        protected Integer doInBackground(Integer... params) {
            id = params[0];
            int i = 0;
            while (i< 100){
                SystemClock.sleep(200);
                i++;
                if(i%5==0){
                    publishProgress(i);
                }
            }
            return i;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: id:"+id+","+values[0]+"%");
            toast.setText(values[0]+"%...");
            toast.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d(TAG, "onPostExecute: id:"+id+","+integer+"%...");
            toast.setText(integer+"%");
            toast.show();
        }
    }

    public class MyTask2 extends AsyncTask<Integer,Integer,Integer>{

        private int id = 0;
        @Override
        protected Integer doInBackground(Integer... params) {
            id = params[0];
            int i = 0;
            while (i< 100){
                SystemClock.sleep(200);
                i++;
                if(i%5==0){
                    publishProgress(i);
                }
            }
            return i;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: id:"+id+","+values[0]+"%");
            toast.setText(values[0]+"%...");
            toast.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d(TAG, "onPostExecute: id:"+id+","+integer+"%...");
            toast.setText(integer+"%");
            toast.show();
        }
    }
}
