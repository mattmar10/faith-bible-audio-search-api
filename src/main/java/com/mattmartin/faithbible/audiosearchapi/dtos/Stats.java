package com.mattmartin.faithbible.audiosearchapi.dtos;

import java.util.Optional;

public class Stats {

    private Optional<Integer> plays;
    private Optional<Integer> likes;
    private Optional<Integer> shares;

    public Stats(){}

    public Stats(final int playCount, final int likesCount, final int shareCount){
        plays = Optional.ofNullable(playCount);
        likes = Optional.ofNullable(likesCount);
        shares = Optional.ofNullable(shareCount);
    }

    public Stats(Optional<Integer> plays, Optional<Integer> likes, Optional<Integer> shares) {
        this.plays = plays;
        this.likes = likes;
        this.shares = shares;
    }

    public Optional<Integer> getPlays() {
        return plays;
    }

    public void setPlays(Optional<Integer> plays) {
        this.plays = plays;
    }

    public Optional<Integer> getLikes() {
        return likes;
    }

    public void setLikes(Optional<Integer> likes) {
        this.likes = likes;
    }

    public Optional<Integer> getShares() {
        return shares;
    }

    public void setShares(Optional<Integer> shares) {
        this.shares = shares;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Stats{");
        sb.append("plays=").append(plays);
        sb.append(", likes=").append(likes);
        sb.append(", shares=").append(shares);
        sb.append('}');
        return sb.toString();
    }
}
