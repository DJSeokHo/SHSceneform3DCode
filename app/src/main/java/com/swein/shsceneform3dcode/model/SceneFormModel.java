package com.swein.shsceneform3dcode.model;

import com.swein.shsceneform3dcode.constants.WebConstants;
import com.swein.shsceneform3dcode.framework.util.debug.ILog;
import com.swein.shsceneform3dcode.framework.util.okhttp.OKHttpWrapper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class SceneFormModel {

    private final static String TAG = "SceneFormModel";

    public interface SceneFormModelDelegate {
        void onResponse(java.lang.String response);
        void onException(Exception e);
    }

    public static SceneFormModel instance = new SceneFormModel();

    public void requestUploadModel(String token, String name, String jsonObj, String filePath, SceneFormModelDelegate sceneFormModelDelegate) {
        String url = WebConstants.getUploadModelUrl();
        ILog.iLogDebug(TAG, url);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("X-AUTH-TOKEN", token);

        OKHttpWrapper.instance.requestPostFormDataModel(url, hashMap, name, jsonObj, filePath, new OKHttpWrapper.OKHttpWrapperDelegate() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sceneFormModelDelegate.onException(e);
                OKHttpWrapper.instance.cancelCall(call);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseString = OKHttpWrapper.instance.getStringResponse(response);
                    sceneFormModelDelegate.onResponse(responseString);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    OKHttpWrapper.instance.cancelCall(call);
                }
            }
        });
    }

    public void requestUploadImage(String token, String modelId, String filePath, SceneFormModelDelegate sceneFormModelDelegate) {
        String url = WebConstants.getUploadModelImageUrl(modelId);
        ILog.iLogDebug(TAG, url);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("X-AUTH-TOKEN", token);

        OKHttpWrapper.instance.requestPostImageFile(url, hashMap, filePath, new OKHttpWrapper.OKHttpWrapperDelegate() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sceneFormModelDelegate.onException(e);
                OKHttpWrapper.instance.cancelCall(call);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseString = OKHttpWrapper.instance.getStringResponse(response);
                    sceneFormModelDelegate.onResponse(responseString);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    OKHttpWrapper.instance.cancelCall(call);
                }
            }
        });
    }

    public void requestSearchModel(String token, String keyWord, String offset, String limitNo, SceneFormModelDelegate sceneFormModelDelegate) {
        String url = WebConstants.getSearchModelUrl(keyWord, offset, limitNo);

        ILog.iLogDebug(TAG, url);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("X-AUTH-TOKEN", token);

        OKHttpWrapper.instance.requestGetWithHeader(url, hashMap, new OKHttpWrapper.OKHttpWrapperDelegate() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sceneFormModelDelegate.onException(e);
                OKHttpWrapper.instance.cancelCall(call);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseString = OKHttpWrapper.instance.getStringResponse(response);
                    sceneFormModelDelegate.onResponse(responseString);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    OKHttpWrapper.instance.cancelCall(call);
                }
            }
        });
    }

    public void requestDeleteModel(String token, String modelId, SceneFormModelDelegate sceneFormModelDelegate) {
        String url = WebConstants.getDeleteModelUrl(modelId);

        ILog.iLogDebug(TAG, url);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("X-AUTH-TOKEN", token);

        OKHttpWrapper.instance.requestDeleteWithHeader(url, hashMap, new OKHttpWrapper.OKHttpWrapperDelegate() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                sceneFormModelDelegate.onException(e);
                OKHttpWrapper.instance.cancelCall(call);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    String responseString = OKHttpWrapper.instance.getStringResponse(response);
                    sceneFormModelDelegate.onResponse(responseString);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    OKHttpWrapper.instance.cancelCall(call);
                }
            }
        });
    }
}
