package com.mattmartin.faithbible.audiosearchapi.dtos;

public class MP3File {

    private final String name;
    private final String path;

    public MP3File(final String name, final String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MP3File mp3File = (MP3File) o;

        if (!name.equals(mp3File.name)) return false;
        return path.equals(mp3File.path);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MP3File{");
        sb.append("name='").append(name).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
