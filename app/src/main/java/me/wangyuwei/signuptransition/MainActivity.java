package me.wangyuwei.signuptransition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mFrtContent;

    private Scene mSceneSignUp;
    private Scene mSceneLogging;
    private Scene mSceneMain;

    private int mTvSighUpWidth, mTvSighUpHeight;
    private int mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrtContent = (FrameLayout) findViewById(R.id.frt_content);

        mDuration = getResources().getInteger(R.integer.duration);

        mSceneSignUp = Scene.getSceneForLayout(mFrtContent, R.layout.scene_sign_up, this);
        mSceneSignUp.setEnterAction(new Runnable() {
            @Override
            public void run() {
                final LoginLoadingView loginView = (LoginLoadingView) mFrtContent.findViewById(R.id.login_view);
                ViewTreeObserver vto = loginView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setSize(loginView.getMeasuredWidth(), loginView.getMeasuredHeight());
                    }
                });
                loginView.setOnClickListener(MainActivity.this);
            }
        });


        mSceneLogging = Scene.getSceneForLayout(mFrtContent, R.layout.scene_logging, this);
        mSceneLogging.setEnterAction(new Runnable() {
            @Override
            public void run() {
                final LoginLoadingView loginView = (LoginLoadingView) mFrtContent.findViewById(R.id.login_view);
                loginView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginView.setStatus(LoginLoadingView.STATUS_LOGGING);
                    }
                }, mDuration);
                loginView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginView.setStatus(LoginLoadingView.STATUS_LOGIN_SUCCESS);
                    }
                }, 4000);

                loginView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TransitionManager.go(mSceneMain, new ChangeBounds().setDuration(mDuration).setInterpolator(new DecelerateInterpolator()));
                    }
                }, 6000);
            }
        });

        mSceneMain = Scene.getSceneForLayout(mFrtContent, R.layout.scene_main, this);
        mSceneMain.setEnterAction(new Runnable() {
            @Override
            public void run() {
                final ImageView imgMenu = (ImageView) mFrtContent.findViewById(R.id.img_menu);
                final ImageView imgUser = (ImageView) mFrtContent.findViewById(R.id.img_user);
                ValueAnimator animator = ValueAnimator.ofInt(0, 255);
                animator.setDuration(mDuration);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int alpha = (int) animation.getAnimatedValue();
                        imgMenu.setImageAlpha(alpha);
                        imgUser.setImageAlpha(alpha);
                    }
                });
                animator.start();

                final RecyclerView recyclerView = (RecyclerView) mFrtContent.findViewById(R.id.rv_common);
                CommonAdapter adapter = new CommonAdapter(MainActivity.this);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                recyclerView.setAdapter(adapter);
            }
        });

        TransitionManager.go(mSceneSignUp);
    }

    private void setSize(int width, int height) {
        mTvSighUpWidth = width;
        mTvSighUpHeight = height;
    }

    @Override
    public void onClick(final View view) {

        float finalRadius = (float) Math.hypot(mFrtContent.getWidth(), mFrtContent.getHeight());

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        Animator anim = ViewAnimationUtils.createCircularReveal(mFrtContent, x + mTvSighUpWidth / 2, y - mTvSighUpHeight / 2, 100, finalRadius);
        mFrtContent.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorBg));
        anim.setDuration(mDuration);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFrtContent.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();

        TransitionManager.go(mSceneLogging, new ChangeBounds().setDuration(mDuration).setInterpolator(new DecelerateInterpolator()));
    }
}
