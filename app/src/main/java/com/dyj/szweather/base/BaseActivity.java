package com.dyj.szweather.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;


import com.dyj.szweather.util.MyUtil;
import com.dylanc.viewbinding.base.ViewBindingUtil;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/25 9:45
 * @description：Activity的base类
 * @modified By：
 * @version: 1.0
 */
public abstract class BaseActivity<P extends BasePresenter<? extends BaseView>,VB extends ViewBinding> extends AppCompatActivity implements BaseView{

    private VB binding;
    /**
     * presenter层的引用
     */
    protected P presenter;

    /**
     * 错误
     *
     * @param bean 错误信息
     */
    @Override
    public void onErrorCode(BaseBean bean) {

    }

    /**
     * 初始化presenter，也是与Activity的绑定
     *
     * @return 返回new的Presenter层的值
     */
    protected abstract P createPresenter();


    /**
     * 载入view的一些操作
     */
    protected abstract void initView();


    /**
     * 载入数据操作
     */
    protected abstract void initData();


    /**
     * {@inheritDoc}
     * <p>
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //强制使用竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ViewBindingUtil.inflateWithGeneric(this, getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = createPresenter();

        initView();
        initData();
    }

    /**
     * 解除presenter与Activity的绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        MyUtil.showLoading(this);
    }

    @Override
    public void SuccessHideLoading() {
        MyUtil.dismissSuccessLoading();
    }

    @Override
    public void FailedHideLoading() {
        MyUtil.dismissFailedLoading();
    }

    /**
     * 查看当前是否为深色模式
     *
     * @param context 传入当前context
     * @return 返回ture 偶然false
     */
    public Boolean getDarkModeStatus(Context context){
        int mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }

    public VB getBinding() {
        return binding;
    }
}