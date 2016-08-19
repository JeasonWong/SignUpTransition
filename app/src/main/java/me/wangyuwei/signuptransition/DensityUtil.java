package me.wangyuwei.signuptransition;

import android.content.Context;

/**
 * 作者： 巴掌 on 16/8/18 22:27
 * Github: https://github.com/JeasonWong
 */
public class DensityUtil {

    private DensityUtil(){}

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
