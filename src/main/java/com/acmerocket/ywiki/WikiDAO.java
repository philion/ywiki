package com.acmerocket.ywiki;

import org.jvnet.hk2.annotations.Contract;

import com.acmerocket.ywiki.model.WikiEntry;

@Contract
public interface WikiDAO {
    
    public WikiEntry get(String path);
    public void update(WikiEntry entry);

}
