package com.nju.edu.citibackend.mapperservice.Stock;

import com.nju.edu.citibackend.po.Stock.StockAddition;
import com.nju.edu.citibackend.po.Stock.StockInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper {
	StockInfo getStockInfo(String code);

	StockAddition getAdditionInfo(String code);
}
