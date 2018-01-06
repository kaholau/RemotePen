package com.example.hoho.remotepen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
public abstract class TextListActivity extends TextBaseActivity{

    Controller mc;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected AlertDialog dialog;
    protected AlertDialog deleteDialog;
    protected List<HistoryItem> historyItemList;
    protected int cur_position;

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
        private  List<HistoryItem> recycleViewHistoryList;

        // Provide a reference to the views for each data item and  provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mHistoryContentTextView;
            public TextView mHistoryTimeTextView;
            public ImageView mIsBookmarkImageView;
            public View cover;
            public ViewHolder(View HistoryItemLayout) {
                super(HistoryItemLayout);
                cover=HistoryItemLayout.findViewById(R.id.selectorLayout);
                mHistoryContentTextView =(TextView)HistoryItemLayout.findViewById(R.id.HistoryContentTextView);
                mHistoryTimeTextView=(TextView)HistoryItemLayout.findViewById(R.id.HistoryTimeTextView);
                mIsBookmarkImageView=(ImageView)HistoryItemLayout.findViewById(R.id.isBookmakeImageView);
            }
        }


        public MyAdapter(List<HistoryItem> histList) {
            recycleViewHistoryList = histList;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from  dataset at this position
            // - replace the contents of the view with that element
            holder.cover.setSelected(false);
            holder.mHistoryContentTextView.setText(recycleViewHistoryList.get(position).getText());
            holder.mHistoryTimeTextView.setText(recycleViewHistoryList.get(position).getTime());
            if(recycleViewHistoryList.get(position).getIsBookmark())
                holder.mIsBookmarkImageView.setVisibility(View.VISIBLE);
            else
                holder.mIsBookmarkImageView.setVisibility(View.GONE);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return recycleViewHistoryList.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mc=new Controller(this);
        historyItemList= getList();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(historyItemList);

        mRecyclerView.setAdapter(mAdapter);
        buildDialog(this);
        buildDeleteDialog(this);
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                View selector = v.findViewById(R.id.selectorLayout);
                selector.setSelected(true);
                cur_position = position;
                dialog.show();
            }

        });
        ItemClickSupport.addTo(mRecyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                View selector = v.findViewById(R.id.selectorLayout);
                selector.setSelected(true);
                cur_position = position;
                deleteDialog.show();
                return false;
            }

        });
    }

    protected abstract List<HistoryItem> getList();
    protected abstract AlertDialog.Builder buildMyOptionDialog();
    protected abstract AlertDialog.Builder buildMyDeleteDialog();

    private  void buildDialog(Context context){
        AlertDialog.Builder builder= buildMyOptionDialog();
        dialog=builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                mAdapter.notifyItemChanged(cur_position);
            }
        });
    }
    private void buildDeleteDialog(Context context){
        AlertDialog.Builder builder = buildMyDeleteDialog();
        deleteDialog=builder.create();
        deleteDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mAdapter.notifyItemChanged(cur_position);
            }
        });
    }

}

