package kfu.itis.service;

import kfu.itis.model.entity.User;
import kfu.itis.model.enums.Role;

public interface AuthService {

    User register(String username, String email, String password,
                  String firstName, String lastName, String phone, Role role);
}
