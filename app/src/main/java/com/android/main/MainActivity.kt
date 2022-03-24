package com.android.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.android.main.databinding.ActivityMainBinding
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.llj.baselib.IOTLib
import com.llj.baselib.IOTViewModel
import com.llj.baselib.ui.IOTMainActivity
import com.llj.baselib.utils.LogUtils
import com.llj.baselib.utils.ToastUtils


class MainActivity : IOTMainActivity<ActivityMainBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    private val vm by viewModels<MainVM>()

    override fun init() {
        super.init()
        initMainView()
//        vm.connect(this, MainDataBean::class.java)
    }

    private fun initMainView() {
        initToolbar()
        mDataBinding.tvScanner.setOnClickListener {
            prepareScanner()
        }
    }

    private fun initToolbar() {
        mDataBinding.toolbar.apply {
            toolbarBaseTitle.text = getString(R.string.app_name)
            toolbarBase.inflateMenu(R.menu.toolbar_menu)
            toolbarBase.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.quit_app -> {
                        startCommonActivity<LoginActivity>()
                        finish()
                    }
                }
                false
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun offDevLine() {
        mDataBinding.tvDevState.setTextColor(R.color.red)
        mDataBinding.tvDevState.text = "设备离线"
    }

    @SuppressLint("ResourceAsColor")
    override fun onDevLine() {
        mDataBinding.tvDevState.setTextColor(R.color.greenDark)
        mDataBinding.tvDevState.text = "设备在线"
    }

    override fun realData(data: Any?) {
        ToastUtils.toastShort("data:${(data as MainDataBean).toString()}")
    }

    override fun webState(state: IOTViewModel.WebSocketType) {
        mDataBinding.tvState.text = state.name
    }

    private fun prepareScanner() {
        ScanUtil.startScan(
            this,
            SAO_CODE,
            HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create()
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SAO_CODE && resultCode == RESULT_OK) {
            val d = data ?: return
            val content = d.getParcelableExtra<HmsScan>(ScanUtil.RESULT)?.originalValue
            if (content.isNullOrEmpty()) {
                ToastUtils.toastShort("未获取到二维码数据 请重试")
                return
            }
            kotlin.runCatching {
                val list = content.split(';')
                val name = list[0]
                val locationId = list[1].toInt()
                val phoneNumber = list[2]
                sendMessage(name,locationId,phoneNumber)
                LogUtils.d(IOTLib.TAG, "onActivityResult: 扫描结果为：$content")
            }.onFailure {
                ToastUtils.toastShort("二维码信息有误")
            }

        }
    }

    private fun sendMessage(name:String,id:Int,number:String){
        val content = """智能快递分拣系统提醒您：${name}先生/女士，您的快递已放置到${id}号储物台，请及时领取"""

        val smstoUri: Uri = Uri.parse("smsto:")
        val intent = Intent(Intent.ACTION_VIEW, smstoUri)
        intent.putExtra("address", number) // 没有电话号码的话为默认的，即显示的时候是为空的

        intent.putExtra("sms_body", content) // 设置发送的内容

        intent.type = "vnd.android-dir/mms-sms"
        startActivity(intent)
    }

    companion object {
        private const val SAO_CODE = 985
    }

}