package cn.droidlover.xdroidmvp.mvp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import android.view.LayoutInflater;
import android.view.Menu;

import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.droidlover.xdroidmvp.XDroidConf;
import cn.droidlover.xdroidmvp.XDroidMvpUtill;
import cn.droidlover.xdroidmvp.event.BusProvider;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by wanglei on 2016/12/29.
 */

public abstract class XActivity<P extends IPresent, E extends ViewBinding> extends RxAppCompatActivity implements IView<P> {

    private VDelegate vDelegate;
    private P p;
    protected Activity context;

    private RxPermissions rxPermissions;

    private E viewBinding;

    @Override
    public void onRefresh(Boolean bRefresh) {

    }

    protected final E getViewBinding() {
        return viewBinding;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getP();
        try {
            Class<E> eClass = getViewBindingClass();
            if (eClass != null) {
                Method method2 = eClass.getMethod("inflate", LayoutInflater.class);
                viewBinding = (E) method2.invoke(null, getLayoutInflater());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (viewBinding != null) {
            setContentView(viewBinding.getRoot());
        }
        bindEvent();
        initData(savedInstanceState);

    }

    protected Class<E> getViewBindingClass() {
        return XDroidMvpUtill.<E>getViewBindingClass(getClass());
    }

    protected VDelegate getvDelegate() {
        if (vDelegate == null) {
            vDelegate = VDelegateBase.create(context);
        }
        return vDelegate;
    }

    protected P getP() {
        if (p == null) {
            p = newP();
        }
        if (p != null) {
            if (!p.hasV()) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusProvider.getBus().register(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getvDelegate().resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        getvDelegate().pause();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            BusProvider.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        getvDelegate().destory();
        p = null;
        vDelegate = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getOptionsMenuId() > 0) {
            getMenuInflater().inflate(getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected RxPermissions getRxPermissions() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(XDroidConf.DEV);
        return rxPermissions;
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    @Override
    public void bindEvent() {

    }
}
