package com.hdcy.app.fragment.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.model.UserBaseInfo;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;
import com.hdcy.base.utils.SizeUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * Created by WeiYanGeorge on 2016-10-28.
 */

public class MineInfoFragment extends BaseBackFragment implements  OnDateSetListener{

    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private Toolbar mToolbar;
    private TextView title;

    private UserBaseInfo userBaseInfo;

    private ImageView iv_mine_personalinfo_avatar;
    private TextView tv_mine_personalinfo_nickname;
    private TextView tv_mine_personalinfo_name;
    private TextView tv_mine_personalinfo_gender;
    private TextView tv_mine_personalinfo_phone;
    private TextView tv_mine_personalinfo_password;
    private TextView tv_mine_personalinfo_birthday;
    private TextView tv_mine_personalinfo_address;
    private TextView tv_mine_personalinfo_cartype;
    private TextView tv_mine_personalinfo_interests;
    private TimePickerDialog mDialogYearMonthDay;

    int avatarWidth;
    int avatarHeight;

    //昵称选择
    AlertDialog generalalertDialog;
    TextView tv_nickname_title;
    Button bt_general_cancel;
    Button bt_general_submit;
    EditText editText_general;

    //性别选择
    AlertDialog sexAlertDialog;
    TextView tv_sex_man;
    TextView tv_sex_woman;
    TextView tv_sex_cancel;

    //头像选择
    AlertDialog avatarAlertDialog;
    TextView tv_avatar_camera;
    TextView tv_avatar_photo;
    TextView tv_avatar_cancel;

    private String editType;
    private String content;
    TextView mTvTime;

    private boolean isEdit;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");



