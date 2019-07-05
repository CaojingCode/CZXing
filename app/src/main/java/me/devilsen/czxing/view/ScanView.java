package me.devilsen.czxing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import me.devilsen.czxing.BarcodeReader;
import me.devilsen.czxing.thread.Callback;
import me.devilsen.czxing.thread.Dispatcher;

/**
 * @author : dongSen
 * date : 2019-06-29 16:18
 * desc :
 */
public class ScanView extends BarCoderView implements Callback {

    private Dispatcher mDispatcher;

    public ScanView(Context context) {
        this(context, null);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDispatcher = new Dispatcher();
    }

    @Override
    public void onPreviewFrame(byte[] data, int left, int top, int width, int height, int rowWidth) {
//        SaveImageUtil.saveData(data, left, top, width, height, rowWidth);
        mDispatcher.newRunnable(data, left, top, width, height, rowWidth, this).enqueue();
    }

    @Override
    public void stopScan() {
        super.stopScan();
        mDispatcher.cancelAll();
    }

    @Override
    public void onDecodeComplete(BarcodeReader.Result result) {

        if (result != null) {
            if (result.getPoints() != null) {
                tryZoom(result);
            }
        }

    }

    private void tryZoom(BarcodeReader.Result result) {
        int len = 0;
        float[] points = result.getPoints();
        if (points.length > 3) {
            float point1X = points[0];
            float point1Y = points[1];
            float point2X = points[2];
            float point2Y = points[3];
            float xLen = Math.abs(point1X - point2X);
            float yLen = Math.abs(point1Y - point2Y);
            len = (int) Math.sqrt(xLen * xLen + yLen * yLen);
        }

        Log.e("len", len + "");

        if (len > 0) {
            handleAutoZoom(len);
        }
    }
}