package com.hellsong.appfilters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hellsong.appfilters.adapter.TabAdapter;
import com.hellsong.appfilters.module.FilterSection;
import com.hellsong.appfilters.widget.FilterButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int FILTER_TYPE_TRAVEL_FOR = 0;
    public static final int FILTER_TYPE_TRAVEL_WITH = 1;

    private FilterButton mIcon;
    private LinearLayout mFilterOption;
    private View mCardView;
    private Animation mCardCloseAnim;
    private Animation mCardOpenAnim;
    private Animation mFilterOptionCloseAnim;
    private Animation mFilterOptionOpenAnim;
    private TextView[] mSelectedFilterOptions;
    private TextView mFilterSelectedCount1;
    private TextView mFilterSelectedCount2;
    private RecyclerView mTabList;
    private TabAdapter mTabAdapter;

    private List<FilterSection> mFilterData;
    private List<String> mTabData;

    private Point mFilterSelectedCountInitPoint;
    private Interpolator mItemCloseInterpolator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIcon = (FilterButton) findViewById(R.id.filter_open_button);
        mFilterOption = (LinearLayout) findViewById(R.id.fliter_option);
        mCardView = findViewById(R.id.card_layout);
        mTabList = (RecyclerView) findViewById(R.id.content_tab);
        mFilterSelectedCount1 = (TextView) findViewById(R.id.filter_selected_count_1);
        mFilterSelectedCount2 = (TextView) findViewById(R.id.filter_selected_count_2);
        mTabAdapter = new TabAdapter();
        mItemCloseInterpolator = new AccelerateInterpolator();

        mCardCloseAnim = AnimationUtils.loadAnimation(this, R.anim.card_close);
        mCardCloseAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCardView.setVisibility(View.GONE);
                mFilterOption.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mCardOpenAnim = AnimationUtils.loadAnimation(this, R.anim.card_open);
        mCardOpenAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCardView.setVisibility(View.VISIBLE);
                mFilterOption.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mFilterOptionCloseAnim = AnimationUtils.loadAnimation(this, R.anim.filter_option_close);
        mFilterOptionOpenAnim = AnimationUtils.loadAnimation(this, R.anim.filter_option_normal);

        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIcon.getProcess() < 0.5f) {
                    mIcon.animateToClose();
                    mCardView.startAnimation(mCardCloseAnim);
                    mFilterOption.startAnimation(mFilterOptionOpenAnim);
                    mFilterSelectedCount1.setVisibility(View.INVISIBLE);
                    mFilterSelectedCount2.setVisibility(View.INVISIBLE);
                } else {
                    animateFilterOptionCloseItem(mSelectedFilterOptions[0], mFilterSelectedCount1);
                    animateFilterOptionCloseItem(mSelectedFilterOptions[1], mFilterSelectedCount2);
                    mIcon.animateToNormal();
                    mCardView.startAnimation(mCardOpenAnim);
                    mFilterOption.startAnimation(mFilterOptionCloseAnim);
                }
            }
        });

        buildTestData();
        mTabAdapter.setData(mTabData);
        bindFilterData();
        mTabList.setAdapter(mTabAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTabList.setLayoutManager(layoutManager);
    }

    private void updateFilterCount() {
        int count = 0;
        for (TextView tv : mSelectedFilterOptions) {
            if (tv != null) {
                count++;
            }
        }
        mFilterSelectedCount2.setText(count + "");
        mFilterSelectedCount1.setText(count + "");
    }

    private void animateFilterOptionCloseItem(final View fromView, final View toView) {
        if (fromView == null || toView == null) {
            return;
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.filter_option_item_close);
        animation.setDuration(getResources().getInteger(R.integer.animation_duration));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Point fromPoint = getViewScreenXY(fromView);
                if (mFilterSelectedCountInitPoint == null) {
                    mFilterSelectedCountInitPoint = getViewScreenXY(toView);
                }
                Point toPoint = mFilterSelectedCountInitPoint;
                int duration = getResources().getInteger(R.integer.filter_count_animation_duration);
                ObjectAnimator animX1 = ObjectAnimator.ofFloat(toView, "x", fromPoint.x + fromView.getWidth() / 2, toPoint.x);
                animX1.setDuration(duration);
                ObjectAnimator animX2 = ObjectAnimator.ofFloat(toView, "y", fromPoint.y + fromView.getHeight() / 2, fromPoint.y + fromView.getHeight() / 2);
                AnimatorSet animSetX = new AnimatorSet();
                animSetX.playTogether(animX1, animX2);
                ObjectAnimator animY = ObjectAnimator.ofFloat(toView, "y", fromPoint.y + fromView.getHeight() / 2, toPoint.y);
                animY.setDuration(duration);
                animY.setInterpolator(mItemCloseInterpolator);
                AnimatorSet animSetXY = new AnimatorSet();
                animSetXY.playSequentially(animSetX, animY);
                animSetXY.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toView.setVisibility(View.VISIBLE);
                        mFilterSelectedCount2.setText("");
                        mFilterSelectedCount1.setText("");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        updateFilterCount();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                animSetXY.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        fromView.startAnimation(animation);
    }

    private Point getViewScreenXY(View view) {
        View rootLayout = view.getRootView().findViewById(android.R.id.content);

        int[] viewLocation = new int[2];
        view.getLocationInWindow(viewLocation);

        int[] rootLocation = new int[2];
        rootLayout.getLocationInWindow(rootLocation);

        int relativeLeft = viewLocation[0] - rootLocation[0];
        int relativeTop = viewLocation[1] - rootLocation[1];
        return new Point(relativeLeft, relativeTop);
    }

    private void bindFilterData() {
        mSelectedFilterOptions = new TextView[mFilterData.size()];
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int areaWidth = metrics.widthPixels - (int) getResources().getDimension(R.dimen.main_activity_card_margin);
        for (int i = 0; i < mFilterData.size(); i++) {
            FilterSection fs = mFilterData.get(i);
            TextView titleTV = (TextView) getLayoutInflater().inflate(R.layout.filter_section_title, mFilterOption, false);
            titleTV.setText(fs.getTitle());
            mFilterOption.addView(titleTV);

            int consumeWidth = 0;
            LinearLayout containerLayout = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            containerLayout.setOrientation(LinearLayout.HORIZONTAL);
            containerLayout.setLayoutParams(lp);
            mFilterOption.addView(containerLayout);
            for (String item : fs.getItems()) {
                TextView itemTV = (TextView) getLayoutInflater().inflate(R.layout.filter_section_item, containerLayout, false);
                itemTV.setText(item);
                if (i == 0) {
                    itemTV.setTag(FILTER_TYPE_TRAVEL_FOR);
                } else {
                    itemTV.setTag(FILTER_TYPE_TRAVEL_WITH);
                }
                itemTV.measure(0, 0);
                itemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int type = (int) v.getTag();
                        if (mSelectedFilterOptions[type] == v) {
                            setFilterOptionNormal((TextView) v);
                            mSelectedFilterOptions[type] = null;
                            return;
                        }
                        setFilterOptionNormal(mSelectedFilterOptions[type]);
                        setFilterOptionSelected((TextView) v);
                        mSelectedFilterOptions[type] = (TextView) v;
                    }
                });
                int itemTVWidth = itemTV.getMeasuredWidth();
                if (consumeWidth + itemTVWidth > areaWidth) {
                    consumeWidth = itemTVWidth;
                    containerLayout = new LinearLayout(this);
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    containerLayout.setOrientation(LinearLayout.HORIZONTAL);
                    containerLayout.setLayoutParams(lp);
                    mFilterOption.addView(containerLayout);
                } else {
                    consumeWidth += itemTVWidth;
                }
                containerLayout.addView(itemTV);
            }
            if (i < mFilterData.size() - 1) {
                lp.setMargins(lp.leftMargin, lp.rightMargin, lp.topMargin, (int) this.getResources().getDimension(R.dimen.main_activity_filter_option_section_margin_bottom));
                containerLayout.setLayoutParams(lp);
            }
        }
    }

    private void setFilterOptionNormal(TextView tv) {
        if (tv == null) {
            return;
        }
        tv.setBackgroundResource(R.drawable.filter_item_back_ground_normal);
        tv.setTextColor(Color.parseColor("#ffffff"));

    }

    private void setFilterOptionSelected(TextView tv) {
        if (tv == null) {
            return;
        }
        tv.setBackgroundResource(R.drawable.filter_item_back_ground_selected);
        tv.setTextColor(getResources().getColor(R.color.filter_item_text_selected_color));
    }

    private void buildTestData() {
        mFilterData = new ArrayList<>();
        {
            FilterSection fs = new FilterSection();
            fs.setmTitle("Traveling for...");
            List<String> items = new ArrayList<>();
            items.add("Holidays");
            items.add("Family");
            items.add("Business");
            items.add("Events");
            items.add("Adventure");
            fs.setItems(items);
            mFilterData.add(fs);
        }
        {
            FilterSection fs = new FilterSection();
            fs.setmTitle("Traveling with...");
            List<String> items = new ArrayList<>();
            items.add("My spouse");
            items.add("Kids");
            items.add("Pets");
            items.add("Large Groups");
            items.add("Large Groups");
            fs.setItems(items);
            mFilterData.add(fs);
        }

        mTabData = new ArrayList<>();
        {
            mTabData.add("Destinations");
            mTabData.add("Events");
            mTabData.add("Meetups");
            mTabData.add("Loops");
            mTabData.add("Destinations");
            mTabData.add("Events");
            mTabData.add("Meetups");
            mTabData.add("Loops");
            mTabData.add("Destinations");
            mTabData.add("Events");
            mTabData.add("Meetups");
            mTabData.add("Loops");
        }
    }
}
