package com.compass.ashrith.swipeablerecyclerview.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.compass.ashrith.swipeablerecyclerview.R;
import com.compass.ashrith.swipeablerecyclerview.helper.SwipeRunnable;
import com.compass.ashrith.swipeablerecyclerview.interfaces.IOnSwipe;
import com.compass.ashrith.swipeablerecyclerview.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ashrith on 30/5/16.
 */
public class ItemListRecyclerAdapter extends RecyclerView.Adapter<ItemListRecyclerAdapter.myViewHolder> {
    Context context;
    private List<ItemModel> itemList = new ArrayList<>();
    private LayoutInflater inflater;
    private IOnSwipe iOnSwipe;
    private myViewHolder viewHolder;
    private Handler mHandler = new Handler();
    public static final int SWIPE_LEFT = -1;
    public static final int SWIPE_RIGHT = 1;
    public static final int TIME_POST_DELAYED = 5000; // in ms
    private final ArrayList<SwipeRunnable> mRunnables = new ArrayList<>();

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listadapter_item, parent, false);

        viewHolder= new myViewHolder(itemView);
        return viewHolder;

    }

    public ItemListRecyclerAdapter(Context context, List<ItemModel> itemList, IOnSwipe iOnSwipe) {
        this.context = context;
        this.itemList = itemList;
        this.iOnSwipe = iOnSwipe;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {
        holder.text.setText(itemList.get(position).getText());
        holder.text.setTag(position);
        if (itemList.get(position).isItem) {
            holder.itemLayout.setVisibility(View.VISIBLE);
            holder.undoLayout.setVisibility(View.GONE);
            holder.undoignoreLayout.setVisibility(View.GONE);
        }

        if (itemList.get(position).isUndo) {
            holder.itemLayout.setVisibility(View.GONE);
            holder.undoLayout.setVisibility(View.VISIBLE);
            holder.undoignoreLayout.setVisibility(View.GONE);
        }
        if (itemList.get(position).isUndoIgnore) {
            holder.itemLayout.setVisibility(View.GONE);
            holder.undoLayout.setVisibility(View.GONE);
            holder.undoignoreLayout.setVisibility(View.VISIBLE);
        }


        //Note:if u want only swipe action remove this button click listener
        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                try {


                    itemList.get(adapterPosition).setUndo(true);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }
                try {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.GONE);
                    holder.undoLayout.setVisibility(View.VISIBLE);
                    holder.undoLayout.startAnimation(inFromRightAnimation());
                    itemList.get(adapterPosition).setUndo(true);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {

                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        final SwipeRunnable runnable = new SwipeRunnable() {
                            @Override
                            public void run() {
                                synchronized (mRunnables) {
                                    iOnSwipe.onSwipe(mRunnables.indexOf(this), SWIPE_RIGHT);
                                }
                            }
                        };
                        synchronized (mRunnables) {
                            mRunnables.set(adapterPosition, runnable);
                            mHandler.postDelayed(runnable, TIME_POST_DELAYED);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Note:Do not remove this click listener,if u want undo action on item added
        holder.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                try {
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(true);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }

                try {
                    holder.itemLayout.setVisibility(View.VISIBLE);
                    holder.undoLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.GONE);
                    holder.undoLayout.startAnimation(outToLeftAnimation());
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(true);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        synchronized (mRunnables) {

                            Runnable runnable = mRunnables.set(adapterPosition, null);
                            mHandler.removeCallbacks(runnable);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });

        //Note:if u want only swipe action remove this button click listener
        holder.ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                try {

                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(true);
                } catch (Exception e) {
                }
                try {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.undoLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.VISIBLE);
                    holder.undoignoreLayout.startAnimation(inFromLeftAnimation());
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(true);
                } catch (Exception e) {

                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        final SwipeRunnable runnable = new SwipeRunnable() {
                            @Override
                            public void run() {
                                synchronized (mRunnables) {
                                    iOnSwipe.onSwipe(mRunnables.indexOf(this), SWIPE_LEFT);
                                }
                            }
                        };
                        synchronized (mRunnables) {
                            mRunnables.set(adapterPosition, runnable);
                            mHandler.postDelayed(runnable, TIME_POST_DELAYED);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Note:Do not remove this click listener,if u want undo action on item ignored
        holder.undoIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                try {
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(true);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }

                try {
                    holder.itemLayout.setVisibility(View.VISIBLE);
                    holder.undoLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.startAnimation(outToRightAnimation());
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(true);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        synchronized (mRunnables) {

                            Runnable runnable = mRunnables.set(adapterPosition, null);
                            mHandler.removeCallbacks(runnable);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });


        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Note:for swipe action,if swipe action is not required u can remove this touch listener
        holder.itemLayout.setOnTouchListener(new OnSwipeTouchListener(context) {
            int adapterPosition=holder.getAdapterPosition();
            @Override
            public boolean onSwipeRight() {
                Toast.makeText(context, " item left to right swipe", Toast.LENGTH_SHORT).show();
                int adapterPosition = holder.getAdapterPosition();
                try {

                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(true);
                } catch (Exception e) {
                }
                try {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.undoLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.VISIBLE);
                    holder.undoignoreLayout.startAnimation(inFromLeftAnimation());
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(true);
                } catch (Exception e) {

                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        final SwipeRunnable runnable = new SwipeRunnable() {
                            @Override
                            public void run() {
                                synchronized (mRunnables) {
                                    iOnSwipe.onSwipe(mRunnables.indexOf(this), SWIPE_LEFT);
                                }
                            }
                        };
                        synchronized (mRunnables) {
                            mRunnables.set(adapterPosition, runnable);
                            mHandler.postDelayed(runnable, TIME_POST_DELAYED);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            public boolean onSwipeLeft() {
                Toast.makeText(context, " item right to left swipe", Toast.LENGTH_SHORT).show();
                try {

                    itemList.get(adapterPosition).setUndo(true);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }
                try {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.GONE);
                    holder.undoLayout.setVisibility(View.VISIBLE);
                    holder.undoLayout.startAnimation(inFromRightAnimation());
                    itemList.get(adapterPosition).setUndo(true);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {

                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        final SwipeRunnable runnable = new SwipeRunnable() {
                            @Override
                            public void run() {
                                synchronized (mRunnables) {
                                    iOnSwipe.onSwipe(mRunnables.indexOf(this), SWIPE_RIGHT);
                                }
                            }
                        };
                        synchronized (mRunnables) {
                            mRunnables.set(adapterPosition, runnable);
                            mHandler.postDelayed(runnable, TIME_POST_DELAYED);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            public boolean onSwipeTop() {
                return true;
            }

            @Override
            public boolean onSwipeBottom() {
                return true;
            }
        });
        /*//Note:for swipe action,if swipe action is not required u can remove this touch listener
        holder.itemLayout.setOnTouchListener(new RelativeLayoutTouchListener(context,holder.getAdapterPosition()) {
            int adapterPosition=holder.getAdapterPosition();
            @Override
            public void onRightToLeftSwipe() {
                super.onRightToLeftSwipe();
                Toast.makeText(context, " item right to left swipe", Toast.LENGTH_SHORT).show();
                try {

                    itemList.get(adapterPosition).setUndo(true);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {
                }
                try {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.GONE);
                    holder.undoLayout.setVisibility(View.VISIBLE);
                    holder.undoLayout.startAnimation(inFromRightAnimation());
                    itemList.get(adapterPosition).setUndo(true);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(false);
                } catch (Exception e) {

                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        final SwipeRunnable runnable = new SwipeRunnable() {
                            @Override
                            public void run() {
                                synchronized (mRunnables) {
                                    iOnSwipe.onSwipe(mRunnables.indexOf(this), SWIPE_RIGHT);
                                }
                            }
                        };
                        synchronized (mRunnables) {
                            mRunnables.set(adapterPosition, runnable);
                            mHandler.postDelayed(runnable, TIME_POST_DELAYED);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onLeftToRightSwipe() {
                super.onLeftToRightSwipe();
                Toast.makeText(context, " item left to right swipe", Toast.LENGTH_SHORT).show();
                int adapterPosition = holder.getAdapterPosition();
                try {

                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(true);
                } catch (Exception e) {
                }
                try {
                    holder.itemLayout.setVisibility(View.GONE);
                    holder.undoLayout.setVisibility(View.GONE);
                    holder.undoignoreLayout.setVisibility(View.VISIBLE);
                    holder.undoignoreLayout.startAnimation(inFromLeftAnimation());
                    itemList.get(adapterPosition).setUndo(false);
                    itemList.get(adapterPosition).setItem(false);
                    itemList.get(adapterPosition).setUndoIgnore(true);
                } catch (Exception e) {

                }
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    try {
                        final SwipeRunnable runnable = new SwipeRunnable() {
                            @Override
                            public void run() {
                                synchronized (mRunnables) {
                                    iOnSwipe.onSwipe(mRunnables.indexOf(this), SWIPE_LEFT);
                                }
                            }
                        };
                        synchronized (mRunnables) {
                            mRunnables.set(adapterPosition, runnable);
                            mHandler.postDelayed(runnable, TIME_POST_DELAYED);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void itemClicked() {
                super.itemClicked();
                Toast.makeText(context, " item Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.additem)
        Button addItem;
        @Bind(R.id.itemlayout)
        RelativeLayout itemLayout;
        @Bind(R.id.undolayout)
        RelativeLayout undoLayout;
        @Bind(R.id.undo)
        TextView undo;
        @Bind(R.id.undoignorelayout)
        RelativeLayout undoignoreLayout;
        @Bind(R.id.undo_ignore)
        TextView undoIgnore;
        @Bind(R.id.ignore)
        Button ignore;

        public myViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //Helpers for swipe action
    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mRunnables.size() == 0) {
            int count = getItemCount();
            for (int i = 0; i < count; i++) {
                mRunnables.add(null);
            }
        }
        registerAdapterDataObserver(new SwipeAdapterDataObserver());
    }

    //helper to remove item
    private class SwipeAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        public void onChanged() {
            synchronized (mRunnables) {
                int size = mRunnables.size();
                int itemCount = getItemCount();
                if (itemCount > size) {
                    onItemRangeChanged(0, size);
                    onItemRangeInserted(size, itemCount - size);
                } else {
                    onItemRangeChanged(0, itemCount);
                    onItemRangeRemoved(itemCount, size - itemCount);
                }
            }
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            synchronized (mRunnables) {
                for (int i = 0; i < itemCount; i++) {
                    Runnable r = mRunnables.set(positionStart + i, null);
                    if (r != null) {
                        mHandler.removeCallbacks(r);
                    }
                }
            }
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            synchronized (mRunnables) {
                for (int i = 0; i < itemCount; i++) {
                    mRunnables.add(positionStart, null);
                }
            }
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            synchronized (mRunnables) {
                for (int i = 0; i < itemCount; i++) {
                    int c = fromPosition > toPosition ? i : 0;
                    mRunnables.set(toPosition + c, mRunnables.remove(fromPosition + c));
                }
            }
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            synchronized (mRunnables) {
                for (int i = 0; i < itemCount; i++) {
                    Runnable r = mRunnables.remove(positionStart);
                    if (r != null) {
                        mHandler.removeCallbacks(r);
                    }
                }
            }
        }
    }

    //remove item after specified delay
    public void remove(int position) {
        try {
            Toast.makeText(context, itemList.get(position).getText() + " removed", Toast.LENGTH_SHORT).show();
            itemList.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*//class required for swipe action,Touch
    public abstract class RelativeLayoutTouchListener implements View.OnTouchListener {

        static final String logTag = "ActivitySwipeDetector";
        private Context activity;
        private int position;
        static final int MIN_DISTANCE = 2;
        private float downX, downY, upX, upY;

        // private MainActivity mMainActivity;

        public RelativeLayoutTouchListener(Context mainActivity,int position) {
            activity = mainActivity;
            this.position=position;
        }

        public void onRightToLeftSwipe() {
            Log.i(logTag, "RightToLeftSwipe!");
        }

        public void onLeftToRightSwipe() {
            Log.i(logTag, "LeftToRightSwipe!");
        }

        public void onTopToBottomSwipe() {
            Log.i(logTag, "onTopToBottomSwipe!");
        }

        public void onBottomToTopSwipe() {
            Log.i(logTag, "onBottomToTopSwipe!");
        }

        public void itemClicked() {
            Log.i(logTag, "Clicked!");
        }
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    // swipe horizontal?
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // left or right
                        if (deltaX < 0) {
                            this.onLeftToRightSwipe();
                            return true;
                        }
                        if (deltaX > 0) {
                            this.onRightToLeftSwipe();
                            return true;
                        }
                    } else {
                           this.itemClicked();
                        return true;
                    }

                    // swipe vertical?
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        // top or down
                        if (deltaY < 0) {
                            this.onTopToBottomSwipe();
                            return true;
                        }
                        if (deltaY > 0) {
                            this.onBottomToTopSwipe();
                            return true;
                        }
                    } else {
                        Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                        // return false; // We don't consume the event
                    }

                    return false; // no swipe horizontally and no swipe vertically
                }// case MotionEvent.ACTION_UP:
            }
            return false;
        }

    }*/

    //class required for swipe action,Gesture
    public abstract class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        public boolean onTouch(final View v, final MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 5;
            private static final int SWIPE_VELOCITY_THRESHOLD = 5;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                result = onSwipeRight();
                            } else {
                                result = onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                result = onSwipeBottom();
                            } else {
                                result = onSwipeTop();
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
            public boolean onSwipeRight() {
                return false;
            }

            public boolean onSwipeLeft() {
                return false;
            }

            public boolean onSwipeTop() {
                return false;
            }

            public boolean onSwipeBottom() {
                return false;
            }

        }
}

