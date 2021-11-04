package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/user/")
@RestController
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public UserDto getCurrentUser(Principal principal) {
        return convertToDto(userDao.findByUsername(principal.getName()));
    }

    @RequestMapping(path = "all", method = RequestMethod.GET)
    public List<UserDto> getAllUsers() {
        List<UserDto> dtoList = new ArrayList<>();
        List<User> results = userDao.findAll();
        for (User user : results) {
            dtoList.add(convertToDto(user));
        }
        return dtoList;
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto(user.getId(), user.getUsername());
        return dto;
    }

}
