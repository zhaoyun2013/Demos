package com.zhaoyun.fangqq.swipe;

import com.zhaoyun.fangqq.swipe.SwipeLayout.Status;

public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
