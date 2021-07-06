package fenping.szlt.com.usbhotel.recyc;

import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.evilbinary.tv.widget.BorderView;
import org.evilbinary.tv.widget.TvGridLayoutManagerScrolling;
import java.util.List;
import fenping.szlt.com.usbhotel.R;
import utils.ApkUtils;

/**
 * 作者:evilbinary on 2/20/16.
 * 邮箱:rootdebug@163.com
 */
public class DemoRecyclerViewActivity extends Activity {

    private BorderView border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_view);
        border = new BorderView(this);
        border.setBackgroundResource(R.drawable.shape_app_focus);
        border.getEffect().setScale(1.0f);
        testRecyclerViewGridLayout();

    }

    private void testRecyclerViewGridLayout() {
        //test grid
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridlayoutManager = new TvGridLayoutManagerScrolling(this, 5);
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridlayoutManager);
        recyclerView.setFocusable(false);
        border.attachTo(recyclerView);
        createData(recyclerView);

    }

    private void createData(RecyclerView recyclerView) {
        //创建数据集
        String[] dataset = new String[100];
        for (int i = 0; i < dataset.length; i++) {
            dataset[i] = "item" + i;
        }
        List<ResolveInfo> allApps = ApkUtils.getAllApps(this);
        // 创建Adapter，并指定数据集
        MyAdapter adapter = new MyAdapter(this, allApps);
        // 设置Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }

}
