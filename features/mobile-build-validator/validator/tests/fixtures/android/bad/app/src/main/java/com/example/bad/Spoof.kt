package com.example.bad

fun shareSession(context: Context, token: String) {
    val share = Intent("com.example.SHARE_SESSION")
    share.putExtra("session_token", token)
    context.startActivity(share)
}
