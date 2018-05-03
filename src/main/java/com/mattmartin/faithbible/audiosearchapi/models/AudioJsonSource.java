package com.mattmartin.faithbible.audiosearchapi.models;

public class AudioJsonSource {

    private String urlToJson;

    public AudioJsonSource(){}

    public String getUrlToJson() {
        return urlToJson;
    }

    public void setUrlToJson(String urlToJson) {
        this.urlToJson = urlToJson;
    }

    public AudioJsonSource(final String url){
        urlToJson = url;
    }

    @Override
    public String toString() {
        return "AudioJsonSource{" +
                "urlToJson='" + urlToJson + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AudioJsonSource that = (AudioJsonSource) o;

        return urlToJson != null ? urlToJson.equals(that.urlToJson) : that.urlToJson == null;
    }

    @Override
    public int hashCode() {
        return urlToJson != null ? urlToJson.hashCode() : 0;
    }
}
