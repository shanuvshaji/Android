package com.baseutilities.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public class BaseFragment extends Fragment {

    Validator mvalidator;
    private AlertDialog NoInternetAlert;
    private AlertDialog mProgressDialog;

    public SharedPreferencesHelper getSharedPreferencesHelper() {

      /*  if (mSharedPreferencesHelper != null) {
            return mSharedPreferencesHelper;
        } else {
            setSharedPreferencesHelper(new SharedPreferencesHelper(getActivity()));
            return mSharedPreferencesHelper;
        }
        */
        return SharedPreferencesHelper.getInstance(getContext());
    }

    public void setSharedPreferencesHelper(SharedPreferencesHelper mSharedPreferencesHelper) {
        this.mSharedPreferencesHelper = mSharedPreferencesHelper;
    }

    SharedPreferencesHelper mSharedPreferencesHelper;

    public Utilities getUtilities() {

        if (mUtilities != null) {
            return mUtilities;
        } else {
            setUtilities(new Utilities(getActivity()));
            return mUtilities;
        }
    }


    public Validator getValidator() {

        if (mvalidator == null)
            mvalidator = new Validator(getActivity());

        return mvalidator;
    }

    public void setUtilities(Utilities mUtilities) {
        this.mUtilities = mUtilities;
    }

    public void showToastMessage(String mMessage) {
        Toast.makeText(getContext(), mMessage, Toast.LENGTH_SHORT).show();
    }





    public void writeLog(String mMessage) {
        if (mMessage != null) {
            Log.i("LOG ", mMessage);
        }
    }

    Utilities mUtilities;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            if (activity != null && activity instanceof BaseActivity) {
                SharedPreferencesHelper mSharedPreferencesHelper = (((BaseActivity) activity)).getSharedPreferencesHelper();
                if (mSharedPreferencesHelper != null) {
                    setSharedPreferencesHelper(mSharedPreferencesHelper);
                }

                Utilities mUtilities = (((BaseActivity) activity)).getUtilities();

                if (mUtilities != null) {
                    setUtilities(mUtilities);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void showProgress(String mMessage) {

        try {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            View vv = getLayoutInflater().inflate(R.layout.progress_layout, null);
//
//            ImageView login_button = (ImageView) vv.findViewById(R.id.loading_image);
//            Glide.with(this)
//                    .load(R.drawable.loader)
//                    .into(login_button);
//            login_button.setColorFilter(ContextCompat.getColor(getActivity(), R.color.orange_300), android.graphics.PorterDuff.Mode.MULTIPLY);
//            builder.setView(vv);
//            builder.setCancelable(false);
//
//
//            if (mProgressDialog == null) {
//                mProgressDialog = builder.create();
//                mProgressDialog.show();
//                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            } else
//                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mProgressDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hideProgress() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }




    public void hideKeyboard() {
        // Check if no view has focus:
        try {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.writeLog("Failed to hide keyboard - " + e.getMessage());
        }
    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}