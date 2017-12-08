package demo.plugin.com.calendardemo.rili;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by wmchao on 2017/11/30.
 */

@SuppressLint("AppCompatCustomView")
public class Calendar2TextView extends TextView {
    private boolean mIsToday;
    private boolean mIsChecked;
    private Paint mTodayBgPaint;
    private Paint mCheckedPaint;
    public Calendar2TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCalendarPaint();

    }

    private void initCalendarPaint() {
        mTodayBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCheckedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTodayBgPaint.setColor(Color.RED);
        mCheckedPaint.setStyle(Paint.Style.STROKE);
        mCheckedPaint.setColor(Color.YELLOW);
        mCheckedPaint.setStrokeWidth(3);
    }

    public void setToday(boolean isToday){
        this.mIsToday = isToday;
    }
    public void setmCHecked(boolean isChecked){
        this.mIsChecked = isChecked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mIsToday){
            int radius = getMeasuredWidth() / 2-3;
            int center = getMeasuredHeight()/2;
            canvas.drawCircle(center,center,radius,mTodayBgPaint);
        }
        if(mIsChecked){
            int radius = getMeasuredWidth() / 2;
            int center = getMeasuredHeight()/2;
            canvas.drawCircle(center,center,radius,mCheckedPaint);
        }
        super.onDraw(canvas);
    }


}
