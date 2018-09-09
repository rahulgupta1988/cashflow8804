package cashflow.sixdcash.com.cashflow.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cashflow.sixdcash.com.cashflow.R;

/**
 * Created by Praveen on 14-Oct-16.
 */

public class ImageSelector {

    ImageListener imageListener;

    public interface ImageListener{
        public void getImageName(String imagename);
    }

    Context mContext;
    public static final int SELECT_FILE = 1;
    public static final int REQUEST_CAMERA = 2;

    ImageView imageView_round;

    public ImageSelector(Context context, ImageView imageView_round,ImageListener imageListener) {
        mContext = context;
        this.imageView_round = imageView_round;
        this.imageListener=imageListener;
    }

    String userChoosenTask;


    public void selectImage() {

        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);
        final Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photoselectordialog);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.SelPhotoDialogAnimation);

        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout camlay=(LinearLayout)window.findViewById(R.id.camlay);
        LinearLayout gallay=(LinearLayout)window.findViewById(R.id.gallay);
        LinearLayout canlay=(LinearLayout)window.findViewById(R.id.canlay);

        camlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
                dialog.dismiss();
            }
        });

        gallay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
                dialog.dismiss();
            }
        });

        canlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void selectImage1() {

        final CharSequence[] items = {"Take Photo", "Choose from Image Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;//Utility.checkPermission();
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Image Gallery")) {
                    userChoosenTask = "Choose from Image Gallery";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    String capture_Photo_name = "";

    private void cameraIntent() {

        capture_Photo_name = System.currentTimeMillis() + ".jpg";//dateFormat.format(cal.getTime());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                getPhotoFileUri(capture_Photo_name));
        ((Activity) mContext).startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        ((Activity) mContext).startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        imageListener.getImageName(capture_Photo_name);

    }

    public void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
                imageView_round.setImageBitmap(bm);
                saveGallryImage(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void onCaptureImageResult(Intent data) {
        Uri takenPhotoUri = getPhotoFileUri(capture_Photo_name);
        // by this point we have the camera photo on disk
        Bitmap thumbnail = null;
        try {
            thumbnail = BitmapFactory.decodeFile(takenPhotoUri.getPath());//(Bitmap) data.getExtras().get("data");
            imageView_round.setImageBitmap(thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Uri getPhotoFileUri(String fileName) {

        File mediaStorageDir = new File(
                mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "expressionApp/");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("expressionApp", "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));

    }

    public void saveGallryImage(Bitmap thumbnail) {
        FileOutputStream fo;
        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            String root = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            File myDir = new File(root + "/expressionApp/");
            myDir.mkdirs();

            capture_Photo_name= System.currentTimeMillis()+".jpg";

            File destination = new File(myDir.getAbsolutePath(),
                    capture_Photo_name);

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
