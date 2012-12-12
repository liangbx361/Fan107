package com.common.helper;

import com.dominicsayers.isemail.IsEMail;
import com.dominicsayers.isemail.IsEMailResult;
import com.dominicsayers.isemail.dns.DNSLookupException;

public class FormatCheck {
	
	/**
	 * ��֤����ĸ�ʽ
	 * @param email
	 * @return
	 * @throws DNSLookupException
	 */
	public static boolean checkEmail(String email){
		if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
			return false;
		} else {
			return true;
		}

//		IsEMailResult result = IsEMail.is_email_verbose(email, true);
//
//		switch (result.getState()) {
//
//		case OK:
//			return true;
//
//		default:
//			return false;
//
//		}
	}
	
	/**
	 * ��֤�ֻ��ĸ�ʽ 
	 * @param photo
	 * @return
	 */
	public static String checkPhoto(String photo) {
		if (null != photo) {
			String reisphoto = photo.replace("��", ",").replace(";", ",")
					.replace("��", ",").replace("��", ",").replace(" ", ",")
					.replace("/", ",").replace("\\", ",");
			String[] photo1 = reisphoto.split(",");
			String[] photo2 = new String[photo1.length];
			boolean isfirst;
			if (null != photo1 && photo1.length > 0) {
				for (int i = 0; i < photo1.length; i++) {
					isfirst = false;
					if (photo1[i]
							.matches("(^[0-9]{3,4}-[0-9]{3,8}$)|^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9])\\d{8}$")) {
						photo2[i] = photo1[i];
						isfirst = true;
					}
					// �ڶ����� ��-��+����λ������ ���ֻ���ǰ���86�����Ҳ����
					if (!isfirst) {
						if (photo1[i]
								.matches("(^[0-9]{3,4}-[0-9]{3,8}-[0-9]{0,100}$)|^((\\+86)|(86))?(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|2|3|5|6|7|8|9])\\d{8}$")) {
							photo2[i] = photo1[i];
						}
					}
				}
				// ��������绰 ֻ��һ��
				if (photo2.length > 0) {
					return photo2[0];
				}
			}
		}
		return null;
	}
}
