package com.app.tanyahukum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.tanyahukum.R;
import com.app.tanyahukum.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emerio on 1/28/2017.
 */
public class ProvinceAdapter extends  RecyclerView.Adapter<ProvinceAdapter.ViewHolder>  {
    private Context mContext;
    private ArrayList<Province> dialogArrayList;
    Boolean selected=false;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_dialog, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Province province = dialogArrayList.get(position);
        holder.name.setText(province.getNama());
    }

    @Override
    public int getItemCount() {
        return dialogArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        RelativeLayout backgroundDialog;
        ImageView radioSelected,radioUnselected;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.nameDialog);
            backgroundDialog= (RelativeLayout) view.findViewById(R.id.bgDialog);
        }
    }


    public ProvinceAdapter(Context mContext, ArrayList<Province> arrayList) {
        this.mContext = mContext;
        this.dialogArrayList = arrayList;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ProvinceAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ProvinceAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
