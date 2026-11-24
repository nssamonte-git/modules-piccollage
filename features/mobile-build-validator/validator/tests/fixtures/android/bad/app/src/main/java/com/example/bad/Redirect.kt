package com.example.bad

class Redirect : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val next = intent.getParcelableExtra<Intent>("next")
        startActivity(next)
        ActivityOptions.makeBasic().removeLaunchSecurityProtection()
    }
}
