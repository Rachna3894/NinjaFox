package com.mojodigi.ninjafox.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.mojodigi.ninjafox.CustomControl.RelativeTimeTextView;
import com.mojodigi.ninjafox.Model.BlockListModel;
import com.mojodigi.ninjafox.Model.SpeedDialModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;


import java.util.ArrayList;
import java.util.List;


public class BlockListAdapter extends ArrayAdapter<BlockListModel> {
    private Context context;
    private int layoutResId;
    private List<BlockListModel> list;
    int flag;
     SharedPreferenceUtil prefs;

    public BlockListAdapter(Context context, int layoutResId, List<BlockListModel> list, int flag) {
        super(context, layoutResId, list);
        this.context = context;
        this.layoutResId = layoutResId;
        this.list = list;
        this.flag=flag;
        prefs=new SharedPreferenceUtil(context);


    }

    private static class Holder {
        TextView title;
        RelativeTimeTextView time;
        TextView url;
        ImageView menuIcon;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            holder = new Holder();
            holder.title = (TextView) view.findViewById(R.id.record_item_title);
            holder.time = (RelativeTimeTextView) view.findViewById(R.id.record_item_time);
            holder.url = (TextView) view.findViewById(R.id.record_item_url);
            holder.menuIcon = view.findViewById(R.id.menuIcon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        final BlockListModel recordItem = list.get(position);
         String string = setTitle(recordItem.getSiteUrl());
         System.out.println(""+string);
         holder.title.setText(setTitle(recordItem.getSiteUrl()));
         holder.time.setReferenceTime(recordItem.getTime());
         holder.url.setText(recordItem.getSiteUrl());


        holder.menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispUnblockDialog(recordItem,position);
            }
        });

        return view;
    }

    private String setTitle(String siteUrl) {
        String[] arr=siteUrl.split("\\.");
        if(arr.length==3)
            return arr[1];
        else if(arr.length==2)
            return arr[0];
        else
            return "";
    }

    private void dispUnblockDialog(final BlockListModel record, final int position) {

        final AlertDialog dialog_remove_site;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(true);

        final LinearLayout dialog_remove_site_layout = (LinearLayout) ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_remove_site, null, false);
        builder.setView(dialog_remove_site_layout);

        TextView removeSite=dialog_remove_site_layout.findViewById(R.id.removeSite);
        removeSite.setText(context.getString(R.string.unblock_site));
        TextView siteUrl_Txt=dialog_remove_site_layout.findViewById(R.id.siteUrl_Txt);
        TextView removeHeading=dialog_remove_site_layout.findViewById(R.id.removeHeading);

        removeHeading.setText(context.getResources().getString(R.string.unblock_heading))
        ;
        TextView cancel_txt=dialog_remove_site_layout.findViewById(R.id.cancel_txt);

        siteUrl_Txt.setText(record.getSiteUrl());
        removeSite.setTypeface(CommonUtility.typeFace_Calibri_Regular(context));
        siteUrl_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(context));
        removeHeading.setTypeface(CommonUtility.typeFace_Calibri_Regular(context));
        cancel_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(context));
        dialog_remove_site=builder.create();
        dialog_remove_site.show();

        removeSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(dialog_remove_site!=null) {

                    dialog_remove_site.dismiss();
                    list.remove(record);
                    notifyDataSetChanged();
                    ArrayList<BlockListModel> listPrefs = prefs.getBlockedArrayList(context);
                    if(list!=null)
                    {
                        listPrefs.remove(position);
                        prefs.saveBlockedList(listPrefs, CommonUtility.PREF_BLOCK_LIST);
                    }


                }



            }
        });

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(dialog_remove_site!=null)
                    dialog_remove_site.dismiss();

            }
        });

    }

}