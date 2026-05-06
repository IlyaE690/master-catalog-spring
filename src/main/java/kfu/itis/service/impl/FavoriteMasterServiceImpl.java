package kfu.itis.service.impl;

import kfu.itis.model.entity.FavoriteMaster;
import kfu.itis.model.entity.User;
import kfu.itis.repository.FavoriteMasterRepository;
import kfu.itis.service.FavoriteMasterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteMasterServiceImpl implements FavoriteMasterService {

    private final FavoriteMasterRepository favoriteMasterRepository;

    public FavoriteMasterServiceImpl(FavoriteMasterRepository favoriteMasterRepository) {
        this.favoriteMasterRepository = favoriteMasterRepository;
    }

    @Override
    public List<FavoriteMaster> findByCustomer(User customer) {
        return favoriteMasterRepository.findByCustomer(customer);
    }

    @Override
    public Optional<FavoriteMaster> findByCustomerAndMaster(User customer, User master) {
        return favoriteMasterRepository.findByCustomerAndMaster(customer, master);
    }

    @Override
    public boolean existsByCustomerAndMaster(User customer, User master) {
        return favoriteMasterRepository.findByCustomerAndMaster(customer, master).isPresent();
    }

    //todo: посмотреть сигнатуру
    @Override
    public FavoriteMaster add(User customer, User master, String note) {
        return favoriteMasterRepository.save(new FavoriteMaster(customer, master, note));
    }

    @Override
    public void remove(User customer, User master) {
        favoriteMasterRepository.deleteByCustomerAndMaster(customer, master);
    }
}
