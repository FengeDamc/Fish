package com.fun.utils.math;

public class PerlinNoise {
    public double next,last;
    public double x;
    public float add=0;
    public PerlinNoise(){
        this.next= Math.random();
        this.last= Math.random();
        add=0.1f;
    }
    public PerlinNoise(float add){
        this.next= Math.random();
        this.last= Math.random();
        this.add=add;
    }
    public double next(){
        x+=add;
        if(x>1){
            x=0;
            this.last= next;

            this.next= Math.random();
        }
        return last+(3*x*x-2*x*x*x)*(next-last);
    }
}
