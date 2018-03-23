package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.ChangePasswordResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHANGEPASSWORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends BaseFragmentWithOptions {

    MainActivity mainActivity;
    EditText editTextOldPassword, editTextNewPassword, editConfirmPassword;
    TextInputLayout inputEditTextOldPassword, inputEditTextNewPassword, inputEditConfirmPassword;

    TextView TextOldPassword, TextNewPassword, ConfirmPassword;
    //ImageView confirmPasswordImage;

    Button changePassword;
    int password = 0, confirmPasword = 0;

    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    String getFragmentTag() {
        return "ChangePassword";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        Analytics.with(getContext()).screen("Change Password");
        editTextNewPassword = (EditText) view.findViewById(R.id.editTextNewPassword);
        editConfirmPassword = (EditText) view.findViewById(R.id.editConfirmPassword);
        editTextOldPassword = (EditText) view.findViewById(R.id.editTextOldPassword);
        // confirmPasswordImage= (ImageView) view.findViewById(R.id.confirmPasswordImage);
        inputEditTextOldPassword = (TextInputLayout) view.findViewById(R.id.inputEditTextOldPassword);
        inputEditConfirmPassword = (TextInputLayout) view.findViewById(R.id.inputEditConfirmPassword);
        inputEditTextNewPassword = (TextInputLayout) view.findViewById(R.id.inputEditTextNewPassword);
        TextOldPassword = (TextView) view.findViewById(R.id.TextOldPassword);
        TextNewPassword = (TextView) view.findViewById(R.id.TextNewPassword);
        ConfirmPassword = (TextView) view.findViewById(R.id.ConfirmPassword);
        TextOldPassword.setVisibility(View.GONE);
        TextNewPassword.setVisibility(View.GONE);
        ConfirmPassword.setVisibility(View.GONE);

        editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //printLog("Password:" + validPassword(charSequence.toString() + ""));
                if (validPassword(charSequence.toString())) {
                    password = 1;
                    TextNewPassword.setVisibility(View.GONE);
                } else {
                    password = 0;
                    TextNewPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        editConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                printLog(editConfirmPassword.getText().toString() + "||" + charSequence.toString());

                if (editConfirmPassword.getText().toString().equals(editTextNewPassword.getText().toString())) {
                    confirmPasword = 1;
                    // confirmPasswordImage.setVisibility(View.VISIBLE);
                    ConfirmPassword.setVisibility(View.GONE);
                } else {
                    confirmPasword = 0;
                    // confirmPasswordImage.setVisibility(View.GONE);
                    ConfirmPassword.setVisibility(View.VISIBLE);
                    ConfirmPassword.setText(getString(R.string.error_in_password_confirm_2));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                printLog(editTextOldPassword.getText().toString() + "||" + charSequence.toString());

                if (editTextOldPassword.getText().toString().trim().equals("")) {
                    TextOldPassword.setVisibility(View.VISIBLE);
                } else {
                    TextOldPassword.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        changePassword = (Button) view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextOldPassword.getText().toString().trim().equals("")) {
                    TextOldPassword.setVisibility(View.VISIBLE);
                    return;
                } else if (editTextNewPassword.getText().toString().trim().equals("")) {
                    TextNewPassword.setVisibility(View.VISIBLE);
                    return;
                } else if (editConfirmPassword.getText().toString().trim().equals("")) {
                    ConfirmPassword.setVisibility(View.VISIBLE);
                    return;
                }
                if (password == 1 && confirmPasword == 1) {
                    Analytics.with(getContext()).track("Change Password", new Properties().putValue("category", "Mobile"));
                    changePasswordData();
                }
            }
        });

        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        TextView back = (TextView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

    }


    private boolean validPassword(String password) {
        printLog("PasswordToIdentify:" + password);
        boolean upCase = false;
        boolean loCase = false;
        boolean isDigit = false;
        if (password.length() > 6) {
            if (password.matches(".*[A-Z].*")) {
                upCase = true;
            }
            if (password.matches(".*[a-z].*")) {
                loCase = true;
            }
            if (password.matches(".*[1-9].*")) {
                isDigit = true;
            }
        }
        printLog("||Upcase:" + upCase + "||LoCase" + loCase + "||Isdigit" + isDigit);
        return (upCase && loCase && isDigit);
    }

    /*private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editTextOldPassword:
                    validateOldPassword();
                    break;
                case R.id.editTextNewPassword:
                    validatePassword();
                    break;
                case R.id.editConfirmPassword:
                    validateConfirmPassword();
                    break;
            }
        }
    }
*/

    private void changePasswordData() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("oldPassword", editTextOldPassword.getText().toString());
                params.put("newPassword", editTextNewPassword.getText().toString());

                JSONObject requestJson = new JSONObject(new Gson().toJson(params));

                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_CHANGEPASSWORD, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);
                        if (response != null) {
                            ChangePasswordResponse surveyPlanDto = new Gson().fromJson(response.toString(), ChangePasswordResponse.class);
                            if (surveyPlanDto != null) {
                                if (surveyPlanDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                                    View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
                                    builder.setView(dialogView);
                                    final android.support.v7.app.AlertDialog alertDialog = builder.create();
                                    TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
                                    TextViewMessage.setText(surveyPlanDto.getMessage());
                                    Button buttonOk = (Button) dialogView.findViewById(R.id.ok_btn);
                                    alertDialog.setCancelable(false);
                                    buttonOk.setText(getString(R.string.ok));
                                    buttonOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alertDialog.dismiss();
                                            //backPage();
                                            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOCAL_DB_TOUCH, false);
                                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    Window window = alertDialog.getWindow();
                                    if (window != null) {
                                        WindowManager.LayoutParams wmlp = window.getAttributes();
                                        wmlp.windowAnimations = R.style.dialog_animation;
                                        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        window.setAttributes(wmlp);
                                    }
                                    alertDialog.show();

                                } else {
                                    showDialogWithOkButton(surveyPlanDto.getMessage());
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
            } catch (Exception e) {
            }
        } else {
            showNoNetworkMessage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    backPage();
                    return true;
                }
                return false;
            }
        });
    }

    private void backPage() {
       /* FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("ChangePassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        getFragmentManager().popBackStack();
    }


    private boolean validatePassword() {
        if (editTextNewPassword.getText().toString().trim().isEmpty()) {
            inputEditTextNewPassword.setError(getString(R.string.err_msg_newpassword));
            //editTextNewPassword.requestFocus();
            return false;
        }
        if (editTextNewPassword.getText().toString().trim().length() < 6) {
            inputEditTextNewPassword.setError(getString(R.string.err_msg_pass_6_dig));
            //editTextNewPassword.requestFocus();
            return false;
        } else {
            inputEditTextNewPassword.setErrorEnabled(false);
            inputEditTextNewPassword.setError(null);
        }

        return true;
    }

   /* private boolean validateName() {
        if (input_name.getText().toString().trim().isEmpty()) {
            input_layout_name.setError(getString(R.string.err_msg_name));
            input_name.requestFocus();
            return false;
        } else if (!validatingName(input_name.getText().toString())) {
            input_layout_name.setError(getString(R.string.err_msg_validfname));
            input_name.requestFocus();
            return false;
        } else {
            input_layout_name.setErrorEnabled(false);
            input_layout_name.setError(null);
        }

        r*/

    public static boolean validatingPassword(String firstName) {
        return firstName.matches("((?=.*\\d)(?=.*[a-z-A-Z]).{0,})");
    }

    private boolean validateOldPassword() {
        if (editTextOldPassword.getText().toString().trim().isEmpty()) {
            inputEditTextOldPassword.setError(getString(R.string.err_msg_oldpassword));
            //editTextOldPassword.requestFocus();
            return false;
        }
        if (editTextOldPassword.getText().toString().trim().length() < 6) {
            inputEditTextOldPassword.setError(getString(R.string.err_msg_pass_6_dig));
            // editTextOldPassword.requestFocus();
            return false;
        } else {
            inputEditTextOldPassword.setErrorEnabled(false);
            inputEditTextOldPassword.setError(null);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        if (editConfirmPassword.getText().toString().trim().isEmpty()) {
            inputEditConfirmPassword.setError(getString(R.string.err_msg_confirmnewpassword));
            // editConfirmPassword.requestFocus();
            return false;
        } else if (!editConfirmPassword.getText().toString().trim().equals(editTextNewPassword.getText().toString().trim())) {
            inputEditConfirmPassword.setError(getString(R.string.err_msg_confirm_mismatch_newpassword));
            //  editConfirmPassword.requestFocus();
            return false;
        } else {
            inputEditConfirmPassword.setErrorEnabled(false);
            inputEditConfirmPassword.setError(null);
        }

        return true;
    }


}
