/**
 * 用于检测网络是否连接
 */
package common.connection.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetWorkCheck {
	public static final String TAG = "NetWorkCheck";

	public static boolean checkNetWorkStatus(Context context) {
		boolean result;
		
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
			Log.i(TAG, "The net was connected");
		} else {
			result = false;
			Log.i(TAG, "The net was bad!");
		}
		
		return result;
	}
}
