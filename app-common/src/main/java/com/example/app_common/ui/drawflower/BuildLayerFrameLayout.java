
package com.example.app_common.ui.drawflower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by mxn on 2016/10/17.
 * BuildLayerFrameLayout
 */
public class BuildLayerFrameLayout extends FrameLayout {

    private boolean mChanged;

    private boolean mHardwareLayersEnabled = true;

    private boolean mAttached;

    private boolean mFirst = true;

    public BuildLayerFrameLayout(Context context) {
        super(context);
        setClipChildren(false);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public BuildLayerFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    public BuildLayerFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    void setHardwareLayersEnabled(boolean enabled) {
        mHardwareLayersEnabled = enabled;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mHardwareLayersEnabled) {
            post(() -> {
                mChanged = true;
                invalidate();
            });
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mChanged) {
            post(() -> {
                if (mAttached) {
                    final int layerType = getLayerType();
                    // If it's already a hardware layer, it'll be built anyway.
                    if (layerType != LAYER_TYPE_HARDWARE || mFirst) {
                        mFirst = false;
                        setLayerType(LAYER_TYPE_HARDWARE, null);
                        buildLayer();
                        setLayerType(LAYER_TYPE_NONE, null);
                    }
                }
            });
            mChanged = false;
        }
        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint
                .FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(pfd);
    }
}
