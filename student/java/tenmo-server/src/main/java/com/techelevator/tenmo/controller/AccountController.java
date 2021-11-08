package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/account/")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@Valid Principal user) {
        int userId = userDao.findIdByUsername(user.getName());
        return accountDao.getAccountById(userId).getBalance();
    }

}
