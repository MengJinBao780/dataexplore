package cn.csdb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by ajian on 2018/5/15.
 */
@Document(collection = "sdo_relate")
public class SdoRelate implements Comparable<SdoRelate> {
    @Id
    private String id;
    @Field("sdo_id")
    private String sdoId;
    @Field("sdo_title")
    private String sdoTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSdoId() {
        return sdoId;
    }

    public void setSdoId(String sdoId) {
        this.sdoId = sdoId;
    }

    public String getSdoTitle() {
        return sdoTitle;
    }

    public void setSdoTitle(String sdoTitle) {
        this.sdoTitle = sdoTitle;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Field("score")

    private double score;


    @Override
    public int compareTo(SdoRelate o) {
        return score > o.getScore() ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SdoRelate sdoRelate = (SdoRelate) o;

        return sdoId.equals(sdoRelate.sdoId);
    }

    @Override
    public int hashCode() {
        return sdoId.hashCode();
    }

}
