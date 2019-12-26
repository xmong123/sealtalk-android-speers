package com.caesar.rongcloudspeed.extend;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.R.dimen;
import io.rong.imkit.R.drawable;
import io.rong.imkit.R.id;
import io.rong.imkit.R.layout;
import io.rong.imkit.R.string;
import io.rong.imkit.manager.SendMediaManager;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imkit.utilities.OptionsPopupDialog.OnOptionsItemClickedListener;
import io.rong.imkit.utils.RongOperationPermissionUtils;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.CircleProgressView;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.IRongCallback.ISendMediaMessageCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.message.SightMessage;
import io.rong.message.utils.BitmapUtil;
import java.io.File;

@ProviderTag(
        messageContent = SightMessage.class,
        showProgress = false,
        showReadState = true
)
public class CaesarSightMessageItemProvider extends MessageProvider<SightMessage> {
    private static final String TAG = "Sight-CaesarSightMessageItemProvider";

    public CaesarSightMessageItemProvider() {
    }

    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(layout.rc_item_sight_message, (ViewGroup)null);
        CaesarSightMessageItemProvider.ViewHolder holder = new CaesarSightMessageItemProvider.ViewHolder();
        holder.operationButton = (RelativeLayout)view.findViewById(id.rc_sight_operation);
        holder.operationIcon = (ImageView)view.findViewById(id.rc_sight_operation_icon);
        holder.message = (FrameLayout)view.findViewById(id.rc_message);
        holder.compressProgress = (ProgressBar)view.findViewById(id.compressVideoBar);
        holder.loadingProgress = (CircleProgressView)view.findViewById(id.rc_sight_progress);
        holder.thumbImg = (AsyncImageView)view.findViewById(id.rc_sight_thumb);
        holder.tagImg = (ImageView)view.findViewById(id.rc_sight_tag);
        holder.duration = (TextView)view.findViewById(id.rc_sight_duration);
        view.setTag(holder);
        return view;
    }

    public void onItemClick(View view, int position, SightMessage content, UIMessage uiMessage) {
        if (content != null) {
            if (!RongOperationPermissionUtils.isMediaOperationPermit(view.getContext())) {
                return;
            }

            String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
            if (!PermissionCheckUtil.checkPermissions(view.getContext(), permissions)) {
                Activity activity = (Activity)view.getContext();
                PermissionCheckUtil.requestPermissions(activity, permissions, 100);
                return;
            }

            Builder builder = new Builder();
            builder.scheme("rong").authority(view.getContext().getPackageName()).appendPath("sight").appendPath("player");
            String intentUrl = builder.build().toString();
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(intentUrl));
            intent.setPackage(view.getContext().getPackageName());
            intent.putExtra("SightMessage", content);
            intent.putExtra("Message", uiMessage.getMessage());
            intent.putExtra("Progress", uiMessage.getProgress());
            if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                view.getContext().startActivity(intent);
            } else {
                Toast.makeText(view.getContext(), "Sight Module does not exist.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void bindView(View v, int position, SightMessage content, UIMessage message) {
        CaesarSightMessageItemProvider.ViewHolder holder = (CaesarSightMessageItemProvider.ViewHolder)v.getTag();
        int progress;
        if (message.getMessageDirection() == MessageDirection.SEND) {
            holder.message.setBackgroundResource(drawable.rc_ic_bubble_no_right);
            progress = (int)v.getContext().getResources().getDimension(dimen.rc_dimen_size_12);
            holder.duration.setPadding(0, 0, progress, 0);
        } else {
            holder.message.setBackgroundResource(drawable.rc_ic_bubble_no_left);
            progress = (int)v.getContext().getResources().getDimension(dimen.rc_dimen_size_6);
            holder.duration.setPadding(0, 0, progress, 0);
        }

        progress = message.getProgress();
        SentStatus status = message.getSentStatus();
        if (content.isDestruct()) {
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(content.getThumbUri().toString());
            if (bitmap != null) {
                Bitmap blurryBitmap = BitmapUtil.getBlurryBitmap(v.getContext(), bitmap, 5.0F, 0.25F);
                holder.thumbImg.setBitmap(blurryBitmap);
            }
        } else {
            holder.thumbImg.setResource(content.getThumbUri());
        }

        holder.duration.setText(this.getSightDuration(content.getDuration()));
        if (progress > 0 && progress < 100) {
            holder.loadingProgress.setProgress(progress, true);
            holder.tagImg.setVisibility(View.GONE);
            holder.loadingProgress.setVisibility(View.VISIBLE);
            holder.compressProgress.setVisibility(View.GONE);
        } else if (status.equals(SentStatus.SENDING)) {
            holder.tagImg.setVisibility(View.GONE);
            holder.loadingProgress.setVisibility(View.GONE);
            holder.compressProgress.setVisibility(View.VISIBLE);
        } else {
            holder.tagImg.setVisibility(View.VISIBLE);
            holder.loadingProgress.setVisibility(View.GONE);
            holder.compressProgress.setVisibility(View.GONE);
        }

    }

    public Spannable getContentSummary(SightMessage data) {
        return null;
    }

    public Spannable getContentSummary(Context context, SightMessage data) {
        return new SpannableString(context.getResources().getString(string.rc_message_content_sight));
    }

    public void onItemLongClick(View view, int position, SightMessage content, final UIMessage message) {
        if (message.getMessage().getSentStatus().getValue() < SentStatus.SENT.getValue()) {
            String[] items = new String[]{view.getContext().getResources().getString(string.rc_dialog_item_message_delete)};
            OptionsPopupDialog.newInstance(view.getContext(), items).setOptionsPopupDialogListener(new OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if (which == 0) {
                        SendMediaManager.getInstance().cancelSendingMedia(message.getConversationType(), message.getTargetId(), message.getMessageId());
                        RongIM.getInstance().cancelSendMediaMessage(message.getMessage(), (OperationCallback)null);
                        RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, (ResultCallback)null);
                    }

                }
            }).show();
        } else {
            super.onItemLongClick(view, position, content, message);
        }

    }

    private boolean isSightDownloaded(SightMessage sightMessage) {
        if (sightMessage.getLocalPath() != null && !TextUtils.isEmpty(sightMessage.getLocalPath().toString())) {
            String path = sightMessage.getLocalPath().toString();
            if (path.startsWith("file://")) {
                path = path.substring(7);
            }

            File file = new File(path);
            return file.exists();
        } else {
            return false;
        }
    }

    private void handleSendingView(final UIMessage message, final CaesarSightMessageItemProvider.ViewHolder holder) {
        final SentStatus status = message.getSentStatus();
        if (status.equals(SentStatus.SENDING)) {
            holder.operationButton.setVisibility(View.VISIBLE);
            holder.operationIcon.setImageResource(drawable.rc_file_icon_cancel);
        } else if (status.equals(SentStatus.CANCELED)) {
            holder.operationButton.setVisibility(View.VISIBLE);
            holder.operationIcon.setImageResource(drawable.rc_ic_warning);
        } else {
            holder.operationButton.setVisibility(View.GONE);
        }

        holder.operationButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (status.equals(SentStatus.SENDING)) {
                    RongIM.getInstance().cancelSendMediaMessage(message.getMessage(), new OperationCallback() {
                        public void onSuccess() {
                            holder.operationButton.setVisibility(View.VISIBLE);
                            holder.operationIcon.setImageResource(drawable.rc_ic_warning);
                            holder.tagImg.setVisibility(View.VISIBLE);
                            holder.loadingProgress.setVisibility(View.GONE);
                            holder.compressProgress.setVisibility(View.GONE);
                        }

                        public void onError(ErrorCode errorCode) {
                        }
                    });
                } else if (message.getSentStatus().equals(SentStatus.CANCELED)) {
                    RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, new ResultCallback<Boolean>() {
                        public void onSuccess(Boolean aBoolean) {
                            if (aBoolean) {
                                message.getMessage().setMessageId(View.VISIBLE);
                                RongIM.getInstance().sendMediaMessage(message.getMessage(), (String)null, (String)null, (ISendMediaMessageCallback)null);
                            }

                        }

                        public void onError(ErrorCode e) {
                        }
                    });
                }

            }
        });
    }

    private String getSightDuration(int time) {
        if (time <= 0) {
            return "00:00";
        } else {
            int minute = time / 60;
            String recordTime;
            int second;
            if (minute < 60) {
                second = time % 60;
                recordTime = this.unitFormat(minute) + ":" + this.unitFormat(second);
            } else {
                int hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }

                minute %= 60;
                second = time - hour * 3600 - minute * 60;
                recordTime = this.unitFormat(hour) + ":" + this.unitFormat(minute) + ":" + this.unitFormat(second);
            }

            return recordTime;
        }
    }

    private String unitFormat(int time) {
        String formatTime;
        if (time >= 0 && time < 10) {
            formatTime = "0" + Integer.toString(time);
        } else {
            formatTime = "" + time;
        }

        return formatTime;
    }

    private static class ViewHolder {
        RelativeLayout operationButton;
        ImageView operationIcon;
        FrameLayout message;
        AsyncImageView thumbImg;
        ImageView tagImg;
        ProgressBar compressProgress;
        CircleProgressView loadingProgress;
        TextView duration;

        private ViewHolder() {
        }
    }
}

