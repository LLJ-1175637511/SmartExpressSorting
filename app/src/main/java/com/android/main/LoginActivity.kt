package com.android.main

import android.Manifest
import com.android.main.databinding.ActivityLoginBinding
import com.llj.baselib.ui.IOTLoginActivity
import com.llj.baselib.utils.ToastUtils


class LoginActivity : IOTLoginActivity<ActivityLoginBinding>() {

    override fun getLayoutId() = R.layout.activity_login

    override fun initPermission() = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.VIBRATE,
        Manifest.permission.SEND_SMS,
    )

    override fun init() {
        super.init()
        mDataBinding.apply {
            etUserNameLogin.setText(getUserInfo().first)
            etUserPwdLogin.setText(getUserInfo().second)
            btLogin.setOnClickListener {
                val name = etUserNameLogin.text.toString()
                val pwd = etUserPwdLogin.text.toString()
                if (name == "nilin123" && pwd == "123456"){
                    login(name,pwd, MainActivity::class.java)
                }else{
                    ToastUtils.toastShort("登陆失败")
                }
            }
        }
    }

}