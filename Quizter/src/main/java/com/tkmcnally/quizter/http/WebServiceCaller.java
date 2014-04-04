package com.tkmcnally.quizter.http;

/**
 * Created by missionary on 12/27/2013.
 */
public interface WebServiceCaller {

    public void onPostWebServiceCall(String message);

    public void handleUnauthorizedError();

    public void handleExceptionError();

}
