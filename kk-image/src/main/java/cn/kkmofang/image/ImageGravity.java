package cn.kkmofang.image;

/**
 * Created by hailong11 on 2018/4/18.
 */

public enum ImageGravity {

    RESIZE,RESIZE_ASPECT,RESIZE_ASPECT_FILL;


    public static ImageGravity valueOfString(String v) {

        if("resizeAspect".equals(v)) {
            return RESIZE_ASPECT;
        }

        if("resizeAspectFill".equals(v)) {
            return RESIZE_ASPECT_FILL;
        }

        return RESIZE;
    }
}
