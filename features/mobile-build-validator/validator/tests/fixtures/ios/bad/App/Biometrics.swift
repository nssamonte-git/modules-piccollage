import LocalAuthentication

func unlock(completion: @escaping (Bool) -> Void) {
    LAContext().evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, localizedReason: "Unlock") { ok, _ in
        completion(ok)
    }
}
