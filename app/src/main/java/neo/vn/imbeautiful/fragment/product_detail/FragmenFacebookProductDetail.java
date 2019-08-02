package neo.vn.imbeautiful.fragment.product_detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import neo.vn.imbeautiful.App;
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
    private Products mProduct;
    private List<String> mList;
    private static String url = "https://3.bp.blogspot.com/-EFwVj5ztKtQ/V8Qs6Viyl6I/AAAAAAAADWs/031SPYFrUnM-wreztTT4fgPe1yQj3LFhgCPcB/s1600/developer.jpg";

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

    private void initEvent() {
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask().execute(url);
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
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + "pics " + System.currentTimeMillis() + ".png");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
           /* PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();*/
           // mProgressDialog.show();
        }


        @Override
        protected void onPostExecute(String result) {
          //  mWakeLock.release();
            //mProgressDialog.dismiss();
        }
    }


}
