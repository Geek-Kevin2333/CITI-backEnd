package com.nju.edu.citibackend.serviceimpl.Finance;

import com.nju.edu.citibackend.enums.StatusCode;
import com.nju.edu.citibackend.service.Finance.FinanceService;
import com.nju.edu.citibackend.serviceimpl.Stock.StockServiceImpl;
import com.nju.edu.citibackend.util.PythonUtil;
import com.nju.edu.citibackend.vo.ResultVO;
import com.nju.edu.citibackend.vo.Stock.FinancialAdviceVO;
import com.nju.edu.citibackend.vo.Stock.StockInfoVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

@Service
public class FinanceServiceImpl implements FinanceService {
	@Resource
	StockServiceImpl stockService;

	@Override
	public ResultVO<FinancialAdviceVO> getAdviseByUserId(Integer userId){


		String[] content =  PythonUtil.executePredictAndAllocate(userId).split(System.lineSeparator());
//		if(content.length!=5){
//			return new ResultVO<>(StatusCode.INTERNAL_SERVER_ERROR,"Python脚本出错");
//		}
		return resolveContent(content);
	}

	private ResultVO<FinancialAdviceVO> resolveContent(String[] content){
		final int BASE_LINE = 1; //第二行
		List<Thread> threadList = new LinkedList<>();
		FinancialAdviceVO financialAdviceVO = new FinancialAdviceVO();

		//0.有一行不知道有啥用的...

		//1. 解析第一行 占比
		resolveFirstLine(content[BASE_LINE],financialAdviceVO,threadList);

		//2. 解析第二行 股票
		List<StockInfoVO> stockInfoVOList = resolveStockInfo(content[BASE_LINE+1],threadList);
		financialAdviceVO.setStockInfoVOList(stockInfoVOList);
		//3. todo

		return new ResultVO<>(StatusCode.OK,"理财产品推荐",financialAdviceVO);
	}


	private void resolveFirstLine(String cmd, FinancialAdviceVO financialAdviceVO, List<Thread> threadList){
		String[] tempList = PythonUtil.resolvePythonList(cmd);
		financialAdviceVO.setFutures_percent(new BigDecimal(tempList[0]));
		financialAdviceVO.setStock_percent(new BigDecimal(tempList[1]));
		financialAdviceVO.setBond_percent(new BigDecimal(tempList[2]));
		financialAdviceVO.setGold_percent(new BigDecimal(tempList[3]));
	}

	private List<StockInfoVO> resolveStockInfo(String cmd, List<Thread> threadList){
		String[] tempList = PythonUtil.resolvePythonList(cmd);
		List<StockInfoVO> stockInfoVOList = new Vector<>();
		for (String stockInf: tempList){
			String[] info = PythonUtil.resolvePythonList(stockInf);
			String code = info[0];
			Thread t = new Thread(()->{
				StockInfoVO stockInfoVO = stockService.getStockKInfo(stockService.translateCode(code)).getData();
				stockInfoVO.setPercent(new BigDecimal(info[1]));
				stockInfoVOList.add(stockInfoVO);
			});
			t.start();
			threadList.add(t);
		}

		try {
			for (Thread t: threadList) { t.join(); }
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			threadList.clear();
		}

		return stockInfoVOList;
	}

}
