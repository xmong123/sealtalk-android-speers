package com.caesar.rongcloudspeed.circle.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caesar.rongcloudspeed.circle.ui.FriendCircle1Activity;
import com.caesar.rongcloudspeed.circle.ui.FriendCircleActivity;
import com.caesar.rongcloudspeed.data.UserInfo;
import com.caesar.rongcloudspeed.data.result.CircleItemResult;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yiw.circledemo.R;
import com.caesar.rongcloudspeed.circle.adapter.CommentAdapter.ICommentItemClickListener;
import com.yiw.circledemo.bean.ActionItem;
import com.yiw.circledemo.bean.CircleItem;
import com.yiw.circledemo.bean.CommentItem;
import com.yiw.circledemo.bean.FavoriteItem;
import com.yiw.circledemo.bean.User;
import com.caesar.rongcloudspeed.circle.contral.CirclePublicCommentController;
import com.yiw.circledemo.mvp.presenter.CirclePresenter;
import com.yiw.circledemo.mvp.view.ICircleViewUpdate;
import com.yiw.circledemo.spannable.ISpanClick;
import com.yiw.circledemo.utils.DatasUtil;
import com.caesar.rongcloudspeed.circle.widgets.AppNoScrollerListView;
import com.caesar.rongcloudspeed.circle.widgets.CircularImage;
import com.caesar.rongcloudspeed.circle.widgets.FavoriteListView;
import com.caesar.rongcloudspeed.circle.widgets.MultiImageView;
import com.caesar.rongcloudspeed.circle.widgets.SnsPopupWindow;
import com.caesar.rongcloudspeed.circle.widgets.dialog.CommentDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiw
 * @ClassName: CircleAdapter
 * @Description: 圈子列表的adapter
 * @date 2015-12-28 上午09:37:23
 */
public class CircleAdapter extends BaseAdapter implements ICircleViewUpdate {
    private static final int ITEM_VIEW_TYPE_DEFAULT = 0;
    private static final int ITEM_VIEW_TYPE_URL = 1;
    private static final int ITEM_VIEW_TYPE_IMAGE = 2;
    String userid= "1";

    private static final String ITEM_TYPE_URL = "1";
    private static final String ITEM_TYPE_IMAGE = "2";
    private static final int ITEM_VIEW_TYPE_COUNT = 3;

    private Context mContext;
    private CirclePresenter mPresenter;
    private CirclePublicCommentController mCirclePublicCommentController;
    private List<CircleItem> datas = new ArrayList<CircleItem>();
    private MultiImageView.OnItemClickListener imageListener = null;

    public void setCirclePublicCommentController(
            CirclePublicCommentController mCirclePublicCommentController) {
        this.mCirclePublicCommentController = mCirclePublicCommentController;
    }

    public List<CircleItem> getDatas() {
        return datas;
    }

    public void setDatas(List<CircleItem> datas) {
        if (datas != null) {
            this.datas = datas;
        }
    }

    public CircleAdapter(Context context) {
        mContext = context;
        userid= UserInfoUtils.getAppUserId(mContext);
        mPresenter = new CirclePresenter(this);
    }

