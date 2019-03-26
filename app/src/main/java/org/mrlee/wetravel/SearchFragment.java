package org.mrlee.wetravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private ArrayList<MyData> myDataset;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private TextView f1, f2, f3, f4;
    private ImageView imageView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mAuth = FirebaseAuth.getInstance();
        myDataset = new ArrayList<MyData>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("board").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //myDataset = new ArrayList<MyData>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Board get = postSnapshot.getValue(Board.class);
                    if(key != null) {
                        //System.out.println("값 확인 테스트:"+ key + " " + get.getTitle().toString());
                        myDataset.add(new MyData(get.getTitle(), get.getImage(), get.getContent(), get.getStartday(), get.getEndday(), false));
                        //Glide.with(getContext()).load(storageRef).transition(DrawableTransitionOptions.withCrossFade()).into(imageView);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);



        //myDataset.add(new MyData("#Mini", R.mipmap.mini));
        //myDataset.add(new MyData("#ToyStroy", R.mipmap.toystory));

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) v.findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) v.findViewById(R.id.fab4);
        f1 = (TextView) v.findViewById(R.id.f1);
        f2 = (TextView) v.findViewById(R.id.f2);
        f3 = (TextView) v.findViewById(R.id.f3);
        f4 = (TextView) v.findViewById(R.id.f4);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Toast.makeText(getActivity(), "Floating Action Button", Toast.LENGTH_SHORT).show();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                Toast.makeText(getActivity(), "새 글쓰기", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), add_travel.class);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                startActivity(new Intent(getActivity(), ChatActivity.class));
                //getActivity().finish();
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                //Toast.makeText(getActivity(), "Button2", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MyprofileActivity.class));
                //getActivity().finish();
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
                mAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        return v;
    }

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            f1.setVisibility(View.INVISIBLE);
            f2.setVisibility(View.INVISIBLE);
            f3.setVisibility(View.INVISIBLE);
            f4.setVisibility(View.INVISIBLE);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            f1.setVisibility(View.VISIBLE);
            f2.setVisibility(View.VISIBLE);
            f3.setVisibility(View.VISIBLE);
            f4.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }
}
