package com.example.bad

class Insecure(private val context: Context) {
    private val apiKey = "q9Xw2LmZ7pR4tV8sK1nB"
    private val prefsKey = "auth_token_storage_key"
    private val endpoint = "http://api.example-bank.io/v1"
    private val local = "http://10.0.2.2:8080/v1"

    private val trustAll = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    fun client() = OkHttpClient.Builder().hostnameVerifier { _, _ -> true }.build()

    fun crypto(data: ByteArray): ByteArray {
        val ecb = Cipher.getInstance("AES/ECB/PKCS5Padding")
        val des = Cipher.getInstance("DES/CBC/PKCS5Padding")
        val digest = MessageDigest.getInstance("MD5")
        val key = SecretKeySpec("0123456789abcdef".toByteArray(), "AES")
        val iv = IvParameterSpec(ByteArray(16))
        return ecb.doFinal(data)
    }

    fun storage(token: String, name: String, db: SQLiteDatabase) {
        context.getSharedPreferences("app", Context.MODE_PRIVATE).edit().putString("auth_token", token).apply()
        context.openFileOutput("dump.txt", Context.MODE_WORLD_READABLE)
        val dir = context.getExternalFilesDir(null)
        db.rawQuery("SELECT * FROM users WHERE name = '" + name + "'", null)
        Log.d(TAG, "token=$token")
        Log.d(TAG, "token refreshed")
    }

    fun keys(prompt: BiometricPrompt, info: BiometricPrompt.PromptInfo) {
        val spec = KeyGenParameterSpec.Builder("k", KeyProperties.PURPOSE_ENCRYPT)
            .setInvalidatedByBiometricEnrollment(false)
            .build()
        val legacy = KeyStore.getInstance("BKS")
        prompt.authenticate(info)
    }

    fun ipc(receiver: BroadcastReceiver, update: Intent, otpCode: String) {
        context.registerReceiver(receiver, IntentFilter("com.example.PING"))
        context.sendBroadcast(update)
        val pending = PendingIntent.getActivity(context, 0, update, 0)
        val mutable = PendingIntent.getBroadcast(context, 1, update,
            PendingIntent.FLAG_IMMUTABLE)
        val flags = PendingIntent.FLAG_MUTABLE
        update.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val clipboard = context.getSystemService(ClipboardManager::class.java)
        clipboard.setPrimaryClip(ClipData.newPlainText("otp", otpCode))
        val n = NotificationCompat.Builder(context, "c").setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        val loader = DexClassLoader("/sdcard/plugin.dex", context.cacheDir.path, null, javaClass.classLoader)
    }

    companion object { const val TAG = "Insecure" }
}
