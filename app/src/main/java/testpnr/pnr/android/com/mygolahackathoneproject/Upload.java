package testpnr.pnr.android.com.mygolahackathoneproject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by bhargavsarvepalli on 26/07/15.
 */
public class Upload extends Activity {

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int KITKAT_RESULTCODE = 2;

    File uploadFile;
    String UploadedFileName;


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Uri result = null;
        if (requestCode == KITKAT_RESULTCODE) {
            result = intent.getData();
            final int takeFlags = intent.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String path = getPath(this, result);
            File selectedFile = new File(path);
//I used you example with a bit of editing so thought i would share, here i added a method to upload the file to the webserver
            UploadFile(selectedFile);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upload_activity);

        WebView myWebView = (WebView) findViewById(R.id.upload_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://192.168.0.163/hack/upload.php");
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAllowContentAccess(true);
        myWebView.setWebChromeClient(new MyWebChromeClient());
// new WebChromeClient() {
//            //The undocumented magic method override
//            //Eclipse will swear at you if you try to put @Override here
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                Upload.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//
//            }
//
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("*/*");
//                Upload.this.startActivityForResult(
//                        Intent.createChooser(i, "File Browser"),
//                        FILECHOOSER_RESULTCODE);
//            }
//
//            //For Android 4.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                mUploadMessage = uploadMsg;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//                Upload.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), Upload.FILECHOOSER_RESULTCODE);
//
//            }
//
//        });

        setContentView(myWebView);

    }


    class MyWebChromeClient extends WebChromeClient {
        // The undocumented magic method override
        // Eclipse will swear at you if you try to put @Override here
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            Upload.this.startActivityForResult(
                    Intent.createChooser(i, "Image Browser"),
                    FILECHOOSER_RESULTCODE);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg);
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            // make sure there is no existing message

            Intent intent = fileChooserParams.createIntent();
            try {
                Upload.this.startActivityForResult(intent, Upload.this.KITKAT_RESULTCODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(Upload.this, "Cannot open file chooser", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }

        public void showPicker(ValueCallback<Uri> uploadMsg) {
            // Here is part of the issue, the uploadMsg is null since it is not triggered from Android
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            Upload.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), Upload.KITKAT_RESULTCODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

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
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @source http://stackoverflow.com/a/20559175
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
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
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @source http://stackoverflow.com/a/20559175
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @source http://stackoverflow.com/a/20559175
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @source http://stackoverflow.com/a/20559175
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //flipscreen not loading again
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public void UploadFile(File selectedFile) {
        Random rnd = new Random();
        String sName = "File" + rnd.nextInt(999999) + selectedFile.getAbsolutePath().substring(selectedFile.getAbsolutePath().lastIndexOf("."));
        UploadedFileName = sName;
        uploadFile = selectedFile;

        new Thread() {

            public void run() {
                int serverResponseCode;
                String serverResponseMessage;
                HttpURLConnection connection = null;
                DataOutputStream outputStream = null;
                DataInputStream inputStream = null;
                String pathToOurFile = uploadFile.getAbsolutePath();
                String urlServer = "http://192.168.0.163/hack/execute.php?updatepic=" + UploadedFileName;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                try {
                    FileInputStream fileInputStream = new FileInputStream(uploadFile);

                    URL url = new URL(urlServer);
                    connection = (HttpURLConnection) url.openConnection();
                    Log.i("File", urlServer);

                    // Allow Inputs &amp; Outputs.
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    // Set HTTP method to POST.
                    connection.setRequestMethod("POST");

                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    Log.i("File", "Open conn");

                    outputStream = new DataOutputStream(connection.getOutputStream());

                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    Log.i("File", "write bytes");

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    Log.i("File", "available: " + fileInputStream.available());

                    // Read file
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    Log.i("file", "Bytes Read: " + bytesRead);
                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = connection.getResponseCode();
                    serverResponseMessage = connection.getResponseMessage();
                    Log.i("file repsonse", serverResponseMessage);

//once the file is uploaded call a javascript function to verify the user wants to save the image

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Log.i("start", "File name: " + UploadedFileName);
                            WebView myWebView = (WebView) findViewById(R.id.upload_webview);
                            myWebView.loadUrl("http://192.168.0.163/hack/execute.php");
                        }
                    });


                    fileInputStream.close();
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception ex) {
                    Log.i("exception", "Error: " + ex.toString());
                }
            }
        }.start();


    }

    class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

    }

}



