package com.example.bonneannee.accelero;


public class Tap {
    private float axeZ;
    private Long time;

    public Tap(float z, Long time){
        this.axeZ = z;
        this.time = time;
    }


    public float getAxeZ() {
        return axeZ;
    }

    public void setAxeZ(float axeZ) {
        this.axeZ = axeZ;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
