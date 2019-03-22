package com.sino.initres;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.sino.initres.joor.Reflect;
import java.util.Map;

public class ResManager {

    private final String TAG="ResManager";
    private static volatile ResManager instance;

    public ResManager() {

    }

    public static ResManager getInstance() {
        if (instance == null) {
            synchronized (ResManager.class) {
                if (instance == null) {
                    instance = new ResManager();
                }
            }
        }
        return instance;
    }

    public void initRes(final Context context,final Res.ResInitListener listener,final Class... cla) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                      load(context, listener, cla);
                }
            })   .start();
    }

    private void load(Context context, Res.ResInitListener listener, Class... cla){
        if (cla == null || cla.length == 0) {
            if (listener != null) {
                listener.onResInitResult(Res.Type.DEFAULTS, Res.Result.FAIL, "The loaded resource class cannot be empty");
                listener.onLoadDone();
            }
            return;
        }
        for (Class resCla : cla) {
            if (resCla != null) {
                if (Res.list.contains(resCla.getSimpleName())) {
                    if (resCla.getSimpleName().contains("drawable")) {
                        loadDrawable(context, resCla);
                        if(listener!=null){
                            listener.onResInitResult(Res.Type.DRAWABLES, Res.Result.SUCC, "");
                        }
                    } else if ( resCla.getSimpleName().contains("mipmap") ) {
                        loadDrawable(context, resCla);
                        if(listener!=null) {
                            listener.onResInitResult(Res.Type.MIPMAPS, Res.Result.SUCC, "");
                        }
                    }else if( resCla.getSimpleName().contains("color") ){
                        loadColor(context, resCla);
                        if(listener!=null) {
                            listener.onResInitResult(Res.Type.COLORS, Res.Result.SUCC, "");
                        }
                    }
                } else {
                    if(listener!=null) {
                        listener.onResInitResult(Res.Type.DEFAULTS, Res.Result.FAIL, "Type modification is not supported");
                    }
                }
            }
        }
        if(listener!=null){
           listener.onLoadDone();
        }
    }

    private void loadDrawable(Context context, Class cla) {
        Map<String, Reflect> reflectMap = Reflect.on(cla).fields();
        if (reflectMap != null && reflectMap.size() > 0) {
            for (Map.Entry<String, Reflect> entry : reflectMap.entrySet()) {
                int resId = entry.getValue().get();
                Log.i(TAG, "Key = " + entry.getKey() + ", Value = " + resId);
                boolean isError = false;
                try {
                    ContextCompat.getDrawable(context, resId);
                } catch (Exception e) {
                    Log.e(TAG, "error msg:" + e.getMessage());
                    isError = true;
                }
                try {
                    if (isError) {
                        ContextCompat.getColorStateList(context, resId);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "error msg:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadColor(Context context, Class cla) {
        Map<String, Reflect> reflectMap = Reflect.on(cla).fields();
        if (reflectMap != null && reflectMap.size() > 0) {
            for (Map.Entry<String, Reflect> entry : reflectMap.entrySet()) {
                int resId = entry.getValue().get();
                Log.i(TAG, "Key = " + entry.getKey() + ", Value = " + resId);
                try {
                   ContextCompat.getColor(context, resId);
                } catch (Exception e) {
                    Log.e(TAG, "error msg:" + e.getMessage());
                }
            }
        }
    }





}
