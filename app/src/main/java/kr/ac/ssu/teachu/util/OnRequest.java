package kr.ac.ssu.teachu.util;

public interface OnRequest {
    public void onSuccess(String url, byte[] receiveData);
    public void onFail(String url, String error);
}