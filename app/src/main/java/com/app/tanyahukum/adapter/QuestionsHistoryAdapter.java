package com.app.tanyahukum.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tanyahukum.R;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/9/17.
 */

public class QuestionsHistoryAdapter extends RecyclerView.Adapter<QuestionsHistoryAdapter.QuestionsHolder> {
    private List<HistoryConsultations> listQuestions;
    private QuestionsAdapterCallback mCallback;
    @Inject
    public QuestionsHistoryAdapter() {
        listQuestions = new ArrayList<>();
    }
    public void setMovies(List<HistoryConsultations> listQuestions) {
        this.listQuestions.addAll(listQuestions);
    }
    @Override
    public QuestionsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_history_questions,
                parent, false);
        return new QuestionsHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsHolder holder, int position) {
        HistoryConsultations questions = listQuestions.get(position);
        holder.setConsultations(questions);
        holder.questionsOld.setText(questions.getQuestionsOld());
        holder.answersOld.setText(questions.getAnswersOld());
        holder.questionsDate.setText(questions.getQuestionsDate());
        holder.answersDate.setText(questions.getAnswersDate());
    }
   private void simpleDateFormat(String date){

   }
    public void setCallback(QuestionsAdapterCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return listQuestions.size();
    }

    class QuestionsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.questions)
        TextView questionsOld;
        @BindView(R.id.answers)
        TextView answersOld;
        @BindView(R.id.questionsDate)
        TextView questionsDate;
        @BindView(R.id.answersDate)
        TextView answersDate;
        HistoryConsultations consultations;
        public QuestionsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void setConsultations(HistoryConsultations consultation) {
            this.consultations = consultation;
        }
        @OnClick(R.id.row_layout)
        void onItemClicked(View view) {
            if (mCallback != null) mCallback.onQuestionsClicked(consultations);
        }
    }

    public static interface QuestionsAdapterCallback {
        public void onQuestionsClicked(HistoryConsultations consultations);
    }
}
