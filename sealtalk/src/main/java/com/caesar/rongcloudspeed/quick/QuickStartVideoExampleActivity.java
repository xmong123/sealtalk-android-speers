package com.caesar.rongcloudspeed.quick;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.circle.ui.AddCircleTaskActivity;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.Config;
import com.caesar.rongcloudspeed.player.PLMediaPlayerActivity;
import com.caesar.rongcloudspeed.ui.BaseActivity;
import com.caesar.rongcloudspeed.ui.widget.ClearWriteEditText;
import com.caesar.rongcloudspeed.utils.FileUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.Tools;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.qiniu.android.common.ServiceAddress;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.AsyncRun;
import com.caesar.rongcloudspeed.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuickStartVideoExampleActivity extends BaseActivity {

    private static final int REQUEST_CODE = 8090;
    private QuickStartVideoExampleActivity context;
    private LinearLayout uploadStatusLayout;
    private ProgressBar uploadProgressBar;
    private TextView uploadSpeedTextView;
    private TextView uploadFileLengthTextView;
    private TextView uploadPercentageTextView;
    private TextView persistentIdTextView;
    private PLVideoView uploadResultVideoView;
    private UploadManager uploadManager;
    private long uploadLastTimePoint;
    private long uploadLastOffset;
    private long uploadFileLength;
    private String uploadFilePath;
    private String postFilePath;
    private TextView pfopResult1TextView;
    private TextView pfopResult2TextView;
    private Button loadPfopVideo1Button;
    private Button loadPfopVideo2Button;
    private ClearWriteEditText votePostEdit;
    private ClearWriteEditText excerptPostEdit;

    public QuickStartVideoExampleActivity() {
        this.context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.quick_start_video_example_activity);
        this.initLayout();
        this.uploadProgressBar = (ProgressBar) this
                .findViewById(R.id.quick_start_video_upload_progressbar);
        this.uploadProgressBar.setMax(100);
        this.uploadStatusLayout = (LinearLayout) this
                .findViewById(R.id.quick_start_video_upload_status_layout);
        this.uploadSpeedTextView = (TextView) this
                .findViewById(R.id.quick_start_video_upload_speed_textview);
        this.uploadFileLengthTextView = (TextView) this
                .findViewById(R.id.quick_start_video_upload_file_length_textview);
        this.uploadPercentageTextView = (TextView) this
                .findViewById(R.id.quick_start_video_upload_percentage_textview);
        this.uploadStatusLayout.setVisibility(LinearLayout.INVISIBLE);this.uploadProgressBar = (ProgressBar) this
                .findViewById(R.id.quick_start_video_upload_progressbar);
        this.uploadProgressBar.setMax(100);
        this.uploadStatusLayout = (LinearLayout) this
                .findViewById(R.id.quick_start_video_upload_status_layout);
        this.uploadSpeedTextView = (TextView) this
                .findViewById(R.id.quick_start_video_upload_speed_textview);
        this.uploadFileLengthTextView = (TextView) this
                .findViewById(R.id.quick_start_video_upload_file_length_textview);
        this.uploadPercentageTextView = (TextView) this
                .findViewById(R.id.quick_start_video_upload_percentage_textview);
        this.uploadStatusLayout.setVisibility(LinearLayout.INVISIBLE);
        this.uploadResultVideoView = (PLVideoView)
                this.findViewById(R.id.quick_start_video_play_pldplayer);
        this.persistentIdTextView = (TextView) this.findViewById(R.id.quick_start_video_pid_textview);
        this.pfopResult1TextView = (TextView) this.findViewById(R.id.quick_start_video1_textview);
        this.pfopResult2TextView = (TextView) this.findViewById(R.id.quick_start_video2_textview);
        this.loadPfopVideo1Button = (Button) this.findViewById(R.id.quick_start_load_video_button_1);
        this.loadPfopVideo2Button = (Button) this.findViewById(R.id.quick_start_load_video_button_2);
        this.votePostEdit = (ClearWriteEditText) this.findViewById(R.id.vote_post_title);
        this.excerptPostEdit = (ClearWriteEditText) this.findViewById(R.id.vote_post_excerpt);
    }

    private void initLayout() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.quick_start_pili_videoview_fixed_layout);
        int width = dm.widthPixels;
        int height = width * 360 / 640;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layout.setLayoutParams(layoutParams);
    }

    public void selectUploadFile(View view) {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target,
                this.getString(R.string.choose_file));
        try {
            this.startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            this.uploadFilePath = path;
                            final PLVideoView videoView = uploadResultVideoView;
                            AVOptions mAVOptions = new AVOptions();
                            mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
                            // 1 -> hw codec enable, 0 -> disable [recommended]
                            mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);
                            mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
                            mAVOptions.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR);
                            mAVOptions.setInteger(AVOptions.KEY_LOG_LEVEL, 0);
                            videoView.setAVOptions(mAVOptions);
                            videoView.setVideoURI(Uri.parse(path));
                            videoView.setOnPreparedListener(i -> videoView.start());
