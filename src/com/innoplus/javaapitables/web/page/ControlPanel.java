package com.innoplus.javaapitables.web.page;

public class ControlPanel extends org.apache.click.Page {

    public ControlPanel(){
    }

    @Override
    public boolean onSecurityCheck() {
        if (getContext().getRequest().getRemoteUser() == null) {
            return false;
        }

        addModel("username", getContext().getRequest().getRemoteUser());

        return true;
    }

    

}