package demo.plugin.com.calendardemo.rili;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.plugin.com.calendardemo.R;

/**
 * Created by wmchao on 2017/11/30.
 */

public class Calendar2 extends LinearLayout implements View.OnClickListener {
    private View mRootView;
    private Button mBtPreView;
    private Button mBtNextView;
    private TextView mTvTodayView;
    private GridView mGvCalendarView;
    private Calendar currentCal;
    private ArrayList<DateItem>mdates;
    private static final int MAXDATENUM = 42;
    private DateAdapter mAdapter;
    private onCalendar2ItemClickLitener mLitener;
    private DateItem mLastCheckedDateItem =null;
    public Calendar2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bindView();
        bindEvent();
    }

    private void bindEvent() {
        mBtNextView.setOnClickListener(this);
        mBtPreView.setOnClickListener(this);
    }

    private void bindView() {
       mRootView =  LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar2,this);
        mBtPreView = mRootView.findViewById(R.id.id_bt_pre);
        mBtNextView = mRootView.findViewById(R.id.id_bt_next);
        mTvTodayView = mRootView.findViewById(R.id.id_tv_today);
        mGvCalendarView = mRootView.findViewById(R.id.id_gv_calendar);
        currentCal = Calendar.getInstance();
        mdates = new ArrayList<>();
        updateCalendar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_bt_next:
                gotoNextMonth();
                break;
            case R.id.id_bt_pre:
                gotoPriMonth();
                break;
        }
    }

    private void gotoPriMonth() {
        currentCal.add(Calendar.MONTH,-1);
        updateCalendar();
    }

    private void gotoNextMonth() {
        currentCal.add(Calendar.MONTH,1);
        updateCalendar();
    }

    private void updateCalendar() {
        //首先将月移动到第一天，然后算出是周几，推算出此月之前显示的天数，再移到到月前的天数，向集合里添加
        Calendar calendar = (Calendar) currentCal.clone();
        calendar.set(Calendar.DAY_OF_MONTH,1);

        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        int predays = weekDay-1;
        calendar.add(Calendar.DAY_OF_MONTH,-predays);
        mdates.clear();
        //要显示的时间
        for(;mdates.size()<MAXDATENUM;){
            DateItem item = new DateItem();
            calendar.add(Calendar.DAY_OF_MONTH,1);
            Date date = calendar.getTime();
            item.date = date;
            mdates.add(item);
        }

        showDays();
    }

    private void showDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        mTvTodayView.setText(sdf.format(currentCal.getTime()));

        mGvCalendarView.setAdapter(mAdapter = new DateAdapter(getContext(),mdates) );
    }

    private class DateAdapter extends ArrayAdapter<DateItem>{
        private LayoutInflater mLayoutInflater;
        public DateAdapter(@NonNull Context context,  @NonNull List<DateItem> dates) {
            super(context, 0, dates);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final DateItem item = mdates.get(position);
            final Date date = item.date;
            ViewHolder holder = null;
            if(convertView ==null){
                convertView = mLayoutInflater.inflate(R.layout.item_calendar2,parent,false);
                holder = new ViewHolder();
                holder.itemView = convertView.findViewById(R.id.id_tv_item);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.itemView.setText(date.getDate()+"");
            holder.itemView.setmCHecked(item.isChecked);
            if(date.getMonth()!=currentCal.get(Calendar.MONTH)){//不是本月
                holder.itemView.setTextColor(Color.DKGRAY);
            }else {
                if(date.getYear()+1900==Calendar.getInstance().get(Calendar.YEAR)&&date.getMonth()==Calendar.getInstance().get(Calendar.MONTH)&&date.getDate()==Calendar.getInstance().get(Calendar.DATE)){
                    holder.itemView.setToday(true);
                }else{
                    holder.itemView.setTextColor(Color.BLACK);
                }

            }
            if(mLitener!=null){
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mLastCheckedDateItem!=null&&item == mLastCheckedDateItem){
                            item.isChecked = false;
                            mLastCheckedDateItem = null;
                        }else{
                            if(mLastCheckedDateItem !=null)
                                mLastCheckedDateItem.isChecked = false;
                            item.isChecked = !item.isChecked;
                            mLastCheckedDateItem = item;
                            mLitener.onClick(position,item, (Calendar2TextView) v);
                        }
                        notifyDataSetChanged();

                    }
                });
            }

            return convertView;
        }
    }

    private static class  ViewHolder {
        Calendar2TextView itemView;
    }

    int slop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(getContext()));
    float downX;
    float upX;
    boolean mIsMove = false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                upX = ev.getX();
                if(Math.abs(upX-downX)>slop){
                    if(upX-downX>0){//右滑
                        gotoPriMonth();
                    }else{//左滑
                        gotoNextMonth();
                    }
                    mIsMove = true;
                }else{
                    mIsMove = false;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mIsMove;
    }

    public void setOnItemClickLitener(onCalendar2ItemClickLitener litener){
        this.mLitener = litener;
    }

    public interface onCalendar2ItemClickLitener{
        void onClick(int position,DateItem item,Calendar2TextView view);
    }
}
