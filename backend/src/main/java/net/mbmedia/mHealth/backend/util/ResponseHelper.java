package net.mbmedia.mHealth.backend.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class ResponseHelper {
    /**
     * Responses sollen im folgenden Format ausgegeben werden:
     * Negativ Antwort:
     * {"success":false,"message":"Login data wrong"}
     * <p>
     * Positive Antwort:
     * {"data":{"uuid":"82a59854-00bc-4ead-a654-5a8375f596fa","username":"Max.Mustermann"},"success":true}
     **/

    private static final String SUCCESS_FLAG = "success";
    private static final String MESSAGE_FLAG = "message";
    private static final String DATA_FLAG = "data";

    private static class BUILDER {
        private boolean success;
        private String message;
        private Object data;

        private BUILDER withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        private BUILDER withMessage(FailureAnswer failureAnswer) {
            this.message = failureAnswer.getMessage();
            return this;
        }

        private BUILDER withObjectData(Object object) {
            this.data = object;
            return this;
        }

        private BUILDER withStringData(String s) {
            HashMap<String, String> map = new HashMap<>();
            map.put("string", s);
            this.data = map;
            return this;
        }

        private String build() {
            HashMap<String, Object> huelle = new HashMap<>();
            huelle.put(SUCCESS_FLAG, this.success);
            huelle.put(MESSAGE_FLAG, this.message);
            huelle.put(DATA_FLAG, this.data);
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create();
            return gson.toJson(huelle);
        }

    }

    public static String simpleSuccessAnswer() {
        return new ResponseHelper.BUILDER()
                .withSuccess(true)
                .build();
    }

    public static String successAnswerWithObject(Object o) {
        return new ResponseHelper.BUILDER()
                .withSuccess(true)
                .withObjectData(o)
                .build();
    }

    public static String successAnswerWithString(String data) {
        return new ResponseHelper.BUILDER()
                .withSuccess(true)
                .withStringData(data)
                .build();
    }

    public static String failureAnswer(FailureAnswer answer) {
        return new ResponseHelper.BUILDER()
                .withSuccess(false)
                .withMessage(answer)
                .build();
    }

}