    public static MineInfoFragment newInstance(UserBaseInfo userBaseInfo){
        Bundle args = new Bundle();
        args.putSerializable("param", userBaseInfo);
        MineInfoFragment fragment = new MineInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_info_detail,container,false);
        userBaseInfo = (UserBaseInfo) getArguments().getSerializable("param");
        Log.e("传递的个人资料",userBaseInfo.getId()+"");
        initView(view);
        avatarHeight = SizeUtils.dpToPx(50);
        avatarWidth = SizeUtils.dpToPx(50);
        setListener();
        return view;
    }
    @Override
    public void onDateSet(TimePickerDialog timePickerDialog, long millseconds) {
        String text = getDateToString(millseconds);
        mTvTime.setText(text);
    }
    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }



    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title =(TextView) view.findViewById(R.id.toolbar_title);
        title.setText("个人资料");
        initToolbarNav(mToolbar);
        iv_mine_personalinfo_avatar = (ImageView) view.findViewById(R.id.iv_personal_info_avatar);
        tv_mine_personalinfo_nickname =(TextView) view.findViewById(R.id.tv_mine_personalinfo_nickname);
        tv_mine_personalinfo_name =(TextView) view.findViewById(R.id.tv_mine_personalinfo_name);
        tv_mine_personalinfo_gender =(TextView) view.findViewById(R.id.tv_mine_personalinfo_gender);
        tv_mine_personalinfo_phone =(TextView) view.findViewById(R.id.tv_mine_personalinfo_phone);
        tv_mine_personalinfo_password =(TextView) view.findViewById(R.id.tv_mine_personalinfo_password);
        tv_mine_personalinfo_birthday=(TextView) view.findViewById(R.id.tv_mine_personalinfo_birthday);
        tv_mine_personalinfo_address =(TextView) view.findViewById(R.id.tv_mine_personalinfo_address);
        tv_mine_personalinfo_cartype =(TextView) view.findViewById(R.id.tv_mine_personalinfo_cartype);
        tv_mine_personalinfo_interests =(TextView) view.findViewById(R.id.tv_mine_personalinfo_interests);
        setData();
    }

    private void setListener(){
        tv_mine_personalinfo_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType = "nickname";
                ShowNickNameDialog();
            }
        });

        tv_mine_personalinfo_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("datepick","onClick");
                mDialogYearMonthDay = new TimePickerDialog.Builder()
                        .setType(Type.YEAR_MONTH_DAY)
                        .setTitleStringId("")
                        .setMinMillseconds(445555555)
                        .setCallBack(new OnDateSetListener() {
                            @Override
                            public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                                String text = getDateToString(millseconds);
                                content = text;
                                editType = "birthday";
                                PublishPersonalInfo();
                                tv_mine_personalinfo_birthday.setText(text);
                            }
                        })
                        .build();
                mDialogYearMonthDay.show(getFragmentManager(),"year_month_day");

            }
        });

        tv_mine_personalinfo_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSexDialog();
            }
        });

        iv_mine_personalinfo_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowUploadAvatarDialog();
            }
        });
    }

    private boolean checkData(){
        content = editText_general.getText().toString();
        if(BaseUtils.isEmptyString(content)||content.trim().isEmpty()){
            Toast.makeText(getActivity(), "请输入你要编辑的文字", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }

    private void setData(){
        if(!userBaseInfo.getHeadimgurl().isEmpty()) {
            Picasso.with(getContext()).load(userBaseInfo.getHeadimgurl())
                    .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                    .resize(50, 50)
                    .centerCrop()
                    .config(Bitmap.Config.RGB_565)
                    .into(iv_mine_personalinfo_avatar);
        }
        tv_mine_personalinfo_nickname.setText(userBaseInfo.getNickname()+"");
        if(userBaseInfo.getSex()=="1"){
            tv_mine_personalinfo_gender.setText("男");
        }else {
            tv_mine_personalinfo_gender.setText("女");
        }
        tv_mine_personalinfo_password.setText("********");
        tv_mine_personalinfo_name.setText(userBaseInfo.getRealname()+"");
        String address = userBaseInfo.getAddress();
        if(BaseUtils.isEmptyString(address)){
            tv_mine_personalinfo_address.setText("");
        }else {
            tv_mine_personalinfo_address.setText(address+"");
        }
        String secretphone = userBaseInfo.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        tv_mine_personalinfo_phone.setText(secretphone+"");

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String date = format.format(userBaseInfo.getBirthday());
        tv_mine_personalinfo_birthday.setText(date);
        tv_mine_personalinfo_cartype.setText(userBaseInfo.getCar());
        tv_mine_personalinfo_interests.setText(userBaseInfo.getTags());
    }

    private void ShowNickNameDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_edit_nickname,null);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEdit = s.length() > 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        bt_general_cancel = (Button) view.findViewById(R.id.bt_nickname_cancel);
        bt_general_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalalertDialog.dismiss();
            }
        });
        bt_general_submit = (Button) view.findViewById(R.id.bt_nickname_submit);
        bt_general_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
                    PublishPersonalInfo();
                }
            }
        });
        editText_general = (EditText) view.findViewById(R.id.edt_nickname);
        editText_general.addTextChangedListener(textWatcher);
        editText_general.requestFocus();
        builder.setView(view);
        builder.create();
        generalalertDialog = builder.create();
        Window windowManager = generalalertDialog.getWindow();
        windowManager.setGravity(Gravity.CENTER);
        generalalertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText_general, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        generalalertDialog.show();

    }

    private void ShowUploadAvatarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_edit_avatar,null);
        tv_avatar_cancel = (TextView) view.findViewById(R.id.tv_avatar_cancel);
        tv_avatar_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatarAlertDialog.dismiss();
            }
        });
        tv_avatar_camera = (TextView) view.findViewById(R.id.tv_avatar_camera);
        tv_avatar_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                            getString(R.string.mis_permission_rationale),
                            REQUEST_STORAGE_READ_ACCESS_PERMISSION);
                }else {
                    MultiImageSelector.create(getContext())
                            .single()
                            .showCamera(true)
                            .start(getActivity(), 1);
                }
            }
        });
        tv_avatar_photo = (TextView) view.findViewById(R.id.tv_avatar_photos);
        tv_avatar_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(view);
        builder.create();
        avatarAlertDialog = builder.create();
        Window windowManager = avatarAlertDialog.getWindow();
        windowManager.setGravity(Gravity.BOTTOM);
        avatarAlertDialog.show();
    }

    private void ShowSexDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog_edit_sex,null);
        tv_sex_cancel = (TextView) view.findViewById(R.id.tv_sex_cancel);
        tv_sex_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexAlertDialog.dismiss();
            }
        });
        tv_sex_man = (TextView) view.findViewById(R.id.tv_sex_man);
        tv_sex_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType = "sex";
                content = "1";
                PublishPersonalInfo();
                tv_mine_personalinfo_gender.setText("男");
            }
        });
        tv_sex_woman = (TextView) view.findViewById(R.id.tv_sex_woman);
        tv_sex_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editType = "sex";
                content = "2";
                PublishPersonalInfo();
                tv_mine_personalinfo_gender.setText("女");
            }
        });

        builder.setView(view);
        builder.create();
        sexAlertDialog = builder.create();
        Window windowManager = sexAlertDialog.getWindow();
        windowManager.setGravity(Gravity.BOTTOM);
        sexAlertDialog.show();

    }

    private void PublishPersonalInfo(){
        NetHelper.getInstance().EditMineInfomation(editType, content, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Toast.makeText(getActivity(), "修改成功!", Toast.LENGTH_SHORT).show();
                generalalertDialog.dismiss();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    private void requestPermission(final String permission, String rationale, final int requestCode){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
