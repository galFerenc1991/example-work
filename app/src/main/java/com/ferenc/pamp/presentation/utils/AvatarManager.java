package com.ferenc.pamp.presentation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.soundcloud.android.crop.Crop;

import org.androidannotations.annotations.EBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by
 * Ferenc on 2017.12.14..
 */

@EBean
public class AvatarManager {

    private Context mCtx;
    private Fragment mFragmentSupport;
    private android.app.Fragment mFragment;
    private Activity mActivity;

    private static String CHOOSER_TITLE = "Take or select a photo";
    private static final String MIME_TYPE_IMAGE = "image/*";
    private static final String TEMP_IMAGE_AVATAR_NAME = "temp_avatar.jpg";
    private static int mRotationAttribute = ExifInterface.ORIENTATION_NORMAL;
    private static int IMAGE_SIDE = 256;  //px

    private File mTakePhotoTempFile;

    public AvatarManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public void attach(Fragment fragment) {
        mFragmentSupport = fragment;
    }

    public void attach(android.app.Fragment fragment) {
        mFragment = fragment;
    }

    public void attach(Activity activity) {
        mActivity = activity;
    }

    public File getCroppedFile() {
        mTakePhotoTempFile = new File(mCtx.getExternalCacheDir(), TEMP_IMAGE_AVATAR_NAME);
        Bitmap b = rotateBitmapOrientation(mTakePhotoTempFile.getPath());
        savePhoto(b);
        return mTakePhotoTempFile;
    }

    /**
     * Build intent to perform user crop image
     *
     * @param picUri - image to be cropped
     */
    private void cropCapturedImage(int requestCode, Uri picUri) {
        startCropActivity(picUri, requestCode);
    }

    /**
     * Init system picker to pick image from camera or gallery
     */
    public void getImage(int requestCode) {
        Intent pickIntent = new Intent();
        pickIntent.setType(MIME_TYPE_IMAGE);
        pickIntent.setAction(Intent.ACTION_PICK);

        mTakePhotoTempFile = new File(mCtx.getExternalCacheDir(), TEMP_IMAGE_AVATAR_NAME);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTakePhotoTempFile));

        pickIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTakePhotoTempFile));

        Intent chooserIntent = Intent.createChooser(pickIntent, CHOOSER_TITLE);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});

        startActivityForResult(chooserIntent, requestCode);
    }

    /**
     * Handle picked full size image and crop it to square
     *
     * @param resultCode
     * @param data
     */
    public void handleFullsizeImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                readExifAngleFromTempFile(data.getData());
                cropCapturedImage(requestCode, data.getData());
            } else {
                if (mTakePhotoTempFile == null) mTakePhotoTempFile = new File(mCtx.getExternalCacheDir(), TEMP_IMAGE_AVATAR_NAME);
                readExifAngleFromTempFile(Uri.parse(mTakePhotoTempFile.getPath()));
                cropCapturedImage(requestCode, Uri.fromFile(mTakePhotoTempFile));
            }
        }
    }

    private void startActivityForResult(Intent itent, int requestCode) {
        if (mActivity != null)
            mActivity.startActivityForResult(itent, requestCode);
        else if (mFragment != null)
            mFragment.startActivityForResult(itent, requestCode);
        else if (mFragmentSupport != null)
            mFragmentSupport.startActivityForResult(itent, requestCode);
    }

    private void startCropActivity(Uri picUri, int requestCode) {
        if (mActivity != null)
            Crop.of(picUri, Uri.fromFile(mTakePhotoTempFile)).asSquare().withMaxSize(IMAGE_SIDE, IMAGE_SIDE).start(mActivity, requestCode);
        else if (mFragment != null)
            Crop.of(picUri, Uri.fromFile(mTakePhotoTempFile)).asSquare().withMaxSize(IMAGE_SIDE, IMAGE_SIDE).start(mFragment.getActivity(), mFragment, requestCode);
        else if (mFragmentSupport != null)
            Crop.of(picUri, Uri.fromFile(mTakePhotoTempFile)).asSquare().withMaxSize(IMAGE_SIDE, IMAGE_SIDE).start(mFragmentSupport.getActivity(), mFragmentSupport, requestCode);
    }

    /*Read exif attribute 1-8 or orientation and save in to mRotation*/
    private void readExifAngleFromTempFile(final Uri uriPath) {
        mRotationAttribute = 0;
        if (uriPath != null) {
            boolean hasRotation = false;
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            try {
                Cursor cursor = mCtx.getContentResolver().query(uriPath, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    int rotateAngle = cursor.getInt(0);
                    mRotationAttribute = rotateAngle == 90 ?
                            ExifInterface.ORIENTATION_ROTATE_90 :
                            (rotateAngle == 270 ? ExifInterface.ORIENTATION_ROTATE_270 : ExifInterface.ORIENTATION_NORMAL);
                    hasRotation = true;
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!hasRotation) {
                // Read EXIF Data
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(uriPath.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (exif != null) {
                    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
                    mRotationAttribute = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
                }
            }
        }
    }

    /*Return proper degree for rotation based on mRotation attr*/
    private int rotateForBitmapWithExif() {
        if (mRotationAttribute == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (mRotationAttribute == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (mRotationAttribute == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /*Rotate bitmap*/
    private Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateForBitmapWithExif(), (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
    }

    /*Save bitmap to file*/
    private void savePhoto(Bitmap b) {
        FileOutputStream fos = null;
        try {
            if (mTakePhotoTempFile == null)
                mTakePhotoTempFile = new File(mCtx.getExternalCacheDir(), TEMP_IMAGE_AVATAR_NAME);
            fos = new FileOutputStream(mTakePhotoTempFile.getPath());
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void setChooserTitle(String title) {
        CHOOSER_TITLE = title;
    }

    public void setCroppedSquareSize(int px) {
        IMAGE_SIDE = px;
    }

}
