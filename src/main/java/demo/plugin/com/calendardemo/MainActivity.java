package demo.plugin.com.calendardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Date;

import demo.plugin.com.calendardemo.rili.Calendar2;
import demo.plugin.com.calendardemo.rili.Calendar2TextView;
import demo.plugin.com.calendardemo.rili.DateItem;

public class MainActivity extends AppCompatActivity implements Calendar2.onCalendar2ItemClickLitener {

    private Calendar2 rili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        bindEvent();
    }

    private void bindEvent() {
        rili.setOnItemClickLitener(this);
    }

    private void bindView() {
        rili = findViewById(R.id.id_rl);

    }

    @Override
    public void onClick(int position, DateItem item, Calendar2TextView view) {
        Toast.makeText(this,item.date+"",Toast.LENGTH_SHORT).show();
    }
}
