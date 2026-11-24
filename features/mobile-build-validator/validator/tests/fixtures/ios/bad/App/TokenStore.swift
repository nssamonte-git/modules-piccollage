import Security

func save(_ token: Data) {
    let query: [String: Any] = [kSecClass as String: kSecClassGenericPassword, kSecValueData as String: token]
    SecItemAdd(query as CFDictionary, nil)
}

func makeKey() {
    let attrs: [String: Any] = [kSecAttrKeyType as String: kSecAttrKeyTypeECSECPrimeRandom, kSecAttrKeySizeInBits as String: 256]
    _ = SecKeyCreateRandomKey(attrs as CFDictionary, nil)
}
