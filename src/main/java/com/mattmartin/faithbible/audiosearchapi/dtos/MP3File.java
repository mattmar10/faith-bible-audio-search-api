package com.mattmartin.faithbible.audiosearchapi.dtos;

public class MP3File {

    private final String name;
    private final String url;
    private final String s3Key;

    public MP3File(final String name, final String url, final String s3Key) {
        this.name = name;
        this.url = url;
        this.s3Key = s3Key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getS3Key() { return s3Key; };


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3File mp3File = (MP3File) o;

        if (!name.equals(mp3File.name)) return false;
        if (!s3Key.equals(mp3File.s3Key)) return false;

        return url.equals(mp3File.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + s3Key.hashCode();
        return result;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MP3File{");
        sb.append("name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", s3Key='").append(s3Key).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
