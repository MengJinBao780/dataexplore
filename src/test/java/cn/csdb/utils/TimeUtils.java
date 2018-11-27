package cn.csdb.utils;

import javafx.scene.input.DataFormat;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ajian on 2018/5/29.
 */

public class TimeUtils {

    @Test
    public void test() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 0 - 51);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(calendar.getTime()));
    }


}
