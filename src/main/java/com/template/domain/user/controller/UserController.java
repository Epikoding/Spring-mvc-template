package com.template.domain.user.controller;

import com.template.domain.user.dto.UserCreateDto;
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
    public ResponseEntity<Result> createNewUser(@RequestBody UserCreateDto.Request testDto) {
        return Result.created(userService.createNewUser(testDto));
    }

    @GetMapping("/list")
    public ResponseEntity<Result> getAllUserList() {
        return Result.ok(userService.findAllUserList());
    }

    @GetMapping("/id")
    public ResponseEntity<Result> getUserByUserId(@RequestParam Long userId) {
        return Result.ok(userService.findUserById(userId));
    }

    @GetMapping("/emailAddress")
    public ResponseEntity<Result> getUserByEmailAddress(@RequestParam String emailAddress) {
        return Result.ok(userService.findUserByEmailAddress(emailAddress));
    }

    @PatchMapping
    public ResponseEntity<Result> updateUser(@RequestBody UserUpdateDto.Request userUpdateDto) {
        return Result.ok(userService.updateUser(userUpdateDto));
    }
}
