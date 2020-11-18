package apps.KGSAPP.net.KGSAPP;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static apps.KGSAPP.net.KGSAPP.BaseApplication.progressOFF;
import static apps.KGSAPP.net.KGSAPP.BaseApplication.progressON;
import static apps.KGSAPP.net.KGSAPP.R.layout.activity_splash;
import static com.androidquery.callback.AbstractAjaxCallback.cancel;


/**
 * Created by yunstone on 2017-02-24.
 */

public class SplashActivity extends AppCompatActivity {

    private String deviceId;
    private String lon;
    private String lat;
    //public static int loginint; /////스플리쉬를 한번만 띄우기위한 조치 변수가 값이 자동으로 초기화됨
    public int positioncount;
    private String versionName;
    private int counttimevar = 10;

    private Handler mHandler;  //타이머 설정
    private Runnable mRunnable; //타이머 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_splash);

        //////로딩 시작/////
        progressON(this, "Loading...");
        /////로딩시작//////

        positioncount = 0;
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        */
        /*
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.e("device_version", "gps_enabled");
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(gps_enabled && network_enabled) {
            // 유저에게 알린다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
        }
        */


        //Log.e("loading...", "SplashActivity!!");
        /*로디 끝나는 시점을 위치정보를 받아오는 시점으로 변경하기위해서 주석처리
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000); // 3초 후 이미지를 닫습니다
        */

        //GetDevicesUUID(); //안드로이드 고유번호 가져오기
        /*
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 1, mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300, 1, mLocationListener);//엄청 빨리 잡아온다
        //////gps 활성화 안될때 잡아오기
        if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "위치정보를 가져올수 없습니다.\n gps를 켜주세요!", Toast.LENGTH_LONG).show();
        }
        */

        ////////////////////////// 첫실행인지 아닌지 체크///////////////
        SharedPreferences prefs = getSharedPreferences("isFirstRun", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //onDestroy();
            prefs.edit().putBoolean("isFirstRun", false).apply();
            firstcheck();
        } else {
            //TimerOn(); //타이머 시작
            countDownTimer(counttimevar);
        }
        ////////////////////////// 첫실행인지 아닌지 체크///////////////
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
            //Log.d("위치정보 : ","스타트");
            //Log.d("test", "onLocationChanged, location:" + location);
            //파일저장을 위한 SharedPreferences 설정
            //파일저장을 위한 SharedPreferences 설정

            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            //Toast.makeText(getApplicationContext(), "provider :" + provider, Toast.LENGTH_LONG).show();
            //d("위치정보 : ", provider + "경도:" + longitude + "위도:" + latitude + "고도:" + altitude + "정확도:" + accuracy);

            //Intent intent = new Intent();
            //ComponentName name = new ComponentName("apps.minatalk.net.minatalk", "apps.minatalk.net.minatalk.MainActivity");
            //intent.setComponent(name);

            lon = Double.toString(longitude);
            lat = Double.toString(latitude);


