package boomba.apps.android.ktmkotha.view.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import boomba.apps.android.ktmkotha.view.adapters.NearbyAdapter;

public class FeedItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        runEnterAnimation((NearbyAdapter.MyViewHolder) viewHolder);
        return false;
    }

    private void runEnterAnimation(final NearbyAdapter.MyViewHolder holder) {
        final int screenHeight = Utils.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }


}
