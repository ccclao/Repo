package com.aotem.banner.view;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.aotem.banner.net.HttpCallback;
import com.aotem.banner.net.HttpClient;
import com.aotem.banner.net.HttpParam;
import com.aotem.banner.net.HttpThreadPool;
import com.aotem.banner.uitls.Constants;
import com.aotem.banner.uitls.LogUtils;
import com.aotem.banner.uitls.MD5Util;

import java.io.File;

/**
 * sub view 基类
 *
 * @author aotem
 * create by 11/20/20 2:35 PM
 */
public abstract class ShowView implements SubView {
    protected ImageView mImageCache;

    public ShowView(Context context) {
        mImageCache = new ImageView(context);
    }

    @Override
    public void onShowStart(final HBanner hBanner, int position) {

    }

    @Override
    public void onShowFinish() {
    }

    /**
     * 因为当前视图还未显示到屏幕中，所以并没有进行渲染，所以无法拿到drawCache。
     * 所以，还是建议给imagecache设置一个背景返回，充当过度，当然过度view只有首尾
     * 两个view才会有效，非首尾view并不会调用此方法。
     *
     * @return
     */
    @Override
    public View getPreView() {
        if (getView() == null)
            throw new RuntimeException("the getView can not be null!");
        return mImageCache;
    }

    @Override
    public String getTag() {
        return null;
    }

    protected void cacheFile(String url, HttpCallback callback) {
        HttpThreadPool.getInstance().post(() -> {
            HttpParam param = new HttpParam(url);
            param.setFileName(MD5Util.md5(url));
            param.setSavePath(Constants.DEFAULT_DOWNLOAD_DIR);
            HttpClient.getInstance().Get(param, callback);
        });
    }

    protected boolean isCached(final String url) {
        return getCacheFile(url).exists();
    }

    protected File getCacheFile(final String url) {
        return new File(Constants.DEFAULT_DOWNLOAD_DIR + File.separator + MD5Util.md5(url));
    }
}
