package com.example.hoho.remotepen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Observer;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class TextHistoryActivity extends TextListActivity {

    @Override
    public int getLayoutResource() {
        return R.layout.history_activity;
    }

    @Override
    protected String getActivityName(){
        return this.getLocalClassName();
    }

    @Override
    protected List<HistoryItem> getList(){
        return mc.readHistory();
    }

    @Override
    protected AlertDialog.Builder buildMyOptionDialog(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);

       builder.setTitle(R.string.hist_dialog_title)
               .setItems(R.array.hist_dialog_array, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       String[] array=getResources().getStringArray(R.array.hist_dialog_array);
                        Log.d(TAG,array[id]);
                       switch (id){
                           case 0:
                               mc.send(historyItemList.get(cur_position).getText(),!Controller.SAVE);
                               mAdapter.notifyItemChanged(cur_position);
                               break;
                           case 1:
                               mc.saveBookmark(historyItemList.get(cur_position).getID());
                               historyItemList.get(cur_position).setIsBookmark(true);
                               mAdapter.notifyItemChanged(cur_position);
                               break;
                           case 2:
                               mAdapter.notifyItemChanged(cur_position);
                               break;
                       }
                   }
               });
       return builder;
   }
    @Override
    protected AlertDialog.Builder buildMyDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.delete_dialog_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mc.deleteHistory(historyItemList.get(cur_position).getID());
                        historyItemList.remove(cur_position);
                        mAdapter.notifyItemRemoved(cur_position);
                        }
                    })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                            mAdapter.notifyItemChanged(cur_position);
                     }
                });
        return builder;
    }

    @Override
    protected void preOnResume(){

    }

    @Override
    protected void postOnResume(){

    }



}
