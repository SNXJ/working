package com.badou.mworking.domain.category;

import com.badou.mworking.domain.UseCase;
import com.badou.mworking.entity.user.UserInfo;
import com.badou.mworking.net.RestRepository;

import rx.Observable;

public class MarkReadUseCase extends UseCase {

    String mRid;

    public MarkReadUseCase(String rid) {
        this.mRid = rid;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return RestRepository.getInstance().markRead(UserInfo.getUserInfo().getUid(), mRid);
    }
}
