package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class UserFindOtherAdapter extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Fragment fragment;
    private int type;

    public UserFindOtherAdapter(Fragment fragment, List data, int type) {
        super(type == 1 ? R.layout.adapter_circle_search_item : R.layout.fragment_search_recyclerview_item, data);
        this.fragment = fragment;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        String titleString = bean.getPost_title();
        if(type==1){
            String postDate = bean.getPost_date();
            String avatarString = bean.getAvatar();
            String user_nicename = bean.getUser_nicename();
            String photos_urls = bean.getPhotos_urls();
            if (!avatarString.startsWith( "http://" )) {
                avatarString = Constant.THINKCMF_PATH + avatarString;
            }
            String strm = postDate.substring(0,10);   //截掉
            Glide.with( fragment ).load( avatarString ).into( (ImageView) helper.getView( R.id.search_headIV ) );
            helper.setText(R.id.search_nameTV, user_nicename);
            helper.setText(R.id.search_timeTV, postDate);
            helper.setText(R.id.search_titleTV, titleString);
            if (!TextUtils.isEmpty(photos_urls) && photos_urls.length() > 32) {
                try {
                    JSONArray imageArray = new JSONArray(photos_urls);
                    if (imageArray != null && imageArray.length() > 0) {
                        if(imageArray.length()>2){
                            String imageString=imageArray.getString(0);
                            String imageString1=imageArray.getString(1);
                            String imageString2=imageArray.getString(2);
                            Glide.with( fragment ).load( imageString ).into( (ImageView) helper.getView( R.id.search_bodyIV ) );
                            Glide.with( fragment ).load( imageString1 ).into( (ImageView) helper.getView( R.id.search_bodyIV1 ) );
                            Glide.with( fragment ).load( imageString2 ).into( (ImageView) helper.getView( R.id.search_bodyIV2 ) );
                            helper.setVisible(R.id.search_bodyIV,true);
                            helper.setVisible(R.id.search_bodyIV1,true);
                            helper.setVisible(R.id.search_bodyIV2,true);
                        }else if(imageArray.length()==2){
                            String imageString=imageArray.getString(0);
                            String imageString1=imageArray.getString(1);
                            Glide.with( fragment ).load( imageString ).into( (ImageView) helper.getView( R.id.search_bodyIV ) );
                            Glide.with( fragment ).load( imageString1 ).into( (ImageView) helper.getView( R.id.search_bodyIV1 ) );
                            helper.setVisible(R.id.search_bodyIV,true);
                            helper.setVisible(R.id.search_bodyIV1,true);
                            helper.setVisible(R.id.search_bodyIV2,false);
                        }else if(imageArray.length()==1){
                            String imageString=imageArray.getString(0);
                            Glide.with( fragment ).load( imageString ).into( (ImageView) helper.getView( R.id.search_bodyIV ) );
                            helper.setVisible(R.id.search_bodyIV,true);
                            helper.setVisible(R.id.search_bodyIV1,false);
                            helper.setVisible(R.id.search_bodyIV2,false);
                        }else{
                            helper.setGone(R.id.search_bodyIV,false);
                            helper.setGone(R.id.search_bodyIV1,false);
                            helper.setGone(R.id.search_bodyIV2,false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }
        }else{
            String tagString;
            switch (type) {
                case 0:
                    tagString = "同行课程";
                    break;
                case 1:
                    tagString = "同行圈";
                    break;
                case 2:
                    tagString = "同行招聘";
                    break;
                case 3:
                    tagString = "同行求助";
                    break;
                default:
                    tagString = "同行课程";
                    break;
            }
            helper.setText(R.id.searchTag, "【" + tagString + "】");
            helper.setText(R.id.searchName, titleString);
        }

    }
}
