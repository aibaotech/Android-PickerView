package com.parkingwang.keyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Window;

import androidx.annotation.ColorInt;

import com.parkingwang.keyboard.engine.KeyboardEngine;
import com.parkingwang.keyboard.view.VinInputView;
import com.parkingwang.keyboard.view.VinKeyboardView;

/**
 * @author Yoojia Chen (yoojiachen@gmail.com)
 * @version 2017-11-03 0.1
 * @since 2017-11-03 0.1
 */
public class VinPopupKeyboard {

    private final VinKeyboardView mKeyboardView;

    private VinKeyboardInputController mController;

    private boolean isDialog = false;

    public VinPopupKeyboard(Context context) {
        mKeyboardView = new VinKeyboardView(context);
    }

    public VinPopupKeyboard(Context context, @ColorInt int bubbleTextColor, ColorStateList okKeyBackgroundColor) {
        mKeyboardView = new VinKeyboardView(context);
        mKeyboardView.setBubbleTextColor(bubbleTextColor);
        mKeyboardView.setOkKeyTintColor(okKeyBackgroundColor);
    }

    public VinKeyboardView getKeyboardView() {
        return mKeyboardView;
    }

    public void attach(VinInputView inputView, final Activity activity) {
        isDialog = false;
        attach(inputView, activity.getWindow());
    }

    public void attach(VinInputView inputView, final Dialog dialog) {
        isDialog = true;
        attach(inputView, dialog.getWindow());
    }

    private void attach(VinInputView inputView, final Window window) {
         if (mController == null) {
            mController = VinKeyboardInputController
                    .with(mKeyboardView, inputView);
//            mController.useDefaultMessageHandler();

            inputView.addOnFieldViewSelectedListener(new VinInputView.OnFieldViewSelectedListener() {
                @Override
                public void onSelectedAt(int index) {
                    show(window);
                }
            });
        }
    }

    public VinKeyboardInputController getController() {
        return checkAttachedController();
    }

    public KeyboardEngine getKeyboardEngine() {
        return mKeyboardView.getKeyboardEngine();
    }

    public void show(Activity activity) {
        show(activity.getWindow());
    }

    public void show(Window window) {
        checkAttachedController();
        PopupHelper.showToWindow(window, mKeyboardView, isDialog);
    }

    public void dismiss(Activity activity) {
        dismiss(activity.getWindow());
    }

    public void dismiss(Window window) {
        checkAttachedController();
        PopupHelper.dismissFromWindow(window);
    }

    public boolean isShown() {
        return mKeyboardView.isShown();
    }

    private VinKeyboardInputController checkAttachedController() {
        if (mController == null) {
            throw new IllegalStateException("Try attach() first");
        }
        return mController;
    }

}
