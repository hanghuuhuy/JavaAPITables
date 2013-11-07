package com.innoplus.javaapitables.web.page;

public class Logout extends org.apache.click.Page {

    public Logout(){

    }

    @Override
    public void onInit() {
        getContext().getSession().invalidate();
        setRedirect(ControlPanel.class);
    }

}