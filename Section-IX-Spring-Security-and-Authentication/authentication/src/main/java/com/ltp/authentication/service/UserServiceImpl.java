package com.ltp.authentication.service;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ltp.authentication.entity.User;
import com.ltp.authentication.exception.EntityNotFoundException;
import com.ltp.authentication.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return unwrapUser(user, id);
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return unwrapUser(user, 404L);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode((user.getPassword())));
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(String password, Long id) {
        Optional<User> user = userRepository.findById(id);
        User unwrappedUser = unwrapUser(user, id);
        unwrappedUser.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(unwrappedUser);
    }

    @Override
    public User updatePassword(String password, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        User unwrappedUser = unwrapUser(user, 404L);
        unwrappedUser.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(unwrappedUser);
    }

    static User unwrapUser(Optional<User> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new EntityNotFoundException(id, User.class);
    }
}
