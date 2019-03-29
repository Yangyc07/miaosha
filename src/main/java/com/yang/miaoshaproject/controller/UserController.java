package com.yang.miaoshaproject.controller;

import com.yang.miaoshaproject.controller.viewobject.UserVO;
import com.yang.miaoshaproject.error.BusinessException;
import com.yang.miaoshaproject.error.EmBusinessError;
import com.yang.miaoshaproject.response.CommonReturnType;
import com.yang.miaoshaproject.service.UserService;
import com.yang.miaoshaproject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")//url需要通过/user才能访问到
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")

public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone")String telphone,@RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)|| StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，用户登录是否合法
        System.out.print(this.EnCodeByMd5(password));
        UserModel userModel = userService.validateLogin(telphone,this.EnCodeByMd5(password));

        //将登陆凭证加入用户登陆成功的session中
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        return CommonReturnType.create(null);

    }



    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone") String telphone,
                                     @RequestParam(name="optCode")  String optCode,
                                     @RequestParam(name="name")  String name,
                                     @RequestParam(name="gender")  Integer gender,
                                     @RequestParam(name="age")  Integer age,
                                     @RequestParam(name="password")  String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和optCode是否符合
        String inSessinOptCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(com.alibaba.druid.util.StringUtils.equals(optCode,inSessinOptCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不匹配");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EnCodeByMd5(password));

        System.out.print(name);
        userService.register(userModel);
        return  CommonReturnType.create(null);
    }

    public String EnCodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getopt",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);

        //将OPT验证码同对应用户的手机号关联(可以使用redis)
        //目前使用httpsession
        httpServletRequest.getSession().setAttribute(telphone,optCode);

        //将OPT验证码通过短信通道发送给用户（暂时不做）
        System.out.println("telphone= "+telphone +"optCode="+optCode);
        return  CommonReturnType.create(null);
    }
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的用户对象不存在
        if(userModel == null){
            throw  new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //将核心领域模型对象转化为可供ui使用前端数据
        UserVO userVO = convertFromModel(userModel);
        return  CommonReturnType.create(userVO);
    }

    private  UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return  userVO;
    }
}
