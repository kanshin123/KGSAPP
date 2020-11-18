package apps.KGSAPP.net.KGSAPP;

/** 이미지 업로드 하다가 실패한 소스임 쓰지 않치만 참고할수있음
 * Created by yunseokchoi on 2017. 4. 8..
 */

// 업로드
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUpLoad extends Activity {

    private FileInputStream mFileInputStream = null;
    private URL connectUrl = null;

    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        String mFilePath = "/storage/emulated/0/minatalk/1491659992413.jpg";
        HttpFileUpload("http://kgs.yunstone.com/jsonp/upload.php",mFilePath);

    }

    private void HttpFileUpload(String urlString, String filepath) {
        try {

            File f = new File(filepath);
            mFileInputStream = new FileInputStream(f);
            connectUrl = new URL(urlString);
            Log.d("test", "mFileInputStream is " + mFileInputStream);

            // open connection
            HttpURLConnection conn = (HttpURLConnection) connectUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                    + f.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            // 한번에 증가하는 양
            float size = (100 / ((float) bytesAvailable / (float) bufferSize));
            // 총 받은 양
            float totalSize = 0;

            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = mFileInputStream.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
                totalSize = totalSize + size;
                Log.d("test", "파일 변환중 " + (int) totalSize);
            }
            Log.d("test", "image byte is " + bytesAvailable);

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.d("test", "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            InputStream is = conn.getInputStream();

            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }
            Log.d("test", sb.toString());
            dos.close();

        } catch (Exception e) {
            Log.d("test", "exception " + e.getMessage());
            // TODO: handle exception
        }
    }
}
