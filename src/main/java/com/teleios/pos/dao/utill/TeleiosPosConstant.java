package com.teleios.pos.dao.utill;

import java.io.Serializable;

public class TeleiosPosConstant implements Serializable {
	private static final long serialVersionUID = -4572631025145178717L;

	public static final String EMAIL_VERIFICATION = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";

	public static final String SMS_API_KEY = "Apikey eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI3ZjA4Zjk3MC0xNDE3LTExZWItYjkzNy1lYjFiMzU0ZDViNDMiLCJzdWIiOiJTSE9VVE9VVF9BUElfVVNFUiIsImlhdCI6MTYwMzMzNzY3MSwiZXhwIjoxOTE4ODcwNDcxLCJzY29wZXMiOnsiYWN0aXZpdGllcyI6WyJyZWFkIiwid3JpdGUiXSwibWVzc2FnZXMiOlsicmVhZCIsIndyaXRlIl0sImNvbnRhY3RzIjpbInJlYWQiLCJ3cml0ZSJdfSwic29fdXNlcl9pZCI6IjcwMzMiLCJzb191c2VyX3JvbGUiOiJ1c2VyIiwic29fcHJvZmlsZSI6ImFsbCIsInNvX3VzZXJfbmFtZSI6IiIsInNvX2FwaWtleSI6Im5vbmUifQ.6_pXO6DLgsqWx_3-C9tDy6K37JGoROJHwXDi1mbOZiY";
	public static final String SMS_BASE_URL = "https://api.getshoutout.com/coreservice/messages";

	// Define Payment Type Constant
	public static final short CHEQUE = 1;
	public static final short CREDICT = 2;
	public static final short CASH = 3;
	public static final short CASHANDCHEQUE = 4;
	public static final short CASHANDCREDI = 5;

	// Define Cheque State
	public static final short CHEQUE_RETURN = 6;
	public static final short CHEQUE_CLEAR = 7;
	public static final short CHEQUE_PENDING = 8;

	// Define Cheque Type
	public static final short CASH_CHEQUE = 1;
	public static final short DATE_CHEQUE = 2;

}
