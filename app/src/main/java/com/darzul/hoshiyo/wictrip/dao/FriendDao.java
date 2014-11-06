package com.darzul.hoshiyo.wictrip.dao;

import java.util.Collection;

/**
 * Created by Guillaume 'DarzuL' Bourderye on 28/10/2014.
 */
public class FriendDao implements IDao {
    private static FriendDao ourInstance = new FriendDao();

    public static FriendDao getInstance() {
        return ourInstance;
    }

    private FriendDao() {
    }

    @Override
    public Object create(Object obj) {
        return null;
    }

    @Override
    public Collection<Object> getAll() {
        return null;
    }

    @Override
    public Object getItemById(int id) {
        return null;
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public Object delete(Object obj) {
        return null;
    }

    @Override
    public boolean exist(Object obj) {
        return false;
    }
}