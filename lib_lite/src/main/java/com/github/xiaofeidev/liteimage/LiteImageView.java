package com.github.xiaofeidev.liteimage;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 自动根据 View 的尺寸去加载合适尺寸的位图，以免浪费内存
 * @author 黎曼
 * @date 2020/7/27
 */
public class LiteImageView extends AppCompatImageView {
    //待加载位图的资源 id
    @DrawableRes
    private int resId = 0;
    //待加载位图的原始尺寸
    private int bitmapOriginWidth;
    private int bitmapOriginHeight;
    //当前控件的尺寸（展示图片的实际容器的尺寸）
    private float displayWidth = 0;
    private float displayHeight = 0;
    //是否已加载好 Bitmap
    private boolean isBitmapLoaded = false;

    private Runnable mRunnableSetBitmap;

    public LiteImageView(Context context) {
        this(context, null);
    }

    public LiteImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LiteImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configure();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LiteImageView,
                0, 0);

        try {
            resId = typedArray.getResourceId(R.styleable.LiteImageView_srcLite, 0);
        } finally {
            //注意资源回收
            typedArray.recycle();
        }

        mRunnableSetBitmap = new Runnable() {
            @Override
            public void run() {
                if (resId == 0) return;
                Bitmap adaptBitmap = getAdaptBitmap(getResources(), resId, (int)displayWidth, (int)displayHeight);
                if (adaptBitmap != null){
                    setImageBitmap(adaptBitmap);
                    Log.d(LiteImageView.this.getClass().getSimpleName(), String.format("实际加载到内存中的图片尺寸：width:%d，height:%d", adaptBitmap.getWidth(), adaptBitmap.getHeight()));
                } else {//获取到的位图是 null 则表示设置的图片资源不是位图
                    setImageResource(resId);
                }
            }
        };

        initBitmap();
    }

    public void setSrcLite(@DrawableRes int resId) {
        this.resId = resId;
        isBitmapLoaded = false;
        initBitmap();
        onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
    }

    protected void configure(){
        //图片填充满当前 View，之所以这样配置，是因为采样加载的图片其尺寸通常要小于当前 View 的尺寸！
        setScaleType(ScaleType.FIT_XY);
        setAdjustViewBounds(true);
    }

    private void initBitmap(){
        if (resId == 0) return;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //加载图片
        BitmapFactory.decodeResource(getResources(), resId, options);
        //待加载位图的实际尺寸
        bitmapOriginWidth = options.outWidth;
        bitmapOriginHeight = options.outHeight;
        Log.d(getClass().getSimpleName(), String.format("待加载图片的原始尺寸：width:%d，height:%d", bitmapOriginWidth, bitmapOriginWidth));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //宽和高至少得有一方得是确定的值
        if ((w != 0 || h != 0) && !isBitmapLoaded){
            if (h == 0){//宽度为 MeasureSpec.EXACTLY 的确定值
                displayWidth = w;
                displayHeight = displayWidth * bitmapOriginHeight / bitmapOriginWidth;
            } else if (w == 0){//高度为 MeasureSpec.EXACTLY 的确定值
                displayHeight = h;
                displayWidth = displayHeight * bitmapOriginWidth / bitmapOriginHeight;
            } else {//宽度和高度都为 MeasureSpec.EXACTLY 的确定值，此时以宽度为基准
                displayWidth = w;
                displayHeight = displayWidth * bitmapOriginHeight / bitmapOriginWidth;
            }
            post(mRunnableSetBitmap);
        }
        if (w > 0 && h > 0){
            Log.d(getClass().getSimpleName(), String.format("ImageView 的实际尺寸：width:%d，height:%d", w, h));
        }
    }

    private Bitmap getAdaptBitmap(Resources res, int resId, int displayWidth, int displayHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        //计算缩放比
        int inSampleSize = 1;
        //如果宽高的任意一方的缩放比例没有达到要求，都继续增大缩放比例，注意 inSampleSize 需是 2 的幂
        while((bitmapOriginWidth / inSampleSize) > displayWidth || (bitmapOriginHeight / inSampleSize) > displayHeight){
            inSampleSize *= 2;
        }
//        inSampleSize = Math.max(Math.round(bitmapOriginWidth / (float)displayWidth), Math.round(bitmapOriginHeight / (float)displayHeight));
        options.inSampleSize = inSampleSize;
        //重新加载图片
        options.inJustDecodeBounds = false;
        isBitmapLoaded = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
        return bitmap;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mRunnableSetBitmap);
    }
}
