package com.mattmartin.faithbible.audiosearchapi.models;

public class AudioJsonSource {

    private String urlToJson;

    public String getUrlToJson() {
        return urlToJson;
    }

    public void setUrlToJson(String urlToJson) {
        this.urlToJson = urlToJson;
    }

    public AudioJsonSource(final String url){
        urlToJson = url;
    }
}
