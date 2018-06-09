package com.nextbit.yassin.movieappudacity1.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yassin on 10/8/17.
 */

public class TrailerList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getTraliers() {
        return results;
    }

    public void setTraliers(List<Trailer> results) {
        this.results = results;
    }
}