//                            videoView.setOnCompletionListener(() -> videoView.start());
                        } catch (Exception e) {
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.qiniu_get_upload_file_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadFileNew(View view) {
        if (this.uploadFilePath == null) {
            return;
        }
        QiniuUtils.getUploadManagerInstance();
        //已经上传过的不上传
        QiniuUtils.uploadFile(this, this.uploadFilePath, QiniuUtils.createImageKey(UserInfoUtils.getPhone(this)), new UpLoadImgCallback() {
            @Override
            public void onSuccess(String videoUrl) {
                Toast.makeText(
                        context,
                        context.getString(R.string.qiniu_upload_success),
                        Toast.LENGTH_LONG).show();
                final PLVideoView videoView = uploadResultVideoView;

                videoView.setVideoURI(Uri.parse(videoUrl));
                videoView.setOnPreparedListener(new PLOnPreparedListener() {
                    @Override
                    public void onPrepared(int i) {
                        videoView.start();
                    }
                });
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        persistentIdTextView.setText(videoUrl);
                    }
                });
                Log.d("视频上传大小提示TAG", videoUrl);
            }

            @Override
            public void onFailure() {
                Toast.makeText(
                        context,
                        context.getString(R.string.qiniu_upload_failed),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void postArticle(View view) {
        String voteTitle = votePostEdit.getText().toString().trim();
        String excerptString = excerptPostEdit.getText().toString().trim();
        if (TextUtils.isEmpty(voteTitle)) {
            showToast("投票标题不能为囧");
            votePostEdit.setShakeAnimation();
            return;
        }
        if (TextUtils.isEmpty(excerptString)) {
            excerptString = voteTitle;
        }
        if (this.postFilePath == null) {
            Toast.makeText(
                    context,
                    "请先上传视频文件再发布",
                    Toast.LENGTH_LONG).show();
        } else {
            showLoadingDialog(R.string.seal_loading_dialog_logining);
            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().addVoteArticle(
                    UserInfoUtils.getAppUserId(this), "44", "3", voteTitle, excerptString, this.postFilePath),
                    new NetworkCallback<BaseData>() {
                        @Override
                        public void onSuccess(BaseData baseData) {
                            if (NetworkResultUtils.isSuccess(baseData)) {
                                dismissLoadingDialog(() -> showToast(R.string.qiniu_upload_success));
                            } else {
                                dismissLoadingDialog(() -> showToast(R.string.qiniu_upload_failed));
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(QuickStartVideoExampleActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    public void uploadFile(View view) {
        if (this.uploadFilePath == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient httpClient = new OkHttpClient();
                Request req = new Request.Builder().url(QiniuLabConfig.makeUrl(
                        QiniuLabConfig.TESTSERVER,
                        QiniuLabConfig.QUICK_START_VIDEO_DEMO_PATH)).method("GET", null).build();

                Response resp = null;
                try {
                    resp = httpClient.newCall(req).execute();
                    JSONObject jsonObject = new JSONObject(resp.body().string());
                    String uploadToken = jsonObject.getString("url");

                    uploadData(uploadToken, QiniuLabConfig.REMOTE_SERVICE_SERVER);
                } catch (Exception e) {
                    AsyncRun.run(() -> showToast(R.string.qiniu_get_upload_token_failed));
                } finally {
                    if (resp != null) {
                        resp.body().close();
                    }
                }
            }
        }).start();
    }

    /**
     * 华南机房, http
     */
    public static final Zone zone2 =
            createZone("upload-z2.qiniu.com", "up-z2.qiniu.com", "183.60.214.197", "14.152.37.7");

    private static Zone createZone(String upHost, String upHostBackup, String upIp, String upIp2) {
        String[] upIps = new String[]{upIp, upIp2};
        ServiceAddress up = new ServiceAddress("http://" + upHost, upIps);
        ServiceAddress upBackup = new ServiceAddress("http://" + upHostBackup, upIps);
        return new Zone(up, upBackup);
    }

    private void uploadData(final String uploadToken, final String domain) {
        if (this.uploadManager == null) {
            Configuration config = new Configuration.Builder()
                    .chunkSize(1024 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
                    //.recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                    //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                    .zone(zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
            this.uploadManager = new UploadManager(config);
        }

        File uploadFile = new File(this.uploadFilePath);
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        updateStatus(percent);
                    }
                }, null);
        final long startTime = System.currentTimeMillis();
        final long fileLength = uploadFile.length();
        this.uploadFileLength = fileLength;
        this.uploadLastTimePoint = startTime;
        this.uploadLastOffset = 0;
        // prepare status
        AsyncRun.run(new Runnable() {
            @Override
            public void run() {
                //clear old status
                loadPfopVideo1Button.setVisibility(View.INVISIBLE);
                loadPfopVideo2Button.setVisibility(View.INVISIBLE);
                persistentIdTextView.setText("");

                uploadPercentageTextView.setText("0 %");
                uploadSpeedTextView.setText("0 KB/s");
                uploadFileLengthTextView.setText(Tools.formatSize(fileLength));
                uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        this.uploadManager.put(uploadFile, null, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {
                        // reset status
                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                uploadStatusLayout
                                        .setVisibility(LinearLayout.INVISIBLE);
                                uploadProgressBar.setProgress(0);
                            }
                        });

                        long lastMillis = System.currentTimeMillis()
                                - startTime;
                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                final String persistentId = jsonData.getString("hash");

                                AsyncRun.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        persistentIdTextView.setText(persistentId);
                                    }
                                });
                                final String videoUrl = domain + "/" + fileKey;
                                postFilePath = videoUrl;
//                                final PLVideoView videoView = uploadResultVideoView;
//
//                                videoView.setVideoURI(Uri.parse(videoUrl));
//                                videoView.setOnPreparedListener(new PLOnPreparedListener() {
//                                    @Override
//                                    public void onPrepared(int i) {
//                                        videoView.start();
//                                    }
//                                });
//                                Intent intent = new Intent(QuickStartVideoExampleActivity.this, PLMediaPlayerActivity.class);
//                                intent.putExtra("videoPath", uploadFilePath);
//                                intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
//                                intent.putExtra("liveStreaming", 0);
//                                intent.putExtra("cache", true);
//                                intent.putExtra("loop", false);
//                                intent.putExtra("video-data-callback", false);
//                                intent.putExtra("audio-data-callback", false);
//                                intent.putExtra("disable-log", false);
//                                startActivity(intent);
                            } catch (JSONException e) {
                                Toast.makeText(
                                        context,
                                        context.getString(R.string.qiniu_upload_file_response_parse_error),
                                        Toast.LENGTH_LONG).show();
                                Log.e(QiniuLabConfig.LOG_TAG, e.getMessage());
                            }
                        } else {
                            Toast.makeText(
                                    context,
                                    context.getString(R.string.qiniu_upload_file_failed),
                                    Toast.LENGTH_LONG).show();
                            Log.e(QiniuLabConfig.LOG_TAG, respInfo.toString());
                        }
                    }

                }, uploadOptions);
    }

    private void updateStatus(final double percentage) {
        long now = System.currentTimeMillis();
        long deltaTime = now - uploadLastTimePoint;
        long currentOffset = (long) (percentage * uploadFileLength);
        long deltaSize = currentOffset - uploadLastOffset;
        if (deltaTime <= 100) {
            return;
        }

        final String speed = Tools.formatSpeed(deltaSize, deltaTime);
        // update
        uploadLastTimePoint = now;
        uploadLastOffset = currentOffset;

        AsyncRun.run(new Runnable() {
            @Override
            public void run() {
                int progress = (int) (percentage * 100);
                uploadProgressBar.setProgress(progress);
                uploadPercentageTextView.setText(progress + " %");
                uploadSpeedTextView.setText(speed);
            }
        });
    }

    public void queryPfopResultButton(View view) {
        Log.d("QiniuLab", "query button clicked");
        final String persistentId = this.persistentIdTextView.getText().toString();
        if (persistentId.isEmpty()) {
            return;
        }

        final OkHttpClient httpClient = new OkHttpClient();
        final Request req = new Request.Builder().url(String.format("%s?persistentId=%s", QiniuLabConfig.makeUrl(
                QiniuLabConfig.REMOTE_SERVICE_SERVER,
                QiniuLabConfig.QUERY_PFOP_RESULT_PATH), persistentId)).method("GET", null).build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response resp = httpClient.newCall(req).execute();
                    JSONObject jsonObject = new JSONObject(resp.body().string());
                    final JSONArray keys = jsonObject.getJSONArray("keys");
                    final String videoDomain = jsonObject.getString("domain");
                    int length = keys.length();
                    if (length == 2) {
                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url1 = videoDomain + "/" + keys.getString(0);
                                    String url2 = videoDomain + "/" + keys.getString(1);
                                    pfopResult1TextView.setText(url1);
                                    pfopResult2TextView.setText(url2);
                                    loadPfopVideo1Button.setVisibility(View.VISIBLE);
                                    loadPfopVideo2Button.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    Log.e("QiniuLab", "get key from keys error");
                                }

                            }
                        });
                    } else if (length == 1) {
                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url1 = videoDomain + "/" + keys.getString(0);
                                    pfopResult1TextView.setText(url1);
                                    loadPfopVideo1Button.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {
                                    Log.e("QiniuLab", "get key from keys error");
                                }

                            }
                        });
                    } else {
                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "no results", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AsyncRun.run(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "pfop query failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }


    public void loadVideo(View view) {
        switch (view.getId()) {
            case R.id.quick_start_load_video_button_1:
                String url1 = this.pfopResult1TextView.getText().toString();
                loadVideoByUrl(url1);
                break;
            case R.id.quick_start_load_video_button_2:
                String url2 = this.pfopResult2TextView.getText().toString();
                loadVideoByUrl(url2);
                break;
        }
    }

    public void loadVideoByUrl(String url) {
        final PLVideoView videoView = uploadResultVideoView;
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                videoView.start();
            }
        });
    }
}
