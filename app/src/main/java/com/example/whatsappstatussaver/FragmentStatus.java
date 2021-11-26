package com.example.whatsappstatussaver;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStatus extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView recyclerView;
    ArrayList<ModelClass> filesList = new ArrayList<>();
    File[] files;
    AdapterRV adapterRV;


    SwipeRefreshLayout refreshLayout;



    public FragmentStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStatus newInstance(String param1, String param2) {
        FragmentStatus fragment = new FragmentStatus();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_status, container, false);
        recyclerView=view.findViewById(R.id.recycler_view_status);


        refreshLayout = view.findViewById(R.id.swipe_status);

        setupLayout();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);

                filesList.clear();
                adapterRV.notifyDataSetChanged();
                setupLayout();

//                adapterRV.notifyDataSetChanged();
//                {
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            refreshLayout.setRefreshing(false);
//                        }
//                    }, 1000);
//                }


                refreshLayout.setRefreshing(false);

            }
        });






        return view;
    }

    private void setupLayout() {

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        adapterRV = new AdapterRV(getContext(),getData());
        recyclerView.setAdapter(adapterRV);
        adapterRV.notifyDataSetChanged();

    }

    private ArrayList<ModelClass> getData() {

        ModelClass f;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constant.FOLDER_NAME+"Media/.Statuses";

        File targetDir = new File(targetPath);
        files = targetDir.listFiles();

        for(int i=0;i<files.length;i++)
        {
            File file = files[i];
            f = new ModelClass();
            f.setUri(Uri.fromFile(file));
            f.setPath(file.getAbsolutePath());
            f.setFileName(file.getName());

            if(!f.getUri().toString().endsWith(".nomedia")) {
                filesList.add(f);
            }
        }
        return filesList;
    }
}