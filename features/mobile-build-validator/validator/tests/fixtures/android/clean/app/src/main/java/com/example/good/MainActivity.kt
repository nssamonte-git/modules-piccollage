package com.example.good

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        val integrity = IntegrityManagerFactory.createStandard(this)
        val pending = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        ContextCompat.registerReceiver(this, receiver, IntentFilter("com.example.good.SYNC"),
            ContextCompat.RECEIVER_NOT_EXPORTED)
        Log.d(TAG, "token refreshed")
    }

    private fun key() = KeyGenParameterSpec.Builder("vault", KeyProperties.PURPOSE_ENCRYPT)
        .setUserAuthenticationRequired(true)
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .build()

    private fun cipher() = Cipher.getInstance("AES/GCM/NoPadding")

    private fun query(db: SQLiteDatabase, name: String) =
        db.rawQuery("SELECT * FROM users WHERE name = ?", arrayOf(name))

    companion object { const val TAG = "Main" }
}
