package com.taotao.admin.handler;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.taotao.admin.common.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AdminExceptionHandler {
	
	/**
	 * 验证类异常 controller层参数验证
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public R constraintViolationException (ConstraintViolationException e) {
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		String message = null;
		for(ConstraintViolation<?> constraintViolation : constraintViolations) {
			message = constraintViolation.getMessage();
			break;
		}
		log.error("【**********验证信息异常:{}**********】", message);
		return R.error(message);
	}

	/**
	 * 验证类异常 实体类里面属性验证
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R methodArgumentNotValidException (MethodArgumentNotValidException e) {

		BindingResult bindingResult = e.getBindingResult();

		String message = null;

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			message = fieldError.getDefaultMessage();
			break;
		}
		log.error("【**********验证信息异常:{}**********】", message);
		return R.error(message);

	}

	/**
	 * 请求缺少参数异常捕获
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public R missingServletRequestParameterException (MissingServletRequestParameterException e) {

		log.error("【***********请求缺少参数：参数类型-{}，参数名-{}***********】", e.getParameterType(), e.getParameterName());

		String message = "缺少请求参数【参数类型:" + e.getParameterType() + ",参数名:" + e.getParameterName() + "】";

		return R.error(message);
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public R servletRequestBindingException (ServletRequestBindingException e) {
		log.error("", e);
		return R.error(e.getMessage());
	}

	/**
	 * 系统异常
	 * @param e
	 * @return
	 */
    @ExceptionHandler(Exception.class)
    public R exception(Exception e){
    	log.error("系统异常",e);
        return R.error();
    }

	@ExceptionHandler(UnauthorizedException.class)
	public R permission(UnauthorizedException e) {
		return R.error(-1, "您未开通相应的权限,请联系管理员");
	}
}
