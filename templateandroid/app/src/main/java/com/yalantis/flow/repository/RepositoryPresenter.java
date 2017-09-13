package com.yalantis.flow.repository;

import com.yalantis.base.BaseMvpPresenterImpl;
import com.yalantis.data.Repository;
import com.yalantis.data.source.repository.ReposRepository;

import java.util.List;

import io.reactivex.functions.Consumer;

class RepositoryPresenter extends BaseMvpPresenterImpl<RepositoryContract.View> implements RepositoryContract.Presenter {

    private static final String ORGANIZATION_NAME = "Yalantis";

    private ReposRepository mRepository;

    @Override
    public void attachView(RepositoryContract.View view) {
        super.attachView(view);
        mRepository = new ReposRepository(view.getContext());
    }

    @Override
    public void initRepositories() {
        fetchRepositories(true);
    }

    @Override
    public void fetchRepositories() {
        mView.showProgress();
        fetchRepositories(false);
    }

    private void fetchRepositories(boolean local) {
        addDisposable(mRepository.getRepositories(ORGANIZATION_NAME, local)
                .subscribe(new Consumer<List<Repository>>() {
                    @Override
                    public void accept(List<Repository> repositories) {
                        mView.hideProgress();
                        mView.showRepositories(repositories);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.hideProgress();
                        mView.showErrorMessage();
                    }
                }));
    }

    @Override
    public void onRepositoryClicked(Repository repository) {
        mView.showInfoMessage("Repository has " + repository.getStarsCount() + " stars.");
    }

    @Override
    public void detachView() {
        super.detachView();
        mRepository.clear();
    }
}
