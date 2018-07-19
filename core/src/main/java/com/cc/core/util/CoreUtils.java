package com.cc.core.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CoreUtils {

    public static void jumpToPhotoAlbum(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, requestCode);
    }

    public static Uri jumpToCamera(Fragment f, int requestCode) {

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + "");

        //使用内容提供者，定义照片保存的Uri
        FragmentActivity activity = f.getActivity();
        if (activity == null) {
            throw new NullPointerException();
        }
        Uri photoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        f.startActivityForResult(intent, requestCode);
        return photoUri;
    }

    public static File getFileFromContentUri(Context context, Uri uri) {

        File file = null;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            file = new File(cursor.getString(columnIndex));
            cursor.close();
        }
        return file;
    }

    public static String getErrorMsg(Resources resources, String resName, String packageName) {
        int resId = resources.getIdentifier(resName, "string", packageName);
        return resId != 0 ? resources.getString(resId) : null;
    }

    public static int hashCode(Object a[]) {
        if (a == null) {
            return 0;
        }

        int result = 1;

        for (Object element : a) {
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }

    public static String calcMD5(String originalData) {
        if (TextUtils.isEmpty(originalData)) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(originalData.getBytes());
            byte[] digest = md5.digest();

            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            int length = digest.length;
            char str[] = new char[length * 2];
            int k = 0;
            for (byte digestByte : digest) {
                str[k++] = hexDigits[digestByte >>> 4 & 0xf];
                str[k++] = hexDigits[digestByte & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }


    public static void hideSoftKeyboard(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, 0);
            }
        }
    }
}
