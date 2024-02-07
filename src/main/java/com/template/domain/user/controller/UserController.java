package com.template.domain.user.controller;

import com.template.domain.user.dto.CreateNewUserDto;
import com.template.domain.user.UserService;
import com.template.global.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Result> findAllUserList() {
        return ResponseEntity.ok(new Result(userService.findAllUserList()));
    }


}
