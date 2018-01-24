package com.lingy.lawei.weibo.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lingy.lawei.MyApp;
import com.lingy.lawei.R;
import com.lingy.lawei.utils.ViewUtil;
import com.lingy.lawei.weibo.adapter.WeiboPhotoAdapter;
import com.lingy.lawei.weibo.api.WeiboApi;
import com.lingy.lawei.weibo.api.WeiboFactory;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.util.BatchCommentHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lingy on 2017-10-22.
 */

public class SendWeiboActivity extends BaseActivity implements BatchCommentHelper.OnRequestStateChangedListener {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 333;
    public static final String WEIBO_ID = "weibo_id";
    public static final String AT_USER_NAMES = "atUserName";
    public static final int SEND_TYPE_CREATE_WEIBO = 0;
    public static final int SEND_TYPE_POST_COMMENT = 1;
    private int mSendType = SEND_TYPE_CREATE_WEIBO;
    private String weiBoId;
    @BindView(R.id.et_weibo)
    EditText etWeibo;
    @BindView(R.id.tv_weibo_number)
    TextView tvWeiboNumber;
    @BindView(R.id.rv_weibo_photo)
    RecyclerView rvWeiboPhoto;
    //表情
    @BindView(R.id.weibo_emotion)
    ImageView ivWeiboEmotion;
    @BindView(R.id.ll_emotion_dashboard)
    LinearLayout llEmotionDashboard;
    @BindView(R.id.vp_emotion_dashboard)
    ViewPager vpEmotionDashboard;

    private List<String> mPhotos = new ArrayList<>();
    private WeiboPhotoAdapter mPhotoAdapter;

    @Override
    protected void init() {
        rvWeiboPhoto.setLayoutManager(new GridLayoutManager(this, 4));
        new ViewUtil(this, vpEmotionDashboard, etWeibo).initEmotion();
        Intent intent = getIntent();
        if (intent != null) {
            String atUsers = intent.getStringExtra(AT_USER_NAMES);
            if (!TextUtils.isEmpty(atUsers)) {
                etWeibo.setText(atUsers);
            }
            weiBoId = intent.getStringExtra(WEIBO_ID);
            if (!TextUtils.isEmpty(weiBoId)) {
                mSendType = SEND_TYPE_POST_COMMENT;
            } else {
                mSendType = SEND_TYPE_CREATE_WEIBO;
            }
        }
    }

