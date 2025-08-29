package org.ecommersebe.ecommersebe.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatPrice(double price) {
        double actualPrice = price * 1000;
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(actualPrice) + " VND";
    }
}