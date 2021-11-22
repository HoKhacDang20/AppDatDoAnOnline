package com.example.datdoanonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.datdoanonline.R;
import com.example.datdoanonline.data_models.UserData;

import java.util.List;


public class EmployeeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<UserData> userDataList;

    public EmployeeAdapter(Context context, int layout, List<UserData> userDataList) {
        this.context = context;
        this.layout = layout;
        this.userDataList = userDataList;
    }

    @Override
    public int getCount() {
        return userDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView txtUserName, txtFullName, txtSDT, txtUserType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        String sUserName = "", sUserFullName = "", sUserType = "", sSDT = "";

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtFullName = (TextView) convertView.findViewById(R.id.txtFullName);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.txtSDT = (TextView) convertView.findViewById(R.id.txtSDT);
            viewHolder.txtUserType = (TextView) convertView.findViewById(R.id.txtUserType);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserData userData = userDataList.get(position);

        sSDT = userData.getsSdt();
        sUserName = userData.getsUserName();
        sUserFullName = userData.getsFullName();
        if(userData.getiPermission() == 1){
            sUserType = "Admin";
        }
        else if(userData.getiPermission() == 2){
            sUserType = "Bếp";
        }
        else if(userData.getiPermission() == 3){
            sUserType = "Shipper";
        }

        viewHolder.txtUserType.setText(sUserType);
        viewHolder.txtSDT.setText(sSDT);
        viewHolder.txtFullName.setText(sUserFullName);
        viewHolder.txtUserName.setText(sUserName);

        return convertView;
    }
}
