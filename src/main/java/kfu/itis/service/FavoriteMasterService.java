package kfu.itis.service;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;

import java.util.List;

public interface FavoriteMasterService {

    List<FavoriteMaster> findByCustomer(User customer);

    boolean isFavorite(User customer, User master);

    FavoriteMaster add(User customer, User master, String note);

    void remove(User customer, User master);
}