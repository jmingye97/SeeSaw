package com.microsoft.projectoxford.face.samples.ui;

import com.microsoft.projectoxford.face.contract.VerifyResult;

/**
 * Created by JonathanYe on 10/8/16.
 */

public class NamedResults extends VerifyResult {
    public String name;
    public boolean verified;
    public double confidence;
    public NamedResults(boolean verified, double confidence, String name){
        this.verified = verified;
        this.confidence = confidence;
        this.name = name;
    };
}
