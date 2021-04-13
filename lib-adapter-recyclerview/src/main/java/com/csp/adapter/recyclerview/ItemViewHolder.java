package com.csp.adapter.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * RecyclerView.ItemViewHolder
 * Created by zhy on 2016/04/09.
 * Modified by csp on 2019/08/20.
 *
 * @author csp
 * @version 1.1.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final static String TAG = "ItemViewHolder";

    private View mConvertView;
    protected SparseArray<View> mViews = new SparseArray<>();
    protected volatile SparseArray<Object> keyTagMap;

    /**
     * @see #getConvertView()
     */
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
    }

    public static ItemViewHolder createViewHolder(@NonNull Context context, @Nullable ViewGroup parent, @LayoutRes int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return createViewHolder(inflater, parent, layoutId);
    }

    public static ItemViewHolder createViewHolder(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @LayoutRes int layoutId) {
        View view = inflater.inflate(layoutId, parent, false);
        return new ItemViewHolder(view);
    }

    /**
     * 通过viewId获取控件
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = getConvertView().findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public Context getContext() {
        return getConvertView().getContext();
    }

    @SuppressWarnings("unchecked")
    public <T> T getTag(int key) {
        return keyTagMap == null ? null : (T) keyTagMap.get(key);
    }

    @SuppressWarnings("UnusedReturnValue")
    public synchronized ItemViewHolder addTag(int key, Object value) {
        if (keyTagMap == null) {
            synchronized (this) {
                if (keyTagMap == null) {
                    keyTagMap = new SparseArray<>();
                }
            }
        }

        keyTagMap.put(key, value);
        return this;
    }

    public synchronized void removeTag(int key) {
        if (keyTagMap != null) {
            keyTagMap.remove(key);
        }
    }

    public synchronized void removeAllTag() {
        if (keyTagMap != null) {
            keyTagMap.clear();
        }
    }

    // ==========
    // 以下为辅助方法
    // ==========

    public ItemViewHolder setText(@IdRes int viewId, CharSequence text) {
        ((TextView) getView(viewId)).setText(text);
        return this;
    }

    public ItemViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        ((TextView) getView(viewId)).setText(resId);
        return this;
    }

    public ItemViewHolder setText(@IdRes int viewId, @StringRes int resId, Object... values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    values[i] = "";
                }
            }
        }

        String text = getContext().getString(resId);
        text = String.format(text, values);
        ((TextView) getView(viewId)).setText(text);
        return this;
    }

    public ItemViewHolder setHint(@IdRes int viewId, @StringRes int resId) {
        ((TextView) getView(viewId)).setHint(resId);
        return this;
    }

    public ItemViewHolder setInputType(@IdRes int viewId, int type) {
        ((TextView) getView(viewId)).setInputType(type);
        return this;
    }

    public ItemViewHolder addTextChangedListener(@IdRes int viewId, TextWatcher watcher) {
        ((TextView) getView(viewId)).addTextChangedListener(watcher);
        return this;
    }

    public ItemViewHolder removeTextChangedListener(@IdRes int viewId, TextWatcher watcher) {
        ((TextView) getView(viewId)).removeTextChangedListener(watcher);
        return this;
    }

    public ItemViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ItemViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ItemViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ItemViewHolder setBackground(@IdRes int viewId, Drawable drawable) {
        getView(viewId).setBackground(drawable);
        return this;
    }

    public ItemViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ItemViewHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ItemViewHolder setTextColor(@IdRes int viewId, @ColorInt int color) {
        ((TextView) getView(viewId)).setTextColor(color);
        return this;
    }

    public ItemViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int colorRes) {
        int color = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M
                ? getContext().getColor(colorRes)
                : getContext().getResources().getColor(colorRes);

        setTextColor(viewId, color);
        return this;
    }

    public ItemViewHolder setTextSize(@IdRes int viewId, float size) {
        ((TextView) getView(viewId)).setTextSize(size);
        return this;
    }

    public ItemViewHolder setAlpha(@IdRes int viewId, float value) {
        AlphaAnimation alpha = new AlphaAnimation(value, value);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        getView(viewId).startAnimation(alpha);
        return this;
    }

    public ItemViewHolder setVisibility(@IdRes int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public ItemViewHolder setVisibility(@IdRes int viewId, boolean showed) {
        getView(viewId).setVisibility(showed ? View.VISIBLE : View.GONE);
        return this;
    }

    public ItemViewHolder linkify(@IdRes int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ItemViewHolder setTypeface(Typeface typeface, @IdRes int... viewIds) {
        for (@IdRes int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ItemViewHolder setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ItemViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ItemViewHolder setMax(@IdRes int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ItemViewHolder setRating(@IdRes int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ItemViewHolder setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ItemViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ItemViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ItemViewHolder setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public ItemViewHolder setSelected(@IdRes int viewId, boolean selected) {
        getView(viewId).setSelected(selected);
        return this;
    }

    public ItemViewHolder setEnabled(@IdRes int viewId, boolean enabled) {
        getView(viewId).setEnabled(enabled);
        return this;
    }

    public ItemViewHolder setActivated(@IdRes int viewId, boolean enabled) {
        getView(viewId).setActivated(enabled);
        return this;
    }

    public ItemViewHolder setFocusableInTouchMode(@IdRes int viewId, boolean enabled) {
        getView(viewId).setFocusableInTouchMode(enabled);
        return this;
    }

    /**
     * 关于事件的
     */
    public ItemViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ItemViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public ItemViewHolder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton compound = getView(viewId);
        compound.setOnCheckedChangeListener(listener);
        return this;
    }

    public ItemViewHolder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }
}
