/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilingdevil.devilstats.api;

/**
 *
 * @author devil
 */
public class APIKey {
    private String key = null;
    
    public APIKey(String value) {
        this.key = value;
    }
    
    public String getValue() {
        return this.key;
    }
}
