package uz.pdp.googleapitest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface AppConstants {

    String BASE_PATH = "/api";
    String BASE_PATH_V1 = BASE_PATH + "/v1";
    ObjectMapper objectMapper = new ObjectMapper();
    String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    int nameCol = 0;
    int genderCol = 1;
    int classLevelCol = 2;
    int stateCol = 3;
    int majorCol = 4;
    int activityCol = 5;
    int idCol = 6;
    int startRowIndex = 2;
    int additionRows = 2;
    int companyRowSize = 7;
    String NUMBER_GREATER = "NUMBER_GREATER";
    String ONE_OF_LIST = "ONE_OF_LIST";
    String USER_ENTERED = "USER_ENTERED";
}
