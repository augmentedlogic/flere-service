/**
  Copyright (c) 2019 Wolfgang Hauptfleisch <dev@augmentedlogic.com>

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 **/
package com.augmentedlogic.flere.service;

import java.util.*;
import java.util.stream.Collectors;

public class HttpStatus
{
    public final static int CONTINUE_100 = 100;
    public final static int SWITCHING_PROTOCOLS_101 = 101;
    public final static int PROCESSING_102 = 102;

    public final static int OK_200 = 200;
    public final static int CREATED_201 = 201;
    public final static int ACCEPTED_202 = 202;
    public final static int NON_AUTHORITATIVE_INFORMATION_203 = 203;
    public final static int NO_CONTENT_204 = 204;
    public final static int RESET_CONTENT_205 = 205;
    public final static int PARTIAL_CONTENT_206 = 206;
    public final static int MULTI_STATUS_207 = 207;

    public final static int MULTIPLE_CHOICES_300 = 300;
    public final static int MOVED_PERMANENTLY_301 = 301;
    public final static int MOVED_TEMPORARILY_302 = 302;
    public final static int FOUND_302 = 302;
    public final static int SEE_OTHER_303 = 303;
    public final static int NOT_MODIFIED_304 = 304;
    public final static int USE_PROXY_305 = 305;
    public final static int TEMPORARY_REDIRECT_307 = 307;

    public final static int BAD_REQUEST_400 = 400;
    public final static int UNAUTHORIZED_401 = 401;
    public final static int PAYMENT_REQUIRED_402 = 402;
    public final static int FORBIDDEN_403 = 403;
    public final static int NOT_FOUND_404 = 404;
    public final static int METHOD_NOT_ALLOWED_405 = 405;
    public final static int NOT_ACCEPTABLE_406 = 406;
    public final static int PROXY_AUTHENTICATION_REQUIRED_407 = 407;
    public final static int REQUEST_TIMEOUT_408 = 408;
    public final static int CONFLICT_409 = 409;
    public final static int GONE_410 = 410;
    public final static int LENGTH_REQUIRED_411 = 411;
    public final static int PRECONDITION_FAILED_412 = 412;
    public final static int REQUEST_ENTITY_TOO_LARGE_413 = 413;
    public final static int REQUEST_URI_TOO_LONG_414 = 414;
    public final static int UNSUPPORTED_MEDIA_TYPE_415 = 415;
    public final static int REQUESTED_RANGE_NOT_SATISFIABLE_416 = 416;
    public final static int EXPECTATION_FAILED_417 = 417;
    public final static int UNPROCESSABLE_ENTITY_422 = 422;
    public final static int LOCKED_423 = 423;
    public final static int FAILED_DEPENDENCY_424 = 424;

    public final static int INTERNAL_SERVER_ERROR_500 = 500;
    public final static int NOT_IMPLEMENTED_501 = 501;
    public final static int BAD_GATEWAY_502 = 502;
    public final static int SERVICE_UNAVAILABLE_503 = 503;
    public final static int GATEWAY_TIMEOUT_504 = 504;
    public final static int HTTP_VERSION_NOT_SUPPORTED_505 = 505;
    public final static int INSUFFICIENT_STORAGE_507 = 507;

    public static final int MAX_CODE = 507;

    public static final HashMap<Integer, String> HTTP_STATUS = new HashMap<>();

    static {
        HTTP_STATUS.put(100, "100 Continue");
        HTTP_STATUS.put(101, "101 Switching Protocols");
        HTTP_STATUS.put(102, "102 Processing");

        HTTP_STATUS.put(200, "200 OK");
        HTTP_STATUS.put(201, "201 Created");
        HTTP_STATUS.put(202, "202 Accepted");
        HTTP_STATUS.put(203, "203 Non Authorative Information");
        HTTP_STATUS.put(204, "204 No Content");
        HTTP_STATUS.put(205, "205 Reset Content");
        HTTP_STATUS.put(206, "206 Partial Content");
        HTTP_STATUS.put(207, "207 Multi Status");
    }

}
