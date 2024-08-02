package com.meli.middleend.client.support;

import com.meli.middleend.dto.ApiCheckResult;

public interface CheckHealth {

    void addOkRecord(ApiCheckResult apiCheckResult);

    void addErrorRecord(ApiCheckResult apiCheckResult);
}
