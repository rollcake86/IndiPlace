package com.rollcake.indi.indiplace.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.facebook.login.LoginManager
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.UnLinkResponseCallback
import com.rollcake.indi.indiplace.AppApplication
import com.rollcake.indi.indiplace.AppApplication.*
import com.rollcake.indi.indiplace.R
import com.rollcake.indi.indiplace.utils.AppkeyManager
import com.rollcake.indi.indiplace.utils.Logs
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    val TAG = SettingActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        pushSwitch.isChecked = AppkeyManager.getKey(this@SettingActivity, PUSH, true)

        if (pushSwitch.isChecked) {
            push_text.text = "푸쉬 허용"
        } else {
            push_text.text = "푸쉬 정지"
        }

        logoutBtn.setOnClickListener {
            if (!AppkeyManager.getKey(this@SettingActivity, LOGIN_CHECK, false)) {
                goToast(getString(R.string.guest_mode))
            } else {
                showArtistdialog("로그아웃 하시겠습니까?", true)
            }
        }

        removeBtn.setOnClickListener {
            if (!AppkeyManager.getKey(this@SettingActivity, LOGIN_CHECK, false)) {
                goToast(getString(R.string.guest_mode))
            } else {
                showArtistdialog("회원 탈퇴를 하시겠습니까?", false)
            }
        }

        backBtn.setOnClickListener {
            finish()
        }

        pushSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            AppkeyManager.setKet(this@SettingActivity, PUSH, true)

            if (isChecked) {
                push_text.text = "푸쉬 허용"
            } else {
                push_text.text = "푸쉬 정지"
            }
        }
    }


    fun showArtistdialog(message: String, logout: Boolean) {
        val builder = AlertDialog.Builder(this@SettingActivity)
        builder.setTitle("인디플레이스")
        builder.setMessage(message)
        builder.setPositiveButton("네 알겠습니다") { _, which ->
            if (logout) {
                getIdLogout(AppkeyManager.getKey(this@SettingActivity, LOGIN_TYPE, KAKAO))
            } else {
                getIdRemove(AppkeyManager.getKey(this@SettingActivity, LOGIN_TYPE, KAKAO))
            }
        }
        builder.setNegativeButton("아니요") { dialog, which ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getIdLogout(appKey: String) {
        if (appKey == KAKAO) {
            kakaoLogout()
        } else if (appKey == FACEBOOK) {
            LoginManager.getInstance().logOut()
            AppkeyManager.setKet(this@SettingActivity, AppApplication.LOGIN_CHECK, false)
            AppkeyManager.setKet(this@SettingActivity, AppApplication.LOGIN_TOKEN, "0")
            val intent = Intent(this@SettingActivity, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun kakaoLogout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                AppkeyManager.setKet(this@SettingActivity, AppApplication.LOGIN_CHECK, false)
                AppkeyManager.setKet(this@SettingActivity, AppApplication.LOGIN_TOKEN, "0")
                val intent = Intent(this@SettingActivity, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun getIdRemove(appKey: String) {
        if (appKey == KAKAO) {
            kakaoOnClickUnlink()
        } else if (appKey == FACEBOOK) {
            LoginManager.getInstance().logOut()
            val intent = Intent(this@SettingActivity, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun kakaoOnClickUnlink() {
        val appendMessage = getString(R.string.com_kakao_confirm_unlink)
        AlertDialog.Builder(this@SettingActivity)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        DialogInterface.OnClickListener { dialog, which ->
                            UserManagement.getInstance().requestUnlink(object : UnLinkResponseCallback() {
                                override fun onFailure(errorResult: ErrorResult?) {
                                    Logs.e(TAG, errorResult!!.toString())
                                }

                                override fun onSessionClosed(errorResult: ErrorResult) {
                                }

                                override fun onNotSignedUp() {
                                }

                                override fun onSuccess(userId: Long?) {
                                    AppkeyManager.setKet(this@SettingActivity, AppApplication.LOGIN_CHECK, false)
                                    val intent = Intent(this@SettingActivity, IntroActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                            dialog.dismiss()
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).show()

    }
}
