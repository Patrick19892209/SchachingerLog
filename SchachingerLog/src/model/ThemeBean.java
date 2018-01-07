package model;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ApplicationScoped
@ManagedBean
public class ThemeBean {

    public String getTheme() {
        //"blitzer" "bluesky" "bootstrap" "cupertino"! "excite-bike" "redmond"!! "south-street"! "start"! "sunny" "ui-lightness"!!;
        return "ui-lightness";
    }

}
