package com.common.helper;

public interface MessageCode {
	
	//�������벻��ͬ
	final int TWICE_PASSWORD_DIFF = 0;
	//�����ʽ����
	final int EMAIL_FORMAT_ERROR = 1;
	//�绰��ʽ����ȷ
	final int PHONE_FORMAT_ERROR = 2;
	//�û�����ע��
	final int USER_NAME_USED = 3;
	//�ֻ��Ѿ���ע��
	final int PHONE_USED = 4;
	//�����Ѿ���ע��
	final int EMAIL_USED = 5;
	//ע��ɹ�
	final int REGISTER_SUCCESS = 6;
	//δ֪����
	final int UNKONW_ERROR = 7;
	
	//��ʾ�Ի���
	final int SHOW_DIALOG = 8;
	
	//��ʾ���¶Ի���
	final int SHOW_UPDATE_DIALOG = 17;
	
	//�����޸ĳɹ�
	final int PASSWORD_CHANGE_SUCCESS = 9;
	//�����޸�ʧ��, ������
	final int PASSWORD_CHANGE_ERROR = 10;
	
	//ɾ������
	final int ADD_NUM = 11;
	//���Ӹ�¦
	final int SUB_NUM = 12;
	//�ش���ordercar
	final int RETURN_ORDER_CAR = 13;
	
	//��ʾ�˵�
	final int DIS_ORDER_ITEM = 14;
	
	//������ͳɹ�
	final int SEND_OPINIONS_SUCCESS = 15;
	//�������ʧ��
	final int SEND_OPINIONS_FAIL = 16;
	
	//��װapk
	final int INSTALL_APK = 18;
}
