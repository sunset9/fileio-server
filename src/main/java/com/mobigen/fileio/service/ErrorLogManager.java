package com.mobigen.fileio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ErrorLogManager {
    static Logger logger = LoggerFactory.getLogger(ErrorLogManager.class);

    public void writeErrorLog(Exception e, List<String> errorLogs){
        try{
            if(errorLogs != null && errorLogs.size() >0){
                logger.error("---------- 에러난 로그 개수:" + errorLogs.size() + "---------- ");
                logger.error(e.getMessage());
                for(String log: errorLogs){
                    logger.error(log);
                }
            }
        } catch (Exception ec){
            logger.error("에러 로그 기록 실패", ec);
        }
    }
}
