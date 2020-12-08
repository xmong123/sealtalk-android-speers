package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.CircleUrl;
import com.caesar.rongcloudspeed.data.CommentsItemData;

import java.util.List;

public class CommentsListData extends BaseData {

    private List<CommentsItemData> url;

    private List<CommentsItemData> referer;

    public List<CommentsItemData> getUrl() {
        return url;
    }

    public void setUrl(List<CommentsItemData> url) {
        this.url = url;
    }

    public List<CommentsItemData> getReferer() {
        return referer;
    }

    public void setReferer(List<CommentsItemData> referer) {
        this.referer = referer;
    }
}
