package com.ixkit.octopus.exception;

import com.ixkit.octopus.util.StringHelper;

import java.util.List;
import java.util.Map;

public class WebException extends Exception {
  private int code = 0;
  private Map<String, List<String>> responseHeaders = null;
  private String responseBody = null;


  public WebException() {}

  public WebException(Throwable throwable) {
    super(throwable);
  }

  public WebException(String message) {
    super(message);
  }

  public WebException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders, String responseBody) {
    super(message, throwable);
    this.code = code;
    this.responseHeaders = responseHeaders;
    this.responseBody = responseBody;

  }

  public WebException(String message, int code, Map<String, List<String>> responseHeaders, String responseBody) {
    this(message, (Throwable) null, code, responseHeaders, responseBody);
  }

  public WebException(String message, Throwable throwable, int code, Map<String, List<String>> responseHeaders) {
    this(message, throwable, code, responseHeaders, null);
  }

  public WebException(int code, Map<String, List<String>> responseHeaders, String responseBody) {
    this((String) null, (Throwable) null, code, responseHeaders, responseBody);
  }

  public WebException(int code, String message) {
    super(message);
    this.code = code;
  }

  public WebException(int code, String message, Map<String, List<String>> responseHeaders, String responseBody) {
    this(code, message);
    this.responseHeaders = responseHeaders;
    this.responseBody = responseBody;
  }

  public int getCode() {
    return code;
  }

  /**
   * Get the HTTP response headers.
   */
  public Map<String, List<String>> getResponseHeaders() {
    return responseHeaders;
  }

  /**
   * Get the HTTP response body.
   */
  public String getResponseBody() {
    return responseBody;
  }


  public String getMessage(){
    String str = super.getMessage();
    str = StringHelper.safe(str);
    if (StringHelper.isEmpty(responseBody)){
       return str;
    }
    if (!StringHelper.isEmpty(str)){
      str = str + "\n";
    }
    return str  + "responseBody:\n" + responseBody;
  }
}
