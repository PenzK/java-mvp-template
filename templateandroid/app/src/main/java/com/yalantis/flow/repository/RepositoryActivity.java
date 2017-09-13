package com.yalantis.flow.repository;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.yalantis.R;
import com.yalantis.base.BaseActivity;
import com.yalantis.data.Repository;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RepositoryActivity extends BaseActivity implements RepositoryContract.View {

    @BindView(R.id.recycler_view_main)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    private RepositoryPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new RepositoryPresenter();
        mPresenter.attachView(this);

        setupRecyclerView();

        mPresenter.initRepositories();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_example;
    }

    private void setupRecyclerView() {
        RepositoryAdapter adapter = new RepositoryAdapter(new RepositoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Repository repository) {
                mPresenter.onRepositoryClicked(repository);
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showRepositories(List<Repository> repositoryList) {
        RepositoryAdapter adapter = (RepositoryAdapter) mRecyclerView.getAdapter();
        adapter.addRepositories(repositoryList);
    }

    @Override
    public void showProgress() {
        mFloatingActionButton.setEnabled(false);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mFloatingActionButton.setEnabled(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showInfoMessage(@NonNull String message) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage() {
        Snackbar.make(mRecyclerView, "Error", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab)
    void onClickFab(View view) {
        mPresenter.fetchRepositories();
    }

    @Override
    public Context getContext() {
        return RepositoryActivity.this;
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

}
