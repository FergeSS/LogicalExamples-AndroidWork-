package com.company.enoxirsten;

import static com.company.enoxirsten.Settings.action;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.company.enoxirsten.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private ActivityGameBinding binding;
    public static boolean active = false;
    Dialog dialog;
    int curStr;
    Drawable correcrtAnswer;
    int counter = 0;

    int[] votesIcons = {R.drawable._1, R.drawable._2, R.drawable._3, R.drawable._4, R.drawable._5, R.drawable._6, R.drawable._7, R.drawable._8, R.drawable._9, R.drawable._0, R.drawable._11, R.drawable._12, R.drawable._13, R.drawable._14, R.drawable._15, R.drawable._16, R.drawable._17, R.drawable._18, R.drawable._19, R.drawable._20, R.drawable._21, R.drawable._22, R.drawable._23, R.drawable.x2, R.drawable.x3};
    int[] firstIcons = {R.drawable._6, R.drawable._6, R.drawable._3, R.drawable._17, R.drawable._17, R.drawable._9, R.drawable._13, R.drawable._8, R.drawable._1, R.drawable._0};
    int[] secondIcons = {R.drawable._18, R.drawable._2, R.drawable._2, R.drawable._20, R.drawable.x2, R.drawable._11, R.drawable._15, R.drawable._2, R.drawable._12, R.drawable._14};
    int[] thirdIcons = {R.drawable._16, R.drawable._7, R.drawable._4, R.drawable._22, R.drawable.x3, R.drawable._5, R.drawable._19, R.drawable._0, R.drawable._23, R.drawable._21};
    ImageButton[] buttons = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioSettings();
        windowSettings();
        dialogSettings();
        active = true;
    }

    public void audioSettings() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
    }

    public void windowSettings() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        buttons = new ImageButton[]{binding.one, binding.two, binding.three, binding.four, binding.five, binding.six, binding.seven, binding.eight, binding.nine, binding.ten, binding.eleven, binding.twelve};
        setNewLevel();
    }

    private void setNewLevel() {
        curStr = new Random().nextInt(10);
        List<Integer> votes = new ArrayList<Integer>();
        binding.firstIcon.setImageResource(firstIcons[curStr]);
        binding.seconIcon.setImageResource(secondIcons[curStr]);
        binding.thirdIcon.setImageResource(thirdIcons[curStr]);
        int question = new Random().nextInt(3);
        if (question == 2) {
            votes.add(thirdIcons[curStr]);
            correcrtAnswer = getResources().getDrawable(thirdIcons[curStr], getTheme());
            binding.thirdIcon.setImageResource(R.drawable.frame_21);
        }
        else if (question == 1) {
            votes.add(secondIcons[curStr]);
            correcrtAnswer = getResources().getDrawable(secondIcons[curStr], getTheme());
            binding.seconIcon.setImageResource(R.drawable.frame_21);
        }
        else {
            votes.add(firstIcons[curStr]);
            correcrtAnswer = getResources().getDrawable(firstIcons[curStr], getTheme());
            binding.firstIcon.setImageResource(R.drawable.frame_21);
        }
        Log.d("TAG", correcrtAnswer.toString());
        int i = 0;
        while (votesIcons[i] != votes.get(0)) {
            ++i;
        }
        for (int j = i + 1; j < i + 12; ++j) {
            votes.add(votesIcons[j % votesIcons.length]);
        }
        Collections.shuffle(votes);
        i = 0;
        for (int vote : votes) {
            buttons[i++].setBackgroundResource(vote);
        }

    }

    private void dialogSettings() {
        dialog = new Dialog(GameActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(wlp);
        dialog.setContentView(R.layout.dialog);
    }

    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void home(View v) {
        action(GameActivity.this);
        finish();
    }

    public void gameButton(View v) {
        action(GameActivity.this);
        ImageButton button = (ImageButton) v;
        try {
            if (button.getBackground().getConstantState().equals(correcrtAnswer.getConstantState())) {
                setNewLevel();
                ++counter;
                binding.counter.setText(String.valueOf(counter));
            }
            else {
                dialog.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

