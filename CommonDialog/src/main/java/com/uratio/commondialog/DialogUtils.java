package com.uratio.commondialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * @author lang
 * @data 2021/7/8
 */
public class DialogUtils {
    private Dialog mDialog;
    private Activity mActivity;
    private TextView msgView;

    public DialogUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public static DialogUtils from(Activity activity) {
        return new DialogUtils(activity);
    }

    /**
     * dialog 标题可以加背景图片的,带有取消和确定按钮
     *
     * @param titleShow                标题是否显示
     * @param title                    标题（默认：温馨提示）
     * @param msg                      内容
     * @param leftShow                 左侧按钮是否显示
     * @param left                     取消（左）
     * @param leftListener             取消监听
     * @param rightShow                右侧按钮是否显示
     * @param right                    确定（右）
     * @param rightListener            确定监听
     * @param isCanceledOnTouchOutside true：点击外部区域示消失dialog
     */
    private void alert(final boolean titleShow,
                       final String title,
                       final String msg,
                       final boolean leftShow,
                       final String left,
                       final View.OnClickListener leftListener,
                       final boolean rightShow,
                       final String right,
                       final View.OnClickListener rightListener,
                       final Boolean isCanceledOnTouchOutside) {
        dismissProgressDialog();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity != null && !mActivity.isFinishing()) {
                    mDialog = new Dialog(mActivity, R.style.style_common_dialog);
                    View alertView = LayoutInflater.from(mActivity).inflate(R.layout.com_uratio_common_dialog_view, null);
                    DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
                    alertView.setMinimumWidth(displayMetrics.widthPixels);
                    //设置布局
                    mDialog.setContentView(alertView);
                    mDialog.getWindow().setGravity(Gravity.CENTER);

                    if (isCanceledOnTouchOutside) {
                        alertView.setOnClickListener(v -> mDialog.dismiss());
                    }
                    alertView.findViewById(R.id.ll_show).setOnClickListener(v -> {
                    });

                    TextView titleView = alertView.findViewById(R.id.dialog_title);
                    msgView = alertView.findViewById(R.id.dialog_msg);

                    TextView leftButton = alertView.findViewById(R.id.left_btn);
                    TextView rightButton = alertView.findViewById(R.id.right_btn);
                    View line = alertView.findViewById(R.id.view_line);

                    //标题
                    if (!titleShow) {
                        titleView.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(title)) {
                        titleView.setText(title);
                    }

                    //内容
                    if (msg != null) {
                        msgView.setText(Html.fromHtml(msg));
                    } else {
                        msgView.setVisibility(View.GONE);
                    }

                    //左边取消按钮，如果只有一个按钮传null
                    if (!leftShow) {
                        line.setVisibility(View.GONE);
                        leftButton.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(left)) {
                        leftButton.setText(left);
                    }
                    leftButton.setOnClickListener(new View.OnClickListener() {
                        @SingleClick(500)
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            if (null != leftListener)
                                leftListener.onClick(v);
                        }
                    });

                    //右边确定按钮
                    if (!rightShow) {
                        rightButton.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(right)) {
                        rightButton.setText(right);
                    }
                    rightButton.setOnClickListener(new View.OnClickListener() {
                        @SingleClick(500)
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            if (null != rightListener)
                                rightListener.onClick(v);
                        }
                    });

                    try {
                        mDialog.setCancelable(isCanceledOnTouchOutside);
                        mDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                        mDialog.show();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * 两个按钮 默认点击外部区域不可消失
     *
     * @param msg
     * @param rightListener
     */
    public void alertTwoBtn(final String msg, final View.OnClickListener rightListener) {
        alert(true, null, msg, true, null, null, true, null, rightListener, false);
    }

    /**
     * 两个按钮 默认点击外部区域不可消失
     *
     * @param msg
     * @param rightListener
     */
    public void alertTwoBtn(final String title, final String msg, final View.OnClickListener rightListener) {
        alert(true, title, msg, true, null, null, true, null, rightListener, false);
    }

    /**
     * 两个按钮 默认点击外部区域不可消失
     *
     * @param title         标题（默认：温馨提示）
     * @param msg
     * @param left          取消（左）
     * @param leftListener
     * @param right         确定（右）
     * @param rightListener
     */
    public void alertTwoBtn(final String title,
                            final String msg,
                            final String left,
                            final View.OnClickListener leftListener,
                            final String right,
                            final View.OnClickListener rightListener) {
        alert(true, title, msg, true, left, leftListener, true, right, rightListener, false);
    }

    /**
     * 只有一个按钮的  默认点击外部区域不可消失
     *
     * @param msg
     */
    public void alertOneBtn(final String msg) {
        alert(true, null, msg, false, null, null, true, "", null, false);
    }

    /**
     * 只有一个按钮的
     *
     * @param title                    标题（默认：温馨提示）
     * @param msg
     * @param right                    确定（右）
     * @param rightListener
     * @param isCanceledOnTouchOutside
     */
    public void alertOneBtn(final String title,
                            final String msg,
                            final String right,
                            final View.OnClickListener rightListener,
                            final Boolean isCanceledOnTouchOutside) {
        alert(true, title, msg, false, null, null, true, right, rightListener, isCanceledOnTouchOutside);
    }

    /**
     * 只有一个按钮的  默认点击外部区域不可消失
     *
     * @param title         标题（默认：温馨提示）
     * @param msg
     * @param right         确定（右）
     * @param rightListener
     */
    public void alertOneBtn(final String title,
                            final String msg,
                            final String right,
                            final View.OnClickListener rightListener) {
        alert(true, title, msg, false, null, null, true, right, rightListener, false);
    }

    public void dismissProgressDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mDialog != null && mDialog.isShowing() && !mActivity.isFinishing()) {
                    try {
                        mDialog.dismiss();
                    } catch (Exception e) {
                    } finally {
                        mDialog = null;
                    }
                }
            }
        });
    }

    public void setMsg(CharSequence msg, int textSize, int gravity) {
        if (mDialog != null && mDialog.isShowing() && msgView != null) {
            msgView.setText(msg);
            msgView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            msgView.setGravity(gravity);
        }
    }

    public void setMsg(String msg) {
        if (mDialog != null && mDialog.isShowing() && msgView != null) {
            msgView.setText(Html.fromHtml(msg));
        }
    }
}

