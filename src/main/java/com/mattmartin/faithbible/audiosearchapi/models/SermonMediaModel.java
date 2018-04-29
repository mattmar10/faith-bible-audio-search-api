package com.mattmartin.faithbible.audiosearchapi.models;

public class SermonMediaModel {

    private String pdfUrl;
    private String mp3Url;

    SermonMediaModel(){}

    public SermonMediaModel(final String pdfUrl, final String mp3Url){
        this.pdfUrl = pdfUrl;
        this.mp3Url = mp3Url;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SermonMediaModel that = (SermonMediaModel) o;

        if (pdfUrl != null ? !pdfUrl.equals(that.pdfUrl) : that.pdfUrl != null) return false;
        return mp3Url != null ? mp3Url.equals(that.mp3Url) : that.mp3Url == null;
    }

    @Override
    public int hashCode() {
        int result = pdfUrl != null ? pdfUrl.hashCode() : 0;
        result = 31 * result + (mp3Url != null ? mp3Url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SermonMediaModel{" +
                "pdfUrl='" + pdfUrl + '\'' +
                ", mp3Url='" + mp3Url + '\'' +
                '}';
    }
}
