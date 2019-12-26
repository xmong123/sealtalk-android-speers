package com.caesar.rongcloudspeed.circle.adapter;

import android.content.Context;
import android.text.TextUtils;
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

import com.caesar.rongcloudspeed.circle.adapter.CommentAdapter.ICommentItemClickListener;
import com.caesar.rongcloudspeed.circle.contral.CirclePublicCommentController;
import com.caesar.rongcloudspeed.circle.widgets.AppNoScrollerListView;
import com.caesar.rongcloudspeed.circle.widgets.CircularImage;
import com.caesar.rongcloudspeed.circle.widgets.FavoriteListView;
import com.caesar.rongcloudspeed.circle.widgets.MultiImageView;
import com.caesar.rongcloudspeed.circle.widgets.SnsPopupWindow;
import com.caesar.rongcloudspeed.circle.widgets.dialog.CommentDialog;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yiw.circledemo.R;
import com.yiw.circledemo.bean.ActionItem;
import com.yiw.circledemo.bean.CircleItem1;
import com.yiw.circledemo.bean.CommentItem;
import com.yiw.circledemo.bean.FavoriteItem;
import com.yiw.circledemo.mvp.presenter.CirclePresenter;
import com.yiw.circledemo.mvp.view.ICircleViewUpdate;
import com.yiw.circledemo.spannable.ISpanClick;
import com.yiw.circledemo.utils.DatasUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiw
 * @ClassName: CircleAdapter
 * @Description: 圈子列表的adapter
 * @date 2015-12-28 上午09:37:23
 */
public class Circle1Adapter extends BaseAdapter implements ICircleViewUpdate {
    private static final int ITEM_VIEW_TYPE_DEFAULT = 0;
    private static final int ITEM_VIEW_TYPE_URL = 1;
    private static final int ITEM_VIEW_TYPE_IMAGE = 2;


    private static final String ITEM_TYPE_URL = "1";
    private static final String ITEM_TYPE_IMAGE = "2";
    private static final int ITEM_VIEW_TYPE_COUNT = 3;

    private Context mContext;
    private CirclePresenter mPresenter;
    private CirclePublicCommentController mCirclePublicCommentController;
    private List<CircleItem1> datas = new ArrayList<CircleItem1>();
    private MultiImageView.OnItemClickListener l = null;

    public void setCirclePublicCommentController(
            CirclePublicCommentController mCirclePublicCommentController) {
        this.mCirclePublicCommentController = mCirclePublicCommentController;
    }

    public List<CircleItem1> getDatas() {
        return datas;
    }

    public void setDatas(List<CircleItem1> datas) {
        if (datas != null) {
            this.datas = datas;
        }
    }

    public Circle1Adapter(Context context) {
        mContext = context;
        mPresenter = new CirclePresenter(this);
    }

    @Override
    public int getItemViewType(int position) {
//        int itemType = ITEM_VIEW_TYPE_IMAGE;
//        CircleItem1 item = datas.get(position);
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
            convertView = View.inflate(mContext, R.layout.adapter_circle_item1, null);
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

        CircleItem1 circleItem = datas.get(position);
        final String circleId = circleItem.getObject_id();
        String name = circleItem.getUser_nicename();
        String headImg = circleItem.getAvatar();
        String content = circleItem.getPost_title();
        ImageLoader.getInstance().displayImage(headImg, holder.headIv);
        holder.nameTv.setText(name);
        holder.contentTv.setText(content);
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

//        if (DatasUtil.curUser.getId().equals(circleItem.getUser().getId())) {
//            holder.deleteBtn.setVisibility(View.VISIBLE);
//        } else {
//            holder.deleteBtn.setVisibility(View.GONE);
//        }
//        holder.deleteBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //删除
//                mPresenter.deleteCircle(circleId);
//            }
//        });
        holder.deleteBtn.setVisibility(View.INVISIBLE);

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
                    if (l != null) {
                        holder.multiImageView.setOnItemClickListener(l);
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

    public void setOnMultiImageItemClickListener(MultiImageView.OnItemClickListener l) {
        this.l = l;
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
        private CircleItem1 mCircleItem1;

        public PopupItemClickListener(int circlePosition, CircleItem1 circleItem, String favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem1 = circleItem;
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
                                mCirclePosition, TYPE_PUBLIC_COMMENT, UserInfoUtils.getUserName(mContext), 0);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void update2DeleteCircle(String circleId) {
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
        String userName = UserInfoUtils.getUserName(mContext);
        FavoriteItem item = DatasUtil.createCurUserFavortItem(userName);
        notifyDataSetChanged();
    }

    @Override
    public void update2DeleteFavort(int circlePosition, String userName) {

    }

    @Override
    public void update2AddComment(int circlePosition, int type, String username) {
        CommentItem newItem = null;
        String content = "";
        if (mCirclePublicCommentController != null) {
            content = mCirclePublicCommentController.getEditTextString();
        }
        if (type == TYPE_PUBLIC_COMMENT) {
            newItem = DatasUtil.createPublicComment(content, username);
        } else if (type == TYPE_REPLY_COMMENT) {
            newItem = DatasUtil.createReplyComment(username, content);
        }
        notifyDataSetChanged();
        if (mCirclePublicCommentController != null) {
            mCirclePublicCommentController.clearEditText();
        }
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {

    }

}
