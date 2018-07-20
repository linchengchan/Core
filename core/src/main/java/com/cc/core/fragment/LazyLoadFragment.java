package com.cc.core.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cc.core.Core;
import com.cc.core.activity.ISingleActivity;
import com.cc.core.global.PermissionHelper;
import com.cc.core.util.CoreUtils;
import com.cc.core.widget.ETFragmentView;

public abstract class LazyLoadFragment extends Fragment {

    protected PermissionHelper permissionHelper;

    private boolean isCreatedView;

    private boolean loadedData;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            arguments(getArguments());
        }
    }

    @CallSuper
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreatedView && !loadedData) {
            loadedData = true;
            lazyLoadData();
        }
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        View root = null;
        if (layoutId != 0) {
            root = inflater.inflate(getLayoutId(), container, false);
        }
        initView(root);
        isCreatedView = true;
        return root;
    }

    @CallSuper
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getUserVisibleHint() && !loadedData) {
            loadedData = true;
            lazyLoadData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
            permissionHelper = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        View view = getView();
        if (view instanceof ETFragmentView) {
            CoreUtils.hideSoftKeyboard(getContext(), view.getWindowToken());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCreatedView = false;
        loadedData = false;
        if (Core.debug()) {
            String fragmentName = getClass().getSimpleName();
            Log.d(fragmentName, "onDestroyView()-> " + fragmentName);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Core.debug()) {
            String fragmentName = getClass().getSimpleName();
            Log.d(fragmentName, "onDestroy()-> " + fragmentName);
        }
    }

    protected void arguments(@Nullable Bundle bundle) {

    }

    protected abstract int getLayoutId();

    protected abstract void initView(View root);

    protected abstract void lazyLoadData();

    protected boolean isCreatedView() {
        return isCreatedView;
    }

    public final void startFragment(Fragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof ISingleActivity) {
            ((ISingleActivity) activity).addFragmentToBackStack(fragment);
        }
    }

    public final void finish() {
        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof ISingleActivity) {
            ((ISingleActivity) activity).popFragment(this);
        }
    }


    public void checkCameraPermissionAndRequest(@Nullable Runnable grantAction, @Nullable Runnable deniedAction) {
        checkPermissionAndRequest(Manifest.permission.CAMERA, grantAction, deniedAction);
    }

    public void checkExternalStoragePermissionAndRequest(@Nullable Runnable grantAction, @Nullable Runnable deniedAction) {
        checkPermissionAndRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE, grantAction, deniedAction);
    }

    private void checkPermissionAndRequest(String permission, @Nullable Runnable grantAction, @Nullable Runnable deniedAction) {
        Context context = getContext();
        assert context != null;
        int hasPermission = ContextCompat.checkSelfPermission(context, permission);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            if (grantAction != null) {
                grantAction.run();
            }
        } else {
            if (permissionHelper != null) {
                throw new IllegalStateException("a permission requesting");
            }
            permissionHelper = new PermissionHelper.Builder(this, 1)
                    .permission(permission, grantAction, deniedAction)
                    .build();
        }
    }

}
