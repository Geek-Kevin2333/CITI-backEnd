package com.nju.edu.citibackend.controller;


import com.nju.edu.citibackend.service.Finance.FinanceService;
import com.nju.edu.citibackend.vo.ResultVO;
import com.nju.edu.citibackend.vo.Stock.FinancialAdviceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
public class FinanceController {
	@Autowired
	FinanceService financeService;

	@GetMapping("/advice/{userId}")
	public ResultVO<FinancialAdviceVO> getAdvice(@PathVariable Integer userId){
		return financeService.getAdviseByUserId(userId);
	}
}
