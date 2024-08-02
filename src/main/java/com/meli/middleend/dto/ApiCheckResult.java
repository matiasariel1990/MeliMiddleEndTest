package com.meli.middleend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiCheckResult {

    private String status;
    private boolean resultOk;
    private boolean timeoutFault;

    public static ApiCheckResult ApiCheckResultBuild(){
        return new ApiCheckResult("200", true, false);
    }

    public static ApiCheckResult ApiCheckBadResultBuild(String code){
        return new ApiCheckResult(code, false, false);
    }

    public static ApiCheckResult ApiCheckNotAvailableResultBuild(){
        return new ApiCheckResult("404", false, true);
    }
}
