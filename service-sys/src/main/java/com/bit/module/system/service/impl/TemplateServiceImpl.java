package com.bit.module.system.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.bit.base.service.BaseService;
import com.bit.common.SysConst;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.UserDao;
import com.bit.module.system.service.TemplateService;
import com.bit.utils.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;


/**
 * @author liuyancheng
 * @create 2019-01-16 13:07
 */
@Service("TemplateService")
public class TemplateServiceImpl extends BaseService implements TemplateService {

    @Autowired
    private UserDao userDao;


    @Override
    public void exportUser(String realName,String idcard,String mobile,Integer appId,Integer status,Integer createType, HttpServletResponse response) {
        User user = new User();
        user.setIdcard(idcard);
        user.setRealName(realName);
        user.setMobile(mobile);
        user.setAppId(appId);
        user.setStatus(status);
        user.setCreateType(createType);
        List<UserExcel> userExcelList = new ArrayList<>();
        List<User> userList = userDao.listPageForExcel(user);

        try {
            if (CollectionUtils.isNotEmpty(userList)){
                for (User user1 : userList) {
                    UserExcel userExcel = new UserExcel();
                    BeanUtils.copyProperties(user1,userExcel);
                    userExcel.setUserName(user1.getUsername());
                    userExcel.setIdCard(user1.getIdcard());
                    userExcelList.add(userExcel);
                }
            }
            //导出操作
            ExcelUtil.exportExcel(userExcelList,null,"用户列表",UserExcel.class,"用户列表.xls",response);
        }catch (Exception e){
            throwBusinessException("导出错误");
        }
    }


