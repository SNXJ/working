package com.badou.mworking.entity.category;

import com.badou.mworking.net.ResponseParameters;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class CategorySearchOverall {
    List<CategorySearch> notice;
    List<CategorySearch> training;
    List<CategorySearch> exam;
    List<CategorySearch> task;
    List<CategorySearch> shelf;
    List<CategorySearch> entry;

    public List<CategorySearch> getNotice() {
        initType(Category.CATEGORY_NOTICE, notice);
        return notice;
    }

    public List<CategorySearch> getTraining() {
        initType(Category.CATEGORY_TRAINING, training);
        return training;
    }

    public List<CategorySearch> getExam() {
        initType(Category.CATEGORY_EXAM, exam);
        return exam;
    }

    public List<CategorySearch> getTask() {
        initType(Category.CATEGORY_TASK, task);
        return task;
    }

    public List<CategorySearch> getShelf() {
        initType(Category.CATEGORY_SHELF, shelf);
        return shelf;
    }

    public List<CategorySearch> getEntry() {
        initType(Category.CATEGORY_ENTRY, entry);
        return entry;
    }

    private void initType(int type, List<CategorySearch> list) {
        for (CategorySearch item : list) {
            item.type = type;
        }
    }
}