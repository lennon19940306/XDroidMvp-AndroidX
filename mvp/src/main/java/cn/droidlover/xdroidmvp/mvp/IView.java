package cn.droidlover.xdroidmvp.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Created by wanglei on 2016/12/29.
 */

public interface IView<P extends IPresent> {

    void bindEvent();

    void initData(Bundle savedInstanceState);

    int getOptionsMenuId();

    int getLayoutId();

    boolean useEventBus();

    P newP();

    void showProgressDialog(String msg);

    void closeProgressDialog();

    void onRefresh(Boolean bRefresh);
}
