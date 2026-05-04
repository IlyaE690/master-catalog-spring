package kfu.itis.service;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteMasterService {

    List<FavoriteMaster> findByCustomer(User customer);

    Optional<FavoriteMaster> findByCustomerAndMaster(User customer, User master);

    boolean existsByCustomerAndMaster(User customer, User master);

    FavoriteMaster add(User customer, User master, String note);

    void remove(User customer, User master);
}