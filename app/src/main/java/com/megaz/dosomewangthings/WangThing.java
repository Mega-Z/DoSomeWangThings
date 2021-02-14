package com.megaz.dosomewangthings;

public class WangThing {
    String thing;
    int times;
    WangThing next=null;

    WangThing(String str, int n){
        thing = str;
        times = n;
    }
}
