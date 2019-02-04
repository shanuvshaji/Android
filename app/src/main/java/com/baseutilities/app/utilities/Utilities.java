package com.baseutilities.app.utilities;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;


public class Utilities {

    private static Utilities INSTANCE = null;

    Context mContext;

    public Utilities(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized Utilities getInstance(Context mContext) {
        if (INSTANCE == null) {
            INSTANCE = new Utilities(mContext);
        }
        return (INSTANCE);
    }


    public static void writeLog(String mMessage) {
        Log.i("App Log ", mMessage);
    }


    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Toast.makeText(context, "You seem to be offline. Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    public long convertTimeToMilliSecond(String mTime) {
        long a = 0;
        writeLog("mTime" + mTime);
        try {
            String s = null;
            if (mTime.contains("h:")) {
                String h = mTime.replace("h", "");
                String m = h.replace("m", "");
                // s = m.substring(0, 8);
                s = m.replace("s", "");
            } else if (mTime.contains("h")) {
                String h = mTime.replace("h", ":");
                String m = h.replace("m", ":");
                //  s = m.substring(0, 8);
                s = m.replace("s", "");
            } else
                s = mTime;
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
            parseFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = parseFormat.parse(s);
            a = date.getTime();
            writeLog("a  " + a);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Math.abs(a);
    }


    public String convert24Hour_To_12Hour(String mTimeSlot) {

        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat parseFormat;
            if (mTimeSlot.split(Pattern.quote(":")).length == 3) {
                parseFormat = new SimpleDateFormat("HH:mm:ss");
            } else {
                parseFormat = new SimpleDateFormat("HH:mm");
            }
            Date date = parseFormat.parse(mTimeSlot);
            return (displayFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return mTimeSlot;
    }

    public String getCurrentTime() {
        Calendar mCal = Calendar.getInstance();
        int mMinute = mCal.get(Calendar.MINUTE);
        int mHour = mCal.get(Calendar.HOUR_OF_DAY);
        return mHour + ":" + mMinute;
    }

    public String getCurrentDate() {
        Calendar mCal = Calendar.getInstance();
        Date currentDate = mCal.getTime();

        String date = formatDate(currentDate,false);

        return date;
    }

    public String getFormattedDate(String date) {
        Date mDateObject = null;
        try {
            mDateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String mFormattedDate = new SimpleDateFormat("dd MMM yyyy").format(mDateObject);
        return mFormattedDate;
    }

    public String formatDate(Date date, boolean hasTimeZone) {
        SimpleDateFormat dateFormat;
        if (hasTimeZone) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        }

        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }


    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }



    public String formatData(String mData) {
        String mm = null;
        if (mData.contains("<p>") && mData.contains("</p>")) {
            String a = mData.replace("<p>", "");
            String b = a.replace("</p>", "");
            mm = b;
        } else
            mm = mData;


        if (mm.endsWith("\\r\\n")) {
            String data = mData.substring(0, (mData.length() - 4));
            return data;
        } else {
            return mData;
        }


    }

    public String removeLinks(String data) {
        String value = data.replaceAll("<a(.+?)>", " ").replaceAll("</a>", " ");
        return value;
}

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = Environment.getExternalStorageDirectory() + File.separator + "QK_Profile";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File mOutFile = new File(fileName);
            FileOutputStream fo = new FileOutputStream(mOutFile);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public String getHash(final String msg) {
        byte[] byteData = new byte[0];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(msg.getBytes());
            byteData = digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utilities.writeLog("UUID-256-" + byteData);
        Utilities.writeLog("UUID-" + msg);
        return encodeBase64(byteData);
    }

    public String encodeBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT).replaceAll(Pattern.quote("="), "").replaceAll(Pattern.quote("+"), "-").replaceAll(Pattern.quote("/"), "_");
    }

    public String encryptUsingBase64(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public String decryptUsingBase64(String input) {
        String data = "";
        try {
            byte[] tmp2 = Base64.decode(input, Base64.DEFAULT);
            data = new String(tmp2, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return data;
    }


    public String getCertificateSHA1Fingerprint() {
        PackageManager pm = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return (hexString);
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }


    public String upperCaseAllFirst(String value) {

        char[] array = value.toCharArray();
        // Uppercase first letter.
        array[0] = Character.toUpperCase(array[0]);
        // Uppercase all letters that follow a whitespace character.
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }
        // Result.
        return new String(array);
    }



    public String changeDateFormat(String oldDate) {
        String newDate = null;

        try {
            SimpleDateFormat oldDateformat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newdateFormat = new SimpleDateFormat("MMM dd, yyyy");


            Date date = oldDateformat.parse(oldDate);
            newDate = newdateFormat.format(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return newDate;


    }

    public String getCurrentAppVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        return version;
    }


}