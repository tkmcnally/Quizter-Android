package com.tkmcnally.quizter.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by missionary on 12/25/2013.
 */
public class WebServiceCallerImpl extends AsyncTask<Object, Boolean, String> {

    private WebServiceCaller callingActivity;
    private ProgressDialog dialog;

    private int STATUS_CODE = 0;

    public WebServiceCallerImpl(Activity activity, String loadingMessage) {
        this.dialog = new ProgressDialog(activity);
        this.dialog.setCancelable(false);
        this.dialog.setMessage(loadingMessage);
        if (!this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog.cancel();

            this.dialog.show();
        }
    }

    public String sendWaitJson(HashMap<String, Object> jsonFields) throws Exception {

        //Map all my Map<String,String> attributes to JSON fields.
        JsonObject jsonObject = mapFields(jsonFields);

        //Retrieve outgoing url path (I added it here from another class ie-quizter.tkmcnally.com/login or /register)
        String API_PATH = jsonObject.get("urlPath").getAsString();
        jsonObject.remove("urlPath");

        //Log.d("quizter", jsonObject.toString());

        //Create HTTP request
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(API_PATH);

        //Set header information for HTTP request.
        StringEntity holder = new StringEntity(jsonObject.toString());
        httpPost.setEntity(holder);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        //Execute post and wait for response.
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();

        int status_code = response.getStatusLine().getStatusCode();
        String result = null;
        if (status_code == 200) {
            //Read incoming JSON into String.
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            result = sb.toString();
            result = result.replace(":\"[", ": [");
            result = result.replace("]\"", "]");
            result = result.replace("\\\"", "\"");

            inputStream.close();
        }
        return result;
    }

    @Override
    protected String doInBackground(Object... objects) {
        HashMap<String, Object> jsonFields = (HashMap<String, Object>) objects[0];
        callingActivity = (WebServiceCaller) objects[1];

        String returnObject = null;
        try {
            returnObject = sendWaitJson(jsonFields);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnObject;
    }

    public JsonObject mapFields(HashMap<String, Object> jsonFields) throws JSONException {

        JsonObject jsonObject = new JsonObject();

        for (String key : jsonFields.keySet()) {
            jsonObject.addProperty(key, (String) jsonFields.get(key));
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            if (response != null) {
                callingActivity.onPostWebServiceCall(response);
            } else {
                if (STATUS_CODE == HttpStatus.SC_UNAUTHORIZED) {
                    callingActivity.handleUnauthorizedError();
                } else {
                    callingActivity.handleExceptionError();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dialog.dismiss();
        super.onPostExecute(response);
    }
}
