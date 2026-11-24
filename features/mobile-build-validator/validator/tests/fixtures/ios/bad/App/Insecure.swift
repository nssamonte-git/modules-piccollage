import Foundation
import CommonCrypto
import CryptoKit
import LocalAuthentication
import WebKit

final class Insecure {
    let apiKey = "q9Xw2LmZ7pR4tV8sK1nB"
    let endpoint = "http://api.example-bank.io/v1"

    func keychain(token: Data) {
        let a: [String: Any] = [kSecAttrAccessible as String: kSecAttrAccessibleAlways]
        let b: [String: Any] = [kSecAttrAccessible as String: kSecAttrAccessibleAfterFirstUnlock]
        let c: [String: Any] = [kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlocked,
                                kSecAttrSynchronizable as String: kCFBooleanTrue!]
        let d: [String: Any] = [kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlockedThisDeviceOnly]
    }

    func files(data: Data, url: URL) throws {
        try data.write(to: url, options: .noFileProtection)
        try data.write(to: url, options: .completeFileProtectionUntilFirstUserAuthentication)
    }

    func defaults(token: String, credential: Credential) throws {
        UserDefaults.standard.set(token, forKey: "authToken")
        UserDefaults(suiteName: "group.com.example")?.set(token, forKey: "sharedToken")
        NSUbiquitousKeyValueStore.default.set(token, forKey: "token")
        let blob = try JSONEncoder().encode(credential)
        print("token: \(token)")
        print("loaded \(items.count) items")
        UIPasteboard.general.string = token
        if UIScreen.main.isCaptured { }
    }

    func crypto(data: Data) {
        let ecb = kCCOptionECBMode
        let des = kCCAlgorithmDES
        let digest = Insecure.MD5.hash(data: data)
        let key = SymmetricKey(data: Data("0123456789abcdef0123456789abcdef".utf8))
        let signing = P256.Signing.PrivateKey()
    }

    func biometrics(context: LAContext) {
        context.touchIDAuthenticationAllowableReuseDuration = 60
        let state = context.evaluatedPolicyDomainState
    }

    func web(webView: WKWebView, config: WKWebViewConfiguration, name: String, page: URL, deviceToken: String) {
        config.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        webView.loadFileURL(page, allowingReadAccessTo: FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0])
        config.userContentController.add(self, name: "native")
        webView.evaluateJavaScript("setUser('\(name)')")
        let legacy: UIWebView? = nil
        print("push token \(deviceToken)")
    }
}

struct Settings { @AppStorage("sessionToken") var sessionToken = "" }
