package com.example.bookshop.util;

import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@NoArgsConstructor
public class ConvetDateTimeUTC {
    public String convertDateTimeUTC(Date dateTimeString){
        SimpleDateFormat sdfGMT7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        sdfGMT7.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdfGMT7.format(dateTimeString);
    }
}

//        DateTimeFormatter formatter=DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//        OffsetDateTime dateTime=OffsetDateTime.parse(dateTimeString, formatter);
//        ZoneId zoneIdPlus7 = ZoneId.of("GMT+7");
//        return dateTime.atZoneSameInstant(zoneIdPlus7).toOffsetDateTime().toString();
