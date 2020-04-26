package com.example.daggertutorial.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RandomUser {
    @SerializedName("results")
    @Expose
    private List<Result> resultList = null;

    @SerializedName("info")
    @Expose
    private Info info;

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("results", resultList)
                .append("info", info)
                .toString();
    }
}
