/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */

package com.parkingwang.keyboard.view;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;

import com.parkingwang.keyboard.Texts;
import com.parkingwang.keyboard.engine.KeyEntry;
import com.parkingwang.keyboard.engine.KeyType;
import com.parkingwang.keyboard.engine.KeyboardEntry;
import com.parkingwang.keyboard.engine.NumberType;

import java.util.List;

/**
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @author 陈永佳 (chenyongjia@parkingwang.com)
 */
public class VinKeyboardView extends KeyboardView {

    private static final String TAG = "VinKeyboardView";

    public VinKeyboardView(Context context) {
        super(context);
    }

    /**
     * 更新车牌键盘。
     * 此操作会触发KeyboardCallback回调。
     *
     * @param number          当前已输入的车牌
     * @param showIndex       当前正在修改的车牌字符所在的序号
     * @param showMore        是否显示“更多”按钮
     * @param fixedNumberType 指定车牌号类型
     */
    @Override
    public void update(@NonNull final String number, final int showIndex, final boolean showMore, final NumberType fixedNumberType) {
        mStashedNumber = number;
        mStashedIndex = showIndex;
        mStashedNumberType = fixedNumberType;
        // 更新键盘布局
        final KeyboardEntry keyboard = mKeyboardEngine.update(number, showIndex, showMore, fixedNumberType);
        renderLayout(keyboard);
        // 触发键盘变更回调
        try {
            for (OnKeyboardChangedListener listener : mKeyboardChangedListeners) {
                listener.onKeyboardChanged(keyboard);
            }
        } catch (Exception e) {
            Log.e(TAG, "On keyboard changed", e);
        }
    }


    private void renderLayout(KeyboardEntry keyboard) {
        // 以第一行的键盘数量为基准
        final int maxColumn = keyboard.layout.get(0).size();

        final int rowSize = keyboard.layout.size();
        mKeyCacheHelper.recyclerKeyRows(this, rowSize);

        for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
            List<KeyEntry> keyEntryRow = keyboard.layout.get(rowIndex);
            KeyRowLayout rowLayout = (KeyRowLayout) getChildAt(rowIndex);
            rowLayout.setMaxColumn(maxColumn);

            final int columnSize = keyEntryRow.size();
            int funKeyCount = 0;
            for (KeyEntry keyEntry : keyEntryRow) {
                if (keyEntry.isFunKey) {
                    funKeyCount++;
                }
            }
            rowLayout.setFunKeyCount(funKeyCount);

            mKeyCacheHelper.recyclerKeyViewsInRow(rowLayout, columnSize, mOnKeyPressedListener);
            for (int i = 0, size = keyEntryRow.size(); i < size; i++) {
                KeyEntry key = keyEntryRow.get(i);
                KeyView keyView = (KeyView) rowLayout.getChildAt(i);
                if (mBubbleTextColor != -1) {
                    keyView.setBubbleTextColor(mBubbleTextColor);
                }

                if (key.keyType == KeyType.FUNC_OK && mOkKeyTintColor != null) {
                    keyView.setOkKeyTintColor(mOkKeyTintColor);
                }
                keyView.bindKey(key);
                if (key.keyType == KeyType.FUNC_DELETE) {
                    keyView.setText("");
                } else {
                    keyView.setText(key.text);
                }
                if (Texts.isEnglishLetterOrDigit(key.text)) {
                    keyView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mENTextSize);
                } else {
                    keyView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mCNTextSize);
                }
                keyView.setShowBubble(mShowBubble);
                keyView.setEnabled(key.enabled);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
