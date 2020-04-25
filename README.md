# Animation_Fragment

### Lottie库的简单使用:

1. build.gradle添加依赖

   ```
   dependencies {
   ...
   	implementation 'com.airbnb.android:lottie:2.7.0'
   ...
   }
   ```

2. xml布局文件中添加AnimationView控件,设置属性

   ```xml
   <!-- TODO ex1-1, 在这里将 lottie 控件的相关属性补全, lottie_rawRes 注意设置为 @raw/material_wave_loading 或者 @raw/muzli-->
       <!-- 参考 Lottie 官方文档 https://airbnb.io/lottie/#/android?id=from-resraw-lottie_rawres-or-assets-lottie_filename -->
       <!-- 你还可以在 https://www.lottiefiles.com/ 这里找到更多的 lottie 资源 -->
       <com.airbnb.lottie.LottieAnimationView
           android:id="@+id/animation_view"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:layout_gravity="center"
   
           app:lottie_autoPlay="true"
           app:lottie_loop="true"
           app:lottie_rawRes="@raw/muzli"/>
   ```

3. 对应java代码中找到该控件,然后使用play方法

   ```java
   private LottieAnimationView animationView;
   
   animationView = findViewById(R.id.animation_view);
   
   animationView.playAnimation();
   ```

#### Ex1

利用seekbar的进度条的进度值改变监听器(需要实现3个方法,onProgressChange是主要的),使用animationView 的setProgress方法即可

```java
loopCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 当选中自动播放的时候，开始播放 lottie 动画，同时禁止手动修改进度
                    animationView.playAnimation();
                    seekBar.setEnabled(false);
                } else {
                    // 当去除自动播放时，停止播放 lottie 动画，同时允许手动修改进度
                    animationView.pauseAnimation();
                    seekBar.setEnabled(true);
                }
            }
        });

seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {               
                animationView.setProgress((float) (progress/100.0));//seekbar的progress是0~100,Lottie的progress是0~1f
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
```

### ObjectAnimator的简单使用

对view控件添加动画

新建animator,设置属性,如缩放,透明度等

新建AnimatorSet,可设置同时播放,顺序播放,持续时间等

```java
ObjectAnimator animator1 = ObjectAnimator.ofArgb(target,
                "backgroundColor",
                getBackgroundColor(startColorPicker),
                getBackgroundColor(endColorPicker));
        //animator1.setDuration(Integer.parseInt(durationSelector.getText().toString()));
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        animator1.setRepeatMode(ObjectAnimator.REVERSE);//有REVERSE倒序和RESTART正序两种重复播放方式,一般选REVERSE,相当于原路返回,不会突兀

        // TODO ex2-1：在这里实现另一个 ObjectAnimator，对 target 控件的大小进行缩放，从 1 到 2 循环
        ObjectAnimator animator2=ObjectAnimator.ofFloat(target,"scaleX",1f,2f);
        animator2.setRepeatCount(ObjectAnimator.INFINITE);
        animator2.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator animator3=ObjectAnimator.ofFloat(target,"scaleY",1f,2f);
        animator3.setRepeatCount(ObjectAnimator.INFINITE);
        animator3.setRepeatMode(ObjectAnimator.REVERSE);
        // TODO ex2-2：在这里实现另一个 ObjectAnimator，对 target 控件的透明度进行修改，从 1 到 0.5f 循环
        ObjectAnimator animator4=ObjectAnimator.ofFloat(target,"alpha",1f,0.5f);
        animator4.setRepeatCount(ObjectAnimator.INFINITE);
        animator4.setRepeatMode(ObjectAnimator.REVERSE);

        // TODO ex2-3: 将上面创建的其他 ObjectAnimator 都添加到 AnimatorSet 中
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1,animator2,animator3,animator4);
        animatorSet.setDuration(Integer.parseInt(durationSelector.getText().toString()));
        animatorSet.start();
    }
```

### Fragment,ViewPage,TabLayout综合使用+淡入淡出动效

1. 在主界面即Activity对应的布局xml文件中添加TabLayout控件和ViewPager控件

   设定TabLayout的高度,确保不被ViewPager覆盖

   ```xml
   
       <android.support.design.widget.TabLayout
           android:layout_width="match_parent"
           android:layout_height="40dp"
           android:id="@+id/tabLayout"
           >
           <android.support.design.widget.TabItem
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:text="@string/Tab1"></android.support.design.widget.TabItem>
           <android.support.design.widget.TabItem
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:text="@string/Tab2"></android.support.design.widget.TabItem>
       </android.support.design.widget.TabLayout>
   
       <android.support.v4.view.ViewPager
           android:id="@+id/viewPager"
           android:layout_width="match_parent"
           android:layout_height="match_parent"
           android:layout_marginTop="40dp"/>
   ```

2. 在Activity的java类文件中编写代码,首先使用findViewById找到这两个控件

   ```java
   ViewPager viewPager=findViewById(R.id.viewPager);
   TabLayout tabLayout=findViewById(R.id.tabLayout);
   ```

   然后对ViewPager添加适配器,使用Fragment作为子页面.用getSupportFragmentManager()获取构造器的参数

   实现3个方法

   getItem 返回子页面对应索引的Fragment(如有两个以上子页面一般先建立ArrayList存储不同的子页面Fragment然后返回array[i],这里的两个子页面都是由同一个Fragment即PlaceholderFragment类创建的)

   getCount 返回子页面个数

   //非必须

   getPageTitle 返回子页面的标题,一般也是放在数组中再使用position索引返回数组元素

   ```java
   viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
               @Override
               public Fragment getItem(int i) {
                   return new PlaceholderFragment();
               }
   
               @Override
               public int getCount() {
                   return PAGE_COUNT;
               }
   
               @Nullable
               @Override
               public CharSequence getPageTitle(int position) {
                   boolean flag=(position==0);
                   return flag?"好友列表":"我的好友";
               }
           });
   ```

   最后是设置TabLayout包含ViewPager,或者说相关联

   ```java
   tabLayout.setupWithViewPager(viewPager);
   ```

3. 为ViewPage使用的子页面Fragment新建一个java类和xml布局文件.在java代码中添加控件变量,并在onCreateContentView中使用view.findViewById找到需要设置的控件

   ```java
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
       ...
   }
   ```

   使用getView()也可以获取到Fragment的view

   然后在onActivityCreated中添加延时的更改UI的线程

   lottie控件的淡出和列表控件的淡入分别使用一个ObjectAnimator来创建(修改alpha值,不需设置重复次数和模式,默认不重复即重复次数为0),再加入到AnimationSet中同时播放

   ```java
   ...
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
   ...
   ```
