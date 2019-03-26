package org.mrlee.wetravel;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class BeforeSearchActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    android.support.v7.app.ActionBar bar;
    private FragmentManager fm;
    private ArrayList<Fragment> fList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyData> myDataset;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_search);
        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 스와이프할 뷰페이저를 정의
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // 프라그먼트 매니져 객체 정의
        fm = getSupportFragmentManager();

        // 액션바 객체 정의
        bar = getSupportActionBar();

        // 액션바 속성 정의
        bar.setDisplayShowTitleEnabled(false);   // 액션바 노출 유무

        //bar.setTitle("Test");
        //bar.setBackgroundDrawable(new ColorDrawable(0xFF339999));
        //bar.setDisplayHomeAsUpEnabled(true);

        // 액션바에 모드 설정 = ActionBar.NAVIGATION_MODE_TABS 로 TAB 모드로 설정
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 액션바에 추가될 탭 생성
        ActionBar.Tab tab1 = bar.newTab().setText("검색하기").setTabListener(tabListener);
        ActionBar.Tab tab2 = bar.newTab().setText("내여행").setTabListener(tabListener);
        ActionBar.Tab tab3 = bar.newTab().setText("알림").setTabListener(tabListener);
        //ActionBar.Tab tab4 = bar.newTab().setText("메세지").setTabListener(tabListener);

        //ActionBar.Tab tab5 = bar.newTab().setText("내 프로필").setTabListener(tabListener);
        //ActionBar.Tab tab6 = bar.newTab().setText("알림").setTabListener(tabListener);

        // 액션바에 탭 추가
        bar.addTab(tab1);
        bar.addTab(tab2);
        bar.addTab(tab3);
        //bar.addTab(tab4);
        //bar.addTab(tab5);
        //bar.addTab(tab6);

        // 각 탭에 들어갈 프라그먼트 생성 및 추가
        fList = new ArrayList<Fragment>();
        fList.add(SearchFragment.newInstance());
        fList.add(MyTravelFragment.newInstance());
        //fList.add(ChatFragment.newInstance());
        fList.add(NoticeFragment.newInstance());
        //fList.add(MyProfileFragment.newInstance());
        //fList.add(NoticeFragment.newInstance());

        // 스와이프로 탭간 이동할 뷰페이저의 리스너 설정
        mViewPager.setOnPageChangeListener(viewPagerListener);

        // 뷰페이져의 아답터 생성 및 연결
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(fm, fList);
        mViewPager.setAdapter(adapter);
    }
    ViewPager.SimpleOnPageChangeListener viewPagerListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            // 뷰페이저 이동시 해당 탭으로 이동
            bar.setSelectedNavigationItem(position);
        }
    };


    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭에서 벚어났을때 처리
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭을 선택시 처리
            // 해당 탭으로 뷰페이저도 이동
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // 해당 탭이 다시 선택됐을때 처리
        }
    };
    /*
    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.
        if (id == android.R.id.home) {
            Toast.makeText(this, "홈아이콘 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(this, "검색 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_button2) {
            Toast.makeText(this, "액션버튼 클릭", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.action_logout) {
            Toast.makeText(BeforeSearchActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
            mAuth.getInstance().signOut();
            startActivity(new Intent(BeforeSearchActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        if(id == R.id.close_friend) {
            Toast.makeText(BeforeSearchActivity.this, "주변친구 찾기", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BeforeSearchActivity.this, CloseFriendActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //액션바 숨기기
    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
    }
    */
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            sendToLogin();
        }
    }

    private void sendToLogin() {

        Intent intent = new Intent(BeforeSearchActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
