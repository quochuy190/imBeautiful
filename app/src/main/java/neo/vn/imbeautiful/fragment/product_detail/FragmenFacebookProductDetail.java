package neo.vn.imbeautiful.fragment.product_detail;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareMedia;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.App;
import neo.vn.imbeautiful.BuildConfig;
import neo.vn.imbeautiful.R;
import neo.vn.imbeautiful.activity.products.ActivityProductDetail;
import neo.vn.imbeautiful.base.BaseFragment;
import neo.vn.imbeautiful.config.Constants;
import neo.vn.imbeautiful.models.ObjLogin;
import neo.vn.imbeautiful.models.Products;
import neo.vn.imbeautiful.untils.SharedPrefs;

import static neo.vn.imbeautiful.activity.products.ActivityProductDetail.mLisIma;

/**
 * Created by: Neo Company.
 * Developer: HuyNQ2
 * Date: 22-April-2019
 * Time: 10:30
 * Version: 1.0
 */
public class FragmenFacebookProductDetail extends BaseFragment {
    private static final String TAG = "FragmenFacebookProductD";
    public static FragmenFacebookProductDetail fragment;
    @BindView(R.id.btn_share)
    ImageView btn_share;
    @BindView(R.id.txt_des_up_face)
    TextView txt_des_up_face;
    @BindView(R.id.txt_update)
    TextView txt_update;

    @BindView(R.id.img_facebook_1)
    ImageView img_facebook_1;
    @BindView(R.id.img_facebook_2)
    ImageView img_facebook_2;
    @BindView(R.id.img_facebook_3)
    ImageView img_facebook_3;
    @BindView(R.id.img_facebook_4)
    ImageView img_facebook_4;
    @BindView(R.id.img_facebook_5)
    ImageView img_facebook_5;
    @BindView(R.id.btn_download)
    Button btn_download;
    @BindView(R.id.ll_bac)
    ConstraintLayout ll_show_image;
    private Products mProduct;
    private List<String> mList;

    public static FragmenFacebookProductDetail getInstance() {
        if (fragment == null) {
            synchronized (FragmenFacebookProductDetail.class) {
                if (fragment == null)
                    fragment = new FragmenFacebookProductDetail();
            }
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_product_detail, container, false);
        ButterKnife.bind(this, view);
        btn_download.setEnabled(true);
        initData();
        get_hash();
        FacebookSdk.sdkInitialize(getContext());
        initFacebook();
        initEvent();

        return view;
    }



