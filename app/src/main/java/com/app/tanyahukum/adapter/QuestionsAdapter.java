package com.app.tanyahukum.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tanyahukum.R;
import com.app.tanyahukum.model.Consultations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/9/17.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsHolder> {
    private List<Consultations> listQuestions;
    private QuestionsAdapterCallback mCallback;

    @Inject
    public QuestionsAdapter() {
        listQuestions = new ArrayList<>();
    }

    public void setQuestions(List<Consultations> listQuestions) {
        this.listQuestions.addAll(listQuestions);
    }

    @Override
    public QuestionsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_questions,
                parent, false);
        return new QuestionsHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsHolder holder, int position) {
        Consultations questions = listQuestions.get(position);
        holder.setConsultations(questions);
        holder.consultationDate.setText(questions.getConsultationsDate());
        holder.title.setText(questions.getTitle());
        holder.status.setText(questions.getStatus());
    }

    public void setCallback(QuestionsAdapterCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return listQuestions.size();
    }

    class QuestionsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.consultationDate)
        TextView consultationDate;
        @BindView(R.id.titleConsultations)
        TextView title;
        @BindView(R.id.statusConsultations)
        TextView status;

        Consultations consultations;
        public QuestionsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setConsultations(Consultations consultation) {
            this.consultations = consultation;
        }

        @OnClick(R.id.row_layout)
        void onItemClicked(View view) {
            if (mCallback != null) mCallback.onQuestionsClicked(consultations);
        }

        @OnClick(R.id.btnDelete)
        public void onDeleteConsultationClicked() {
            if (mCallback != null) mCallback.onDeleteConsultationClicked(consultations.getConsultationId());
        }

    }

    public interface QuestionsAdapterCallback {
        void onQuestionsClicked(Consultations consultations);
        void onDeleteConsultationClicked(String consultationId);
    }
}
