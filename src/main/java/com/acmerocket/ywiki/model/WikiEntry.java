/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.acmerocket.ywiki.model;

import java.util.Date;

import com.acmerocket.ywiki.Dates;

public class WikiEntry {
    private String path;
    
    private Date edited = new Date();
    private String title;
    private String user;
    private String content;
        
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return this.path;
    }

    public Date getEdited() {
        return this.edited;
    }

    public void setEdited(Date edited) {
      this.edited = edited;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String toString() {
        // /what #when @who; what happened when, to/by whom
        return "/" + this.path + " #" + Dates.toShortString(this.edited) + " @" + this.user;
    }
}
