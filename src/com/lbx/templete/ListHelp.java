package com.lbx.templete;

import java.util.List;

public class ListHelp<E> {
	
	public void addArray(List<E> mList, E[] type) {
		
		for(int i=0; i<type.length; i++) {
			mList.add(type[i]);
		}
	}
		
}
