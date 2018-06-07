package cn.kkmofang.image;

import android.content.Context;

/**
 * Created by zhanghailong on 2018/4/18.
 */

public class ImageStyle {

    public int capLeft = 0;
    public int capTop = 0;
    public int capSize = 0;
    public float scale = 1.0f;
    public float density = 1.0f;
    public float radius = 0.0f;

    public ImageGravity gravity = ImageGravity.RESIZE_ASPECT_FILL;

    public ImageStyle(Context context) {
        this(0,0,0,1.0f,0.0f,context.getResources().getDisplayMetrics().density,ImageGravity.RESIZE_ASPECT_FILL);
    }

    public ImageStyle(int capLeft,int capTop,int capSize,float scale,float radius,float density,ImageGravity gravity) {
        this.capLeft = (int) (capLeft * scale);
        this.capTop = (int) (capTop * scale);
        this.capSize = (int) Math.ceil(capSize * scale);
        this.radius = radius;
        this.scale = scale;
        this.density = density;
        this.gravity = gravity;
    }

}
