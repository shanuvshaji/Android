package com.baseutilities.utilities;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * Created by ty12 on 9/6/16.
 */
public class Validator {

    private static Validator INSTANCE = null;

    Context mContext;


    public Validator(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized Validator getInstance(Context mContext) {
        if (INSTANCE == null) {
            INSTANCE = new Validator(mContext);
        }
        return (INSTANCE);
    }

    public boolean isEmailValid(CharSequence input) {
        boolean isValid = true;
        CharSequence inputStr = input;
     /*  // Pattern pattern = Pattern.compile(_Settings.Email_Validation_Regex, Pattern.CASE_INSENSITIVE);
      //  Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }*/
        return isValid;
    }
    public boolean isValidMobileNumber(String userMobileNo, String selectedItem) {

        try {
            if(selectedItem.equalsIgnoreCase("UAE +971"))
            {
                return ((userMobileNo.trim().length()==9) &&(userMobileNo.charAt(0)=='5'));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    public boolean isValidName(String name) {

        return !(name == null || name.isEmpty() || name.trim().isEmpty() || name.equals("null"));

    }

    public boolean isValidMobileNumber(String num) {
        return (num.trim().length() >= 7) && (num.trim().length() <= 12);


    }

    public String getDate(String date, boolean isMonth) {
        String[] dd = TextUtils.split(date, " ");
        return isMonth ? dd[0] : dd[1];
    }

    public boolean validatePasswords(String password, String Confirm_password) {
        return password.equals(Confirm_password);
    }

    public boolean hasSpecialCharacter(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isLetterOrDigit(value.charAt(i)))
                return true;
        }
        return false;
    }

    public boolean notnull(String data) {

        return (!(data == null || data.trim().isEmpty() || data.equals("null") || data.equals("")));
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

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

     public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

     public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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

}
