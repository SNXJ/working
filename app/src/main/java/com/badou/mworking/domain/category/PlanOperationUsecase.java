package com.badou.mworking.domain.category;

import com.badou.mworking.domain.UseCase;
import com.badou.mworking.entity.user.UserInfo;
import com.badou.mworking.net.RestRepository;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by SNXJ on 2015/8/5.
 */
public class PlanOperationUsecase extends UseCase {

    List<String> rids = new ArrayList<>();

    public void setRids(List<String> rids) {
        this.rids.clear();
        this.rids.addAll(rids);
    }

    public void setRid(String rid) {
        rids.clear();
        rids.add(rid);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return RestRepository.getInstance().getResourceInfo(UserInfo.getUserInfo().getUid(), rids);
    }
}