    public static void toAtAndSendWeiBo(Context context, String atUser) {
        Intent intent = new Intent(context, SendWeiboActivity.class);
        intent.putExtra(AT_USER_NAMES, atUser);
        context.startActivity(intent);
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @OnClick(R.id.weibo_photo)
    void pickphoto() {
        mPhotoAdapter = new WeiboPhotoAdapter(this, mPhotos, true);
        rvWeiboPhoto.setAdapter(mPhotoAdapter);
        mPhotos.clear();
        mPhotoAdapter.paths.clear();
        PermissionToVerify();
    }

    /**
     * 判断当前权限是否允许,弹出提示框来选择
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void PermissionToVerify() {
        // 需要验证的权限
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            // 弹窗询问 ，让用户自己判断
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        photoPick();

    }

    /**
     * 用户进行权限设置后的回调函数 , 来响应用户的操作，无论用户是否同意权限，Activity都会
     * 执行此回调方法，所以我们可以把具体操作写在这里
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取图片
                    photoPick();
                } else {
                    showTips("权限没有开启");
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void photoPick() {
        PhotoPicker.builder()
                .setPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> listExtra =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                loadAdapter(listExtra);
            }
        }
    }

    //更新数据适配器
    public void loadAdapter(ArrayList<String> paths) {
        if (mPhotos == null) {
            mPhotos = new ArrayList<>();
        }
        mPhotos.clear();
        mPhotos.addAll(paths);
        mPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_send:
                if (TextUtils.isEmpty(etWeibo.getText())) {
                    showTips("微博内容不能为空");
                } else {
                    switch (mSendType) {
                        case SEND_TYPE_CREATE_WEIBO:
                            sendWeiBo(etWeibo.getText().toString());
                            break;
                        case SEND_TYPE_POST_COMMENT:
                            postComment(etWeibo.getText().toString());
                            break;
                    }

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getPathFromAdapter() {
        List<String> paths = new ArrayList<>();
        if (mPhotoAdapter != null) {
            List<Object> objects = mPhotoAdapter.paths;
            for (Object object : objects) {
                if (object instanceof String) {
                    paths.add((String) object);
                }
            }
            return paths;
        } else {
            return null;
        }
    }

    private void postComment(String comment) {
        BatchCommentHelper helper = new BatchCommentHelper(weiBoId);
        helper.setRequestStateChangedListener(this);
        helper.normalComment(comment);
    }

    private void sendWeiBo(String content) {
        WeiboApi weiBoApi = WeiboFactory.getWeiBoApiSingleton();
        if (mPhotoAdapter != null && getPathFromAdapter().size() > 0) {
            weiBoApi.sendWeiBoWithImg(getTokenStr(), getTextStr(content), getImage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(status -> {
                        finishAndToast();
                    }, this::loadError);
        } else {
            String token = MyApp.getInstance().getAccessTokenHack();
            weiBoApi.sendWeiBoWithText(getSendMap(token, content))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(status -> {
                        finishAndToast();
                    }, this::loadError);
        }
    }

    public static void toSendNewWeiBo(Context context) {
        Intent intent = new Intent(context, SendWeiboActivity.class);
        context.startActivity(intent);
    }

    public static void toPostComment(Context context, String weiboId) {
        Intent intent = new Intent(context, SendWeiboActivity.class);
        intent.putExtra(WEIBO_ID, weiboId);
        context.startActivity(intent);
    }

    private void finishAndToast() {
        showTips("发送成功");
        finish();
    }

    // get request params
    private RequestBody getTokenStr() {
        RequestBody accessBody = RequestBody.create(
                MediaType.parse("text/plain"),
                MyApp.getInstance().getAccessTokenHack());
        return accessBody;
    }

    private RequestBody getTextStr(String text) {
        RequestBody contentBody = RequestBody.create(
                MediaType.parse("text/plain"), text);
        return contentBody;
    }

    private RequestBody getImage() {
        List<String> stringList = getPathFromAdapter();
        File file = new File(stringList.get(0));
        RequestBody imageBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file);
        return imageBody;
    }

    private Map<String, Object> getSendMap(String token, String status) {
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("status", status);
        return map;
    }

    @OnClick(R.id.weibo_emotion)
    void insertEmotion() {
        //软键盘，显示或隐藏
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if (llEmotionDashboard.getVisibility() == View.VISIBLE) {
            // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
            ivWeiboEmotion.setImageResource(R.drawable.btn_insert_emotion);
            llEmotionDashboard.setVisibility(View.GONE);
            imm.showSoftInput(etWeibo, 0);
        } else {
            // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
            ivWeiboEmotion.setImageResource(R.drawable.btn_insert_keyboard);
            llEmotionDashboard.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(etWeibo.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.weibo_topic)
    void insertTopic() {
        int curPosition = etWeibo.getSelectionStart();
        StringBuilder sb = new StringBuilder(etWeibo.getText().toString());
        sb.insert(curPosition, "##");
        // 特殊文字处理,将表情等转换一下
        etWeibo.setText(sb);
        // 将光标设置到新增完表情的右侧
        etWeibo.setSelection(curPosition + 1);
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.activity_write_weibo;
    }

    @Override
    public void commentFinish() {
        finishAndToast();
    }

    @Override
    public void publishProgress(String comment, String tag, int currentUserCount) {

    }

    @Override
    public void onError(Throwable throwable) {
        showTips("加载出错");
    }
}