    @Override
    public int getItemViewType(int position) {
//        int itemType = ITEM_VIEW_TYPE_IMAGE;
//        CircleItem item = datas.get(position);
//        if (ITEM_TYPE_URL.equals(item.getType())) {
//            itemType = ITEM_VIEW_TYPE_URL;
//        } else if (ITEM_TYPE_IMAGE.equals(item.getType())) {
//            itemType = ITEM_VIEW_TYPE_IMAGE;
//        } else {
//            itemType = ITEM_VIEW_TYPE_DEFAULT;
//        }
        int itemType = ITEM_VIEW_TYPE_IMAGE;
        return itemType;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.adapter_circle_item, null);
            ViewStub linkOrImgViewStub = (ViewStub) convertView.findViewById(R.id.linkOrImgViewStub);
            switch (itemViewType) {
                case ITEM_VIEW_TYPE_URL:// 链接view
                    linkOrImgViewStub.setLayoutResource(R.layout.viewstub_urlbody);
                    linkOrImgViewStub.inflate();
                    LinearLayout urlBodyView = (LinearLayout) convertView.findViewById(R.id.urlBody);
                    if (urlBodyView != null) {
                        holder.urlBody = urlBodyView;
                        holder.urlImageIv = (ImageView) convertView.findViewById(R.id.urlImageIv);
                        holder.urlContentTv = (TextView) convertView.findViewById(R.id.urlContentTv);
                    }
                    break;
                case ITEM_VIEW_TYPE_IMAGE:// 图片view
                    linkOrImgViewStub.setLayoutResource(R.layout.viewstub_imgbody);
                    linkOrImgViewStub.inflate();
                    MultiImageView multiImageView = (MultiImageView) convertView.findViewById(R.id.multiImagView);
                    if (multiImageView != null) {
                        holder.multiImageView = multiImageView;
                    }
                    break;
                default:
                    break;
            }
            holder.headIv = (CircularImage) convertView.findViewById(R.id.headIv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
            holder.digLine = convertView.findViewById(R.id.lin_dig);

            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            holder.urlTipTv = (TextView) convertView.findViewById(R.id.urlTipTv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
            holder.deleteBtn = (TextView) convertView.findViewById(R.id.deleteBtn);
            holder.snsBtn = (ImageView) convertView.findViewById(R.id.snsBtn);
            holder.favortListTv = (FavoriteListView) convertView.findViewById(R.id.favortListTv);

            holder.digCommentBody = (LinearLayout) convertView.findViewById(R.id.digCommentBody);

            holder.commentList = (AppNoScrollerListView) convertView.findViewById(R.id.commentList);

            holder.bbsAdapter = new CommentAdapter(mContext);
            holder.favoriteListAdapter = new FavoriteListAdapter();

            holder.favortListTv.setAdapter(holder.favoriteListAdapter);
            holder.commentList.setAdapter(holder.bbsAdapter);

            holder.snsPopupWindow = new SnsPopupWindow(mContext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CircleItem circleItem = datas.get(position);
        final String circleId = circleItem.getObject_id();
        String name = circleItem.getUser_nicename();
        String headImg = circleItem.getAvatar();
        String content = circleItem.getPost_title();
        String authorID = circleItem.getPost_author();
        String timeString = circleItem.getPost_date();
        final List<FavoriteItem> favortDatas = circleItem.getPost_likes();
        final List<CommentItem> commentsDatas = circleItem.getLast_comments();
        boolean hasFavort = favortDatas.size() != 0 ? true : false;
        boolean hasComment = commentsDatas.size() != 0 ? true : false;
        ImageLoader.getInstance().displayImage(headImg, holder.headIv);
        holder.nameTv.setText(name);
        holder.timeTv.setText(timeString);
        holder.contentTv.setText(content);
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

        if (authorID.equals(userid)) {
            Log.d("authorID:",authorID+",userid,:"+userid);
            holder.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            holder.deleteBtn.setVisibility(View.GONE);
        }
        holder.deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                mPresenter.deleteCircle(circleId);
            }
        });
        holder.headIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                Intent intent=new Intent(mContext, FriendCircle1Activity.class);
                intent.putExtra("userid",authorID);
                mContext.startActivity(intent);
            }
        });

        if (hasFavort || hasComment) {
            if (hasFavort) {//处理点赞列表
                holder.favortListTv.setSpanClickListener(new ISpanClick() {
                    @Override
                    public void onClick(int position) {
                        String userName = favortDatas.get(position).getFull_name();
                        String userId = favortDatas.get(position).getUser();
                        Toast.makeText(mContext, userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.favoriteListAdapter.setDatas(favortDatas);
                holder.favoriteListAdapter.notifyDataSetChanged();
                holder.favortListTv.setVisibility(View.VISIBLE);
            } else {
                holder.favortListTv.setVisibility(View.GONE);
            }
            if (hasComment) {//处理评论列表
                holder.bbsAdapter.setDatasource(commentsDatas);
                holder.bbsAdapter.setCommentClickListener(new ICommentItemClickListener() {
                    @Override
                    public void onItemClick(int commentPosition) {

                        CommentItem commentItem = commentsDatas.get(commentPosition);
//                        if (DatasUtil.curUser.getId().equals(commentItem.getUser().getId())) {//复制或者删除自己的评论
//
//                            CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position);
//                            dialog.show();
//                        } else {//回复别人的评论
//
//                            if (mCirclePublicCommentController != null) {
//                                mCirclePublicCommentController.editTextBodyVisible(View.VISIBLE, mPresenter, position, TYPE_REPLY_COMMENT, commentItem.getUser(), commentPosition);
//                            }
//                        }
                        if (mCirclePublicCommentController != null) {
                            mCirclePublicCommentController.editTextBodyVisible(View.VISIBLE, mPresenter, position, TYPE_REPLY_COMMENT, commentItem.getUid(), commentPosition);
                        }
                    }
                });
                holder.bbsAdapter.notifyDataSetChanged();
                holder.commentList.setVisibility(View.VISIBLE);
                holder.commentList.setOnItemClickListener(null);
                holder.commentList.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View view, final int commentPosition, long id) {

                        //长按进行复制或者删除
                        CommentItem commentItem = commentsDatas.get(commentPosition);
                        CommentDialog dialog = new CommentDialog(mContext, mPresenter, commentItem, position);
                        dialog.show();
                        return true;
                    }
                });
            } else {

                holder.commentList.setVisibility(View.GONE);
            }
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }

        holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

        final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        //判断是否已点赞
        String curUserFavortId = "";
        if (circleItem.getPost_likes().size() == 0) {
            snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
        } else {
            boolean isSupport = false;
            for (FavoriteItem favoriteItem : favortDatas) {
                if (favoriteItem.getFull_name().equals(UserInfoUtils.getNickName(mContext))) {
                    isSupport = true;
                }
            }
            if (isSupport) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }
        }

        snsPopupWindow.update();
        snsPopupWindow.setItemClickListener(new PopupItemClickListener(position, circleItem, UserInfoUtils.getNickName(mContext)));
        holder.snsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });

        holder.urlTipTv.setVisibility(View.GONE);
        switch (itemViewType) {
            case ITEM_VIEW_TYPE_URL:// 处理链接动态的链接内容和和图片
//                String linkImg = circleItem.getLinkImg();
//                String linkTitle = circleItem.getLinkTitle();
//                ImageLoader.getInstance().displayImage(linkImg, holder.urlImageIv);
//                holder.urlContentTv.setText(linkTitle);
//                holder.urlBody.setVisibility(View.VISIBLE);
//                holder.urlTipTv.setVisibility(View.VISIBLE);
                break;
            case ITEM_VIEW_TYPE_IMAGE:// 处理图片
                final List<String> photos = circleItem.getPhotos_urls();
                if (photos != null && photos.size() > 0) {
                    holder.multiImageView.setVisibility(View.VISIBLE);
                    holder.multiImageView.setList(photos);
                    if (imageListener != null) {
                        holder.multiImageView.setOnItemClickListener(imageListener);
                    }
                } else {
                    holder.multiImageView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        return convertView;
    }

    public void setOnMultiImageItemClickListener(MultiImageView.OnItemClickListener listener) {
        this.imageListener = listener;
    }

    class ViewHolder {
        public CircularImage headIv;
        public TextView nameTv;
        public TextView urlTipTv;
        /**
         * 动态的内容
         */
        public TextView contentTv;
        public TextView timeTv;
        public TextView deleteBtn;
        public ImageView snsBtn;
        /**
         * 点赞列表
         */
        public FavoriteListView favortListTv;

        public LinearLayout urlBody;
        public LinearLayout digCommentBody;
        public View digLine;

        /**
         * 评论列表
         */
        public AppNoScrollerListView commentList;
        /**
         * 链接的图片
         */
        public ImageView urlImageIv;
        /**
         * 链接的标题
         */
        public TextView urlContentTv;
        /**
         * 图片
         */
        public MultiImageView multiImageView;
        // ===========================
        public FavoriteListAdapter favoriteListAdapter;
        public CommentAdapter bbsAdapter;
        public SnsPopupWindow snsPopupWindow;
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private CircleItem mCircleItem;

        public PopupItemClickListener(int circlePosition, CircleItem circleItem, String favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if ("赞".equals(actionitem.mTitle.toString())) {
                        mPresenter.addFavorite(mCirclePosition);
                    } else {//取消点赞
                        mPresenter.deleteFavorite(mCirclePosition, mFavorId);
                    }
                    break;
                case 1://发布评论
                    if (mCirclePublicCommentController != null) {
                        mCirclePublicCommentController.editTextBodyVisible(View.VISIBLE, mPresenter,
                                mCirclePosition, TYPE_PUBLIC_COMMENT, UserInfoUtils.getNickName(mContext), 0);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void update2DeleteCircle(String circleId) {
        deleteSupport(circleId);
        for (int i = 0; i < datas.size(); i++) {
            if (circleId.equals(datas.get(i).getObject_id())) {
                getDatas().remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddFavorite(int circlePosition) {
        String userName = UserInfoUtils.getNickName(mContext);
        FavoriteItem item = DatasUtil.createCurUserFavortItem(userName);
        getDatas().get(circlePosition).getPost_likes().add(item);
        notifyDataSetChanged();
        updateSupport(getDatas().get(circlePosition).getObject_id());
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String userName) {
        List<FavoriteItem> items = getDatas().get(circlePosition).getPost_likes();
        for (int i = 0; i < items.size(); i++) {
            if (userName.equals(items.get(i).getFull_name())) {
                getDatas().get(circlePosition).getPost_likes().remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void update2AddComment(int circlePosition, int type, String username) {
        CommentItem newItem = null;
        String content = "";
        if (mCirclePublicCommentController != null) {
            content = mCirclePublicCommentController.getEditTextString();
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(mContext, "内容不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        if (type == TYPE_PUBLIC_COMMENT) {
            newItem = DatasUtil.createPublicComment(content, username);
        } else if (type == TYPE_REPLY_COMMENT) {
            newItem = DatasUtil.createReplyComment(username, content);
        }
        getDatas().get(circlePosition).getLast_comments().add(newItem);
        notifyDataSetChanged();
        updateComment(getDatas().get(circlePosition).getObject_id(), content);
        if (mCirclePublicCommentController != null) {
            mCirclePublicCommentController.clearEditText();
        }

    }

    private void updateComment(String postid, String comment) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateComment(UserInfoUtils.getAppUserId(mContext),
                postid, UserInfoUtils.getNickName(mContext), comment),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData circleItemResult) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateSupport(String postid) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateSupport(UserInfoUtils.getAppUserId(mContext), postid),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData circleItemResult) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void deleteSupport(String postid) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().deleteSupport(UserInfoUtils.getAppUserId(mContext), postid),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData circleItemResult) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        List<CommentItem> items = getDatas().get(circlePosition).getLast_comments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId.equals(items.get(i).getUid())) {
                getDatas().get(circlePosition).getLast_comments().remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

}
