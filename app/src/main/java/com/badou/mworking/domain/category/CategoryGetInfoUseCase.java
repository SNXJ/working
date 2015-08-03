package com.badou.mworking.domain.category;

import com.badou.mworking.domain.UseCase;
import com.badou.mworking.entity.user.UserInfo;
import com.badou.mworking.net.RestRepository;

import java.util.List;

import rx.Observable;

public class CategoryGetInfoUseCase extends UseCase {

    List<String> ridList;

    public CategoryGetInfoUseCase(List<String> ridList) {
        this.ridList = ridList;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return RestRepository.getInstance().getResourceInfo(UserInfo.getUserInfo().getUid(), ridList);
    }
}
