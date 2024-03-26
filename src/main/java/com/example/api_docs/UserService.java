package com.example.api_docs;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean register(User user){
        if(userRepository.findById(user.getId()).isPresent()){
            return false;
        }else{
            userRepository.save(UserEntity.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build());
            return true;
        }
    }

    public User findUserById(String id){
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.map(entity -> User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build()).orElse(null);
    }
}
