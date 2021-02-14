package com.megaz.dosomewangthings;

import android.text.format.Time;

import java.util.Random;

public class WangDex {
    public int WangThingsNum;
    public WangThing head_thing;
    private Random random = new Random(System.currentTimeMillis());
    WangDex(String str){
        String str_things[] = str.split("@");
        head_thing = new WangThing("empty",0);
        WangThingsNum = 0;
        WangThing tp=head_thing;
        for(int i=0;i<str_things.length;i++){
            if(str_things[i].split("#").length==2){
                String str_a_thing[]=str_things[i].split("#");
                tp.next = new WangThing(str_a_thing[0], Integer.parseInt(str_a_thing[1]));
                tp=tp.next;
                WangThingsNum++;
            }
        }
    }
    public String DexToString(){
        String str="";
        WangThing tp=head_thing;
        while(tp.next!=null){
            str += "@"+tp.next.thing+"#"+String.valueOf(tp.next.times);
            tp = tp.next;
        }
        return str;
    }
    public WangThing GetWangThing(int index){
        WangThing tp=head_thing;
        if(index>=WangThingsNum || index<0)
            return null;
        for(int i=0;i<index;i++){
            tp = tp.next;
        }
        return tp.next;
    }
    public void DeleteWangThing(int index){
        WangThing tp=head_thing;
        //if(index>=WangThingsNum || index<0)
        for(int i=0;i<index;i++) {
            tp = tp.next;
        }
        tp.next = tp.next.next;
        WangThingsNum--;
    }
    public void AddWangThing(String str){
        WangThing tp=head_thing;
        while(tp.next!=null){
            tp = tp.next;
        }
        tp.next = new WangThing(str,0);
        WangThingsNum++;
    }
    public int[] RandomThree(){
        int indexs[] = new int[3];
        if(WangThingsNum<3){
            indexs[0] = 0;
            indexs[1] = 0;
            indexs[2] = 0;
        }
        else{
            indexs[0] = random.nextInt(WangThingsNum);
            indexs[1] = random.nextInt(WangThingsNum);
            while(indexs[0]==indexs[1]){
                indexs[1] = random.nextInt(WangThingsNum);
            }
            indexs[2] = random.nextInt(WangThingsNum);
            while(indexs[0]==indexs[2]||indexs[1]==indexs[2]){
                indexs[2] = random.nextInt(WangThingsNum);
            }
        }
        return indexs;
    }
}
