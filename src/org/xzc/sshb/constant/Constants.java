package org.xzc.sshb.constant;

import java.util.HashMap;
import java.util.Map;

//TODO 需要整理一下
public class Constants {
	public static final String SESSION_LOGINED_USER_KEY = "SESSION_LOGINED_USER_KEY";
	public static final String SESSION_CART_KEY = "SESSION_CART_KEY";

	public static final String EXCEPTION_ACTION_TO_EXCEPTION_UI = "toExceptionUI";
	public static final String EXCEPTION_ACTION_EXCEPTION_UI = "exceptionUI";

	public static final String USER_ACTION_TO_LIST_UI = "toListUI";
	public static final String USER_ACTION_TO_LOGIN_UI = "toLoginUI";
	public static final String USER_ACTION_ADD_UI = "addUI";
	public static final String USER_ACTION_UPDATE_UI = "updateUI";
	public static final String USER_ACTION_LOGIN_UI = "loginUI";
	public static final String USER_ACTION_REGISTER_UI = "registerUI";
	public static final String USER_ACTION_LIST_UI = "listUI";
	public static final String USER_ACTION_INPUT = "input";

	public static final String PREVENT_LOGIN = "preventLogin";
	public static final String SHOULD_LOGIN = "shouldLogin";

	public static final String TO_NAV_INDEX_UI = "toNavIndexUI";
	public static final String NAV_INDEX_UI = "navIndexUI";

	public static final class ItemAction {
		public static final String LIST_UI = "listUI";

		public static final String ADD_UI = "addUI";
		public static final String UPDATE_UI = "updateUI";
		public static final String TO_LIST_UI = "toListUI";

		public static final String LIST_RECORD_UI = "listRecordUI";;

	}

	public static final String IS_NOT_ADMIN = "isNotAdmin";
	public static final String WRONG_PASSWORD = "wrongPassword";
	public static final String ITEM_VIEW_DETAIL_UI = "viewDetailUI";
	public static final String ITEM_VIEW_DETAIL_DESCRIPTION = "viewDetailDescription";
	public static final String ITEM_VIEW_DETAIL_EVALUATION = "viewDetailEvaluation";
	public static final String ITEM_VIEW_DETAIL_SALES_VOLUME = "viewDetailSalesVolume";
	public static final String RECORD_VIEW_DETAIL_RECORD_UI = "viewDetailRecordUI";
	public static final String USER_VIEW_USER_UI = "viewUserUI";
	public static final String PREFERENCE = "xzc.preference";

	public static final class State {
		public static final int USER_NORMAL = 100;
		public static final int USER_DELETED = 101;
		public static final int USER_ADMIN = 107;

		public static final int ITEM_NORMAL = 200;
		public static final int ITEM_DELETED = 201;

		public static final int RECORD_PAID = 300;
		public static final int RECORD_SENT = 301;
		public static final int RECORD_RECEIVED = 302;
		public static final int RECORD_FINISHED = 303;
		public static final int RECORD_CANCELED = 304;
		public static final int RECORD_DELETED = 399;

		public static final int RECORDITEM_NOT_RECEIVED = 400;
		public static final int RECORDITEM_RECEIVED = 401;
		public static final int RECORDITEM_EVALUATED = 402;
		public static final int RECORDITEM_CANCELED = 403;

		public static final Map<String, Integer> DESCRIPTION = new HashMap<String, Integer>();
		static {
			DESCRIPTION.put( "USER_NORMAL", 100 );
			DESCRIPTION.put( "USER_DELETED", 101 );
			DESCRIPTION.put( "USER_ADMIN", 107 );

			DESCRIPTION.put( "ITEM_NORMAL", 200 );
			DESCRIPTION.put( "ITEM_DELETED", 201 );

			DESCRIPTION.put( "RECORD_PAID", 300 );
			DESCRIPTION.put( "RECORD_SENT", 301 );
			DESCRIPTION.put( "RECORD_RECEIVED", 302 );
			DESCRIPTION.put( "RECORD_FINISHED", 303 );
			DESCRIPTION.put( "RECORD_CANCELED", 304 );
			DESCRIPTION.put( "RECORD_DELETED", 399 );

			DESCRIPTION.put( "RECORDITEM_NOT_RECEIVED", 400 );
			DESCRIPTION.put( "RECORDITEM_RECEIVED", 401 );
			DESCRIPTION.put( "RECORDITEM_EVALUATED", 402 );
			DESCRIPTION.put( "RECORDITEM_CANCELED", 403 );
		}
	}
}
