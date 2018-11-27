package cn.csdb.utils;

import com.alibaba.fastjson.JSONObject;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {
    private Result.Head head = new Result.Head();
    private JSONObject body;

    public Result() {
    }

    public Result.Head getHead() {
        return this.head;
    }

    public void setHead(Result.Head head) {
        this.head = head;
    }

    public JSONObject getBody() {
        return this.body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public class Head {
        private int code;
        private String message;

        public Head() {
        }

        public int getCode() {
            return this.code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}