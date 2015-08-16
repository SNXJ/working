package com.badou.mworking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.badou.mworking.adapter.PlandetailsAdapter;
import com.badou.mworking.base.BaseBackActionBarActivity;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.entity.category.Plan;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by badou1 on 2015/7/31.
 */
public class PlandetailsActivity extends BaseBackActionBarActivity {


    @Bind(R.id.plan_top_title)
    TextView planTopTitle;
    @Bind(R.id.plan_top_detail)
    TextView planTopDetail;
    @Bind(R.id.listview_paln)
    ListView listviewPaln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_plan_detail);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent= this.getIntent();
        CategoryDetail categoryDetail= (CategoryDetail)intent.getSerializableExtra("CategoryDetail");

        planTopTitle.setText(categoryDetail.getPlan().getconfig().getSubject());
        planTopDetail.setText(categoryDetail.getPlan().getconfig().getDesc());

        PlandetailsAdapter adapter = new PlandetailsAdapter(categoryDetail,PlandetailsActivity.this);
        listviewPaln.setAdapter(adapter);
    }
}
