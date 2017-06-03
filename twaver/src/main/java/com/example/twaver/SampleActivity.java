package com.example.twaver;

import android.app.Activity;
import android.os.Bundle;
import twaver.Styles;
import twaver.alarm.Alarm;
import twaver.alarm.AlarmSeverity;
import twaver.model.ElementBox;
import twaver.model.Link;
import twaver.model.Node;
import twaver.network.Network;

public class SampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Network network = new Network(this);
        ElementBox box = network.getElementBox();

        Node node = new Node();
//        node.setImage(arg0);
        node.setName("Hello");
        node.setStyle(Styles.LABEL_OUTLINE_COLOR, 1);
        node.setLocation(50, 100);
        box.add(node);
        Node node2 = new Node();
        node2.setName("TWaver");
        node2.setLocation(250, 200);
        box.add(node2);
        Link link = new Link(node, node2);
        link.setName("Hello TWaver");
        box.add(link);

        box.getAlarmBox().add(new Alarm(node.getId(), AlarmSeverity.CRITICAL));
        this.setContentView(network);
    }
}
