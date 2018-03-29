package com.uplink.carins.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.uplink.carins.BuildConfig;
import com.uplink.carins.R;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.Config;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;
import com.uplink.carins.utils.AbFileUtil;
import com.uplink.carins.utils.ToastUtil;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tiansj on 15/2/27.
 */
public class HttpClient {

    private static final int CONNECT_TIME_OUT = 10;
    private static final int WRITE_TIME_OUT = 60;
    private static final int READ_TIME_OUT = 60;
    private static final int MAX_REQUESTS_PER_HOST = 10;
    private static final String TAG = HttpClient.class.getSimpleName();
    private static final String UTF_8 = "UTF-8";
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain;");
    private static OkHttpClient client;
    //json请求
    private static final MediaType MediaType_JSON = MediaType.parse("application/json; charset=utf-8");

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new LoggingInterceptor());

        client = builder.build();


        client.dispatcher().setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);
    }

    /**
     * LoggingInterceptor
     */
    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            //long t1 = System.nanoTime();
            //LogUtil.i(TAG, String.format("Sending request %s on %s%n%s",
            //request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            //long t2 = System.nanoTime();
            //LogUtil.i(TAG, String.format("Received response for %s in %.1fms%n%s",
            //response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

    /**
     * 网络判断
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        } catch (Exception e) {
            LogUtil.v("ConnectivityManager", e.getMessage());
        }
        return false;
    }

    /**
     * 普通get方法
     *
     * @param url
     * @param param
     * @param handler
     */
    public static void get(String url, Map<String, String> param, final HttpResponseHandler handler) {

        if (!isNetworkAvailable()) {
            ToastUtil.showMessage(AppContext.getInstance(), "网络连接不可用", Toast.LENGTH_SHORT);

            return;
        }

        if (param != null && param.size() > 0) {
            url = url + "?" + mapToQueryString(param);
        }
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    handler.sendSuccessMessage(response.body().string());
                } catch (Exception e) {
                    handler.sendFailureMessage(call.request(), e);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendFailureMessage(call.request(), e);
            }
        });
    }


    /**
     * 普通post方法
     *
     * @param url
     * @param param
     * @param handler
     */
    public static void post(String url, Map<String, String> param, final HttpResponseHandler handler) {
        if (!isNetworkAvailable()) {
            ToastUtil.showMessage(AppContext.getInstance(), "网络连接不可用", Toast.LENGTH_LONG);
            return;
        }
        String paramStr = "";
        if (param != null && param.size() > 0) {
            paramStr = url += mapToQueryString(param);
            url = url + "?" + paramStr;
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, paramStr);
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    handler.sendSuccessMessage(response.body().string());
                } catch (Exception e) {
                    handler.sendFailureMessage(call.request(), e);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendFailureMessage(call.request(), e);
            }
        });
    }

    public static void getWithMy(String url, Map<String, String> param, final HttpResponseHandler handler) {

        if (!isNetworkAvailable()) {
            ToastUtil.showMessage(AppContext.getInstance(), "网络连接不可用", Toast.LENGTH_SHORT);
            handler.sendCompleteMessage();
            return;
        }

        handler.sendBeforeSendMessage();

        String data = "";
        if (param != null && param.size() > 0) {
            data = mapToQueryString(param);
            url = url + "?" + data;

        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.addHeader("key", "" + BuildConfig.APPKEY);
        String currenttime = (System.currentTimeMillis() / 1000) + "";
        requestBuilder.addHeader("timestamp", currenttime);
        String sign = Config.getSign(data, currenttime);
        requestBuilder.addHeader("sign", "" + sign);


        Request request = requestBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    handler.sendSuccessMessage(response.body().string());
                } catch (Exception e) {
                    handler.sendFailureMessage(call.request(), e);
                }

                handler.sendCompleteMessage();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendFailureMessage(call.request(), e);
                handler.sendCompleteMessage();
            }
        });
    }

    public static void postWithMy(String url, Map<String, Object> params, Map<String, String> filePaths, final HttpResponseHandler handler) {
        if (!isNetworkAvailable()) {
            Toast.makeText(AppContext.getInstance(), R.string.no_network_connection_toast, Toast.LENGTH_SHORT).show();
            handler.sendCompleteMessage();
            return;
        }
        //Toast.makeText(AppContext.getInstance(), "dsadaddd", Toast.LENGTH_SHORT).show();
        handler.sendBeforeSendMessage();

//        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//
//        if (params != null){
//            for (String key : params.keySet()) {
//                multipartBodyBuilder.addFormDataPart(key, params.get(key).toString());
//            }
//        }
//
//
//        if (filePaths != null){
//            for (Map.Entry<String, String> filePath : filePaths.entrySet()) {
//
//                MediaType type=MediaType.parse("application/octet-stream");
//                File file=new File(filePath.getValue());
//
//                LogUtil.i(filePath.getKey()+":"+filePath.getValue());
//
//                RequestBody fileBody=RequestBody.create(type,file);
//                multipartBodyBuilder.addFormDataPart(filePath.getKey(), file.getName(), fileBody);
//            }
//        }

        JSONObject json = new JSONObject();
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendCompleteMessage();
            return;
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.addHeader("key", "" + BuildConfig.APPKEY);
        String currenttime = (System.currentTimeMillis() / 1000) + "";
        requestBuilder.addHeader("timestamp", currenttime);
        String sign = Config.getSign(json.toString(), currenttime);
        requestBuilder.addHeader("sign", "" + sign);


        try {
            JSONObject jsonImgData = new JSONObject();
            if (filePaths != null) {
                if (filePaths.size() > 0) {
                    for (Map.Entry<String, String> entry : filePaths.entrySet()) {
                        JSONObject jsonImgItem = new JSONObject();
                        String filePath = entry.getValue();
                        LogUtil.e(TAG, "filePath>>==>>" + filePath);
                        jsonImgItem.put("type", filePath.substring(filePath.lastIndexOf(".")));
                        String base64ImgStr = AbFileUtil.GetBase64ImageStr(filePath);
                        LogUtil.e(TAG, "filePath>>==>>" + base64ImgStr.length());
                        jsonImgItem.put("data", base64ImgStr);
                        jsonImgData.put(entry.getKey(), jsonImgItem);
                    }
                    json.put("imgData", jsonImgData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendCompleteMessage();
            return;
        }

        String data = json.toString();

        LogUtil.i("POST DATA:" + data);


        RequestBody body = RequestBody.create(MediaType_JSON, data);

        requestBuilder.post(body);


        client.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {
                    handler.sendSuccessMessage(response.body().string());

                } catch (Exception e) {
                    handler.sendFailureMessage(call.request(), e);
                }

                handler.sendCompleteMessage();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendFailureMessage(call.request(), e);
                handler.sendCompleteMessage();
            }
        });
    }


    public static String postWithMy2(String url, Map<String, Object> params, Map<String, String> filePaths) {
        if (!isNetworkAvailable()) {
            ToastUtil.showMessage(AppContext.getInstance(), "网络连接不可用", Toast.LENGTH_SHORT);
            return "";
        }

        JSONObject json = new JSONObject();
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.addHeader("key", "" + BuildConfig.APPKEY);
        String currenttime = (System.currentTimeMillis() / 1000) + "";
        requestBuilder.addHeader("timestamp", currenttime);
        String sign = Config.getSign(json.toString(), currenttime);
        requestBuilder.addHeader("sign", "" + sign);


        try {
            JSONObject jsonImgData = new JSONObject();
            if (filePaths != null) {
                if (filePaths.size() > 0) {
                    for (Map.Entry<String, String> entry : filePaths.entrySet()) {
                        JSONObject jsonImgItem = new JSONObject();
                        String filePath = entry.getValue();
                        LogUtil.e(TAG, "filePath>>==>>" + filePath);
                        jsonImgItem.put("type", filePath.substring(filePath.lastIndexOf(".")));
                        String base64ImgStr = AbFileUtil.GetBase64ImageStr(filePath);
                        LogUtil.e(TAG, "filePath>>==>>" + base64ImgStr.length());
                        jsonImgItem.put("data", base64ImgStr);
                        jsonImgData.put(entry.getKey(), jsonImgItem);
                    }
                    json.put("imgData", jsonImgData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

        String data = json.toString();

        LogUtil.i("POST DATA:" + data);


        RequestBody body = RequestBody.create(MediaType_JSON, data);

        requestBuilder.post(body);

        String result = "";
        Response response;
        try {

            response = client.newCall(requestBuilder.build()).execute();
            result = response.body().string();
        } catch (Exception ex) {

            String x=ex.getMessage();
        }

        return result;

    }


    /**
     * 判断是否为 json
     *
     * @param responseBody
     * @return
     * @throws Exception
     */

    private static String judgeJSON(String responseBody) throws Exception {
        if (!isJsonString(responseBody)) {
            throw new Exception("server response not json string (response = " + responseBody + ")");
        }
        return responseBody;
    }

    /**
     * 判断是否为 json
     *
     * @param responseBody
     * @return
     */
    private static boolean isJsonString(String responseBody) {
        return !TextUtils.isEmpty(responseBody) && (responseBody.startsWith("{") && responseBody.endsWith("}"));
    }

    /**
     * get
     *
     * @param map
     * @return
     */
    public static String mapToQueryString(Map<String, String> map) {
        StringBuilder string = new StringBuilder();
        /*if(map.size() > 0) {
            string.append("?");
        }*/
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                string.append(entry.getKey());
                string.append("=");
                string.append(URLEncoder.encode(entry.getValue(), UTF_8));
                string.append("&");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string.toString().substring(0, string.length() - 1);
    }

}
