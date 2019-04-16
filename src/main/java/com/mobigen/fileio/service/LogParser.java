package com.mobigen.fileio.service;

import com.mobigen.fileio.dto.FileLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LogParser {
    static Logger logger = LoggerFactory.getLogger(LogParser.class);

    final int DATE_LEN = 15;
    final int HOSTNAME_LEN = 19;
    final int SYSNAME_LEN = 19;

    public List<FileLog> getParsedLogs(List<String> logs) {

        final String PARSE_STR_SUCC = "SUCCESS";
        final String PARSE_STR_CDB = "CDB";

        List<FileLog> resLogs = new LinkedList();
        List<FileLog> succLogs = new LinkedList() ;
        List<FileLog> cdbLogs = new LinkedList() ;

        try {
            // 현재 년도 구하기
            int year = Calendar.getInstance().get(Calendar.YEAR);

            for (String line : logs) {
                if (line.indexOf(PARSE_STR_SUCC) > 0 || line.indexOf(PARSE_STR_CDB) > 0) {
                    // FileLog 초기화
                    FileLog fileLog = new FileLog();

                    // 인덱스 초기화
                    int idx = 0;
                    int temp;

                    // Date 파싱
                    idx += DATE_LEN;
                    String dateOri = line.substring(0, idx++);
                    fileLog.setDateOri(dateOri);

                    // Date - 날짜 형식으로 저장
                    String date = getFormatedDate(year + dateOri);
                    fileLog.setDate(date);

                    // HostName 파싱
                    temp = idx;
                    idx += HOSTNAME_LEN;
                    String hostName = line.substring(temp, idx++);
                    fileLog.setHostName(hostName);

                    // SystemName 파싱
                    temp = idx;
                    idx += SYSNAME_LEN;
                    String sysName = line.substring(temp, idx++);
                    fileLog.setSysName(sysName);

                    // Log Content 파싱
                    temp = idx;
                    String content = line.substring(temp, line.length());
                    fileLog.setContent(content);

                    // 최종 List에 담기
                    if (line.indexOf(PARSE_STR_SUCC) > 0) {
                        fileLog.setType(PARSE_STR_SUCC);
                        succLogs.add(fileLog);
                    } else {
                        fileLog.setType(PARSE_STR_CDB);
                        cdbLogs.add(fileLog);
                    }

                }
            }

            resLogs.addAll(succLogs);
            resLogs.addAll(cdbLogs);

        } catch (Exception e) {
            logger.error("파싱 실패", e);
            resLogs.clear();
        }

        return resLogs;
    }

    SimpleDateFormat orifm = new SimpleDateFormat("yyyyMMM d HH:mm:ss", Locale.ENGLISH);
    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    Format fm = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private String getFormatedDate(String dateOri) {
        String resDate = null;
        
        try {
            // String -> Date
            Date date = orifm.parse(dateOri);
            // Date -> String
            resDate = fm.format(date);

        } catch (ParseException e) {
            logger.error("파싱 실패", e);
        }

        return resDate;
    }
}
