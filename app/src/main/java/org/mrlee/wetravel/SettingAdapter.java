package org.mrlee.wetravel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Lee on 2019-01-11.
 */

public class SettingAdapter extends PagerAdapter {
    Context context;

    public SettingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.viewpager_setting,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.image_container);
        switch (position){
            case 0:
                Glide.with(context).load(R.drawable.view_setting_0).into(image_container);
                break;
            case 1:
                Glide.with(context).load(R.drawable.view_setting_1).into(image_container);
                break;
            case 2:
                Glide.with(context).load(R.drawable.view_setting_2).into(image_container);
                break;
            case 3:
                Glide.with(context).load(R.drawable.view_setting_3).into(image_container);
                break;
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
