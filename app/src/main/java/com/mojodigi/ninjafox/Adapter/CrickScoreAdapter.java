package com.mojodigi.ninjafox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojodigi.ninjafox.Model.CricScoreModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.ViewUnit;


import java.util.ArrayList;

public class CrickScoreAdapter extends  RecyclerView.Adapter<CrickScoreAdapter.cricViewHolder> {

    private ArrayList<CricScoreModel> scoreList;
    Context mContext;

    public CrickScoreAdapter(ArrayList<CricScoreModel> scoreList, Context mContext) {
        this.scoreList = scoreList;
        this.mContext = mContext;
    }


    @Override
    public CrickScoreAdapter.cricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_crick_score, parent, false);

        ViewGroup.LayoutParams vw = itemView.getLayoutParams();
        vw.width = ViewUnit.getWindowWidth(mContext)-ViewUnit.getWindowHeight(mContext)/5;
        itemView.setLayoutParams(vw);

        return new CrickScoreAdapter.cricViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CrickScoreAdapter.cricViewHolder holder, int position) {


        holder.matchNote.setText(scoreList.get(position).getMatchNote());
        holder.team1Name.setText(scoreList.get(position).getTeam1Name());
        holder.team2Name.setText(scoreList.get(position).getTeam2Name());

        holder.team1Score.setText(scoreList.get(position).getTeam1Score());
        holder.team2Score.setText(scoreList.get(position).getTeam2Score());

            holder.team1Flag.setImageResource(getFlag(scoreList.get(position).getTeam1Code()));
            holder.team2Flag.setImageResource(getFlag(scoreList.get(position).getTeam2Code()));




    }

    private int getFlag(String teamCode) {

        switch (teamCode.toUpperCase()) {
            case "IND":
                return R.drawable.flag_ic_india;
            case "BGD":
                return R.drawable.flag_ic_bangladesh;
            case "ZIM":
                return R.drawable.flag_ic_zimbabwe;
            case "AFG":
                return R.drawable.flag_ic_afghanistan;
            case "NZ":
                return R.drawable.flag_ic_new_zealand;
            case "NZL":
                return R.drawable.flag_ic_new_zealand;
            case "AUS":
                return R.drawable.flag_ic_australia;
            case "ENG":
                return R.drawable.flag_ic_england;
            case "ZAF":
                return R.drawable.flag_ic_south_africa;
            case "SA":
                return R.drawable.flag_ic_south_africa;
            case "RSA":
                return R.drawable.flag_ic_south_africa;
            case "SL":
                return R.drawable.flag_ic_sri_lanka;
            case "LKA":
                return R.drawable.flag_ic_sri_lanka;
            case "WI":
                return R.drawable.flag_ic_westindies;
            case "PAK":
                return R.drawable.flag_ic_pakistan;
            case "KEN":
                return R.drawable.flag_ic_kenya;
            case "IRE":
                return R.drawable.flag_ic_ireland;
            case "UAE":
                return R.drawable.flag_ic_united_arab_emi;
            case "SCO":
                return R.drawable.flag_ic_scotland;
            case "NED":
                return R.drawable.flag_ic_netherlands;
            case "NEP":
                return R.drawable.flag_ic_nepal;
            case "NAM":
                return R.drawable.flag_ic_namibia;
            default:
                return R.drawable.flag_ic_default;

        }

    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public class cricViewHolder extends RecyclerView.ViewHolder {
        public TextView entertainTitle;

        TextView matchNote,team1Name,team2Name,team1Score,team2Score;
        ImageView team1Flag,team2Flag;

        public cricViewHolder(View view) {
            super(view);


            matchNote=view.findViewById(R.id.note);

            team1Name=view.findViewById(R.id.team1Name);
            team2Name=view.findViewById(R.id.team2Name);

            team1Flag=view.findViewById(R.id.cFlag1);
            team2Flag=view.findViewById(R.id.cFlag2);

            team1Score=view.findViewById(R.id.team1Score);
            team2Score=view.findViewById(R.id.team2Score);

            matchNote.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
            team1Name.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
            team2Name.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
            team1Score.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
            team2Score.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


        }
    }
}




