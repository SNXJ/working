package com.badou.mworking;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.badou.mworking.util.ImageChooser;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by badou1 on 2015/8/7.
 */
public class TaskSignImageActivity extends Activity {
    @Bind(R.id.task_image)
    ImageView taskImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_image);
        ButterKnife.bind(this);
        ImageChooser imageChooser= new ImageChooser(getApplicationContext(), false, true, false);
      //  imageChooser.getBt();//bitMap对象
        taskImage.setImageBitmap( imageChooser.getBt());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
