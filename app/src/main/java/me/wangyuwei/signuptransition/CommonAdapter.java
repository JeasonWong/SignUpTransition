package me.wangyuwei.signuptransition;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者： 巴掌 on 16/8/19 12:57
 * Github: https://github.com/JeasonWong
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {

    private final LayoutInflater mLayoutInflater;

    public CommonAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonViewHolder(mLayoutInflater.inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class CommonViewHolder extends RecyclerView.ViewHolder {

        public CommonViewHolder(View itemView) {
            super(itemView);
        }
    }
}
