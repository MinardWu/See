package com.minardwu.see.util;

import android.hardware.Camera.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtil {

	private static CameraUtil myCamPara = null;
	private CameraSizeComparator sizeComparator = new CameraSizeComparator();

	private CameraUtil(){}

	public static CameraUtil getInstance(){
		if(myCamPara == null){
			myCamPara = new CameraUtil();
			return myCamPara;
		}else{
			return myCamPara;
		}
	}

	public Size getPropPreviewSize(List<Size> list, float th, int minWidth){
		Collections.sort(list, sizeComparator);
		int i = 0;
		for(Size s:list){
			if((s.width >= minWidth) && equalRate(s, th))
				break;
			i++;
		}
		if(i == list.size())
			i = 0;
		return list.get(i);
	}

	public Size getPropPictureSize(List<Size> list, float th, int minWidth){
		Collections.sort(list, sizeComparator);
		int i = 0;
		for(Size s:list){
			if((s.width >= minWidth) && equalRate(s, th))
				break;
			i++;
		}
		if(i == list.size())
			i = 0;
		return list.get(i);
	}

	public boolean equalRate(Size s, float rate){
		float r = (float)(s.width)/(float)(s.height);
		if(Math.abs(r - rate) <= 0.03) {
			return true;
		} else{
			return false;
		}
	}

	public  class CameraSizeComparator implements Comparator<Size>{
		public int compare(Size lhs, Size rhs) {
			if(lhs.width == rhs.width){
				return 0;
			} else if(lhs.width > rhs.width){
				return 1;
			} else{
				return -1;
			}
		}
	}
}
