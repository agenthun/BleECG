package com.agenthun.bleecg.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.agenthun.bleecg.R;
import com.agenthun.bleecg.activity.ReportActivity;
import com.agenthun.bleecg.activity.TipsActivity;
import com.agenthun.bleecg.utils.ApiLevelHelper;
import com.agenthun.bleecg.utils.DataLogUtils;
import com.agenthun.bleecg.view.HeartRateTextView;
import com.txusballesteros.SnakeView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @project BleECG
 * @authors agenthun
 * @date 16/5/24 16:39.
 */
public class ECGHelperFragment extends Fragment {
    private static final String TAG = "ECGHelperFragment";
    private View replayView;
    private FrameLayout replayContainer;

    private Animator mCircularReveal;
    private ObjectAnimator mColorChange;
    private Interpolator mInterpolator;

    private static final int MSG_WHAT_RAW = 0x100;
    private static final int MSG_WHAT_RATE = 0x101;

    private boolean isReplay = false;

    @Bind(R.id.current_heart_rate)
    HeartRateTextView textCurrentHeartRate;

    @Bind(R.id.snake)
    SnakeView snakeView;

    public static ECGHelperFragment newInstance() {
        Bundle args = new Bundle();
        ECGHelperFragment fragment = new ECGHelperFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ecg_helper, container, false);
        ButterKnife.bind(this, view);

        replayView = view.findViewById(R.id.replay_page);
        replayContainer = (FrameLayout) view.findViewById(R.id.replay_container);

        mInterpolator = new FastOutSlowInInterpolator();

        return view;
    }

    int dataIndex = 0;

    private Timer timer = new Timer();

    @OnClick(R.id.replay_page)
    public void onReplayBtnClick() {
        revealFragmentContainer(replayView, replayContainer);
        //初始化波形显示界面
        clearWaveView();
        textCurrentHeartRate.setText("60");
        textCurrentHeartRate.setBeating(true);

        byte[] buffer = DataLogUtils.FileToBytes();
        final String[] dataStr = new String(buffer).split("\n");
        dataIndex = 0;
        isReplay = true;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isReplay && dataIndex < dataStr.length) {
                    Message msg = new Message();
                    String[] tmp = dataStr[dataIndex].split(" ");
                    msg.what = tmp[0].equals(DataLogUtils.RAW_TYPE) ? MSG_WHAT_RAW : MSG_WHAT_RATE;
                    msg.arg1 = Integer.parseInt(tmp[1]);
                    handler.sendMessage(msg);

                    dataIndex++;
                } else if (dataIndex == dataStr.length) {
                    if (isReplay) {
                        isReplay = false;
                        textCurrentHeartRate.setBeating(false);
                    }
                }
            }
        }, 1500, 10);
    }

    @OnClick(R.id.closeFab)
    public void onCloseReplayBtnClick() {
        isReplay = false;
        replayContainer.setVisibility(View.GONE);
        replayView.setVisibility(View.VISIBLE);
        ViewCompat.animate(replayView)
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(mInterpolator)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        replayView.setVisibility(View.VISIBLE);
                        replayContainer.setVisibility(View.GONE);
                    }
                })
                .start();

        handler.removeCallbacksAndMessages(null);
        timer.cancel();
        timer = new Timer();
    }

    @OnClick(R.id.card_report)
    public void onReportBtnClick() {
        Intent intent = new Intent(getContext(), ReportActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.card_tips)
    public void onTipsBtnClick() {
        Intent intent = new Intent(getContext(), TipsActivity.class);
        startActivity(intent);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_WHAT_RAW:
                    Log.d(TAG, "handleMessage: raw = " + msg.arg1);
                    updateWaveView(msg.arg1);
                    break;
                case MSG_WHAT_RATE:
                    Log.d(TAG, "handleMessage: rate = " + msg.arg1);
                    int heartRate = (msg.arg1 & 0xff);
                    textCurrentHeartRate.setText(Integer.toString(heartRate));
                    break;
            }
        }
    };

    public void updateWaveView(int data) {
        float point = (float) (data * 2048.0 / 32768.0);
        if (point > 512) point = 512;
        if (point < -512) point = -512;
        snakeView.addValue(point);
        Log.d(TAG, "float point: " + point);
    }

    public void clearWaveView() {
        snakeView.clear();
    }

    private void revealFragmentContainer(View clickedView, FrameLayout fragmentContainer) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            revealFragmentContainerLollipop(clickedView, fragmentContainer);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
            clickedView.setVisibility(View.GONE);
//            mIcon.setVisibility(View.GONE);
/*            ViewCompat.animate(fab).scaleX(1).scaleY(1)
                    .setInterpolator(new LinearOutSlowInInterpolator())
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View view) {
                            if ((ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1))) {
                                return;
                            }
                            fab.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();*/
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealFragmentContainerLollipop(final View clickedView, final FrameLayout fragmentContainer) {
        prepareCircularReveal(clickedView, fragmentContainer);

        ViewCompat.animate(clickedView)
                .scaleX(0)
                .scaleY(0)
                .alpha(0)
                .setInterpolator(mInterpolator)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        fragmentContainer.setVisibility(View.VISIBLE);
                        clickedView.setVisibility(View.GONE);
                    }
                })
                .start();

        fragmentContainer.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(mCircularReveal).with(mColorChange);
        animatorSet.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void prepareCircularReveal(View startView, FrameLayout targetView) {
        int centerX = (startView.getLeft() + startView.getRight()) / 2;
        // Subtract the start view's height to adjust for relative coordinates on screen.
        int centerY = (startView.getTop() + startView.getBottom()) / 2 - startView.getHeight();
        float endRadius = (float) Math.hypot(centerX, centerY);
        mCircularReveal = ViewAnimationUtils.createCircularReveal(
                targetView, centerX, centerY, startView.getWidth(), endRadius);
        mCircularReveal.setInterpolator(new FastOutLinearInInterpolator());

        mCircularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                mIcon.setVisibility(View.GONE);
                mCircularReveal.removeListener(this);
            }
        });
        // Adding a color animation from the FAB's color to transparent creates a dissolve like
        // effect to the circular reveal.
        int accentColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        mColorChange = ObjectAnimator.ofInt(targetView, FOREGROUND_COLOR, accentColor, Color.TRANSPARENT);
        mColorChange.setEvaluator(new ArgbEvaluator());
        mColorChange.setInterpolator(mInterpolator);
    }

    public static final Property<FrameLayout, Integer> FOREGROUND_COLOR =
            new IntProperty<FrameLayout>("foregroundColor") {

                @Override
                public void setValue(FrameLayout layout, int value) {
                    if (layout.getForeground() instanceof ColorDrawable) {
                        ((ColorDrawable) layout.getForeground().mutate()).setColor(value);
                    } else {
                        layout.setForeground(new ColorDrawable(value));
                    }
                }

                @Override
                public Integer get(FrameLayout layout) {
                    if (layout.getForeground() instanceof ColorDrawable) {
                        return ((ColorDrawable) layout.getForeground()).getColor();
                    } else {
                        return Color.TRANSPARENT;
                    }
                }
            };

    public static abstract class IntProperty<T> extends Property<T, Integer> {

        public IntProperty(String name) {
            super(Integer.class, name);
        }

        public abstract void setValue(T object, int value);

        @Override
        final public void set(T object, Integer value) {
            //noinspection UnnecessaryUnboxing
            setValue(object, value.intValue());
        }
    }
}
