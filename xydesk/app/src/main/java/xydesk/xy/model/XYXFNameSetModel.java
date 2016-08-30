package xydesk.xy.model;

import java.io.Serializable;

/**
 * Created by haizeiym
 * on 2016/8/30
 */
public class XYXFNameSetModel implements Serializable {
    //设置的APP名称
    public String set_app_name;
    //设置的APP包名（无法修改）
    public String set_app_package_name;
    //设置的联系人名称
    public String set_contact_name;
    //设置的联系人的号码
    public String set_contact_number;
}