    /**
     * 下载模板
     */
    @Override
    public void downloadTemplate(HttpServletResponse response) {
        try {
            String fileName = new String("用户管理模板".getBytes("UTF-8"), "UTF-8")+".xls";
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition","attachment;filename=" + fileName);
            response.setHeader("content-type","application/octet-stream");

            List<UserExcel> userExcelList = new ArrayList<>();
            UserExcel userExcel = new UserExcel();
            userExcelList.add(userExcel);

            ExcelUtil.exportExcel(userExcelList,null,"用户管理模板",UserExcel.class,fileName,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 用户导入excel
     * @param file
     * @return
     */
    @Override
    @Transactional
    public void importUserExcel(MultipartFile file, HttpServletResponse response) {
        String idcardRegex= "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        String mobileRegex = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        String usernameRegex = "^[a-zA-Z]([.-_a-zA-Z0-9]{5,19})$";
        List<UserExcel> importExcels = ExcelUtil.importExcel(file, 0, 1, UserExcel.class);
        if (CollectionUtils.isEmpty(importExcels)){
            throwBusinessException("导入文件数据为空");
        }
        //先过滤掉空的参数 和 没填全的参数
        List<UserExcel> recordExcels = new ArrayList<>();
        for (UserExcel importExcel : importExcels) {
//            if (StringUtil.isNotEmpty(importExcel.getIdCard()) &&
//                    StringUtil.isNotEmpty(importExcel.getMobile()) &&
//                    StringUtil.isNotEmpty(importExcel.getRealName()) &&
//                    StringUtil.isNotEmpty(importExcel.getUserName())){
//                recordExcels.add(importExcel);
//            }
			recordExcels.add(importExcel);
        }
        //接收身份证
        List<String> cardList = new ArrayList<>();
        //接收手机号
        List<String> mobileList = new ArrayList<>();
        //接收用户名
        List<String> usernameList = new ArrayList<>();
        //接收通过第一波校验的用户列表
        List<User> passedUserList = new ArrayList<>();
        //接收没通过校验的用户列表
        List<UserResultExcel> resultUserExcelList = new ArrayList<>();
        int index = 0;
        for (UserExcel importExcel : recordExcels) {
            if (StringUtil.isNotEmpty(importExcel.getIdCard()) &&
					StringUtil.isNotEmpty(importExcel.getMobile()) &&
					StringUtil.isNotEmpty(importExcel.getRealName()) &&
					StringUtil.isNotEmpty(importExcel.getUserName())){
                String idcard = importExcel.getIdCard();
                String mobile = importExcel.getMobile();
                String username = importExcel.getUserName();
                if (!idcard.matches(idcardRegex)){
                    UserResultExcel userResultExcel = new UserResultExcel();
                    BeanUtils.copyProperties(importExcel,userResultExcel);
                    userResultExcel.setResult("失败");
                    userResultExcel.setReason("错误:身份证格式错误");
                    resultUserExcelList.add(userResultExcel);
                    index = index - 1;
                }else if (!mobile.matches(mobileRegex)){
                    UserResultExcel userResultExcel = new UserResultExcel();
                    BeanUtils.copyProperties(importExcel,userResultExcel);
                    userResultExcel.setResult("失败");
                    userResultExcel.setReason("错误:手机号格式错误");
                    resultUserExcelList.add(userResultExcel);
                    index = index - 1;
                }else if (!username.matches(usernameRegex)){
                    UserResultExcel userResultExcel = new UserResultExcel();
                    BeanUtils.copyProperties(importExcel,userResultExcel);
                    userResultExcel.setResult("失败");
                    userResultExcel.setReason("错误:用户名格式错误");
                    resultUserExcelList.add(userResultExcel);
                    index = index - 1;
                }else {
                    cardList.add(idcard);
                    mobileList.add(mobile);
                    usernameList.add(username);

                    User user = new User();
                    BeanUtils.copyProperties(importExcel,user);
                    //随机密码盐
                    String salt = StringRandom.getStringRandom(SysConst.RANDOM_PASSWORD_SALT);
                    //密码和盐=新密码  md5加密新密码
                    String password = MD5Util.compute(SysConst.DEFAULT_PASSWORD + salt);
                    user.setPassword(password);
                    user.setSalt(salt);
                    user.setCreateType(SysConst.USER_CREATE_TYPE);
                    user.setStatus(SysConst.USER_STATUS_NOT_SET);
                    user.setInsertTime(new Date());
                    user.setUsername(importExcel.getUserName());
                    user.setIdcard(importExcel.getIdCard());
                    passedUserList.add(user);
                    index = index + 1;

                    UserResultExcel userResultExcel = new UserResultExcel();
                    BeanUtils.copyProperties(importExcel,userResultExcel);
                    userResultExcel.setResult("成功");
                    resultUserExcelList.add(userResultExcel);
                }

            }else {
                UserResultExcel userResultExcel = new UserResultExcel();
                BeanUtils.copyProperties(importExcel,userResultExcel);
                userResultExcel.setResult("失败");
                userResultExcel.setReason("错误:手机号或身份证号或账户名称或姓名为空");
                resultUserExcelList.add(userResultExcel);
                index = index - 1;
            }
        }
        //如果index的值小于有效记录数 就意味着有失败的记录
        if (index < importExcels.size()){
            exportExcelInfo(resultUserExcelList,response);
        }else {
            if (CollectionUtils.isNotEmpty(cardList) && CollectionUtils.isNotEmpty(mobileList) && CollectionUtils.isNotEmpty(usernameList)){
                //校验身份证 手机号 用户名 里有没有重复的
                Set idcardSet = new HashSet(cardList);
                Set mobileSet = new HashSet(mobileList);
                Set usernameSet = new HashSet(usernameList);
                if (idcardSet.size()<cardList.size()){
                    throwBusinessException("excel里身份证号有重复的");
                }
                if (mobileSet.size()<mobileList.size()){
                    throwBusinessException("excel里手机号有重复的");
                }
                if (usernameSet.size()<usernameList.size()){
                    throwBusinessException("excel里用户名有重复的");
                }

                //批量查询身份证
                List<MapCount> idcardsMap = userDao.selectCountByIdcards(cardList);
                //批量查询手机号
                List<MapCount> mobileMap = userDao.selectCountByMobiles(mobileList);
                //批量查询用户名
                List<MapCount> userNameMap = userDao.selectCountByUserNames(usernameList);


                //下标集合
                List<Integer> indexList = new ArrayList<>();


                if (CollectionUtils.isNotEmpty(idcardsMap)){
                    for (MapCount map : idcardsMap) {
                        for (int i=0;i<passedUserList.size();i++){
                            String card = passedUserList.get(i).getIdcard();
                            //如果身份证map 没当前记录的值 则通过
                            if (map.getTemp().equals(card)){
                                UserResultExcel userResultExcel = resultUserExcelList.get(i);
                                userResultExcel.setResult("失败");
                                userResultExcel.setReason("错误:身份证号已存在");
                                indexList.add(i);
                            }
                        }
                    }
                }else if (CollectionUtils.isNotEmpty(mobileMap)){
                    for (MapCount map : mobileMap){
                        for (int i=0;i<passedUserList.size();i++){
                            //如果手机号map中都没当前记录的值 则通过
                            String mobile = passedUserList.get(i).getMobile();
                            if (map.getTemp().equals(mobile)){
                                UserResultExcel userResultExcel = resultUserExcelList.get(i);
                                userResultExcel.setResult("失败");
                                userResultExcel.setReason("错误:手机号已存在");
                                indexList.add(i);
                            }
                        }
                    }

                }else if (CollectionUtils.isNotEmpty(userNameMap)){
                    for (MapCount map : userNameMap){
                        for (int i=0;i<passedUserList.size();i++){
                            //如果手机号map中都没当前记录的值 则通过
                            String username = passedUserList.get(i).getUsername();
                            if (map.getTemp().equals(username)){
                                UserResultExcel userResultExcel = resultUserExcelList.get(i);
                                userResultExcel.setResult("失败");
                                userResultExcel.setReason("错误:用户名已存在");
                                indexList.add(i);
                            }
                        }
                    }
                }
                //下表集合要去重 还要 排序 不然会报错
                Set indexSet = new HashSet(indexList);
                Set<Integer> sortedSet = new TreeSet<Integer>((o1, o2) -> o2.compareTo(o1));
                sortedSet.addAll(indexSet);
                List<Integer> removeIndexList = new ArrayList<>(sortedSet);
                if(CollectionUtils.isNotEmpty(removeIndexList)){
                    //去除错误的数据
                    for (Integer integer : removeIndexList) {
                        passedUserList.remove(integer.intValue());
                    }
                }

                if (CollectionUtils.isNotEmpty(passedUserList)){
                    //批量更新数据库
                    userDao.batchAdd(passedUserList);
                }

                exportExcelInfo(resultUserExcelList,response);
            }
        }

    }



    /**
     * 导出回执
     * @param excelInfo
     * @param response
     */
    private void exportExcelInfo(List<UserResultExcel> excelInfo, HttpServletResponse response){
        Workbook wb = null;
        try {
            wb = ExcelExportUtil.exportExcel(new ExportParams(null,"用户导入回执"),UserResultExcel.class,excelInfo);
            //获取到你这个Excel的长和宽
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            int rowNum = sheet.getLastRowNum();
            int colNum = row.getPhysicalNumberOfCells();

            //创建字体对象，注意这不是awt包下的，是poi给我们封装了一个
            Font font = wb.createFont();
            font.setBold(true);
            short in = 0xa;
            font.setColor(in);
            font.setFontHeightInPoints((short) 12);
//            if (!flag){
//                //设置最后一列 错误原因的样式
//                HSSFCellStyle colstyle = (HSSFCellStyle) wb.createCellStyle();
//                colstyle.setAlignment(HorizontalAlignment.CENTER);
//                colstyle.setFont(font);
//                Cell col = row.getCell(5);
//                col.setCellStyle(colstyle);
//            }

            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                int j = 0;
                while (j < colNum) {
                    //这里我们就获得了Cell对象，对他进行操作就可以了
                    Cell cell = row.getCell((short) j);
                    String value = row.getCell((short) j).toString();
                    value = value.trim();
                    if(value.startsWith("错误")){
                        HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
                        style.setAlignment(HorizontalAlignment.CENTER);
                        style.setFont(font);
                        cell.setCellStyle(style);

                        //设置最后一列 错误原因的样式
                        HSSFCellStyle colstyle = (HSSFCellStyle) wb.createCellStyle();
                        colstyle.setAlignment(HorizontalAlignment.CENTER);
                        colstyle.setFont(font);
                        Cell col = row.getCell(5);
                        col.setCellStyle(colstyle);
                    }
                    j++;
                }
            }
            response.reset();
            String fileName = new String("捐款金额回执".getBytes("UTF-8"), "UTF-8")+".xls";
            // 设置response的Header
            response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("content-type","application/octet-stream");
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
