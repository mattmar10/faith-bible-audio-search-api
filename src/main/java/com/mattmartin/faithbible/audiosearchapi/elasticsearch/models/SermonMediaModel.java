package com.mattmartin.faithbible.audiosearchapi.elasticsearch.models;

import java.util.Optional;

public class SermonMediaModel {

    private Optional<String> pdf;
    private Optional<String> mp3;

    SermonMediaModel(){}

    public SermonMediaModel(final String pdf, final String mp3) throws IllegalArgumentException{


        this.pdf = Optional.ofNullable(pdf);
        this.mp3 = Optional.ofNullable(mp3);
    }

    public Optional<String> getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = Optional.ofNullable(pdf);
    }

    public Optional<String> getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) throws IllegalArgumentException {
        this.mp3 = Optional.ofNullable(mp3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SermonMediaModel that = (SermonMediaModel) o;

        if (pdf != null ? !pdf.equals(that.pdf) : that.pdf != null) return false;
        return mp3 != null ? mp3.equals(that.mp3) : that.mp3 == null;
    }

    @Override
    public int hashCode() {
        int result = pdf != null ? pdf.hashCode() : 0;
        result = 31 * result + (mp3 != null ? mp3.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SermonMediaModel{" +
                "pdf='" + pdf + '\'' +
                ", mp3='" + mp3 + '\'' +
                '}';
    }
}
