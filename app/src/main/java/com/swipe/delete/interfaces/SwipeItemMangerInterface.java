package com.swipe.delete.interfaces;

import com.swipe.delete.SwipeLayout;
import com.swipe.delete.implments.SwipeItemMangerImpl;

import java.util.List;

public interface SwipeItemMangerInterface {

    void openItem(int position);

    void closeItem(int position);

    void closeAllExcept(SwipeLayout layout);

    List<Integer> getOpenItems();

    List<SwipeLayout> getOpenLayouts();

    void removeShownLayouts(SwipeLayout layout);

    boolean isOpen(int position);

    SwipeItemMangerImpl.Mode getMode();

    void setMode(SwipeItemMangerImpl.Mode mode);
}
