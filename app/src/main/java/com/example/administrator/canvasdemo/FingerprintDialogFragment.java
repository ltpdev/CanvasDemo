package com.example.administrator.canvasdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;


@TargetApi(23)
public class FingerprintDialogFragment extends DialogFragment {
    private FingerprintManager fingerprintManager;
    private CancellationSignal cancellationSignal;
    private Cipher mCipher;
    private LoginActivity loginActivity;
    private TextView errorMsg;
    /**
     * 标识是否是用户主动取消的认证。
     */
    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher) {
       mCipher = cipher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity= (LoginActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fingerprintManager=getContext().getSystemService(FingerprintManager.class);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fingerprint_dialog,container,false);
        errorMsg=view.findViewById(R.id.error_msg);
        TextView cancel=view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                stopListening();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始指纹认证监听
        startListening(mCipher);
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止指纹认证监听
        stopListening();
    }

    private void startListening(Cipher cipher) {
         isSelfCancelled=false;
         cancellationSignal=new CancellationSignal();
         fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher), cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
             @Override
             public void onAuthenticationError(int errorCode, CharSequence errString) {
                 if (!isSelfCancelled){
                     errorMsg.setText(errString);
                     if (errorCode==FingerprintManager.FINGERPRINT_ERROR_LOCKOUT){
                         Toast.makeText(loginActivity,errString,Toast.LENGTH_SHORT).show();
                         dismiss();
                     }
                 }
             }

             @Override
             public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                 errorMsg.setText(helpString);
             }

             @Override
             public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                 Toast.makeText(loginActivity, "指纹认证成功", Toast.LENGTH_SHORT).show();
                 loginActivity.onAuthenticated();
             }

             @Override
             public void onAuthenticationFailed() {
                 errorMsg.setText("指纹认证失败，请再试一次");
             }
         },null);
    }

    private void stopListening() {
        if (cancellationSignal!=null){
            cancellationSignal.cancel();
            cancellationSignal=null;
            isSelfCancelled=true;
        }
    }
}
