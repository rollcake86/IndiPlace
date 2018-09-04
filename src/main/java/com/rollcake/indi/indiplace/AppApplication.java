package com.rollcake.indi.indiplace;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.rollcake.indi.indiplace.utils.LruBitmapCache;


/**
 * Created by Administrator on 2018-03-12.
 */

public class AppApplication extends MultiDexApplication {

    private static AppApplication mInstance;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    private static final String TAG = AppApplication.class.getSimpleName();

    public static final String DOMAIN = "http://sunju.pythonanywhere.com/indiplace";

    // 회원 정보
    public static final String LOGIN_TOKEN = "login_token";
    public static final String LOGIN_TYPE = "login_type";
    public static final String NICK_NAME = "nickname";
    public static final String EMAIL = "email";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String LOCATION = "location";
    public static final String LOGIN_CHECK = "logincheck";
    public static final String POGINT_CHECK = "point_check";
    public static final String DATE = "date";
    public static final String TOKEN = "token";
    public static final String POINT = "point";
    public static final String COUNT = "count";
    public static final String MEMBER_ID = "memberid";

    public static final String NORMAL= "NORMAL";
    public static final String ARTIST= "ARTIST";
    public static final String MEMBER= "member";

    // 아티스트 정보
    public static final String ARTIST_ID = "artist_id";
    public static final String ARTIST_NAME = "artist_name";
    public static final String INSTA_ADDR = "insta";
    public static final String FACEBOOK_ADDR = "facebook";
    public static final String YOUTUBE_ADDR = "youtube";
    public static final String GENER = "gener";
    public static final String ARTIST_LOC = "artist_loc";
    public static final String ARTIST_IMG = "artist_img";


    public static final String PUSH = "push";
    public static final String FIRST = "first";

    public static final String FACEBOOK = "faceBookId";
    public static final String KAKAO = "kakaoTalkId";



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSDK.init(new KakaoSDKAdapter());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config에 대해서는 default값들이 존재한다.
         * 필요한 상황에서만 override해서 사용하면 됨.
         * @return Session의 설정값.
         */
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return AppApplication.this.getApplicationContext();
                }
            };
        }
    }

    public static synchronized AppApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}

