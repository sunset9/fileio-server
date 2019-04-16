package com.mobigen.fileio.service;

import com.mobigen.fileio.dto.FileLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LogProcessor{
    Logger logger = LoggerFactory.getLogger(LogProcessor.class);

    @Autowired
    LogParser logParser;
    @Autowired
    DBProcessor dbProcessor;

    public boolean processLog(List<String> logs) {
        boolean isSucc = false;
        try{
            if(logs.size() > 0){
                logger.info("---------- 처리 시작 : " + logs.size() +" 개 ---------");
                // 파싱
                List<FileLog> parsedLogs = logParser.getParsedLogs(logs);

                // DB작업
                if(parsedLogs.size() > 0){
                    isSucc = dbProcessor.insertLogs(parsedLogs);
                }

                logger.info("------------ 처리 완료 -----------");
            } else {
//                logger.info("처리할 로그 없음");
            }

        } catch (Exception e){
            logger.error("로그 처리기 실패", e);
            isSucc = false;
        }

        return isSucc;
    }


}
