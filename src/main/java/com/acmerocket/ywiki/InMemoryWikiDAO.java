package com.acmerocket.ywiki;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.hk2.annotations.Service;

import com.acmerocket.ywiki.model.WikiEntry;

@Service
public class InMemoryWikiDAO implements WikiDAO {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(InMemoryWikiDAO.class);

    private final Map<String, WikiEntry> entries = new HashMap<>();

    @Override
    public WikiEntry get(String path) {
        WikiEntry entry = this.entries.get(path);
        LOG.info("getting: {} -> {} [{}]", path, entry, this.entries.size());
        return entry;
    }

    @Override
    public void update(WikiEntry entry) {
        LOG.info("updating: {} -> {}", entry.getPath(), entry);
        
        WikiEntry old = this.entries.put(entry.getPath(), entry);
        LOG.info("### size: {}", this.entries.size());
        if (old != null) {
            LOG.info("replacing: {}", old);
        }
    }
}
