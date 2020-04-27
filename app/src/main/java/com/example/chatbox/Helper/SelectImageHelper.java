package com.example.chatbox.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class SelectImageHelper {
    private Uri mImageCaptureUri, URI_FOR_SELECTED_IMAGE = null;
    private static final int REQUEST_PERMISSION_REQ_CODE = 34;
    private final int CAMERA_CODE = 101;
    private boolean isImageSelected = false;
    private ImageView imageView;
    private Activity context;

    public SelectImageHelper(Activity context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    public void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        checkPermission(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Choose from Gallery")) {

                    Crop.pickImage(context);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(context.getCacheDir(), "cropped"));
        //Crop.of(source, destination).asSquare().start(this);
        Crop.of(source, destination).withAspect(1, 1).withMaxSize(200, 200).asSquare().start(context);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            URI_FOR_SELECTED_IMAGE = Crop.getOutput(result);
            imageView.setImageURI(URI_FOR_SELECTED_IMAGE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),URI_FOR_SELECTED_IMAGE);
                String path = URI_FOR_SELECTED_IMAGE.getPath();
                imageView.setImageBitmap(rotateBitmap(bitmap, path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            isImageSelected = true;
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void checkPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }

    /**
     * Rotate a bitmap based on orientation metadata.
     * src - image path
     */
    private Bitmap rotateBitmap(Bitmap bitmap, String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    Log.d("1234", "rotateBitmap: "+1);
                    matrix.setScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.d("1234", "rotateBitmap: "+2);
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    Log.d("1234", "rotateBitmap: "+3);
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    Log.d("1234", "rotateBitmap: "+4);
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d("1234", "rotateBitmap: "+5);
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    Log.d("1234", "rotateBitmap: "+6);
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.d("1234", "rotateBitmap: "+7);
                    matrix.setRotate(-90);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                case ExifInterface.ORIENTATION_UNDEFINED:
                default:
                    Log.d("1234", "rotateBitmap: "+9);
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void handleResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            beginCrop(mImageCaptureUri);
        }
    }

    public void handleGrantedPermisson(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getURI_FOR_SELECTED_IMAGE() {
        return URI_FOR_SELECTED_IMAGE;
    }

    public boolean isImageSelected() {
        return isImageSelected;
    }
}



