package com.caesar.rongcloudspeed.utils.qrcode.barcodescanner.camera;

import com.caesar.rongcloudspeed.utils.qrcode.barcodescanner.SourceData;

/**
 * Callback for camera previews.
 */
public interface PreviewCallback {
    void onPreview(SourceData sourceData);
    void onPreviewError(Exception e);
}
