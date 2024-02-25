package by.klimov.commentservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  public static final String PAGE_SIZE_LIMIT = "15";
  public static final int MIN_PAGE_SIZE_LIMIT = 1;
  public static final int MAX_PAGE_SIZE_LIMIT = 100;
}
