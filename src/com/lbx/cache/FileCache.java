package com.lbx.cache;

import java.io.File;

import android.content.Context;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
            // 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
            // 没有SD卡就放在系统的缓存目录中
            if (android.os.Environment.getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED))
                    cacheDir = new File(
                                    android.os.Environment.getExternalStorageDirectory(),
                                    "fan107");
            else
                    cacheDir = context.getCacheDir();

            if (!cacheDir.exists())
                    cacheDir.mkdirs();
    }

    public File getFile(String url) {
    		File f = null;
    		if(url != null && !url.equals("")) {
    			String filename = url.substring(url.lastIndexOf("/"), url.length());
            	f = new File(cacheDir, filename);
    		} 
            return f;

    }

    public void clear() {
            File[] files = cacheDir.listFiles();
            if (files == null)
                    return;
            for (File f : files)
                    f.delete();
    }

}