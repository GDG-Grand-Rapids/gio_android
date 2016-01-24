package com.sagetech.conference_android.app.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagetech.conference_android.app.R;
import com.sagetech.conference_android.app.ui.Views.ConferenceSessionViewItem;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionType;
import com.sagetech.conference_android.app.ui.viewModel.ConferenceSessionViewModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by carlushenry on 3/5/15.
 */
public class ConferenceSessionListAdapter extends RecyclerView.Adapter<ConferenceSessionListAdapter.BaseViewHolder> {
    private final List<ConferenceSessionViewModel> conferenceSessions;
    private ConferenceSessionListOnClickListener onClickListener;

    public interface ConferenceSessionListOnClickListener {
        void clicked(Long id);
    }

    public ConferenceSessionListAdapter(List<ConferenceSessionViewModel> conferenceSessions, ConferenceSessionListOnClickListener onClickListener) {
        this.conferenceSessions = conferenceSessions;
        this.onClickListener = onClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_day_hdr, parent, false);
            return new DayViewHolder(v);
        }

        ConferenceSessionViewItem v = new ConferenceSessionViewItem( parent.getContext() );

        return new ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getListItemType().getViewType();
    }

    @Override
    public void onBindViewHolder(ConferenceSessionListAdapter.BaseViewHolder holder, int position) {
        holder.setData(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public int getItemCount() {
        return conferenceSessions.size();
    }

    public ConferenceSessionViewModel getItem(int position) {
        return conferenceSessions.get(position);
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View v) {
            super(v);
        }

        public abstract void setData(ConferenceSessionViewModel confereneSessionViewModel);
    }


    public class ViewHolder extends BaseViewHolder implements View.OnClickListener
    {


        private ConferenceSessionViewModel conferenceSessionViewModel;
        private ConferenceSessionViewItem itemView;

        // each data item is just a string in this case
        public ViewHolder(View v)
        {
            super(v);

            if( v instanceof ConferenceSessionViewItem ) {
                itemView = (ConferenceSessionViewItem) v;
            }

            itemView.setOnClickListener( this );

        }


        public void setData(ConferenceSessionViewModel conferenceSessionViewModel)
        {
            this.conferenceSessionViewModel = conferenceSessionViewModel;

            itemView.setSessionInfo( conferenceSessionViewModel );
        }

        @Override
        public void onClick(View v) {
            onClickListener.clicked(conferenceSessionViewModel.getId());
        }
    }


    public class DayViewHolder extends BaseViewHolder {
        @Bind(R.id.dayTxt) public TextView dayView;

        private ConferenceSessionViewModel confereneSessionViewModel;

        // each data item is just a string in this case
        public DayViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void setDay(final String day) {
            this.dayView.setText(day);
        }


        public void setData(ConferenceSessionViewModel confereneSessionViewModel) {
            this.confereneSessionViewModel = confereneSessionViewModel;

            setDay(confereneSessionViewModel.getDay());
        }
    }
}
