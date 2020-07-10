package com.example.jun.base.weight;


import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;
import de.hdodenhof.circleimageview.CircleImageView;


import com.example.jun.base.R;
import com.example.jun.base.utils.CommonUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class DialogBuilder {

    /**
     * 选择 dialog
     *
     * @param context
     */
    public static MyDialog select(final Context context, View.OnClickListener onClickListener, String content) {

        View view = View.inflate(context, R.layout.dialog_select_tip, null);
        TextView contentTv = view.findViewById(R.id.content_tv);
        TextView confirm = view.findViewById(R.id.confirm_tv);
        TextView cancel = view.findViewById(R.id.cancel_tv);
//        titleTv.setText(title);
        contentTv.setText(content);
        final MyDialog builder = new MyDialog(context, view, R.style.DialogCenterAnim, Gravity.CENTER);
        builder.setCancelable(false);
        builder.setCanceledOnTouchOutside(false);
        if (onClickListener == null) {
            confirm.setOnClickListener(v -> builder.cancel());
        } else {
            confirm.setOnClickListener(view1 -> {
                onClickListener.onClick(view1);
                builder.cancel();
            });
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.cancel();
            }
        });
        builder.show();
        return builder;
    }

    public static MyDialog selectListButtonDialog(Context context, List<String> titles, String title, MultiItemTypeAdapter.OnItemClickListener onItemClickListener) {


        View view = View.inflate(context, R.layout.dialog_select_list, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        TextView title_content = view.findViewById(R.id.title_content);
        if ("".equals(title)) {
            title_content.setVisibility(View.GONE);
        }
        title_content.setText(title);
        CommonAdapter adapter = new CommonAdapter<String>(context, R.layout.dialog_select_list_item, titles) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {
                View convertView = holder.getConvertView();
                convertView.findViewById(R.id.view_line).setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                TextView item_title = convertView.findViewById(R.id.item_title);
                item_title.setText(o);
            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
                if (titles.size() >= 4) {
                    super.setMeasuredDimension(childrenBounds, wSpec, View.MeasureSpec.makeMeasureSpec(CommonUtil.dip2px(context, 220), View.MeasureSpec.AT_MOST));
                } else {
                    super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
                }
            }
        });
        final MyDialog builder = new MyDialog(context, view, R.style.DialogBottomAnim, Gravity.BOTTOM);
        builder.setCancelable(true);
        builder.setCanceledOnTouchOutside(true);
        builder.show();
        view.findViewById(R.id.cancel_tv).setOnClickListener(v -> {
            builder.cancel();
        });
        return builder;
    }


    public interface GraphicVerificationCodeListener {
        void getCodePic(ImageView imageView);

        void submit(String codeStr);
    }


    public static void showBigImage(Context context, List<ImageInfo> imageInfoList, int index) {
        if (imageInfoList.isEmpty()) return;
        ImagePreview
                .getInstance()
                .setContext(context)
                .setIndex(index)
                .setShowDownButton(false)
                .setImageInfoList(imageInfoList)
                // 开启预览
                .start();
    }

    public static void showBigImageStr(Context context, List<String> imageInfoList, int index) {
        if (imageInfoList.isEmpty()) return;
        ImagePreview
                .getInstance()
                .setContext(context)
                .setIndex(index)
                .setShowDownButton(false)
                .setImageList(imageInfoList)
                // 开启预览
                .start();
    }

    public static void showBigImageSingle(Context context, String url) {
        List<ImageInfo> imageInfoList = new ArrayList<>();
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setOriginUrl(url);// 原图url
        imageInfo.setThumbnailUrl(url);// 缩略图url
        imageInfoList.add(imageInfo);
        showBigImage(context, imageInfoList, 0);
    }
}
