package com.acmerocket.ywiki;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class WikiBinder extends AbstractBinder {
    
    public static final WikiBinder INSTANCE = new WikiBinder();
    
    @Override
    protected void configure() {
        bind(InMemoryWikiDAO.class).to(WikiDAO.class);
    }
}