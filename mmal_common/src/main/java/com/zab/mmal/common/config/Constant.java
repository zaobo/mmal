package com.zab.mmal.common.config;

import com.google.common.collect.Sets;

import java.util.Set;

public interface Constant {

    String CURRENT_USER = "currentUser";
    String TOKEN_PRFIX = "token_";

    int ROLE_CUSTOMER = 0;
    int ROLE_ADMIN = 1;

    Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");

}