            if (lon != null) {
                //onDestroy();  //타이머 중지
                /*인텐트로 통신은 메인액티비와 프레그먼의 에러로 그만둠
                Toast.makeText(getApplicationContext(),"lonstart :" + lon,Toast.LENGTH_LONG).show();
                intent.putExtra("lon",lon);
                intent.putExtra("lat",lat);
                intent.putExtra("starflag",1);
                Fragment fragment = new ListContentFragment(); // Fragment 생성
                Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                bundle.putString("lon", lon); // key , value
                bundle.putString("lat", lat); // key , value
                fragment.setArguments(bundle);
                startActivityForResult(intent,1001);
                */
                SharedPreferences location1 = getSharedPreferences("location", MODE_PRIVATE);
                SharedPreferences.Editor editor = location1.edit();
                editor.putString("lon", lon); //First라는 key값으로 infoFirst 데이터를 저장한다.
                editor.putString("lat", lat); //Second라는 key값으로 infoSecond 데이터를 저장한다.
                editor.putString("uuid", deviceId); //고유번호 안드로이드 아이디
                editor.putString("logincheck", "1");
                editor.commit(); //완료한다.
                lm.removeUpdates(mLocationListener);
                //finish(); 이곳에서 액티비티가 종료되면 아래쪽 체크가 이루어 지지않는다 안된다.
                getLogin(0); //위도 경도를 가져온후 회원가입여부와 로그인체크 체크
            } else if(lon == null || lon == "") { ////전혀 실행이 안되고 있음
                Log.d("positioncount0", String.valueOf(positioncount));
                if(positioncount == 10){
                    Toast.makeText(getApplicationContext(), getString(R.string.toast18) + "\n" + getString(R.string.toast19), Toast.LENGTH_LONG).show();

                    //theend();
                    Log.d("positioncount1", String.valueOf(positioncount));
                }else{
                    lm.removeUpdates(mLocationListener);
                    positioncount = positioncount + 1;
                    Log.d("positioncount", String.valueOf(positioncount));
                    SystemClock.sleep(1000); ///위치 정보를 못가져오면 1초 쉬고 다시 로딩
                }
            }


        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    public void getLogin(int wherecheck) {
        //final String version;
        final String whereurl;
        SystemClock.sleep(10);  ////10초간 대기한다
        //loginint = 1; /////앱을 실행했는지 안했는지 판단한다
        AQuery aq = new AQuery(this);
        // public JSONArray mItems = new JSONArray();
        //aq.ajax(AppVar.API_ROOT + sListName + ".php?key=" + AppVar.API_KEY + "&last=" + sLastNo, JSONObject.class, new AjaxCallback<JSONObject>() {
        if (wherecheck == 0) {
            whereurl = "http://kgs.yunstone.com/ajax/logincheck.php?lat=" + lat + "&lon=" + lon + "&uuid=" + deviceId;
        } else {
            whereurl = "http://kgs.yunstone.com/ajax/logincheck.php?lat=0&lon=0&uuid=" + deviceId;
        }
        aq.ajax(whereurl, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                Log.d("whereurl", url);
                if (object != null) {
                    try {
                        Log.d("logincheck", url);
                        JSONObject oData = new JSONObject(object.toString());
                        //peoples = jsonObj.getJSONArray(TAG_RESULTS);
                        JSONArray arrData = new JSONArray(oData.getString("result"));
                        Log.d("위치정보", "경도:" + lon + "위도:" + lat);

                        ////////object가 없으면 이곳으로 들어올수없음/////
                        //for (int i = 0; i < arrData.length(); i++) {

                        JSONObject getlist = arrData.getJSONObject(0);
                        String logincheck = getlist.getString("y");

                        //Log.d("resultFor", logincheck);


                        if (logincheck.equals("0")) {
                            final Intent intent = new Intent();
                            ComponentName name = new ComponentName("apps.minatalk.net.minatalk", "apps.minatalk.net.minatalk.policy");
                            intent.setComponent(name);
                            startActivityForResult(intent, 1001);
                        } else {
                            String version = getlist.getString("version");
                            //Log.d("version", version);

                            TextView versionNtxt = (TextView) findViewById(R.id.versionNTxt);
                            versionNtxt.setText("最新バージョン:" + version);

                            PackageInfo pInfo = null;
                            try {
                                pInfo = getPackageManager().getPackageInfo("apps.minatalk.net.minatalk", PackageManager.GET_META_DATA);
                                //Log.d("versionName", pInfo.versionName);
                                versionName = pInfo.versionName;
                                TextView versionPtxt = (TextView) findViewById(R.id.versionPTxt);
                                versionPtxt.setText("現在のバージョン:" + versionName);


                            } catch (Exception e) {

                            }
                            if (versionName.equals(version) || version.equals("0")) {
                                String imagepath = getlist.getString("imagepath");
                                String cash = getlist.getString("cash");
                                String point = getlist.getString("point");
                                String uid = getlist.getString("uid");
                                String nickname = getlist.getString("nickname");
                                String sex = getlist.getString("sex");
                                String mscount = getlist.getString("mscount");
                                String age = getlist.getString("age");
                                String greetings = getlist.getString("greetings");
                                SharedPreferences location1 = getSharedPreferences("location", MODE_PRIVATE);
                                SharedPreferences.Editor editor = location1.edit();
                                editor.putString("imagepath", imagepath); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                editor.putString("cash", cash); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                editor.putString("point", point); //First라는 key값으로 infoFirst 데이터를 저장한다.
                                editor.putString("uid", uid); //디비의 고유 번호를 가져온다.
                                editor.putString("sex", sex);
                                editor.putString("age", age);
                                editor.putString("mscount", mscount);
                                editor.putString("nickname", nickname);
                                editor.putString("greetings", greetings);
                                editor.putString("uuid", deviceId);
                                editor.commit(); //완료한다.
                                //Toast.makeText(getBaseContext(), "sex:" + sex, Toast.LENGTH_LONG).show();

                                /////로딩 끝/////
                                progressOFF();
                                /////로딩 끝////
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class); // 다음 넘어갈 클래스 지정
                                startActivity(intent);
                                /*
                                Intent intent = new Intent(getApplicationContext(),MessengerActivity.class); // 다음 넘어갈 클래스 지정
                                startActivity(intent);
                                */
                                //Intent intent = new Intent(getApplicationContext(),ChatPage_Fragment.class); // 다음 넘어갈 클래스 지정
                                //Intent intent = new Intent(getApplicationContext(),policy.class); // 다음 넘어갈 클래스 지정
                                //startActivity(intent);

                            } else {
                                /// 구글 플레이로 이동하기////////////////////
                                Toast.makeText(getBaseContext(), getString(R.string.toast27), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                                startActivity(intent);

                                android.os.Process.killProcess(android.os.Process.myPid());
                            }

                        }
                        // }


                        //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                    } catch (Exception e) {

                    }
                }

            }
        });
    }


    public void countDownTimer(int counttime) {

        if (ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    Toast.makeText(SplashActivity.this, getString(R.string.toast15), Toast.LENGTH_SHORT).show();
                    //countDownTimer(10000);
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class); // 다음 넘어갈 클래스 지정
                    startActivity(intent);

                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Toast.makeText(SplashActivity.this, getString(R.string.toast16) + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

                }


            };

            TedPermission.with(SplashActivity.this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage(getString(R.string.toast17)) // "we need permission for read contact and find your location"
                    .setPermissions(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
                    .check();
            return;
        }


        try {
            String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("device_version", device_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        GetDevicesUUID(); //안드로이드 고유번호 가져오기
                /*///////////////////////// 첫실행인지 아닌지 체크///////////////
                SharedPreferences prefs = getSharedPreferences("isFirstRun", MODE_PRIVATE);
                boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
                if (isFirstRun) {
                    prefs.edit().putBoolean("isFirstRun", false).apply();
                    firstcheck();
                }
                *////////////////////////// 첫실행인지 아닌지 체크///////////////
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 1, mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300, 1, mLocationListener);//엄청 빨리 잡아온다


        if (!lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //Toast.makeText(getApplicationContext(), "위치정보를 가져올수 없습니다.\n gps를 켜주세요!", Toast.LENGTH_LONG).show();
            //Log.d("  emailAuthCount: ", String.valueOf(emailAuthCount - ((emailAuthCount / 60) * 60)));
            //Log.d("  networkcancel(): ", String.valueOf(emailAuthCount - ((emailAuthCount / 60) * 60)));
            //Log.d("  cancel(): ", String.valueOf(emailAuthCount - ((emailAuthCount / 60) * 60)));

        } else {
            //Toast.makeText(getApplicationContext(), "NETWORK_PROVIDER", Toast.LENGTH_SHORT).show();
            cancel();
        }


        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(getApplicationContext(), "위치정보를 가져올수 없습니다.\n gps를 켜주세요!", Toast.LENGTH_LONG).show();
            //Log.d("  emailAuthCount: ", String.valueOf(emailAuthCount - ((emailAuthCount / 60) * 60)));
            //Log.d("  gpscancel(): ", String.valueOf(emailAuthCount - ((emailAuthCount / 60) * 60)));
            //cancel();
        } else {
            //Toast.makeText(getApplicationContext(), "GPS_PROVIDER", Toast.LENGTH_SHORT).show();
            cancel();
        }
        //Log.d("positioncount0", String.valueOf(positioncount));
        /*
        if(positioncount == 10){
            Toast.makeText(getApplicationContext(), getString(R.string.toast18) + "\n" + getString(R.string.toast19), Toast.LENGTH_LONG).show();

            //theend();
            //Log.d("positioncount1", String.valueOf(positioncount));
        }else{
            lm.removeUpdates(mLocationListener);
            positioncount = positioncount + 1;
            countDownTimer(10000);
            Log.d("positioncount", String.valueOf(positioncount));
            SystemClock.sleep(1000); ///위치 정보를 못가져오면 1초 쉬고 다시 로딩
        }
        */

    }


    //안드로이드 디바이스 아이디 획득하기
    public String GetDevicesUUID() {
        final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        deviceId = deviceUuid.toString();
        Log.d("deviceId uuid : ", deviceId);
        return deviceId;
    }

    public void theend() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid()); //앱종료
    }

    public void firstcheck() {
        AQuery aq = new AQuery(this);
        aq.ajax("http://kgs.yunstone.com/ajax/firstcheck.php?uuid=" + deviceId, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    Log.d("firstcheck", url);

                    counttimevar = 30;
                    countDownTimer(counttimevar); //위치정보를 못가져 올때를 대비해 시간설정을 했지만 먹히지 않고있
                    //return;
                    //showToast(Integer.toString(mRvListAdapter.mItems.length()));
                } catch (Exception e) {

                }

            }
        });
    }

    /*/////타이머가  잘안됨//////////////
    public void TimerOn() {
        //Toast.makeText(getApplicationContext(), "타이머 시작", Toast.LENGTH_LONG).show();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("timerinside",getString(R.string.toast18));
                Toast.makeText(getApplicationContext(), getString(R.string.toast18) + "\n" + getString(R.string.toast19), Toast.LENGTH_LONG).show();
                //theend();
            }
        };

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 10000);
    }
    @Override
    protected void onDestroy(){
        //Log.i("onDstory", "onDstory");
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
    *//////타이머 관련//////////////



}
