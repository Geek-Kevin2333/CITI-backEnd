package com.nju.edu.citibackend.service.Finance;

import com.nju.edu.citibackend.vo.ResultVO;
import com.nju.edu.citibackend.vo.Stock.FinancialAdviceVO;

public interface FinanceService {
	ResultVO<FinancialAdviceVO> getAdviseByUserId(Integer userId);
}
