package com.topnews.helper;

public class Constants {
	public static final boolean TRACKING = false;

	public final static String GET = "GET";
	public final static String POST = "POST";
	public static final int TIMEOUT = 30000;
	public static final int TIMEOUT_UPLOAD_FILE = 100000;
	
	public static final int THUMBNAIL_SIZE = 100;
	public static final String TEMP_PHOTO_FILE = "temporary_holder.PNG";
	
	public static final int STATUS_SUCCESS 		= 200;
	public static final int STATUS_BAD_REQUEST			= 400;
	public static final int STATUS_SERVER_ERROR = 500;
	
	public static final int DEFAULT_LIMIT 	= 10;
	public static final int DEFAULT_PAGE 	= 1;
	
	public final static double SING_LAT = 1.352083;
	public final static double SING_LONG = 103.819836;
	
	public static final int IS_ADMIN 	= 1;
	
	public static final String CLIENT_SECRET 	= "cV34pwY76kWvkj45kkjwewrjkezhdg";
	public static final String NPARK 			= "Nparks";
	public static final String LARGE			= "large";
	
	public static final String SNB_CLIENT_SECRET 	= "fanacrexexub5cha";
	public static final String SNB_ACC_TOKEN 		= "f813d4525a41e4ad1e6fdb9dabe41d36";
	
	public static final String SUCCESS 	= "success";
	public static final String FAILED	= "failed";

	public static final String PARK 			= "park";
	public static final String PCN 				= "pcn";
	public static final String PARKNAME 		= "ParkName";
	
	public static final int MAX_YEAR = 70;
	public static final int MAX_POSTAL = 83;
	public static final int BADGE_TEXT_SIZE = 15;
	public static final int LINE_TRESHOLD = 3;
	
	public static final String SNB = "shownearby";
	
	public static final String TITLE = "title";
	public static final String BADGE = "badge";
	public static final String POINT = "point";
	
	public static final int GET_IMAGE_CODE = 23123;
	public static final int GET_CUSTOMER_CODE = 23122;
	public static final int GET_NEW_LAUNCH = 23121;
	
	public static final String FEEDBACK = "feedback";
	
	public static final String PLACE = "place";
	public static final float PREVIEW_SIZE = 250; //in dp
	public static final String TRAIL_NO = "Trail point no.";
	public static final String NO = "No";
	public static final String YES = "Yes";
	public static final int SENSOR_DELAY = 80000;
	
	public static final int FRIEND_STATUS_NOT_FRIEND 		= 0;
	public static final int FRIEND_STATUS_REQUEST_SENT 		= 1;
	public static final int FRIEND_STATUS_REQUEST_RECEIVED	= 2;
	public static final int FRIEND_STATUS_CONFIRMED 		= 3;
	
	public static final String NPARK_TWITTER_SCREEN_NAME = "nparksbuzz";
	
	public static final String PLAY_STORE_LINK = "http://play.google.com/store/apps/details?id=";
	public static final String DISTANCE = "distance";

	public static final String SALES = "sales";

	public static final String AVAILABLE = "Available";

	public static final String CONDO = "Condo";

	public static final String EXCLUSIVE = "Ex";

	public static final String NOT_EXCLUSIVE = "A";

	public static final String list_imge = "list_imge";

	public static final String list_cea = "list_cea";

	public static final String LANDED = "Landed";

	public static final String list_l_imge ="list_l_imge"; 
	
	public static final String list_hdb_imge = "list_hdb_imge";
	
	public static final String list_cm_image = "list_cm_imge";
	public static final String HDB = "HDB";
	
	public static final String COMMERCIAL = "Commercial";

	public static final String ADD = "ADD";
	
	public static final String PHOTOLISTING = "Photo_Listing";

	public static final String EDIT = "EDIT";
	
	public static final int ADD_POSITION = -1;

	public static final String DATE_DISPLAY_FORMAT = "dd-MM-yyyy";
	public static final String DATE_SQL_FORMAT = "yyyy-MM-dd";

	public static final String REPORT_TYPE_BUYER = "1";

	public static final String REPORT_TYPE_SELLER = "2";

	public static final String CUSTOMER_SELECTION = "CUSTOMER_SELECTION";

	public static final String VIEW = "VIEW";

	public static final int REQUEST_CODE_GET_AGENT = 7000;
	public static final int REQUEST_CODE_SORT_GALLERY = 7001;
	
	public enum ImageUploadType
	{
		ImageUploadTypeListingPhoto,
		ImageUploadTypeCEAForm3,
		ImageUploadTypeCEAForm5,
		ImageUploadTypeLOC
	}
}
