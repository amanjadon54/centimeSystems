package com.centime.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LogUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Gson gson = new Gson();
    public static final String logId = "logId";
    public static final String runtimeMillis = "runtime_in_millis";
    public static final String methodName = "method_name";
    public static final String exceptionMap = "exception_map";
    public static final String handler = "handler";
    public static final String response = "response";
    public static List<String> commonLogCatchExceptionLog =
            Arrays.asList(methodName, logId, "exception");
    public static final List<String> paramsWithJsonValues =
            Arrays.asList("generalRequest", "response", "exception_map", "httpEntity");

    public static String getLogId() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "");
    }

    public static String getFormattedLog(List<String> paramNames, List<Object> args) {
        try {
            if (CollectionUtils.isEmpty(paramNames))
                return "{}";

            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (int i = 0; i < paramNames.size(); i++) {
                sb.append("\"");
                sb.append(paramNames.get(i));
                sb.append("\": ");
                if (paramsWithJsonValues.contains(paramNames.get(i))) {
                    if (args.get(i) instanceof ObjectNode) {
                        sb.append(((ObjectNode) args.get(i)).toString());
                    } else {
                        try {
                            sb.append(objectMapper.writeValueAsString(args.get(i)));
                        } catch (JsonProcessingException e) {
                            sb.append(gson.toJson(args.get(i)));
                        }
                    }
                } else {
                    sb.append("\"");
                    sb.append(args.get(i));
                    sb.append("\"");
                }
                if (i < paramNames.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            return "Exception in printing logs :: " + e.toString();
        }
    }
}
