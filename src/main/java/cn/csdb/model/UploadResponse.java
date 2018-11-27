package cn.csdb.model;

/**
 * Created by sophie on 2018/4/27.
 */
public class UploadResponse {
    private String name;
    private long size;
    private String state;
    private String type;
    private String url;

    public UploadResponse(String name, long size, String type, String url, String state) {
        this.name = name;
        this.size = size;
        this.state = state;
        this.type = type;
        this.url = url;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}