  /*  private void init() {
        mList = new ArrayList<>();
        adapterService = new AdapterImageUpFace(mList, getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL,
                false);
        recycle_product.setNestedScrollingEnabled(false);
        recycle_product.setHasFixedSize(true);
        recycle_product.setLayoutManager(mLayoutManager);
        recycle_product.setItemAnimator(new DefaultItemAnimator());
        recycle_product.setAdapter(adapterService);
      *//*  adapterService.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {
                setResult(RESULT_OK, new Intent());
                App.mCity = (City) item;
            }
        });*//*
        adapterService.setOnIListener(new ItemClickListener() {
            @Override
            public void onClickItem(int position, Object item) {

            }
        });

    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData() {
        mList = new ArrayList<>();
        mProduct = App.mProduct;
        if (mProduct != null) {
            if (mProduct.getsUrlImage() != null) {
                mList.add(mProduct.getsUrlImage());
            }
            if (mProduct.getIMG1() != null) {
                mList.add(mProduct.getIMG1());
            }
            if (mProduct.getIMG2() != null) {
                mList.add(mProduct.getIMG2());
            }
            if (mProduct.getIMG3() != null) {
                mList.add(mProduct.getIMG3());
            }

            if (mProduct.getCONTENT_FB() != null) {
                txt_des_up_face.setText(Html.fromHtml(mProduct.getCONTENT_FB()));
            } else if (mProduct.getDESCRIPTION() != null) {
                txt_des_up_face.setText(Html.fromHtml(mProduct.getDESCRIPTION()));
            } else {
                txt_des_up_face.setText("Mô tả: ...");
            }
            if (mList.size() == 0)
                mList.add("abc");
        }
        if (mLisIma.size() > 0) {
            ll_show_image.setVisibility(View.VISIBLE);
            btn_download.setVisibility(View.VISIBLE);
            if (mLisIma.get(0) != null) {
                img_facebook_1.setVisibility(View.VISIBLE);
                img_facebook_1.setImageBitmap(mLisIma.get(0));
            }
            if (mLisIma.size() > 1 && mLisIma.get(1) != null) {
                img_facebook_2.setVisibility(View.VISIBLE);
                img_facebook_2.setImageBitmap(mLisIma.get(1));
            }
            if (mLisIma.size() > 2 && mLisIma.get(2) != null) {
                img_facebook_3.setVisibility(View.VISIBLE);
                img_facebook_3.setImageBitmap(mLisIma.get(2));
            }
            if (mLisIma.size() > 3 && mLisIma.get(3) != null) {
                img_facebook_4.setVisibility(View.VISIBLE);
                img_facebook_4.setImageBitmap(mLisIma.get(3));
            }
            if (mLisIma.size() > 4 && mLisIma.get(4) != null) {
                img_facebook_5.setVisibility(View.VISIBLE);
                img_facebook_5.setImageBitmap(mLisIma.get(4));
            }
        } else {
            ll_show_image.setVisibility(View.GONE);
            btn_download.setVisibility(View.GONE);
        }
        if (mProduct.getVIDEO_FB() != null) {
            img_facebook_5.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(R.drawable.play_video_default).into(img_facebook_5);
            //img_facebook_5.setImageDrawable(getResources().getDrawable(R.drawable.play_video_default));
        }

    }

    private void get_hash() {
        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo(
                    "com.neo.motherland",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkDiskPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "No Permissions", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);
        } else {
            start_download_media();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    start_download_media();
                    Toast.makeText(getContext(), "Permission given",
                            Toast.LENGTH_SHORT).show();
                    //saveImage(finalBitmap, image_name); <- or whatever you want to do after permission was given . For instance, open gallery or something
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    private void initEvent() {
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDiskPermission();
            }
        });
        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjLogin objLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
                String sString = txt_des_up_face.getText().toString() + "\n" + "Tư vấn bán hàng: " + objLogin.getUSERNAME();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("imBeautiful",
                        sString);
                clipboard.setPrimaryClip(clip);
                share_multil_image();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjLogin objLogin = SharedPrefs.getInstance().get(Constants.KEY_SAVE_USER_LOGIN, ObjLogin.class);
                String sString = txt_des_up_face.getText().toString() + "\n" + "Tư vấn bán hàng: " + objLogin.getUSERNAME();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("imBeautiful",
                        sString);
                clipboard.setPrimaryClip(clip);
                share_multil_image();
            }
        });
    }

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    private void initFacebook() {

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        //  MessageDialog.show(this, getContext());
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                // This doesn't work
                hideDialogLoading();
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                hideDialogLoading();
                Toast.makeText(getContext(), "You Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                // This doesn't work
                hideDialogLoading();
                e.printStackTrace();
            }
        });
    }

    public void share_multil_image() {
        SharePhoto sharePhoto1 = null;
        SharePhoto sharePhoto2 = null;
        SharePhoto sharePhoto3 = null;
        SharePhoto sharePhoto4 = null;
        if (mLisIma.size() > 0) {
            if (mLisIma.get(0) != null) {
                sharePhoto1 = new SharePhoto.Builder()
                        .setBitmap(mLisIma.get(0))
                        .setCaption("abcedfbk")
                        .setUserGenerated(false)
                        .build();
            }
            if (mLisIma.size() > 1 && mLisIma.get(1) != null) {
                sharePhoto2 = new SharePhoto.Builder()
                        .setBitmap(mLisIma.get(1))
                        .setCaption("abcedfbk")
                        .setUserGenerated(false)
                        .build();
            }
            if (mLisIma.size() > 2 && mLisIma.get(2) != null) {
                sharePhoto3 = new SharePhoto.Builder()
                        .setBitmap(mLisIma.get(2))
                        .setCaption("abcedfbk")
                        .setUserGenerated(false)
                        .build();
            }
            if (mLisIma.size() > 3 && mLisIma.get(3) != null) {
                sharePhoto4 = new SharePhoto.Builder()
                        .setBitmap(mLisIma.get(3))
                        .setCaption("abcedfbk")
                        .setUserGenerated(false)
                        .build();
            }
        }
        ShareVideo shareVideo2 = null;
        if (mProduct.getVIDEO_FB() != null) {
            shareVideo2 = new ShareVideo.Builder()
                    .setLocalUrl(ActivityProductDetail.mUri)
                    .build();
        }
        ShareContent shareContent = null;
        List<ShareMedia> mLis = new ArrayList<>();
        for (Bitmap bitmap : mLisIma) {
            mLis.add(new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .setCaption("imBeautiful")
                    .setUserGenerated(false)
                    .build());
        }

       /* mLis.add(photo1);
        mLis.add(photo2);
        mLis.add(photo3);
        mLis.add(photo4);*/
        if (mLis.size() > 0) {
            shareContent = new ShareMediaContent.Builder()
                    .addMedia(mLis)
/*                    .addMedium(sharePhoto1)
                    .addMedium(sharePhoto2)
                    .addMedium(sharePhoto3)
                    .addMedium(sharePhoto4)*/
                    .addMedium(shareVideo2)
                    .build();
        } else {
            shareContent = new ShareMediaContent.Builder()
                    //   .addMedia(mLis)
                    .addMedium(sharePhoto1)
                    .addMedium(sharePhoto2)
                    .addMedium(sharePhoto3)
                    .addMedium(sharePhoto4)
                    .addMedium(shareVideo2)
                    .build();
        }
        showDialogLoading();
        shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);

        //ShareDialog.show(this, content);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        @Override
        protected String doInBackground(String... sUrl) {
            download_all(sUrl[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
            //  mWakeLock.release();
            //mProgressDialog.dismiss();
        }
    }

    public void start_download_media() {
        if (mProduct.getMEDIA_FB() != null && mProduct.getMEDIA_FB().length() > 0) {
            String[] arrayImage = mProduct.getMEDIA_FB().split("\\|\\|");
            if (arrayImage.length > 0) {
                for (String sLink : arrayImage) {
                    if (sLink != null && sLink.length() > 0)
                        //  new DownloadTask().execute(sLink);
                        download_all(sLink);
                }
            }
        }
        if (mProduct.getVIDEO_FB() != null && mProduct.getVIDEO_FB().length() > 0) {
            download_video(mProduct.getVIDEO_FB());
        }
    }

    public void download_all(String sUrl) {
        btn_download.setEnabled(false);
        Toast.makeText(getContext(), "Bắt đầu tải ảnh về máy", Toast.LENGTH_SHORT).show();
        String filename = "filename.jpg";
        String downloadUrlOfImage = sUrl;
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + BuildConfig.APPLICATION_ID + "/");


        if (!direct.exists()) {
            direct.mkdir();
            //     Log.d(LOG_TAG, "dir created for first time");
        }
        DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + BuildConfig.APPLICATION_ID + File.separator + filename);

        dm.enqueue(request);
    }

    public void download_video(String sUrl) {
        btn_download.setEnabled(false);
        Toast.makeText(getContext(), "Bắt đầu tải ảnh về máy", Toast.LENGTH_SHORT).show();
        String filename = "filename.mp4";
        String downloadUrlOfImage = sUrl;
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID + "/");


        if (!direct.exists()) {
            direct.mkdir();
            //     Log.d(LOG_TAG, "dir created for first time");
        }
        DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("video/mp4")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + BuildConfig.APPLICATION_ID + File.separator + filename);

        dm.enqueue(request);
    }

}
