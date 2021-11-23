package com.vnpay.anlmk.networks.entities;

import com.google.gson.annotations.SerializedName;

public class AccountEntity {
    @SerializedName("accountNo")
    private String accountNo ;
    @SerializedName("ccy")
    private String ccy;
    @SerializedName("balance")
    private String balance;
    @SerializedName("isDefault")
    private boolean isDefault;
    @SerializedName("accountType")
    private String accountType;

    public AccountEntity(String accountNo, String balance, String accountType, String ccy, boolean isDefault) {
        this.accountNo = accountNo;
        this.ccy = ccy;
        this.balance = balance;
        this.isDefault = isDefault;
        this.accountType = accountType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getCcy() {
        return ccy;
    }

    public String getBalance() {
        return balance;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getAccountType() {
        return accountType;
    }
}
