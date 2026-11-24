import Foundation
import CryptoKit
import DeviceCheck
import Security

final class Vault {
    let endpoint = URL(string: "https://api.example.com/v1")!

    func save(_ token: Data) {
        var error: Unmanaged<CFError>?
        let access = SecAccessControlCreateWithFlags(nil, kSecAttrAccessibleWhenUnlockedThisDeviceOnly, .biometryCurrentSet, &error)
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlockedThisDeviceOnly,
            kSecAttrAccessControl as String: access as Any,
            kSecValueData as String: token,
        ]
        SecItemAdd(query as CFDictionary, nil)
    }

    func seal(_ data: Data, key: SymmetricKey) throws -> Data {
        try AES.GCM.seal(data, using: key).combined!
    }

    func attest() {
        guard DCAppAttestService.shared.isSupported else { return }
        DCAppAttestService.shared.generateKey { _, _ in }
    }

    func write(_ data: Data, to url: URL) throws {
        try data.write(to: url, options: .completeFileProtection)
    }
}

final class Trust: NSObject, URLSessionDelegate {
    func urlSession(_ session: URLSession, didReceive challenge: URLAuthenticationChallenge,
                    completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        guard let trust = challenge.protectionSpace.serverTrust, SecTrustEvaluateWithError(trust, nil) else {
            completionHandler(.cancelAuthenticationChallenge, nil)
            return
        }
        completionHandler(.useCredential, URLCredential(trust: trust))
    }
}
