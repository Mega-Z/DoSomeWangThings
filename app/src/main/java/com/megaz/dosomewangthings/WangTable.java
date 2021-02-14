package com.megaz.dosomewangthings;

import java.util.Calendar;

public class WangTable {
    public int WangingLenth;
    public int WangedLenth;
    public WangPlan wanging_head;
    public WangPlan wanged_head;

    WangTable(String str){
        String str_split[] = str.split("/",-1);
        wanging_head = new WangPlan(-1);
        wanged_head = new WangPlan(-1);
        WangingLenth = 0;
        WangedLenth = 0;
        WangPlan pp1 = wanging_head;
        WangPlan pp2 = wanged_head;
        if(!str_split[0].equals("")){
            String plan_ing[]=str_split[0].split("#");
            for(int i=0;i<plan_ing.length;i++){
                if(!plan_ing[i].equals("")){
                    pp1.next = new WangPlan(Integer.parseInt(plan_ing[i]));
                    pp1 = pp1.next;
                    WangingLenth++;
                }
            }
        }
        if(!str_split[1].equals("")){
            String plan_ed[]=str_split[1].split("#");
            for(int i=0;i<plan_ed.length;i++){
                if(plan_ed[i].split("@").length==2){
                    String history[] = plan_ed[i].split("@");
                    pp2.next = new WangPlan(Integer.parseInt(history[0]));
                    pp2.next.cleared = history[1];
                    pp2 = pp2.next;
                    WangedLenth++;
                }
            }
        }
    }
    public String TableToString(){
        String str="";
        WangPlan pp1 = wanging_head;
        WangPlan pp2 = wanged_head;
        while(pp1.next!=null){
            str += "#"+String.valueOf(pp1.next.wang_id);
            pp1 = pp1.next;
        }
        str+="/";
        while(pp2.next!=null){
            str += "#"+String.valueOf(pp2.next.wang_id)+"@"+pp2.next.cleared;
            pp2 = pp2.next;
        }
        return str;
    }
    public WangPlan GetWangPlan(int index){
        if(index<WangingLenth+WangedLenth){
            if(index<WangingLenth){
                WangPlan pp = wanging_head;
                for(int i=0;i<index;i++){
                    pp = pp.next;
                }
                return pp.next;
            }else{
                index -= WangingLenth;
                WangPlan pp = wanged_head;
                for(int i=0;i<index;i++){
                    pp = pp.next;
                }
                return pp.next;
            }
        }
        else{
            return null;
        }
    }
    public void AddWangPlan(int id){
        WangPlan pp = wanging_head;
        while (pp.next!=null){
            pp = pp.next;
        }
        pp.next = new WangPlan(id);
        WangingLenth++;
    }
    public void ClearWangPlan(int index){
        if(index<WangingLenth){
            WangPlan pp1 = wanging_head;
            WangPlan pp2 = wanged_head;
            for(int i=0;i<index;i++){
                pp1 = pp1.next;
            }
            int clear_id = pp1.next.wang_id;
            pp1.next = pp1.next.next;
            WangingLenth--;
            while(pp2.next!=null){
                pp2 = pp2.next;
            }
            pp2.next = new WangPlan(clear_id);
            pp2.next.cleared = GetDate();
            WangedLenth++;
        }
    }
    public void DeleteWangPlan(int index){
        if(index<WangingLenth+WangedLenth){
            if(index<WangingLenth){
                WangPlan pp = wanging_head;
                for(int i=0;i<index;i++){
                    pp = pp.next;
                }
                pp.next = pp.next.next;
                WangingLenth--;
            }else{
                index -= WangingLenth;
                WangPlan pp = wanged_head;
                for(int i=0;i<index;i++){
                    pp = pp.next;
                }
                pp.next = pp.next.next;
                WangedLenth--;
            }
        }
    }
    public void DelWangPlanByID(int id){
        WangPlan pp1 = wanging_head;
        WangPlan pp2 = wanged_head;
        while(pp1.next!=null){
            if(pp1.next.wang_id==id){
                pp1.next = pp1.next.next;
                WangingLenth--;
            }else if(pp1.next.wang_id>id){
                pp1.next.wang_id--;
                pp1 = pp1.next;
            }else{
                pp1 = pp1.next;
            }
        }
        while(pp2.next!=null){
            if(pp2.next.wang_id==id){
                pp2.next = pp2.next.next;
                WangedLenth--;
            }else if(pp2.next.wang_id>id){
                pp2.next.wang_id--;
                pp2 = pp2.next;
            }else{
                pp2 = pp2.next;
            }
        }
    }
    private String GetDate(){
        String mYear,mMonth,mDay;
        Calendar calendar = Calendar.getInstance();
        mYear = String.valueOf(calendar.get(Calendar.YEAR));
        mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);        //获取日期的月
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));      //获取日期的天
        return(mYear+"."+mMonth+"."+mDay);
    }
}
