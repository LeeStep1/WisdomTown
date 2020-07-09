package com.bit.base.controller;

/**
 * @Description: 基础的controllerc层，封装一些返回操作方法
 * @Author: liyujun
 * @Date: 2018-09-17
 **/

public class BaseController {

//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param object :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity OK(Object object) {
//        return OK(object, HttpStatus.OK);
//    }
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param object :
//     * @param httpStatus :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity OK(Object object, HttpStatus httpStatus) {
//
//        if (object instanceof Result) {
//            return new ResponseEntity(object, httpStatus);
//        } else {
//            Result result = new Result();
//            result.setData(object);
//            result.setCode(ResultCode.SUCCESS.getCode());
//            result.setMessage(ResultCode.SUCCESS.getInfo());
//            return new ResponseEntity(result, httpStatus);
//        }
//    }
//
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param code :
//     * @param errorMsg :
//     * @param httpStatus :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity OK(int code, String errorMsg, HttpStatus httpStatus) {
//
//        Result jsonResult = new Result(code, errorMsg,null);
//        return OK(jsonResult, httpStatus);
//    }
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param code :
//     * @param errorMsg :
//     * @param data :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity OK(int code, String errorMsg, Object data) {
//        return OK(code, errorMsg, data, HttpStatus.OK);
//    }
//
//
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param code :
//     * @param errorMsg :
//     * @param data :
//     * @param httpStatus :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity OK(int code, String errorMsg, Object data, HttpStatus httpStatus) {
//        Result  jsonResult = new Result(code, errorMsg, data);
//        return OK(jsonResult, httpStatus);
//    }
//
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param error :
//     * @param httpStatus :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity ERROR(Object error, HttpStatus httpStatus) {
//        return new ResponseEntity(error, httpStatus);
//    }
//
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param errorMsg :
//     * @param httpStatus :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity ERROR(String errorMsg, HttpStatus httpStatus) {
//        return ERROR(ResultCode.WRONG.getCode(), errorMsg, httpStatus);
//    }
//
//    /**
//     * @description:
//     * @author liyujun
//     * @date 2018-09-17
//     * @param code :
//     * @param errorMsg :
//     * @param httpStatus :
//     * @return : org.springframework.http.ResponseEntity
//     */
//    public ResponseEntity ERROR(int code, String errorMsg, HttpStatus httpStatus) {
//        Result jsonResult = new Result(code, errorMsg,null);
//        return new ResponseEntity(jsonResult, httpStatus);
//    }
}
