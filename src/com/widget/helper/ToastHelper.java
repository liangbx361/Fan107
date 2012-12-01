package com.widget.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {
	
	public static void showToastInBottom(Context mContext, String toast) {
		Toast mToast = Toast.makeText(mContext, toast, 500);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
		mToast.show();
	}
	
	public static void showToastInBottom(Context mContext, String toast, int xOffer, int yOffer) {
		Toast mToast = Toast.makeText(mContext, toast, 500);
		mToast.setGravity(Gravity.BOTTOM, xOffer, yOffer);
		mToast.show();
	}

}
