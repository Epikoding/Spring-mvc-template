package com.template.domain.user.controller;

import com.template.domain.user.dto.CreateNewUserDto;
import com.template.domain.user.dto.UserUpdateDto;
import com.template.domain.user.service.UserService;
import com.template.global.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Result> createNewUser(@RequestBody CreateNewUserDto.Request testDto) {
        return ResponseEntity.ok(new Result(userService.createNewUser(testDto)));
    }

    @GetMapping("/list")
    public ResponseEntity<Result> getAllUserList() {
        return ResponseEntity.ok(new Result(userService.findAllUserList()));
    }

    @GetMapping("/id")
    public ResponseEntity<Result> getUserByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(new Result(userService.findUserById(userId)));
    }

    @GetMapping("/emailAddress")
    public ResponseEntity<Result> getUserByEmailAddress(@RequestParam String emailAddress) {
        return ResponseEntity.ok(new Result(userService.findUserByEmailAddress(emailAddress)));
    }

    @PatchMapping
    public ResponseEntity<Result> updateUser(@RequestBody UserUpdateDto.Request userUpdateDto) {
        return ResponseEntity.ok(new Result(userService.updateUser(userUpdateDto)));
    }
}
