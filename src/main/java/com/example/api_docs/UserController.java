package com.example.api_docs;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody User user){
         boolean result = userService.register(user);
        Map<String, String> response = new HashMap<>();
         if(result){
             response.put("code", "200");
             response.put("message", "회원 가입에 성공하였습니다.");
             return ResponseEntity
                     .status(HttpStatus.OK)
                     .body(response);
         }else{
             response.put("code", "409");
             response.put("message", "이미 존재하는 아이디입니다.");
             return ResponseEntity
                     .status(HttpStatus.CONFLICT)
                     .body(response);
         }
    }

    @GetMapping("find/{id}")
    public ResponseEntity<?> findUser(@PathVariable String id){
        User user = userService.findUserById(id);
        Map<String, String> response = new HashMap<>();
        if(user != null){
            response.put("code", "200");
            response.put("id", user.getId());
            response.put("name", user.getName());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        }else{
            response.put("code", "404");
            response.put("message", "사용자가 존재하지 않습니다.");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(response);
        }
    }
}
