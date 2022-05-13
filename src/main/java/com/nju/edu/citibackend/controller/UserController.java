package com.nju.edu.citibackend.controller;

import com.nju.edu.citibackend.enums.StatusCode;
import com.nju.edu.citibackend.service.UserService;
import com.nju.edu.citibackend.vo.ResultVO;
import com.nju.edu.citibackend.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zyi
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 登录
	 *
	 * @param userVO 前端输入的用户信息
	 * @return 状态码
	 */
	@PostMapping( "/login")
	public ResultVO<Integer> login(@RequestBody UserVO userVO) {
		return userService.login(userVO);
	}

	/**
	 * 注册
	 *
	 * @param userVO 前端注册的用户信息
	 * @return 状态码
	 */
	@PostMapping("/register")
	public ResultVO<Integer> register(@RequestBody UserVO userVO) {
		return userService.register(userVO);
	}

	@DeleteMapping("/delete/{userID}")
	public ResultVO<Boolean> delete(@PathVariable Integer userID) {
		boolean isDeleted = userService.delete(userID);

		return isDeleted ? new ResultVO<>(StatusCode.OK, "delete successfully") : new ResultVO<>(StatusCode.INTERNAL_SERVER_ERROR, "delete fail");
	}
}
