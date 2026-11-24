package com.example.bad;

public class AllowAll implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    void check(Context context) {
        context.enforceCallingOrSelfPermission("com.example.bad.permission.READ", "denied");
    }
}
