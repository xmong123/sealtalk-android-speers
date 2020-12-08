package io.rong.contactcard.message;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import io.rong.contactcard.ISeekCardClickListener;
import io.rong.contactcard.R;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by Beyond on 2016/12/5.
 */

@ProviderTag(messageContent = ContactSeekInfo.class, showProgress = false, showReadState = true)
public class ContactSeekItemProvider extends IContainerItemProvider.MessageProvider<ContactSeekInfo> {
    private final static String TAG = "ContactSeekInfoItemProvider";
    public static String THINKCMF_PATH = "http://qiniu.500-china.com/";
    private ISeekCardClickListener iSeekCardClickListener;

    public ContactSeekItemProvider(ISeekCardClickListener iSeekCardClickListener) {
        this.iSeekCardClickListener = iSeekCardClickListener;
    }

    private static class ViewHolder {
        TextView mTitle;
        AsyncImageView mImage;
        TextView mExpert;
        LinearLayout mLayout;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.rc_message_contact_seekinfo, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.mTitle = (TextView) view.findViewById(R.id.seek_info_title);
        viewHolder.mImage = (AsyncImageView) view.findViewById(R.id.seek_info_image);
        viewHolder.mExpert = (TextView) view.findViewById(R.id.seek_info_name);
        viewHolder.mLayout = (LinearLayout) view.findViewById(R.id.rc_layout);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View v, int position, final ContactSeekInfo content, final UIMessage message) {
        final ViewHolder viewHolder = (ViewHolder) v.getTag();

        if (!TextUtils.isEmpty(content.getSeek_title())) {
            viewHolder.mTitle.setText(content.getSeek_title());
        }
        String photos_urls = content.getPhotos_urls();
        if (!TextUtils.isEmpty(photos_urls) && photos_urls.length() > 32) {
            try {
                JSONArray imageArray = new JSONArray(photos_urls);
                if (imageArray.length() > 0) {
                    ArrayList<String> viewpageDatas = new ArrayList<>();
                    String str = imageArray.getString(0);
                    if (!str.startsWith("http://")) {
                        str = THINKCMF_PATH + str;
                    }
                    viewHolder.mImage.setAvatar(str, R.drawable.rc_ad_list_key_icon);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (!TextUtils.isEmpty(content.getSeek_expert())) {
            viewHolder.mExpert.setText(content.getSeek_expert());
        }

        if (message.getMessageDirection() == Message.MessageDirection.RECEIVE)
            viewHolder.mLayout.setBackgroundResource(R.drawable.rc_ic_bubble_left_file);
        else
            viewHolder.mLayout.setBackgroundResource(R.drawable.rc_ic_bubble_right_file);
    }

    @Override
    public Spannable getContentSummary(final ContactSeekInfo ContactSeekInfo) {
        return null;
    }

    @Override
    public Spannable getContentSummary(Context context, final ContactSeekInfo ContactSeekInfo) {
        return new SpannableString("[" + context.getResources().getString(R.string.rc_plugins_seekinfo) + "]");
    }

    @Override
    public void onItemClick(View view, int position, ContactSeekInfo content, UIMessage message) {
        if (iSeekCardClickListener != null) {
            iSeekCardClickListener.onSeekCardClick(view, content);
        }
    }
}
