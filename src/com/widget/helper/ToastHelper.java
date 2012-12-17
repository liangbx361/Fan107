package com.widget.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {
	
	public static final int BUTTOM_OFFER = 80;
	
	public static void showToastInBottom(Context context, String toast) {
		Toast mToast = Toast.makeText(context, toast, 500);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
		mToast.show();
	}
	
	public static void showToastInBottom(Context context, String toast, int xOffer, int yOffer) {
		Toast mToast = Toast.makeText(context, toast, 500);
		mToast.setGravity(Gravity.BOTTOM, xOffer, yOffer);
		mToast.show();
	}
	
	public static void showToastInBottom(Context context, String toast, int xOffer) {
		Toast mToast = Toast.makeText(context, toast, 500);
		mToast.setGravity(Gravity.BOTTOM, xOffer, BUTTOM_OFFER);
		mToast.show();
	}
}
