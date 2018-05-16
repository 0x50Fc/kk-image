package cn.kkmofang.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by zhanghailong on 2018/4/18.
 */

public class Image extends Drawable {

    private final BitmapProvider _bitmapProvider;
    private final Paint _paint;
    private final Rect _src = new Rect();
    private final RectF _dest = new RectF();
    private final PaintFlagsDrawFilter _drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    private final ImageStyle _style;

    public Image(BitmapProvider bitmapProvider,ImageStyle style) {
        _bitmapProvider = bitmapProvider;
        _style = style;
        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setDither(true);
        _paint.setFilterBitmap(true);
    }

    public ImageStyle getStyle() {
        return _style;
    }

    public Bitmap getBitmap() {
        return _bitmapProvider.getBitmap();
    }

    public float width() {
        return (float) _bitmapProvider.getBitmap().getWidth() / _style.scale;
    }

    public float height() {
        return (float) _bitmapProvider.getBitmap().getHeight() / _style.scale;
    }


    @Override
    public void draw(Canvas canvas) {

        Bitmap _bitmap = _bitmapProvider.getBitmap();

        canvas.setDrawFilter(_drawFilter);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int mWidth = _bitmap.getWidth();
        int mHeight = _bitmap.getHeight();

        float v = _style.density / _style.scale;

        int mCapLeft = _style.capLeft;
        int mCapTop = _style.capTop;

        float xScale = 1.0f;
        float yScale = 1.0f;

        if(mCapLeft != 0 && mCapTop != 0) {
            if(mWidth * v >= width) {
                mCapLeft = 0;
            }
            if(mHeight * v >= height) {
                mCapTop = 0;
            }
        } else if(mCapLeft != 0 && mCapTop == 0) {
            xScale = (float) height / (float) mHeight;
            if(mWidth * v >= mWidth * xScale) {
                mCapLeft = 0;
            }
        } else if(mCapLeft == 0 && mCapTop != 0) {
            yScale = (float) width / (float) mWidth;
            if(mHeight * v >= mHeight * yScale) {
                mCapTop = 0;
            }
        }

        int sc= canvas.saveLayer(0,0,width,height,_paint,Canvas.ALL_SAVE_FLAG);

        if(mCapLeft == 0 && mCapTop == 0) {

            if(_style.gravity == ImageGravity.RESIZE_ASPECT || _style.gravity == ImageGravity.RESIZE_ASPECT_FILL) {

                xScale = (float) width / (float) mWidth;
                yScale = (float) height / (float) mHeight;
                float scale = _style.gravity == ImageGravity.RESIZE_ASPECT ? Math.min(xScale,yScale) : Math.max(xScale,yScale);
                float toWidth = mWidth * scale;
                float toHeight = mHeight * scale;

                _src.left = 0;
                _src.right = mWidth;
                _src.top = 0;
                _src.bottom = mHeight;

                _dest.left = (width - toWidth) * 0.5f;
                _dest.right = _dest.left + toWidth;
                _dest.top = (height - toHeight) * 0.5f;
                _dest.bottom = _dest.top + toHeight;

                canvas.drawBitmap(_bitmap, _src, _dest, _paint);

            } else {

                _src.left = 0;
                _src.right = mWidth;
                _src.top = 0;
                _src.bottom = mHeight;

                _dest.left = 0;
                _dest.right = width;
                _dest.top = 0;
                _dest.bottom = height;

                canvas.drawBitmap(_bitmap, _src, _dest, _paint);
            }

        } else {

            if(mCapTop == 0) {

                xScale = (float) height / (float) mHeight;

                float capLeft = (mCapLeft * xScale);
                float capRight = width - (mWidth * xScale - capLeft - (_style.capSize * xScale));

                {
                    _src.left = 0;
                    _src.right = mCapLeft;
                    _src.top = 0;
                    _src.bottom = mHeight;

                    _dest.left = 0;
                    _dest.right = capLeft;
                    _dest.top = 0;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = mCapLeft;
                    _src.right = mCapLeft + _style.capSize;
                    _src.top = 0;
                    _src.bottom = mHeight;

                    _dest.left = capLeft;
                    _dest.right = capRight;
                    _dest.top = 0;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = mCapLeft + _style.capSize;
                    _src.right = mWidth;
                    _src.top = 0;
                    _src.bottom = mHeight;

                    _dest.left = capRight;
                    _dest.right = width;
                    _dest.top = 0;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }


            } else if(mCapLeft == 0) {

                yScale = (float) width / (float) mWidth;

                float capTop = (mCapTop * yScale);
                float capBottom = height - (mHeight * yScale - capTop - (_style.capSize * yScale));

                {
                    _src.left = 0;
                    _src.right = mWidth;
                    _src.top = 0;
                    _src.bottom = mCapTop;

                    _dest.left = 0;
                    _dest.right = width;
                    _dest.top = 0;
                    _dest.bottom = capTop;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = 0;
                    _src.right = mWidth;
                    _src.top = mCapTop;
                    _src.bottom = mCapTop + _style.capSize;

                    _dest.left = 0;
                    _dest.right = mWidth;
                    _dest.top = capTop;
                    _dest.bottom = capBottom;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = 0;
                    _src.right = mWidth;
                    _src.top = mCapTop + _style.capSize;
                    _src.bottom = mHeight;

                    _dest.left = 0;
                    _dest.right = mWidth;
                    _dest.top = capBottom;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

            } else {

                float capLeft = mCapLeft * v;
                float capRight = width - (mWidth - capLeft - _style.capSize) * v;
                float capTop = mCapTop * v;
                float capBottom = height - (mHeight - capTop - _style.capSize) * v;

                //left
                {
                    _src.left = 0;
                    _src.right = mCapLeft;
                    _src.top = 0;
                    _src.bottom = mCapTop;

                    _dest.left = 0;
                    _dest.right = capLeft;
                    _dest.top = 0;
                    _dest.bottom = capTop;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = 0;
                    _src.right = mCapLeft;
                    _src.top = mCapTop;
                    _src.bottom = mCapTop + _style.capSize;

                    _dest.left = 0;
                    _dest.right = capLeft;
                    _dest.top = capTop;
                    _dest.bottom = capBottom;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = 0;
                    _src.right = mCapLeft;
                    _src.top = mCapTop + _style.capSize;
                    _src.bottom = mHeight;

                    _dest.left = 0;
                    _dest.right = capLeft;
                    _dest.top = capBottom;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                //right
                {
                    _src.left = mCapLeft + _style.capSize;
                    _src.right = mWidth;
                    _src.top = 0;
                    _src.bottom = mCapTop;

                    _dest.left = capRight;
                    _dest.right = width;
                    _dest.top = 0;
                    _dest.bottom = capTop;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = mCapLeft + _style.capSize;
                    _src.right = mWidth;
                    _src.top = mCapTop;
                    _src.bottom = mCapTop + _style.capSize;

                    _dest.left = capRight;
                    _dest.right = width;
                    _dest.top = capTop;
                    _dest.bottom = capBottom;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = mCapLeft + _style.capSize;
                    _src.right = mWidth;
                    _src.top = mCapTop + _style.capSize;
                    _src.bottom = mHeight;

                    _dest.left = capRight;
                    _dest.right = width;
                    _dest.top = capBottom;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                //center
                {
                    _src.left = mCapLeft ;
                    _src.right = mCapLeft + _style.capSize;
                    _src.top = 0;
                    _src.bottom = mCapTop;

                    _dest.left = capLeft;
                    _dest.right = capRight;
                    _dest.top = 0;
                    _dest.bottom = capTop;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = mCapLeft;
                    _src.right = mCapLeft + _style.capSize;
                    _src.top = mCapTop;
                    _src.bottom = mCapTop + _style.capSize;

                    _dest.left = capLeft;
                    _dest.right = capRight;
                    _dest.top = capTop;
                    _dest.bottom = capBottom;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

                {
                    _src.left = mCapLeft ;
                    _src.right = mCapLeft + _style.capSize;
                    _src.top = mCapTop + _style.capSize;
                    _src.bottom = mHeight;

                    _dest.left = capLeft;
                    _dest.right = capRight;
                    _dest.top = capBottom;
                    _dest.bottom = height;

                    canvas.drawBitmap(_bitmap, _src, _dest, _paint);
                }

            }


        }

        if(_style.radius > 0) {

            float r = _style.radius * _style.density;

            Path path = new Path();

            path.addRoundRect(new RectF(0, 0, width, height), r,r, Path.Direction.CW);

            _paint.setColor(Color.BLACK);
            _paint.setStyle(Paint.Style.FILL);

            _paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

            canvas.drawPath(path,_paint);

            _paint.setXfermode(null);

        }

        canvas.restoreToCount(sc);
    }

    @Override
    public void setAlpha(int alpha) {
        _paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        _paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    public interface BitmapProvider {
        Bitmap getBitmap();
    }
}
