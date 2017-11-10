package com.yunnex.canteen.takeout.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunnex.canteen.R;
import com.yunnex.canteen.common.BaseActivity;
import com.yunnex.canteen.takeout.base.BaseItitleCallback;
import com.yunnex.canteen.takeout.modle.IShopModel;
import com.yunnex.canteen.takeout.modle.ShopModleImpl;
import com.yunnex.canteen.common.utils.KeyBoardUtil;
import com.yunnex.canteen.common.utils.SharePrefUtil;
import com.yunnex.canteen.common.utils.ToastUtil;
import com.yunnex.ui.dialog.CustomDialogLayout;

/**
 * 公告发布
 */
public class NoticePubActivity extends BaseActivity {


    private EditText etText;
    private TextView tvTextNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_pub);
        initView();
        initData();
    }

    private void initData() {
        IShopModel model = new ShopModleImpl();
        model.getNotice(this, new IShopModel.NoticeGettingListener() {
            @Override
            public void onGettingNotice(String notice) {
                if (TextUtils.isEmpty(notice)) {
                    ToastUtil.showTestToast(getApplicationContext(), "暂无公告");
                    return;
                }
                etText.setText(notice);
                SharePrefUtil.saveString(getApplicationContext(), "oldText", notice);
            }
        });
    }

    private void initView() {

        ((RelativeLayout) findViewById(R.id.title_container)).addView(customTitle);

        customTitle.setTitleTxt("发布公告").
                setMidTextOff(titleTextPadingLeft).
                setRightTVBackground(R.drawable.selector_title).
                setRightText("        保存       ");

        customTitle.onclick(new BaseItitleCallback() {
            @Override
            public void onleftClik(View view) {

                ToastUtil.showTestToast(getApplicationContext(), "isChange" + isContentChange());

                if (isContentChange()) {

                    // 隐藏软键盘
                    KeyBoardUtil.closeKeybord(etText,getApplicationContext());


                    showDialogCommon(
                            0,
                            null,
                            "",
                            "直接返回将不保存修改内容\n 确定返回？",
                            new BaseCommonDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogLayout layout) {
                            finish();
                        }
                    });
                    return;
                }
                finish();
            }

            @Override
            public void onRightTVClick(View view) {

                IShopModel noticeModel = new ShopModleImpl();
                noticeModel.publishNOtice(NoticePubActivity.this, etText.getText().toString(),
                        new IShopModel.NoticePubListener() {
                            @Override
                            public void onPubSuccess() {
                                finish();
                            }

                            @Override
                            public void onPubFailed() {
                            }
                        });
            }
        });
        etText = (EditText) findViewById(R.id.et_text);
        tvTextNum = (TextView) findViewById(R.id.tv_text_num);
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >=251) {
                    Toast.makeText(NoticePubActivity.this, "字数太多了", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvTextNum.setText((250 - s.length()) + "");
            }
        });
    }

    private boolean isContentChange() {
        String oldText = SharePrefUtil.getString(getApplicationContext(), "oldText", "");
        String newText = etText.getText().toString();
        return !oldText.equalsIgnoreCase(newText);
    }
}
