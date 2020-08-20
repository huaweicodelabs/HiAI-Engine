package com.example.templateocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;

import com.google.gson.GsonBuilder;
import com.huawei.hiai.pdk.resultcode.HwHiAIResultCode;
import com.huawei.hiai.vision.common.ConnectionCallback;
import com.huawei.hiai.vision.common.VisionBase;
import com.huawei.hiai.vision.common.VisionImage;
import com.huawei.hiai.vision.text.TemplateDetector;
import com.huawei.hiai.vision.visionkit.text.config.VisionCardConfiguration;
import com.huawei.hiai.vision.visionkit.text.templateocr.TemplateData;
import com.huawei.hiai.vision.visionkit.text.templateocr.TemplateText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = AppCompatActivity.class.getSimpleName();
    private Bitmap mSourceBitmap;
    private ImageView mImageView;
    private TextView mTextViewResult;
    private TemplateDetector templateDetector;
    private TemplateText templateText;
    private JSONObject mTemplateJson;
    private static int prepareReturnCode = -1;
    private static int resultCode = -1;
    private static int sTestImageIndex = 0;
    private static final String[] TEST_IMAGE_PATH = {"test-1.jpg", "test-2.jpg", "test-3.jpg"};
    private static final int IMAGE_NUM = 3;
    private static final String TEMPLATE_JSON_PATH = "template.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MainActivity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.iv);
        mTextViewResult = findViewById(R.id.result_table);
        mTextViewResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        /* Initialize with the VisionBase static class and asynchronously get the connection of the service */
        Log.d(TAG, "start to connect HiAi service");
        initHiAI();
        setTemplateJson();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                // prepare once, then run run run
                setBitmap();
                detect();
            }
        }, 500);

    }

    private void initHiAI() {




    }

    private void detect() {




        this.templateDetector = templateDetector;
        this.prepareReturnCode = prepareReturnCode;
        this.templateText = templateText;
        this.resultCode = resultCode;
        dealResult();
    }

    private void setTemplateJson() {
        // read template json
        try {
            mTemplateJson = new JSONObject(readJsonFile(TEMPLATE_JSON_PATH));
        } catch (JSONException err) {
            Log.d("trans json error", err.toString());
        }
        if (mTemplateJson == null) {
            Log.e(TAG, "mTemplateJson is null");
            return;
        }
    }

    private void setBitmap() {
        // read local test image
        try {
            mSourceBitmap = BitmapFactory.decodeStream(getApplicationContext().getAssets().open(TEST_IMAGE_PATH[sTestImageIndex % IMAGE_NUM]));
        } catch (IOException e) {
            Log.d(TAG, "read test image error");
        }
        if (mSourceBitmap == null) {
            Log.e(TAG, "image is null");
            return;
        }
        mImageView.setImageBitmap(mSourceBitmap);
        detect();

    }

    private void dealResult() {

        if (prepareReturnCode != 0) {
            Log.e(TAG, "prepare fail, error code: " + prepareReturnCode);
            mTextViewResult.setText("");
            mTextViewResult.append("prepare fail, error code: " + prepareReturnCode + "\n");
            mTextViewResult.append("see detail in: com.huawei.hiai.pdk.resultcode.HwHiAIResultCode\n");
            return;
        }

        if (resultCode != HwHiAIResultCode.AIRESULT_SUCCESS) {
            Log.e(TAG, "template detect fail");
            mTextViewResult.setText("");
            mTextViewResult.append("run fail, error code: " + resultCode + "\n");
            mTextViewResult.append("see detail in: com.huawei.hiai.pdk.resultcode.HwHiAIResultCode\n");
        } else {
            int errorCode = templateText.getErrorCode();
            Log.d(TAG, "error code is :" + errorCode);
            mTextViewResult.append("error code:" + errorCode + "\n");
            List<TemplateData> templateDataList = templateText.getTemplateData();
            if (templateDataList != null) {
                mTextViewResult.append("The key value pairs of identification results are as follows:\n");
                int index = 0;
                for (TemplateData element : templateDataList) {
                    mTextViewResult.append("recognition area" + index + "\n");
                    mTextViewResult.append("Key:" + element.getWordKey() + "\n");
                    mTextViewResult.append("Value:" + element.getWordValue() + "\n");
                    mTextViewResult.append("\n");
                    index++;
                }
            }

            JSONObject json = new JSONObject();
            try {
                json.put("resultCode", resultCode);
                json.put("common_text", new GsonBuilder().disableHtmlEscaping().create().toJson(templateText));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "json output: " + json.toString());
        }
        Log.d(TAG, "detect template end");
    }

    public void choose(View view) {
        mTextViewResult.setText("");
        sTestImageIndex++;
        setBitmap();
        detect();
    }

    /**
     * read json file，return json String
     */
    private String readJsonFile(String fileName) {
        try {
            InputStream is = getApplicationContext().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (templateDetector != null) {
            templateDetector.release();
        }
        super.onDestroy();
    }


}