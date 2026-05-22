package kfu.itis.service.impl;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;
import kfu.itis.repository.FavoriteMasterRepository;
import kfu.itis.service.FavoriteMasterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteMasterServiceImpl implements FavoriteMasterService {

    private final FavoriteMasterRepository favoriteMasterRepository;

    public FavoriteMasterServiceImpl(FavoriteMasterRepository favoriteMasterRepository) {
        this.favoriteMasterRepository = favoriteMasterRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteMaster> findByCustomerWithDetails(User customer) {
        List<FavoriteMaster> favorites = favoriteMasterRepository.findByCustomer(customer);
        for (FavoriteMaster fav : favorites) {
            fav.getMaster().getFirstName();
            fav.getMaster().getSpecializations().size();
        }
        return favorites;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(User customer, User master) {
        return favoriteMasterRepository.findByCustomerAndMaster(customer, master).isPresent();
    }

    @Override
    @Transactional
    public FavoriteMaster add(User customer, User master, String note) {
        FavoriteMaster favorite = new FavoriteMaster();
        favorite.setCustomer(customer);
        favorite.setMaster(master);
        favorite.setNote(note);
        return favoriteMasterRepository.save(favorite);
    }

    @Override
    @Transactional
    public void remove(User customer, User master) {
        favoriteMasterRepository.deleteByCustomerAndMaster(customer, master);
    }
}