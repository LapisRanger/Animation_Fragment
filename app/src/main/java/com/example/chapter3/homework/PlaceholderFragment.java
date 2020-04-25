package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class PlaceholderFragment extends Fragment {

    private LottieAnimationView loading;
    private TextView textView;
    private AnimatorSet animatorSet;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View view=inflater.inflate(R.layout.fragment_placeholder, container, false);
        loading=view.findViewById(R.id.loading);
        textView=view.findViewById(R.id.testView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(loading,"alpha",1f,0f);

                ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(textView,"alpha",0f,1f);

                animatorSet=new AnimatorSet();
                animatorSet.playTogether(objectAnimator1,objectAnimator2);
                animatorSet.setDuration(1000);
                animatorSet.start();
            }
        }, 5000);
    }
}
