package common.file.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private String SDCardroot;

	public String getSDPATH() {
		return SDCardroot;
	}
	public FileUtils() {
		//得到当前外部存储设备的目录
		// /SDCARD
		SDCardroot = Environment.getExternalStorageDirectory() + "/";
	}
	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatFileInSDCard(String fileName, String dir) throws IOException {
		File file = new File(SDCardroot + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dir) {
		File dirFile = new File(SDCardroot + dir + File.separator);
		System.out.println(dirFile.mkdir());
		return dirFile;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName, String path){
		File file = new File(SDCardroot + path + File.separator + fileName);
		return file.exists();
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName, InputStream input){
		int temp=0;
		File file = null;
		OutputStream output = null;
		try{
			creatSDDir(path);
			file = creatFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			while((temp = input.read(buffer)) != -1){
				output.write(buffer, 0, temp);
			}
			output.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * 将输入流写入SD卡文件中
	 * @param file
	 * @param input
	 * @return
	 */
	public static File write2SDFromInput(File file, InputStream input){
		int temp=0;
		OutputStream output = null;
		File tempFile = null;
		try{
			tempFile = new File(file.getParent(), file.getName() + ".temp");
			tempFile.createNewFile();
			output = new FileOutputStream(tempFile);
			byte buffer [] = new byte[1024];
			while((temp = input.read(buffer)) != -1){
				output.write(buffer, 0, temp);
			}
			output.flush();
			
			//下载成功重命名为原来的文件后缀
			File newFile = new File(tempFile.getParent(), replaceExtensions(tempFile.getName(), ""));
			if(!tempFile.renameTo(newFile)) {
				tempFile.delete();
				tempFile = null;
			}
		
		} catch(IOException e){
			e.printStackTrace();
			if(tempFile != null) {
				tempFile.delete();
				tempFile = null;
			}
			
		} finally{
			try{
				output.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		
		return tempFile;
	}
	
	/**
	 * 替换文件的后缀名 (例如: 输入 a.mp3 .mp4, 返回 a.mp4)
	 * @param filename
	 * @param replaceExtensions
	 * @return
	 */
	public static String replaceExtensions(String filename, String replaceExtensions ) {
		return filename.substring(0, filename.lastIndexOf(".")) + replaceExtensions;
	}
	
	/**
	 * 获得文件的后缀名 (例如: 输入 a.mp3, 返回 .mp3)
	 * 
	 * @param name	
	 * @return 
	 */
	public static String getFileExtensions(String filename) {
		return filename.substring(filename.lastIndexOf("."), filename.length());
	}
}