package com.mk.admin.payroll.model;

import java.io.Serializable;

/**
 * Created by admin on 2/5/2018.
 */

public class AuthUtil implements Serializable
{
    public String access_token;
    public String token_type;
    public String refresh_token;
    public long expires_in;
    public String scope;
}
