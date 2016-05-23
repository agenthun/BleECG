package com.agenthun.bleecg.connectivity.manager;

import com.agenthun.bleecg.App;
import com.agenthun.bleecg.bean.AllDynamicDataByContainerId;
import com.agenthun.bleecg.bean.DynamicDataDetailByPositionId;
import com.agenthun.bleecg.bean.FreightInfoByImplementID;
import com.agenthun.bleecg.bean.FreightInfosByToken;
import com.agenthun.bleecg.bean.MACByOpenCloseContainer;
import com.agenthun.bleecg.bean.ResetImplementByContainerId;
import com.agenthun.bleecg.bean.UserInfoByGetToken;
import com.agenthun.bleecg.connectivity.service.Api;
import com.agenthun.bleecg.connectivity.service.FreightTrackWebService;
import com.agenthun.bleecg.connectivity.service.PathType;
import com.agenthun.bleecg.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/2 下午8:59.
 */
public class RetrofitManager {

    //设缓存有效期为一天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    public static final String TOKEN = "TOKEN";

    private final FreightTrackWebService freightTrackWebService;
    private static OkHttpClient mOkHttpClient;


    //创建实例
    public static RetrofitManager builder(PathType pathType) {
        return new RetrofitManager(pathType);
    }

    //配置Retrofit
    public RetrofitManager(PathType pathType) {
        initOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getPath(pathType)).client(mOkHttpClient)
                .addConverterFactory(XMLGsonConverterFactory.create())
                .build();
        freightTrackWebService = retrofit.create(FreightTrackWebService.class);
    }

    //配置OKHttpClient
    private void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitManager.class) {
                if (mOkHttpClient == null) {
                    File cacheFile = new File(App.getContext().getCacheDir(), "HttpCache");
                    Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);
                    Interceptor interceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (!NetUtil.isConnected(App.getContext())) {
                                request = request.newBuilder()
                                        .cacheControl(CacheControl.FORCE_CACHE)
                                        .build();
                            }
                            Response response = chain.proceed(request);
                            if (NetUtil.isConnected(App.getContext())) {
                                String cacheControl = request.cacheControl().toString();
                                return response.newBuilder()
                                        .header("Cache-Control", cacheControl)
                                        .removeHeader("Pragma").build();
                            } else {
                                return response.newBuilder()
                                        .header("Cache-Control", "public, only-if-cache, " + CACHE_STALE_SEC)
                                        .removeHeader("Pragma").build();
                            }
                        }
                    };
                    mOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(interceptor)
                            .addInterceptor(interceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    //获取相应的web路径
    private String getPath(PathType pathType) {
        switch (pathType) {
            case BASE_WEB_SERVICE:
                return Api.K_API_BASE_URL_STRING;
            case AMAP_SERVICE:
                return Api.AMAP_SERVICE_URL_STRING;
        }
        return "";
    }

    public static String getCacheControlCache() {
        return NetUtil.isConnected(App.getContext()) ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }


    //登陆,获取token
    public Call<UserInfoByGetToken> getTokenObservable(String userName, String password) {
        return freightTrackWebService.userInfoByGetToken(userName, password, "l");
/*                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());*/
    }


    //根据Token获取所有在途中的货物信息
    public Call<FreightInfosByToken> getFreightListObservable(String token) {
        return freightTrackWebService.getFreightInfoByToken(token, "l");
    }

    //根据设备ID获取具体某一货物信息 implementID="12345678" //2016-03-03 find not support
    public Call<FreightInfoByImplementID> getFreightObservable(String token, String implementID) {
        return freightTrackWebService.getFreightInfoByImplementID(token, implementID, "l");
    }

    //设置某货物终端监控参数 containerId=1070, frequency=2
    public Call<ResetImplementByContainerId> setFreightObservable(String token, String containerId, String frequency) {
        return freightTrackWebService.resetImplement(token, containerId, frequency, "0", "0", "0", "l");
    }


    //获取集装箱数据列表 containerId=1070, currentPageIndex=1
    public Call<AllDynamicDataByContainerId> getFreightDataListObservable(final String token, final String containerId, final Integer currentPageIndex) {
        return freightTrackWebService.getAllDynamicData(token, containerId, currentPageIndex, "l");
/*                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());*/
    }

    //获取集装箱某一具体数据 positionId="268996"
    public Call<DynamicDataDetailByPositionId> getFreightDataObservable(String token, String positionId) {
        return freightTrackWebService.getDynamicDataDetail(token, positionId, "l");
    }


    //开箱操作 - 获取MAC implementID="12345678", random=1234
    public Call<MACByOpenCloseContainer> getMACByOpenOperationObservable(String token, String implementID, String random) {
        return freightTrackWebService.openContainer(token, implementID, random, "l");
    }

    //关箱操作 - 获取MAC implementID="12345678", random=1234
    public Call<MACByOpenCloseContainer> getMACByCloseOperationObservable(String token, String implementID, String random) {
        return freightTrackWebService.closeContainer(token, implementID, random, "l");
    }


    //根据ContainerId获取轨迹 Type="0", containerId="1070"
    public Call<ResponseBody> getFreightTrackPathObservable(String token, String type, String containerId) {
        return freightTrackWebService.getFreightTrackPath(token, type, containerId, "l");
    }
}
