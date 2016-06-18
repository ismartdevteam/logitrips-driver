package com.logitrips.userapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.logitrips.userapp.util.MySingleton;


public class CarImageFrag extends Fragment {
    private static final String ARG_PARAM1 = "image";
    private String mParam1;
    private ImageLoader mImageLoader;

    public static CarImageFrag newInstance(String param1) {
        CarImageFrag fragment = new CarImageFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
     
        fragment.setArguments(args);
        return fragment;
    }
    public CarImageFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.item_image,container,false);
        NetworkImageView image= (NetworkImageView)view.findViewById(R.id.car_det_image);
        mImageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        Log.e("pic_url",mParam1);
        image.setImageUrl(getActivity().getString(R.string.car_image_url)+mParam1,mImageLoader);
        image.setDefaultImageResId(R.drawable.nocar);
        return view;
    }
}
