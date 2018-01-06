package com.example.hoho.remotepen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 */
public class TextBookmarkActivity extends TextListActivity {


    @Override
    public int getLayoutResource() {
        return R.layout.bookmark_activity;
    }

    @Override
    protected String getActivityName(){
        return this.getLocalClassName();
    }

    @Override
    protected List<HistoryItem> getList(){
        return mc.readBookmark();
    }

    @Override
    protected AlertDialog.Builder buildMyOptionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.bookmark_dialog_title)
                .setItems(R.array.bookmark_dialog_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        switch (id) {
                            case 0:
                                mc.send(historyItemList.get(cur_position).getText(),!Controller.SAVE);
                                mAdapter.notifyItemChanged(cur_position);
                                break;
                            case 1:
                                mc.removeBookmark(historyItemList.get(cur_position).getID());
                                historyItemList.remove(cur_position);
                                mAdapter.notifyItemRemoved(cur_position);
                                break;
                            case 2:
                                mc.deleteHistory(historyItemList.get(cur_position).getID());
                                historyItemList.remove(cur_position);
                                mAdapter.notifyItemRemoved(cur_position);
                                break;
                            case 3:
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
