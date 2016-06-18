package com.logitrips.userapp.menu;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.logitrips.userapp.LoginAc;
import com.logitrips.userapp.R;
import com.logitrips.userapp.model.DatabaseHelper;
import com.logitrips.userapp.util.CircleImageView;
import com.logitrips.userapp.util.CustomRequest;
import com.logitrips.userapp.util.MySingleton;
import com.logitrips.userapp.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AccountFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences sp;
    private TextView name;
    private TextView email;

    private TextView phone;
    private TextView change_pass;
    private Button logout;
    private CircleImageView image;
    private ImageLoader mImageLoader;
    ActionBar actionBar;

    private EditText current_pass;
    private EditText new_pass;
    private EditText conf_new_pass;
    private ImageView show_current_pass;
    private ImageView show_new_pass;
    private ImageView show_conf_new_pass;
    private Dialog dialog;

    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();

        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.menu_account_orange);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(R.string.account);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        MenuUtils.inflateMenu(view,getActivity());
        image=(CircleImageView)view.findViewById(R.id.account_pro_img);

        name = (TextView) view.findViewById(R.id.account_name);

        email = (TextView) view.findViewById(R.id.account_email);
        phone = (TextView) view.findViewById(R.id.account_phone);
        change_pass = (TextView) view.findViewById(R.id.account_change_password);
        logout=(Button) view.findViewById(R.id.account_log_out);
        change_pass.setOnClickListener(this);
        logout.setOnClickListener(this);

        mImageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        init();
        return view;
    }

    private void init(){
        sp=getActivity().getSharedPreferences("user", 1);
        image.setImageUrl(sp.getString("profile_pic_url",""),mImageLoader);
        name.setText(sp.getString("name",""));
        email.setText(sp.getString("email",""));
        phone.setText(sp.getString("phone",""));
    }
    @Override
    public void onClick(View v) {
            if(v==logout){
                sp.edit().clear().commit();
                new DatabaseHelper(getActivity()).DeleteFavCars();

                Intent intent=new Intent(getActivity(),LoginAc.class);
                intent.putExtras(getActivity().getIntent().getExtras());
                getActivity().finish();
                getActivity().startActivity(intent);
            }
        if(v==change_pass){
         dialog = new Dialog(getActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.change_pass_dialog);

            final Button change_my_pass=(Button) dialog.findViewById(R.id.change_my_pass);
            final TextView dismiss=(TextView)dialog.findViewById(R.id.change_pass_dismiss);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            current_pass=(EditText)dialog.findViewById(R.id.current_pass);
            new_pass=(EditText)dialog.findViewById(R.id.new_password);
            conf_new_pass=(EditText)dialog.findViewById(R.id.conf_new_password);

            show_current_pass=(ImageView)dialog.findViewById(R.id.show_current_btn);
            show_current_pass.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        current_pass.setInputType(InputType.TYPE_CLASS_TEXT);

                        current_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        current_pass.setSelection(current_pass.getText().length());
                        return true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        current_pass.setInputType(
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        current_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        current_pass.setSelection(current_pass.getText().length());

                        return true;
                    }
                    return false;
                }
            });

            show_new_pass=(ImageView)dialog.findViewById(R.id.show_new_pass_btn);
            show_new_pass.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        new_pass.setInputType(InputType.TYPE_CLASS_TEXT);

                        new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        new_pass.setSelection(new_pass.getText().length());
                        return true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        new_pass.setInputType(
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        new_pass.setSelection(new_pass.getText().length());

                        return true;
                    }
                    return false;
                }
            });

            show_conf_new_pass=(ImageView)dialog.findViewById(R.id.show_confirm_new_btn);
            show_conf_new_pass.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        conf_new_pass.setInputType(InputType.TYPE_CLASS_TEXT);

                        conf_new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        conf_new_pass.setSelection(conf_new_pass.getText().length());
                        return true;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        conf_new_pass.setInputType(
                                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        conf_new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        conf_new_pass.setSelection(conf_new_pass.getText().length());

                        return true;
                    }
                    return false;
                }
            });

            change_my_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (new_pass.getText().toString().equals(conf_new_pass.getText().toString()) && !new_pass.getText().toString().equals("")) {
                        changePassword(sp.getString("user_id", "0"), current_pass.getText().toString(), new_pass.getText().toString());
                    } else {
                        Toast.makeText(getActivity(),R.string.password_mismatch,Toast.LENGTH_LONG).show();
                    }
                }
            });
            dialog.show();
        }
    }
    private void changePassword(final String user_id,final String old,final String newpass){
        if (!Utils.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_net, Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                    "", getActivity().getString(R.string.loading));

            CustomRequest loginReq = new CustomRequest(Request.Method.POST,
                    getActivity().getString(R.string.main_url) + "/mobile/changepassword" , null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    Log.e("change pass res", response.toString());
                    try {
                        switch (response.getInt("response")) {
                            case 200:
                                Toast.makeText(getActivity(), R.string.changed_password_successfully, Toast.LENGTH_SHORT).show();
                                break;
                            case 300:
                                Toast.makeText(getActivity(), R.string.incorrect_current, Toast.LENGTH_SHORT).show();
                                break;
                            case 100:
                                Toast.makeText(getActivity(), R.string.error_request, Toast.LENGTH_SHORT).show();
                                break;

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", user_id + "");
                    params.put("current_password", old + "");
                    params.put("new_password", newpass + "");
                    return params;
                }
            };

            MySingleton.getInstance(getActivity()).addToRequestQueue(loginReq);
        }

    }

}
