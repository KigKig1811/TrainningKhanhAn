package com.vnpay.anlmk.utils.captures;

/**
 * Created by Lvhieu2016 on 11/3/2016.
 */

public interface IMoneyReader {
    String convert(String value);

    void setCcy(String ccy);
}
