package com.android.mobile.common.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.mobile.common.R;

import java.util.Arrays;

import butterknife.ButterKnife;

public class ViewUtils {

    private static ButterKnife.Action<View> ACTION_HIDE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };
    private static ButterKnife.Action<View> ACTION_SHOW = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    public static void onPreDraw(final View view, final Runnable action) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);

                final boolean shouldExecute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    shouldExecute = view.isAttachedToWindow();
                } else {
                    shouldExecute = view.isShown();
                }

                if (shouldExecute) {
                    action.run();
                }
                return true;
            }
        });
    }

    public static void setTextOrHide(TextView tv, Spanned text) {
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
        tv.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public static void setCheckedOrHide(CheckBox cb, boolean isChecked) {
        cb.setChecked(isChecked);
        cb.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    public static float getPixelValueFromDIP(Context context, float dipValue) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                context.getResources().getDisplayMetrics()
        );
    }

    public static void hide(View... views) {
        if (views != null) {
            ButterKnife.apply(Arrays.asList(views), ACTION_HIDE);
        }
    }

    public static void show(View... views) {
        if (views != null) {
            ButterKnife.apply(Arrays.asList(views), ACTION_SHOW);
        }
    }

    public static boolean isHidden(View view) {
        return !isVisible(view);
    }

    public static boolean isVisible(View view) {
        return (view == null) ? false : view.getVisibility() == View.VISIBLE;
    }

    public static void fadeIn(final View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setAlpha(1f);
        } else {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);

            view.animate().cancel();
            view.animate().alpha(1f).setListener(null);
        }
    }

    public static void fadeOut(final View view) {
        view.animate().cancel();
        view.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void beginTransition(ViewGroup target) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(target);
        }
    }

    public static void redirectToExternalLinkWithConfirm(final Context context, final String url) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(context.getString(R.string.redirect_title));

        dialogBuilder.setMessage(url);
        dialogBuilder.setNegativeButton(context.getString(R.string.redirect_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setPositiveButton(context.getString(R.string.redirect_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                redirectToExternalLink(context, Uri.parse(url));
            }
        });
        dialogBuilder.show();
    }

    public static void redirectToExternalLink(Context context, String url) {
        String fullUrl = autocompleteUrl(url);
        redirectToExternalLink(context, Uri.parse(fullUrl));
    }

    public static void redirectToExternalLink(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static String autocompleteUrl(String url) {
        return url.startsWith("http://") || url.startsWith("HTTP://")
                || url.startsWith("https://") || url.startsWith("HTTPS://") ? url : "http://" + url;
    }

    /**
     * Display the multi text on the button
     *
     * @param btn
     * @param upperLine          text
     * @param lowerLine          text
     * @param lowerLineSizeRatio proportion of lower line w.r.t. upper line
     */
    public static void setMultiTextButton(Button btn, String upperLine, String lowerLine, float lowerLineSizeRatio) {
        int upperLineTxtSize = upperLine.length();
        int lowerLineTxtSize = lowerLine.length();

        Spannable span = new SpannableString(upperLine + "\n" + lowerLine);
        span.setSpan(new StyleSpan(Typeface.NORMAL), 0, upperLineTxtSize, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(lowerLineSizeRatio), upperLineTxtSize, (upperLineTxtSize + lowerLineTxtSize + 1),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        btn.setText(span);
    }

    /**
     * Check if EditText is empty
     *
     * @param editText
     * @return boolean true  - edittext is empty
     * false - edittext is not empty
     */
    public static boolean isEditTextEmpty(EditText editText) {
        return editText == null || editText.getText().toString().trim().length() == 0;
    }
}
