package com.darzul.hoshiyo.wictrip.dao;

import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public interface IDao {
    public Object create(Object obj);
    public Collection<Object> getAll();
    public Object getItemById(int id);
    public Object update(Object obj);
    public Object delete(Object obj);
    public boolean exist(Object obj);
    public void refreshData();
}