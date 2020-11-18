package apps.KGSAPP.net.KGSAPP;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE;
import static apps.KGSAPP.net.KGSAPP.R.id.user_image;

/**
 * Created by jack on 2020. 7. 19..
 */

public class login extends Activity implements View.OnClickListener {
    private EditText nickname;
    private EditText greetings;
    private int sex_text;
    private String age_text;
    private Button btnDone;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absoultePath;

    private String fileName;
    private String filePath;
    private String serverUrl;
    private String token;
    private int age_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(login.this, getString(R.string.toast6), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(login.this, getString(R.string.toast7)+"\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getString(R.string.toast8)+"\n\n"+getString(R.string.toast9)+" [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

        /////push id 얻어오기//////
        //FirebaseMessaging.getInstance().subscribeToTopic("news");
        //FirebaseInstanceId.getInstance().getToken();
        token = FirebaseInstanceId.getInstance().getToken();

        // dbmanger = new DB_Manger();

        iv_UserPhoto = (ImageView) this.findViewById(user_image);
        Button btn_agreeJoin = (Button) this.findViewById(R.id.btn_UploadPicture);

        btn_agreeJoin.setOnClickListener(this);


        nickname = (EditText) findViewById(R.id.nickname);
        greetings = (EditText) findViewById(R.id.greetings);
        final Spinner spinner_sex = (Spinner) findViewById(R.id.spinner_sex);

        final Spinner spinner_age = (Spinner) findViewById(R.id.spinner_age);

        btnDone = (Button) findViewById(R.id.btn_signup);

        spinner_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int positon_sex = (position - 1) ;
                Log.d("position : " , positon_sex +"//" + parent.getItemAtPosition(position));
                sex_text = positon_sex  ;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinner_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("position : " , position +"//" + parent.getItemAtPosition(position));
                age_text = String.valueOf(parent.getItemAtPosition(position));
                age_position = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        //회원가입버튼 클릭시 체크
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(login.this, "닉네임확인"+nicname.getText().toString(), Toast.LENGTH_SHORT).show();
                // 닉네임 입력 확인
                if (nickname.getText().toString().length() == 0) {
                    Toast.makeText(login.this, getString(R.string.toast10), Toast.LENGTH_SHORT).show();
                    nickname.requestFocus();
                    return;
                }

                // 인사말 입력 확인
                if (greetings.getText().toString().length() == 0) {
                    Toast.makeText(login.this, getString(R.string.toast12), Toast.LENGTH_SHORT).show();
                    greetings.requestFocus();
                    return;
                }

                if (sex_text == -1) {
                    Toast.makeText(login.this, getString(R.string.toast11), Toast.LENGTH_SHORT).show();
                    spinner_sex.requestFocus();
                    return;
                }

                if (age_position == 0) {
                    Toast.makeText(login.this, getString(R.string.toast13), Toast.LENGTH_SHORT).show();
                    spinner_age.requestFocus();
                    return;
                }

                getLogin();

            }

        });

    }

    //////////////////사진 관련 부분시작/////////

    /**
     * 카메라에서 사진 촬영
     */
    //////////////////사진 관련 부분시작/////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
            //Toast.makeText(Setting.this, "album on!!", Toast.LENGTH_SHORT).show();
        }else if(requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK){
            beginCrop(mImageCaptureUri);
        }else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {

        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
        Uri destination = Uri.fromFile(new File(getCacheDir(), url));

        Crop.of(source, destination).asSquare().start(this);


    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
            Uri destination = Uri.fromFile(new File(getCacheDir(), url));
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/minatalk/" + url;
            fileName = url;
            serverUrl = "http://kgs.yunstone.com/jsonp/upload.php";
            Bitmap photo = null;
            try {

                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
                //iv_UserPhoto.setImageBitmap(photo);
                storeCropImage(photo, filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
    {
        Intent result = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        String url = String.valueOf(System.currentTimeMillis()) + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            mImageCaptureUri = FileProvider.getUriForFile(login.this, BuildConfig.APPLICATION_ID + ".provider", new File(Environment.getExternalStorageDirectory(), url));
            Log.d("URI!!", String.valueOf(mImageCaptureUri));

        } else {
            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        }
        result.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(result, PICK_FROM_CAMERA);
        //Crop.pickImage(Setting.this);
    }

    @Override
    public void onClick(View v) {
        id_view = v.getId();
        if (v.getId() == R.id.btn_UploadPicture) {
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //doTakeAlbumAction();
                    //resultView.setImageDrawable(null);
                    Crop.pickImage(login.this);
                    //return true;
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.login1))
                    .setPositiveButton(getString(R.string.login4), cameraListener)
                    .setNegativeButton(getString(R.string.login3), albumListener)
                    .setNeutralButton(getString(R.string.toast1), cancelListener)
                    .show();

        }

    }

    /*
    * Bitmap을 저장하는 부분
    */
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // minatalk 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/minatalk/";
        File directory_minatalk = new File(dirPath);

        if (!directory_minatalk.exists()) // minatalk 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_minatalk.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {

            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);


            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            Glide.with(this).load(filePath).bitmapTransform(new CropCircleTransformation(new login.CustomBitmapPool())).into(iv_UserPhoto);

            ///////////사진 파일 올리는 스레드로 넘길때 파라미터도 같이 넘기는 방법///////
            //Log.d("filePath",filePath);
            //Log.d("fileName",fileName);
            uploadFile thread = new uploadFile(filePath, fileName, serverUrl);
            thread.start();
            ///////////////////////////////////////////////////////////////

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////glied를 쓰기위한 선언
    public class CustomBitmapPool implements BitmapPool {
        @Override
        public int getMaxSize() {
            return 0;
        }

        @Override
        public void setSizeMultiplier(float sizeMultiplier) {

        }

        @Override
        public boolean put(Bitmap bitmap) {
            return false;
        }

        @Override
        public Bitmap get(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public Bitmap getDirty(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public void clearMemory() {

        }

        @Override
        public void trimMemory(int level) {

        }
    }


    ////////////////완료시 서버에 파라미터 전달////////////////////
    public void getLogin() {
        SharedPreferences location = getSharedPreferences("location", 0);
        String lon = location.getString("lon", "");
        String lat = location.getString("lat", "");
        final String uuid = location.getString("uuid", "");
        AQuery aq = new AQuery(this);
        // public JSONArray mItems = new JSONArray();
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        aq.ajax("http://kgs.yunstone.com/ajax/join.php?lat=" + lat + "&lon=" + lon + "&uuid=" + uuid +
                        "&nickname=" + nickname.getText().toString() + "&Greetings=" + greetings.getText().toString() +
                        "&sex=" + sex_text + "&age=" + age_text + "&imagepath=" + fileName +"&pushid="+ token,
                JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        try {
                            JSONObject oData = new JSONObject(object.toString());
                            //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                            JSONArray arrData = new JSONArray(oData.getString("result"));

                            for (int i = 0; i < arrData.length(); i++) {
                                JSONObject getlist = arrData.getJSONObject(i);
                                String logincheck = getlist.getString("y");
                                final String uid = getlist.getString("uid");
                                Log.d("resultFor", logincheck);
                                Log.d("uidresult", uid);
                                if (logincheck.equals("0")) {
                                    SharedPreferences location1 = getSharedPreferences("location", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = location1.edit();
                                    editor.putString("imagepath", fileName);
                                    editor.putString("cash", "1000"); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                    editor.putString("point", "10000"); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                    editor.putString("uid", uid); //디비의 고유 번호를 가져온다.
                                    editor.putString("sex", String.valueOf(sex_text));
                                    editor.putString("age", String.valueOf(age_text));
                                    editor.putString("mscount", "0");
                                    editor.putString("nickname", nickname.getText().toString());
                                    editor.putString("greetings", greetings.getText().toString());
                                    editor.putString("uuid", uuid);
                                    editor.commit();


                                    Toast.makeText(getApplicationContext(), getString(R.string.toast14), Toast.LENGTH_LONG).show();

                                    ////가입 버튼을 누르면 마지막 이곳에서 이미지 업로드 실행 하는게 맞는것같음/////

                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class); // 다음 넘어갈 클래스 지정
                                    startActivity(intent); // 다음 화면으로 넘어간다

                                }
                            }

                        } catch (Exception e) {

                        }

                    }
                });
    }
////////////////완료시 서버에 파라미터 전달 끝////////////////////

    @Override
    public void onBackPressed() {
        // Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
        // startActivity(intent);
        // super.onBackPressed();
        moveTaskToBack(true);
        Intent intent = new Intent(login.this,login.class);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        }else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());


    }
}

