package com.topnews.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Helper {
	private static final String MIN_AGO = "min ago";
	private static final String MINS_AGO = "mins ago";
	private static final String HOUR_AGO = "hour ago";
	private static final String HOURS_AGO = "hours ago";
	private static final String DAY_AGO = "day ago";
	private static final String DAYS_AGO = "days ago";


	private static final String MIN_LEFT = "min left.";
	private static final String MINS_LEFT = "mins left.";
	private static final String HOUR_LEFT = "hour left.";
	private static final String HOURS_LEFT = "hours left.";
	private static final String DAY_TO_GO = "day to go.";
	private static final String DAYS_TO_GO = "days to go.";

	private static Point mScreenSize;

	public static String getAndroidId(Context ctx) {
		return "01-01-" + getId(ctx);
	}

	/**
	 * Use calcTimeDiff(Date, Context)
	 *
	 * @param target
	 * @return
	 */
	public static String calcTimeDiff(Date target) {
		if (target == null) { return null; }
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		calendar1.setTime(target);

		long milsecs1 = calendar1.getTimeInMillis();
		long milsecs2 = calendar2.getTimeInMillis();

		long diff = milsecs2 - milsecs1;
		long dminutes = diff / (60 * 1000);
		long dhours = diff / (60 * 60 * 1000);
		long ddays = diff / (24 * 60 * 60 * 1000);

		String toReturn = "";
		if (diff >= 0) {
			// In the past
			if (dminutes < 60) {
				toReturn = String.valueOf(dminutes);
				if (dminutes == 1) {
					toReturn += " " + MIN_AGO;
				}
				else {
					toReturn += " " + MINS_AGO;
				}
			}
			else if (dhours < 24) {
				toReturn = String.valueOf(dhours);
				if (dhours == 1) {
					toReturn += " " + HOUR_AGO;
				}
				else {
					toReturn += " " + HOURS_AGO;
				}
			}
			else if (ddays <= 7) {
				toReturn = String.valueOf(ddays);
				if (ddays == 1) {
					toReturn += " " + DAY_AGO;
				}
				else {
					toReturn += " " + DAYS_AGO;
				}
			}
			else {
				toReturn = formatDate(target, "dd MMM yyy");
			}
		}
		else {
			// In the future
			diff *= -1;
			dminutes *= -1;
			dhours *= -1;
			ddays *= -1;
			if (dminutes < 60) {
				toReturn = String.valueOf(dminutes);
				if (dminutes == 1) {
					toReturn += " " + MIN_LEFT;
				}
				else {
					toReturn += " " + MINS_LEFT;
				}
			}
			else if (dhours < 24) {
				toReturn = String.valueOf(dhours);
				if (dhours == 1) {
					toReturn += " " + HOUR_LEFT;
				}
				else {
					toReturn += " " + HOURS_LEFT;
				}
			}
			else if (ddays <= 7) {
				toReturn = String.valueOf(ddays);
				if (ddays == 1) {
					toReturn += " " + DAY_TO_GO;
				}
				else {
					toReturn += " " + DAYS_TO_GO;
				}
			}
			else {
				toReturn = formatDate(target, "dd MMM yyy");
			}
		}
		return toReturn;
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat postFormater = new SimpleDateFormat(format, Locale.ENGLISH);

		String newDateStr = postFormater.format(date);
		return newDateStr;
	}

	public static int getCacheSize() {
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/6th of the available memory for this memory cache.
		int cacheSize = maxMemory / 6;

		return cacheSize;
	}

	private static String getId(Context context) {
		return Secure
				.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public static void disable(View view) {
		if (view != null) {
			view.setEnabled(false);
		}
	}

	public static boolean isNumeric(String number) {
		try {
			double b = Double.parseDouble(number);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static String removeDot(String number) {
		if (number.contains(",")) {
			number = number.replace(",", "");
		}

		return number;
	}

	public static boolean isEmpty(String string) {
		if (string == null || string.trim().length() == 0) {
			return true;
		}

		return false;
	}

	public static String getDisplayDate(String date) {
		return formateDate(parseDate(date, Constants.DATE_SQL_FORMAT),
				Constants.DATE_DISPLAY_FORMAT);
	}

	public static String getSQLDate(String date) {
		return formateDate(parseDate(date, Constants.DATE_DISPLAY_FORMAT),
				Constants.DATE_SQL_FORMAT);
	}

	public static String getAvailStr(String string) {
		if (string == null) {
			return "";
		}
		
		if(string.trim().length() == 0){
			return "";
		}

		if (string == "null" || string.equals("null"))
			return "";

		return string;
	}

	public static void goneView(View v) {
		if (v != null) {
			v.setVisibility(View.GONE);
		}

	}

	public static String LevelCorrect(String level) {

		if (level.startsWith("B")) {
			level = level.replace("B", "-");
		} else if (level.startsWith("-")) {
			level = level.replace("-", "B");
		}
		return level;
	}

	public static String getPlainNumber(EditText ed) {
		if (ed == null)
			return "0";
		if (ed.getText().toString().trim().length() == 0) {
			return "0";
		}
		return ed.getText().toString().replace(",", "");
	}

	public static void visibleView(View v) {
		if (v != null) {
			v.setVisibility(View.VISIBLE);
		}
	}

	public static void invisibleView(View v) {
		if (v != null) {
			v.setVisibility(View.INVISIBLE);
		}
	}

	public static AlertDialog showAlert(Context context, String title,
			String message) {
		return new Builder(context)
				.setMessage(message)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	public static Builder buildAlert(Context context, String title,
			String message) {
		Builder dlg = new Builder(context).setMessage(message)
				.setTitle(title).setCancelable(true);
		return dlg;
	}



	public static Bitmap getFullBitmap(Uri uri, Context activity)
			throws FileNotFoundException, IOException {
		InputStream input = activity.getContentResolver().openInputStream(uri);

		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1)
				|| (onlyBoundsOptions.outHeight == -1))
			return null;

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = 1;
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = activity.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	public static String parseSQLDateString(String sqlDate, String format) {
		return formateDate(parseDate(sqlDate, format), "yyyy-MM-dd");
	}

	public static Date parseSQLDate(String sqlDate) {
		// 2012-12-24 02:01:57
		// SimpleDateFormat curFormater = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		return parseDate(sqlDate, "yyyy-MM-dd HH:mm:ss");

	}

	public static Date parseDate(String sqlDate, String format) {
		// 2012-12-24 02:01:57
		SimpleDateFormat curFormater = new SimpleDateFormat(format,
				Locale.ENGLISH);
		Date dateObj = null;
		try {
			dateObj = curFormater.parse(sqlDate);
			return dateObj;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371000; // in meter
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);
		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

	public static String getFormattedDistance(double lat1, double lng1,
			double lat2, double lng2) {
		return Helper.formatDistance(Helper.distFrom(lat1, lng1, lat2, lng2));
	}

	public static String getFormattedDistance(String lat1, String lng1,
			double lat2, double lng2) {
		try {
			double lat = Double.parseDouble(lat1);
			double lon = Double.parseDouble(lng1);

			return Helper.formatDistance(Helper.distFrom(lat, lon, lat2, lng2));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return "";
		}

	}

	public static String convertToSQLDate(Date startDate) {

		return Helper.formateDate(startDate, "yyyy-MM-dd HH:mm:ss");
	}

	public static ArrayList<String> getYOB(String hintYOB, int MAX_YEAR) {
		ArrayList<String> arrYOB = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		arrYOB.add(hintYOB);
		int years = c.get(Calendar.YEAR);

		for (int i = 0; i < MAX_YEAR; i++) {
			arrYOB.add(String.valueOf((years - i)));
		}
		return arrYOB;
	}

	public static ArrayList<String> getPostal(String hintPostal, int MAX_POSTAL) {
		ArrayList<String> arrPostal = new ArrayList<String>();
		arrPostal.add(hintPostal);

		for (int i = 1; i < MAX_POSTAL; i++) {
			if (i < 10) {
				arrPostal.add("0" + i);
			} else {
				arrPostal.add(String.valueOf(i));
			}
		}

		return arrPostal;
	}

	public static Date formatYOB(String yobDate) {
		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy",
				Locale.ENGLISH);
		Date dateObj = null;
		try {
			dateObj = curFormater.parse(yobDate);
			return dateObj;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String formateDate(Date date, String format) {
		SimpleDateFormat postFormater = new SimpleDateFormat(format,
				Locale.ENGLISH);

		String newDateStr = postFormater.format(date);
		return newDateStr;
	}

	public static String formatDistance(double d) {
		DecimalFormat formatter = new DecimalFormat("#,### 'm'");
		d = Math.round(d);
		if (d > 1000) {
			formatter = new DecimalFormat("#,###.# 'km'");
			d = d / 1000;
		}

		return formatter.format(d);
	}

	public static void sendSMS(Activity activity, String smsBody,
			String phoneNumber) {

		try {

			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			if (phoneNumber != null && phoneNumber.trim().length() > 0) {
				// sendIntent.setData(Uri.fromParts("sms", phoneNumber, null));
				sendIntent.putExtra("address", phoneNumber);
			}
			if (smsBody != null) {
				sendIntent.putExtra("sms_body", smsBody);
			}
			sendIntent.setType("vnd.android-dir/mms-sms");

			activity.startActivity(sendIntent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(activity, "Unable to send SMS", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public static String escapeURL(String term) {
		term = term.replace(" ", "%20");
		return term;
	}
}
