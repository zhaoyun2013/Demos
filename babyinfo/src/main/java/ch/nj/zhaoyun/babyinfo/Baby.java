package ch.nj.zhaoyun.babyinfo;

import android.content.Context;

/**
 * Created by Administrator on 2017/5/19.
 */

public class Baby {
    private String name;
    private String birthday;
    private String photo;

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Baby(String name, String birthday,String photo){
        this.name = name;
        this.birthday = birthday;
        this.photo = photo;
    }

    public String getConstellation(Context context) {
        int[] times = DateUtils.getYMDHMS(birthday);
       return DateUtils.getConstellation(times[1],times[2],context);
    }
}
