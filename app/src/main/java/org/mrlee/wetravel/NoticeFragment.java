package org.mrlee.wetravel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NoticeFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    private DatabaseReference mDatabase;

    public static NoticeFragment newInstance(){
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notice, container, false);
        listView = (ExpandableListView)v.findViewById(R.id.lvExp);
        initData();
        return v;
    }

    private void initData(){
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("notice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Notice get = postSnapshot.getValue(Notice.class);
                    System.out.println("들어오나?:");
                    if(key != null) {
                        List<String> temp = new ArrayList<>();
                        temp.add(get.getContent());
                        listDataHeader.add(get.getTitle());
                        listHash.put(get.getTitle(),temp);
                        System.out.println("공지사항 체크"+get.getTitle() + " " + get.getContent());
                    }
                }
                listAdapter = new ExpandableListAdapter(getContext(),listDataHeader,listHash);
                listView.setAdapter(listAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
