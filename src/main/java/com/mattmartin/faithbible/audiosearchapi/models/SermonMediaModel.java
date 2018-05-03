package com.mattmartin.faithbible.audiosearchapi.models;

public class SermonMediaModel {

    private String pdf;
    private String mp3;

    SermonMediaModel(){}

    public SermonMediaModel(final String pdf, final String mp3){
        this.pdf = pdf;
        this.mp3 = mp3;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
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
