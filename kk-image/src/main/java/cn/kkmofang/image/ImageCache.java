package cn.kkmofang.image;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hailong11 on 2018/5/17.
 */

public class ImageCache {

    private final Map<String,SoftReference<Drawable>> _images = new TreeMap<>();

    public Drawable getImage(File file) {
        Drawable v = getImage(file.getAbsolutePath());
        if(v == null) {
            try {
                FileInputStream in = new FileInputStream(file);
                try {
                    v = getImage(in, file.getAbsolutePath());
                }
                finally {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return v;
    }

    public Drawable getImage(String key) {

        if(key == null) {
            return null;
        }

        if(_images.containsKey(key)) {
            SoftReference<Drawable> v = _images.get(key);
            Drawable vv = v.get();
            if(vv == null) {
                _images.remove(key);
            } else {
                return vv;
            }
        }

        return null;
    }

    public Drawable getImage(InputStream in, String key) {

        if(key == null) {
            return null;
        }

        if(_images.containsKey(key)) {
            SoftReference<Drawable> v = _images.get(key);
            Drawable vv = v.get();
            if(vv == null) {
                _images.remove(key);
            } else {
                return vv;
            }
        }

        Drawable vv = Drawable.createFromStream(in,key);

        if(vv != null) {
            _images.put(key,new SoftReference<>(vv));
        }

        return vv;
    }

    public final static ImageCache main = new ImageCache();
}
