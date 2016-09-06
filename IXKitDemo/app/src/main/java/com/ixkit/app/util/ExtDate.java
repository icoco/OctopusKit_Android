package com.ixkit.app.util;

/**
 * Created by icoco on 8/25/16.
 */
import java.util.Date;

public class ExtDate {
    public Date from;
    public Date to;

    public  long durationMilliSecond(){
        Tracer.d("durationSecond from:%s to:%s",from,to);

        long difference = to.getTime() - from.getTime();

        return difference;// / 1000 ;
        // return difference / (1000 * 60);
    }
}
