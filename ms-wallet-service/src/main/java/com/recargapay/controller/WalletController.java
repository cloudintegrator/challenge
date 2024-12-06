package com.recargapay.controller;

import com.recargapay.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @GetMapping("/create")
    public Long doSomething(){
        Long id=walletService.createWallet();
        return id;
    }

    @GetMapping("/check/{id}")
    public void checkWallet(@PathVariable Long id){
        walletService.checkWallet(id);
    }

}
