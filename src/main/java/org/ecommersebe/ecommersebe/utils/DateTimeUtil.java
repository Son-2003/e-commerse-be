package org.ecommersebe.ecommersebe.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtil {
    public static LocalDateTime nowInVietnam() {
        return LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }
}
