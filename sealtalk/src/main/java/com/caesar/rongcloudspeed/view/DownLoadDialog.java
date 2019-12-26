package com.caesar.rongcloudspeed.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.config.DownloadKey;
import com.caesar.rongcloudspeed.module.Download;

/**
 * Created by hugeterry(http://hugeterry.cn)
 */

public class DownLoadDialog extends Activity {
    private ImageView close;
    public ProgressBar progressBar;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_dialog);

        close = (ImageView) findViewById(R.id.downloaddialog_close);
        progressBar = (ProgressBar) findViewById(R.id.downloaddialog_progress);
        textView = (TextView) findViewById(R.id.downloaddialog_count);

        if (DownloadKey.interceptFlag) DownloadKey.interceptFlag = false;
        new Download(this).start();

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DownloadKey.TOShowDownloadView = 1;
                DownloadKey.interceptFlag = true;
                if (DownloadKey.ISManual) {
                    DownloadKey.LoadManual = false;
                }
                finish();
            }
        });

    }


}
