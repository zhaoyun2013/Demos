package com.example.twaver;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import java.util.Hashtable;

import twaver.Styles;
import twaver.model.ElementBox;
import twaver.model.Link;
import twaver.model.Node;
import twaver.network.Network;

public class TuopuActivity extends Activity {

    private static final String TAG = TuopuActivity.class.getSimpleName();
    ElementBox box;
    Network network;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        network = new Network(TuopuActivity.this);
        network.setBackgroundColor(Color.BLUE);
        box = network.getElementBox();
        loadData();
        this.setContentView(network);
    }


    public void loadData(){
        long timeStart = System.currentTimeMillis();
        int count = 10;
        int r =300;
        for(int i =0;i<count;i++){
            Node node = new Node();
            node.setName("n"+i);
            node.setImage(R.mipmap.ic_launcher);
            double angle = Math.PI * 2 /count * i;
            int x = r+(int)(r*Math.cos(angle));
            int y = r+(int)(r*Math.sin(angle));
            Log.d(TAG, "setLocation X:"+x+",Y:"+y);
            node.setLocation(x,y);
            box.add(node);
        }

        Hashtable links = new Hashtable();
        for (int i = 0;i<count;i++){
            for(int j = 0;j<count;j++){
                if(i==j){
                    continue;
                }
                String key1 = "link"+i+"_"+j;
                String key2 = "link"+j+"_"+i;
                if(links.containsKey(key1)||links.containsKey(key2)){
                    continue;
                }
                Node from = (Node)box.getByIndex(i);
                Node to = (Node)box.getByIndex(j);
                Link l = new Link(from,to);
                l.setStyle(Styles.LINK_COLOR, 0xFF00FF00);
                box.add(l);
                links.put(key1,l);
            }
        }

        long timeEnd = System.currentTimeMillis();
        System.out.println("Time:"+(timeEnd-timeStart)+"."+"Count:"+box.size()+".");
    }
}
