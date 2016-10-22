package xydesk.xy.contact;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import xydesk.xy.appAll.ui.NameSetUI;
import xydesk.xy.base.XYBaseActivity;
import xydesk.xy.base.XYBaseAdapter;
import xydesk.xy.contant.XYContant;
import xydesk.xy.viewHolder.XYViewHolder;
import xydesk.xy.xydesk.R;

/**
 * Created by haizeiym
 * on 2016/9/1
 */
public class AddContactNameUI extends XYBaseActivity {

    @Bind(R.id.all_app)
    ListView allApp;
    List<String> allContactName, allContactNum;

    @Override
    public void initView() {
        setContentView(R.layout.allsome_show_ui);
        ButterKnife.bind(this);
        allContactName = new ArrayList<>();
        allContactNum = new ArrayList<>();
        for (String name : ContactManUtils.allContact.keySet()) {
            allContactName.add(name);
            allContactNum.add(ContactManUtils.allContact.get(name));
        }
    }

    @Override
    public void setAdapter() {
        allApp.setAdapter(new ContactAdapter());
    }

    @OnItemClick(R.id.all_app)
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(instance, NameSetUI.class);
        intent.putExtra(XYContant.VoiceSet.IS_VOICE, false);
        intent.putExtra(XYContant.VoiceSet.NAME_SET, allContactNum.get(position));
        startActivity(intent);
    }

    class ContactAdapter extends XYBaseAdapter {
        @Override
        public int setCount() {
            return allContactName.size();
        }

        @Override
        public View setHolderViewInit(XYViewHolder holder, int position, ViewGroup parent) {
            View convertView = View.inflate(instance, R.layout.add_item_view, null);
            holder.contactName = (TextView) convertView.findViewById(R.id.add_item_view_text);
            holder.item_line = convertView.findViewById(R.id.item_line);
            return convertView;
        }

        @Override
        public void setHolderViewData(XYViewHolder holder, int position) {
            if (position == 0) {
                holder.item_line.setVisibility(View.GONE);
            } else {
                holder.item_line.setVisibility(View.VISIBLE);
            }
            holder.contactName.setText(allContactName.get(position));
        }
    }
}
