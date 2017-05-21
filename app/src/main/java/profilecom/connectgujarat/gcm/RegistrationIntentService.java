package profilecom.connectgujarat.gcm;

/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationIntentService extends IntentService {

  private static final String TAG = "RegIntentService";
  private static final String[] TOPICS = { "global" };

  public RegistrationIntentService() {
    super(TAG);
  }

  public void SP(String key, String value) {
    SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(key, value);
    editor.apply();
  }

  public String getSP(String key) {
    SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
    return sharedpreferences.getString(key, "");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    final SharedPreferences sharedpreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
    final String fcmToken = sharedpreferences.getString("fcm_token", null);
    final boolean fcmRegistered = sharedpreferences.getBoolean("fcm_registered", false);

    if (!fcmRegistered && !TextUtils.isEmpty(fcmToken)) {

      RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("token", fcmToken)
        .addFormDataPart("os", "Android")
        .build();

      final Request request = new Request.Builder().url("http://connectgujarat.com/pnfw/register/")
        .method("POST", RequestBody.create(null, new byte[0]))
        .post(requestBody)
        .build();

      new OkHttpClient().newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
          Log.d("Background service", response.body().toString());
          if (response.body() != null && response.code() == 200 && !TextUtils.isEmpty(response.message())) {
            sharedpreferences.edit().putBoolean("fcm_registered", true).apply();
          }
        }
      });
    }
  }
}