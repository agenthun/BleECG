package com.agenthun.bleecg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agenthun.bleecg.R;
import com.agenthun.bleecg.adapter.SectionsPagerAdapter;
import com.agenthun.bleecg.utils.ApiLevelHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @project MainActivity
 * @authors agenthun
 * @date 16/3/9 下午7:22.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private final int SDK_PERMISSION_REQUEST = 127;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected() returned: " + position);
                if (position == 0) {
                    fab.setVisibility(View.VISIBLE);
                    ViewCompat.animate(fab).scaleX(1).scaleY(1)
                            .setInterpolator(new LinearOutSlowInInterpolator())
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    if (isFinishing() || (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1) && isDestroyed())) {
                                        return;
                                    }
                                    fab.setVisibility(View.VISIBLE);
                                }
                            })
                            .start();
                } else {
                    if (fab.isShown()) {
                        ViewCompat.animate(fab).scaleX(0).scaleY(0)
                                .setInterpolator(new FastOutSlowInInterpolator())
                                .setStartDelay(100)
                                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(View view) {
                                        if (isFinishing() || (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1) && isDestroyed())) {
                                            return;
                                        }
                                        fab.setVisibility(View.GONE);
                                    }
                                })
                                .start();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "fab onClick: ");
                if (mOnFABClickListener != null) {
                    mOnFABClickListener.OnFABClickListener(view);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getPermissions(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPermissions(Context context) {
        List<String> permissions = new ArrayList<>();

        // 蓝牙权限必须开启ACCESS_COARSE_LOCATIO或ACCESS_FINE_LOCATION权限，用户如果禁止，则每次进入都会申请
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 读写外设为必须权限，用户如果禁止，则每次进入都会申请
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context, permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
        }
    }

    private boolean addPermission(Context context, List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //FABClick interface
    public interface OnFABClickListener {
        void OnFABClickListener(View view);
    }

    private OnFABClickListener mOnFABClickListener;

    public void setOnItemClickListener(OnFABClickListener mOnFABClickListener) {
        this.mOnFABClickListener = mOnFABClickListener;
    }
}
